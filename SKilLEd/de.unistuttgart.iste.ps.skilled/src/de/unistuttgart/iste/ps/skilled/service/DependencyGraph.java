package de.unistuttgart.iste.ps.skilled.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;

import de.unistuttgart.iste.ps.skilled.sKilL.File;
import de.unistuttgart.iste.ps.skilled.service.Tarjan.StronglyConnectedComponent;
import de.unistuttgart.iste.ps.skilled.service.Tarjan.TarjanAlgorithm;
import de.unistuttgart.iste.ps.skilled.service.Tarjan.Vertex;


/**
 * Class for creating dependency graph for skill files. Reflexive transitive includes are supported. It will uses Tarjan's
 * Algorithm for creating a DAG first.
 * 
 * @author Marco Link
 *
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
        dependencyGraphNodes = new HashMap<String, DependencyGraphNode>();
        List<Vertex> dependencyNodes = new ArrayList<Vertex>();

        for (File file : files) {
            if (file.eResource() == null) {
                return false;
            }
            DependencyGraphNode dependencyNode = new DependencyGraphNode(file);
            dependencyNodes.add(dependencyNode);
            dependencyGraphNodes.put(dependencyNode.getName(), dependencyNode);
        }

        for (Vertex v : dependencyNodes) {
            DependencyGraphNode g = (DependencyGraphNode) v;
            for (URI uri : g.getIncludedURIs()) {
                DependencyGraphNode inc = dependencyGraphNodes.get(uri.path());
                if (inc != null) {
                    g.addEdge(inc);
                }
            }
        }

        tarjan = new TarjanAlgorithm(dependencyNodes);
        tarjan.runAlgorithm();
        computeTransitiveComponents();
        return true;
    }

    public void computeTransitiveComponents() {
        for (StronglyConnectedComponent s : tarjan.getConnectedComponentsList()) {
            s.addReferencedComponent(s);
            boolean change = s.addReferencedComponents(computeReferencedComponents(s));
            while (change) {
                change = s.addReferencedComponents(computeReferencedComponents(s));
            }
        }
    }

    public static Set<StronglyConnectedComponent> computeReferencedComponents(StronglyConnectedComponent s) {
        Set<StronglyConnectedComponent> referencedComponents = new HashSet<StronglyConnectedComponent>();
        for (StronglyConnectedComponent referencedComponent : s.getReferencedComponents()) {
            for (Vertex v : referencedComponent.getContainedVertices()) {
                DependencyGraphNode g = (DependencyGraphNode) v;
                referencedComponents.addAll(g.getReferencedComponent());
            }
        }
        return referencedComponents;
    }

    /**
     * Returns all the included URIs (direct and transitive) of the given resource.
     * 
     * @param resource
     * @return a Set of URIs or null if the resource or its URI is null.
     */
    public Set<URI> getIncludedURIs(Resource resource) {
        Set<URI> uris = new HashSet<URI>();
        if (resource == null) {
            return null;
        }
        DependencyGraphNode g = dependencyGraphNodes.get(resource.getURI().path());
        if (g == null) {
            return null;
        }
        if (g.getRootContainer() == null) {
            return null;
        }
        for (StronglyConnectedComponent s : g.getRootContainer().getReferencedComponents()) {
            for (Vertex v : s.getContainedVertices()) {
                DependencyGraphNode g2 = (DependencyGraphNode) v;
                uris.add(g2.getFileURI());
            }
        }
        return uris;
    }
}
