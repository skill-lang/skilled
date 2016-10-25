package de.unistuttgart.iste.ps.skilled.util.DependencyGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.EcoreUtil2;

import de.unistuttgart.iste.ps.skilled.skill.DeclarationReference;
import de.unistuttgart.iste.ps.skilled.skill.File;
import de.unistuttgart.iste.ps.skilled.skill.TypeDeclarationReference;
import de.unistuttgart.iste.ps.skilled.util.Tarjan.StronglyConnectedComponent;
import de.unistuttgart.iste.ps.skilled.util.Tarjan.TarjanAlgorithm;
import de.unistuttgart.iste.ps.skilled.util.Tarjan.Vertex;


/**
 * Class for creating dependency graph for skill files. Reflexive transitive includes are supported. It will uses Tarjan's
 * Algorithm for creating a DAG first.
 * 
 * @author Marco Link
 * @author Daniel Ryan Degutis
 */
public class DependencyGraph {

    private Map<String, DependencyGraphNode> dependencyGraphNodes;
    private TarjanAlgorithm tarjan;

    /**
     * Generates a dependency graph of the given files.
     * 
     * @param files
     *            - for which the dependency graph should be generated.
     * @return false if one or more files don't have an eResource.
     */
    public boolean generate(Set<File> files) {
        dependencyGraphNodes = new HashMap<>();
        List<Vertex> dependencyNodes = new ArrayList<>();

        for (File file : files) {
            if (file.eResource() == null) {
                return false;
            }
            DependencyGraphNode dgn = new DependencyGraphNode(file);
            dependencyNodes.add(dgn);
            dependencyGraphNodes.put(dgn.getName(), dgn);
        }

        for (Vertex v : dependencyNodes) {
            DependencyGraphNode dgn = (DependencyGraphNode) v;
            for (URI uri : dgn.getIncludedURIs()) {
                DependencyGraphNode inc = dependencyGraphNodes.get(uri.path());
                if (inc != null) {
                    dgn.addEdge(inc);
                }
            }
        }

        tarjan = new TarjanAlgorithm(dependencyNodes);
        tarjan.runAlgorithm();
        computeTransitiveComponents();
        return true;
    }

    /**
     * Generates a dependency graph of the given files but it will ignore the origin file, so it won't appear as a needed
     * node.
     * 
     * @param origin
     *            - the file which will be ignored.
     * @param files
     *            - for which the dependency graph should be generated.
     * @return false if one or more files don't have an eResource.
     */
    public boolean generateIgnoreOrigin(File origin, Set<File> files) {
        dependencyGraphNodes = new HashMap<>();
        List<Vertex> dependencyNodes = new ArrayList<>();

        for (File file : files) {
            if (file.eResource() == null) {
                return false;
            }
            DependencyGraphNode dgn = new DependencyGraphNode(file);
            dependencyNodes.add(dgn);
            dependencyGraphNodes.put(dgn.getName(), dgn);
        }

        for (Vertex v : dependencyNodes) {
            DependencyGraphNode dgn = (DependencyGraphNode) v;
            for (URI uri : dgn.getIncludedURIs()) {
                DependencyGraphNode inc = dependencyGraphNodes.get(uri.path());
                if (inc != null) {
                    if (inc != dependencyGraphNodes.get(origin.eResource().getURI().path())) {
                        dgn.addEdge(inc);
                    }
                }
            }
        }

        tarjan = new TarjanAlgorithm(dependencyNodes);
        tarjan.runAlgorithm();
        computeTransitiveComponents();
        return true;
    }

    /**
     * It will computes for all files in the dependnecy graph, which file they can reach transitively.
     */
    private void computeTransitiveComponents() {
        for (StronglyConnectedComponent scc : tarjan.getConnectedComponentsList()) {
            scc.addReferencedComponent(scc);
            boolean changed = scc.addReferencedComponents(computeReferencedComponents(scc));
            while (changed) {
                changed = scc.addReferencedComponents(computeReferencedComponents(scc));
            }
        }
    }

    /**
     * This will compute all the connected components which can be reach by one connected component.
     * 
     * @param scc
     *            -- The strongly connected compononet for which it should be computed.
     * @return - all other connected components which can be reach.
     */
    private static Set<StronglyConnectedComponent> computeReferencedComponents(StronglyConnectedComponent scc) {
        Set<StronglyConnectedComponent> referencedComponents = new HashSet<>();
        for (StronglyConnectedComponent referencedComponent : scc.getReferencedComponents()) {
            for (Vertex v : referencedComponent.getContainedVertices()) {
                referencedComponents.addAll(((DependencyGraphNode) v).getReferencedComponent());
            }
        }
        return referencedComponents;
    }

    /**
     * This will give all included uris, which are direct or indirectly included.
     * 
     * @param resource
     *            - The resource which is contained in the dependency graph for which the included uris shall be returned.
     * @return - A set which includes all included uris.
     */
    public Set<URI> getIncludedURIs(Resource resource) {
        Set<URI> uris = new HashSet<>();
        if (resource == null) {
            return null;
        }
        DependencyGraphNode depGraph = dependencyGraphNodes.get(resource.getURI().path());
        if (depGraph == null || depGraph.getRootContainer() == null) {
            return null;
        }
        for (StronglyConnectedComponent scc : depGraph.getRootContainer().getReferencedComponents()) {
            for (Vertex v : scc.getContainedVertices()) {
                DependencyGraphNode dgn = (DependencyGraphNode) v;
                uris.add(dgn.getFileURI());
            }
        }
        return uris;
    }

    /**
     * This will give all included uris, which are direct or indirectly included.
     * 
     * @param uri
     *            - The uri which stands for a resource in the dependency graph for which the included uris shall be
     *            returned.
     * @return - A set which includes all included uris.
     */
    public Set<URI> getIncludedURIsFromURI(URI uri) {
        Set<URI> uris = new HashSet<>();
        if (uri == null) {
            return null;
        }
        DependencyGraphNode depGraph = dependencyGraphNodes.get(uri.path());
        if (depGraph == null || depGraph.getRootContainer() == null) {
            return null;
        }
        for (Vertex v : depGraph.getRootContainer().getContainedVertices()) {
            DependencyGraphNode dgn = (DependencyGraphNode) v;
            uris.add(dgn.getFileURI());
        }

        for (StronglyConnectedComponent scc : depGraph.getRootContainer().getReferencedComponents()) {
            for (Vertex v : scc.getContainedVertices()) {
                DependencyGraphNode dgn = (DependencyGraphNode) v;
                uris.add(dgn.getFileURI());
            }
        }
        return uris;

    }

    /**
     * Computes all the includes which are needed for a file.
     * 
     * @param resource
     *            - The resource which stands for a node in the dependency graph for which all needed includes shall be
     *            returned.
     * @return - A set with all needed uris.
     */
    public Set<URI> getNeededIncludes(Resource resource) {
        if (resource == null) {
            return null;
        }
        Set<URI> uris = new HashSet<URI>();
        DependencyGraphNode depGraph = dependencyGraphNodes.get(resource.getURI().path());
        if (depGraph == null || depGraph.getRootContainer() == null) {
            return null;
        }
        File f = depGraph.getFile();
        List<TypeDeclarationReference> dec2 = EcoreUtil2.getAllContentsOfType(f, TypeDeclarationReference.class);

        List<DeclarationReference> dec = EcoreUtil2.getAllContentsOfType(f, DeclarationReference.class);

        for (TypeDeclarationReference ref : dec2) {
            uris.add(EcoreUtil2.getNormalizedResourceURI(ref.getType()));
        }
        for (DeclarationReference ref : dec) {
            uris.add(EcoreUtil2.getNormalizedResourceURI(ref.getType()));
        }
        uris.remove(resource.getURI());
        return uris;
    }

    /**
     * Computes all missing includes of a file.
     * 
     * @param resource
     *            - The resource which stands for a node in the dependency graph for which all missing includes shall be
     *            returned.
     * @return - A set with all missing uris.
     */
    public Set<URI> getMissingIncludes(Resource resource) {
        Set<URI> uris = new HashSet<URI>();
        Set<URI> included = getIncludedURIs(resource);
        Set<URI> needed = getNeededIncludes(resource);

        for (URI uri : needed) {
            if (!included.contains(uri)) {
                uris.add(uri);
            }
        }
        return uris;
    }
}
