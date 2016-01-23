package de.unistuttgart.iste.ps.skilled.util.Tarjan;

import java.util.HashSet;
import java.util.Set;


/**
 * This class represents a strongly connected component and has the information about which components can be reach
 * (transitive).
 * 
 * @author Marco Link
 */
public class StronglyConnectedComponent {

    private Vertex rootVertex;
    // The vertices which belongs to the component.
    private Set<Vertex> containedVertices;
    // Other components which can be reached.
    private Set<StronglyConnectedComponent> referencedComponents;

    public StronglyConnectedComponent() {
        containedVertices = new HashSet<>();
        referencedComponents = new HashSet<>();
    }

    public Vertex getRootVertex() {
        return rootVertex;
    }

    public void setRootVertex(Vertex rootVertex) {
        this.rootVertex = rootVertex;
    }

    public Set<Vertex> getContainedVertices() {
        return containedVertices;
    }

    public void setContainedVertices(Set<Vertex> containedVertices) {
        this.containedVertices = containedVertices;
    }

    public boolean addVertices(Vertex vertex) {
        return containedVertices.add(vertex);
    }

    public Set<StronglyConnectedComponent> getReferencedComponents() {
        return referencedComponents;
    }

    public void setReferencedComponents(Set<StronglyConnectedComponent> referencedComponents) {
        this.referencedComponents = referencedComponents;
    }

    public boolean addReferencedComponent(StronglyConnectedComponent component) {
        return referencedComponents.add(component);
    }

    public boolean addReferencedComponents(Set<StronglyConnectedComponent> components) {
        return referencedComponents.addAll(components);
    }
}
