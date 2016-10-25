package de.unistuttgart.iste.ps.skilled.formatting2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.xtext.nodemodel.ICompositeNode;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.util.TextRegion;
import org.eclipse.xtext.util.Triple;
import org.eclipse.xtext.util.Tuples;

import de.unistuttgart.iste.ps.skilled.skill.SkillPackage;
import de.unistuttgart.iste.ps.skilled.skill.File;
import de.unistuttgart.iste.ps.skilled.skill.Include;
import de.unistuttgart.iste.ps.skilled.skill.IncludeFile;
import de.unistuttgart.iste.ps.skilled.util.SKilLServices;
import de.unistuttgart.iste.ps.skilled.util.DependencyGraph.DependencyGraph;

/**
 * This class contains helping methods for organize imports and the validation
 * for included uris.
 * 
 * @author Marco Link
 *
 */
public class SKilLImportOrganizer {

    private DependencyGraph dependencyGraph;
    private SKilLServices services = new SKilLServices();

    public SKilLImportOrganizer() {
    }

    public SKilLImportOrganizer(DependencyGraph dependencyGraph) {
        this.dependencyGraph = dependencyGraph;
    }

    /**
     * Checks for duplicate includes in a skill file.
     * 
     * @param file
     *            The file for which the duplicate includes shall be returned.
     * @return A list which includes all duplicated IncludeFiles (the element in
     *         the AST which contains the included uri).
     */
    public static List<IncludeFile> getDuplicateIncludes(File file) {
        if (file == null) {
            return null;
        }
        List<IncludeFile> duplicates = new ArrayList<IncludeFile>();
        Set<String> visited = new HashSet<String>();
        for (Include inc : file.getIncludes()) {
            for (IncludeFile i : inc.getIncludeFiles()) {
                if (visited.contains(i.getImportURI())) {
                    duplicates.add(i);
                } else {
                    visited.add(i.getImportURI());
                }
            }
        }
        return duplicates;
    }

    /**
     * Checks for unused includes in a skill file.
     * 
     * @param file
     *            The file for which the unsued includes shall be returned.
     * @return A list which includes all unused IncludeFiles (the element in the
     *         AST which contains the included uri).
     */
    public List<IncludeFile> getUnusedImports(File file) {
        Map<URI, IncludeFile> map = new HashMap<URI, IncludeFile>();
        List<Triple<URI, Integer, List<URI>>> list = new ArrayList<Triple<URI, Integer, List<URI>>>();

        Set<URI> allIncludedURIs = new HashSet<URI>();

        for (Include include : file.getIncludes()) {
            for (IncludeFile includeFile : include.getIncludeFiles()) {
                allIncludedURIs.add(
                        services.createAbsoluteURIFromRelative(includeFile.getImportURI(), file.eResource().getURI()));
                map.put(services.createAbsoluteURIFromRelative(includeFile.getImportURI(), file.eResource().getURI()),
                        includeFile);
            }
        }

        Set<URI> neededURIs = dependencyGraph.getNeededIncludes(file.eResource());

        for (URI uri : allIncludedURIs) {
            Triple<URI, Integer, List<URI>> triple = getCount(uri, neededURIs);
            list.add(triple);
        }

        list.sort(new Comparator<Triple<URI, Integer, List<URI>>>() {
            @Override
            public int compare(Triple<URI, Integer, List<URI>> o1, Triple<URI, Integer, List<URI>> o2) {
                if (o1.getSecond() < o2.getSecond()) {
                    return 1;
                } else if (o1.getSecond() == o2.getSecond()) {
                    return 0;
                }
                return -1;
            }
        });

        List<IncludeFile> unusedURIs = new ArrayList<IncludeFile>();
        for (Triple<URI, Integer, List<URI>> triple : list) {
            if (neededURIs.size() > 0) {
                if (!neededURIs.removeAll(triple.getThird())) {
                    unusedURIs.add(map.get(triple.getFirst()));
                }
            } else {
                unusedURIs.add(map.get(triple.getFirst()));
            }
        }
        return unusedURIs;
    }

    /**
     * Computes the number of direct and transitive reachable needed uris of a
     * uri.
     * 
     * @param includeURI
     *            The uri for which the number shall be computed.
     * @param neededURIs
     *            The uris which will increase the count if it will be included.
     * @return A triple with the original uri, the amount of uris which are
     *         included and the included uris.
     */
    private Triple<URI, Integer, List<URI>> getCount(URI includeURI, Set<URI> neededURIs) {
        int count = 0;
        List<URI> uris = new ArrayList<URI>();
        Set<URI> includes = dependencyGraph.getIncludedURIsFromURI(includeURI);

        if (includes != null) {
            for (URI uri : neededURIs) {
                if (includes.contains(uri)) {
                    count++;
                    uris.add(uri);
                }
            }
        }
        return Tuples.create(includeURI, count, uris);
    }

    public void setDependencyGraph(DependencyGraph dependencyGraph) {
        this.dependencyGraph = dependencyGraph;
    }

    /**
     * Finds the position (region) in a file at which the includes are or can be
     * written.
     * 
     * @param file
     *            The file for which the region shall be found.
     * @return A TextRegion which represents the position of the includes.
     */
    public static TextRegion getIncludeImportRegion(File file) {
        if (file != null) {
            if (file.getIncludes().size() > 0) {
                List<Include> inc = file.getIncludes();
                int begin = NodeModelUtils.getNode(inc.get(0)).getOffset();
                int end = NodeModelUtils.getNode(inc.get(inc.size() - 1)).getTotalEndOffset();

                return new TextRegion(begin, end - begin);
            } else if (file.getDeclarations().size() > 0) {
                ICompositeNode node = NodeModelUtils.getNode(file.getDeclarations().get(0));
                int off = node.getTotalOffset();
                return new TextRegion(off, 0);
            } else if (file.getHeadComments().size() > 0) {
                List<INode> nodes = NodeModelUtils.findNodesForFeature(file, SkillPackage.Literals.FILE__HEAD_COMMENTS);
                if (nodes != null) {
                    int off = nodes.get(nodes.size() - 1).getTotalEndOffset();
                    return new TextRegion(off, 0);
                }
            } else {
                return new TextRegion(0, 0);
            }
        }
        return null;
    }

}
