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
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.util.TextRegion;
import org.eclipse.xtext.util.Triple;
import org.eclipse.xtext.util.Tuples;

import de.unistuttgart.iste.ps.skilled.sKilL.File;
import de.unistuttgart.iste.ps.skilled.sKilL.Include;
import de.unistuttgart.iste.ps.skilled.sKilL.IncludeFile;
import de.unistuttgart.iste.ps.skilled.util.SKilLServices;
import de.unistuttgart.iste.ps.skilled.util.DependencyGraph.DependencyGraph;


public class SKilLImportOrganizer {

    private DependencyGraph dependencyGraph;
    private SKilLServices services = new SKilLServices();

    public SKilLImportOrganizer() {
    }

    public SKilLImportOrganizer(DependencyGraph dependencyGraph) {
        this.dependencyGraph = dependencyGraph;
    }

    public String getOrganizedImportSection(File file) {
        StringBuilder includes = new StringBuilder();
        List<IncludeFile> test = getUnusedImports2(file);
        Set<URI> missing = dependencyGraph.getMissingIncludes(file.eResource());
        test.addAll(a(file));
        // var allIncludeFiles = new ArrayList<IncludeFile>();

        for (Include inc : file.getIncludes()) {
            for (IncludeFile incF : inc.getIncludeFiles()) {
                if (!test.contains(incF)) {
                    includes.append("include " + "\"" + incF.getImportURI() + "\"" + "\n");
                }
            }
        }

        for (URI u : missing) {
            URI referencedURI = u.deresolve(file.eResource().getURI());
            if (referencedURI != null) {
                includes.append("include " + "\"" + referencedURI.path() + "\"" + "\n");
            }

        }
        return includes.toString();
    }

    public List<IncludeFile> a(File file) {
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

    public List<Integer> getIndexOfDuplicateImports(List<URI> uris) {
        List<Integer> index = new ArrayList<Integer>();
        Set<URI> visited = new HashSet<URI>();
        for (int i = 0; i < uris.size(); i++) {
            if (visited.contains(uris.get(i))) {
                index.add(i);
            } else {
                visited.add(uris.get(i));
            }
        }
        return index;
    }

    public List<IncludeFile> getUnusedImports2(File file) {
        Map<URI, IncludeFile> map = new HashMap<URI, IncludeFile>();
        List<Triple<URI, Integer, List<URI>>> list = new ArrayList<Triple<URI, Integer, List<URI>>>();

        Set<URI> uris2 = new HashSet<URI>();

        for (Include inc : file.getIncludes()) {
            for (IncludeFile i : inc.getIncludeFiles()) {
                uris2.add(services.createAbsoluteURIFromRelative(i.getImportURI(), file.eResource().getURI()));
                map.put(services.createAbsoluteURIFromRelative(i.getImportURI(), file.eResource().getURI()), i);
            }
        }

        Set<URI> neededURIs = dependencyGraph.getNeededIncludes(file.eResource());

        for (URI uri : uris2) {
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

    public Set<URI> getUnusedImports(Set<URI> includedURIs, Set<URI> neededURIs) {
        List<Triple<URI, Integer, List<URI>>> list = new ArrayList<Triple<URI, Integer, List<URI>>>();

        for (URI uri : includedURIs) {
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

        Set<URI> unusedURIs = new HashSet<URI>();
        for (Triple<URI, Integer, List<URI>> triple : list) {
            if (neededURIs.size() > 0) {
                if (!neededURIs.removeAll(triple.getThird())) {
                    unusedURIs.add(triple.getFirst());
                }
            } else {
                unusedURIs.add(triple.getFirst());
            }
        }

        return unusedURIs;
    }

    public Triple<URI, Integer, List<URI>> getCount(URI includeURI, Set<URI> neededURIs) {
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

    public TextRegion getImportRegion(File file) {
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
            }
            // TODO Other scenarios
        }
        return null;
    }

}
