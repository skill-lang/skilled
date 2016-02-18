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

import de.unistuttgart.iste.ps.skilled.sKilL.DeclarationReference;
import de.unistuttgart.iste.ps.skilled.sKilL.File;
import de.unistuttgart.iste.ps.skilled.sKilL.TypeDeclarationReference;
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

    public void computeTransitiveComponents() {
        for (StronglyConnectedComponent scc : tarjan.getConnectedComponentsList()) {
            scc.addReferencedComponent(scc);
            boolean changed = scc.addReferencedComponents(computeReferencedComponents(scc));
            while (changed) {
                changed = scc.addReferencedComponents(computeReferencedComponents(scc));
            }
        }
    }

    public static Set<StronglyConnectedComponent> computeReferencedComponents(StronglyConnectedComponent scc) {
        Set<StronglyConnectedComponent> referencedComponents = new HashSet<>();
        for (StronglyConnectedComponent referencedComponent : scc.getReferencedComponents()) {
            for (Vertex v : referencedComponent.getContainedVertices()) {
                referencedComponents.addAll(((DependencyGraphNode) v).getReferencedComponent());
            }
        }
        return referencedComponents;
    }

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
