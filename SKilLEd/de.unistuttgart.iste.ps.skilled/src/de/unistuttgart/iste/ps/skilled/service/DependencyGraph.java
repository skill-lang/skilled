package de.unistuttgart.iste.ps.skilled.service;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;

import de.unistuttgart.iste.ps.skilled.sKilL.File;


/**
 * Class for creating dependency graph for skill files. Reflexive and transitive includes are supported. Not performant at
 * all :(
 * 
 * @author Marco Link
 *
 */
public class DependencyGraph {

    private List<DependencyGraphNode> dependencyNodes;

    private List<DependencyGraphNode> visited;
    private List<DependencyGraphNode> visited2;

    public DependencyGraph() {
    }

    public boolean generate(List<File> files, List<URI> fileURIS) {
        reset();
        if (files.size() != fileURIS.size()) {
            return false;
        }
        for (int i = 0; i < files.size(); i++) {
            DependencyGraphNode dependencyNode = new DependencyGraphNode(files.get(i), fileURIS.get(i));
            dependencyNodes.add(dependencyNode);
        }
        computeDependencies();
        return true;
    }

    public void reset() {
        dependencyNodes = new LinkedList<DependencyGraphNode>();
        visited = new LinkedList<DependencyGraphNode>();
        visited2 = new LinkedList<DependencyGraphNode>();

    }

    private void computeDependencies() {
        for (DependencyGraphNode dependencyNode : dependencyNodes) {
            computeDirectDependencies(dependencyNode);
        }

        for (DependencyGraphNode dependencyNode : dependencyNodes) {
            computeTransitiveIncludes(dependencyNode);
        }
    }

    private void computeDirectDependencies(DependencyGraphNode dependencyNode) {
        Set<URI> includeURIs = dependencyNode.getIncludedURIs();
        for (URI includeURI : includeURIs) {
            for (DependencyGraphNode potentialIncludedDependencyNode : dependencyNodes) {

                if (potentialIncludedDependencyNode.getFileURI().equals(includeURI)) {
                    dependencyNode.addToDirectIncludes(potentialIncludedDependencyNode);
                    potentialIncludedDependencyNode.addToIndirectIncludes(dependencyNode);
                    break;
                }
            }
        }
    }

    private void computeTransitiveIncludes(DependencyGraphNode dependencyNode) {
        visited = new LinkedList<DependencyGraphNode>();
        visited2 = new LinkedList<DependencyGraphNode>();

        Set<DependencyGraphNode> allIncludes = dependencyGraphNodes(dependencyNode);
        dependencyNode.setAllIncludes(allIncludes);
    }

    private Set<DependencyGraphNode> dependencyGraphNodes(DependencyGraphNode dependencyNode) {
        Set<DependencyGraphNode> allIncludes = new HashSet<DependencyGraphNode>();
        if (!visited.contains(dependencyNode)) {
            visited.add(dependencyNode);
            allIncludes.add(dependencyNode);
            allIncludes.addAll(dependencyGraphNodesIncluded(dependencyNode));
        }
        return allIncludes;
    }

    private Set<DependencyGraphNode> dependencyGraphNodesIncluded(DependencyGraphNode dependencyNode) {
        LinkedHashSet<DependencyGraphNode> dependencyNodes = new LinkedHashSet<DependencyGraphNode>();
        for (DependencyGraphNode importedDependencyNode : dependencyNode.getDirectAndIndirectIncludes()) {
            if (!visited2.contains(importedDependencyNode)) {
                visited2.add(importedDependencyNode);
                dependencyNodes.addAll(dependencyGraphNodes(importedDependencyNode));

            }
        }
        return dependencyNodes;
    }

    public Set<URI> getDirectIncludesURI(Resource resource) {
        LinkedHashSet<URI> uris = new LinkedHashSet<URI>();
        DependencyGraphNode node = getDependencyGraphNode(resource.getURI());
        if (node != null) {
            for (DependencyGraphNode includedNode : node.getDirectIncludes()) {
                uris.add(includedNode.getFileURI());
            }
        }
        return uris;
    }

    public Set<URI> getIndirectIncludesURI(Resource resource) {
        LinkedHashSet<URI> uris = new LinkedHashSet<URI>();
        DependencyGraphNode node = getDependencyGraphNode(resource.getURI());
        if (node != null) {
            for (DependencyGraphNode includedNode : node.getIndirectIncludes()) {
                uris.add(includedNode.getFileURI());
            }
        }
        return uris;
    }

    public Set<URI> getAllIncludesURI(Resource resource) {
        LinkedHashSet<URI> uris = new LinkedHashSet<URI>();
        DependencyGraphNode node = getDependencyGraphNode(resource.getURI());
        if (node != null) {
            for (DependencyGraphNode includedNode : node.getAllIncludes()) {
                uris.add(includedNode.getFileURI());
            }
        }
        return uris;
    }

    protected DependencyGraphNode getDependencyGraphNode(URI uri) {
        for (DependencyGraphNode node : dependencyNodes) {
            if (node.getFileURI().path().equals(uri.path())) {
                return node;
            }
        }
        return null;
    }
}
