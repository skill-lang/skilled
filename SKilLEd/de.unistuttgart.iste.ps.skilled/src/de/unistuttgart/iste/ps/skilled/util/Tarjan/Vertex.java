package de.unistuttgart.iste.ps.skilled.util.Tarjan;

import java.util.ArrayList;
import java.util.List;


/**
 * Class which represents a vertex for Tarjan's Algorithm.
 * 
 * @author Marco Link
 *
 */
public class Vertex {

    private String name;
    private List<Vertex> edges;
    private boolean visited;
    private boolean onStack;
    private int lowlink;
    private int index;
    private StronglyConnectedComponent rootContainer;

    public Vertex(String name) {
        this.name = name;
        edges = new ArrayList<>();
    }

    public StronglyConnectedComponent getRootContainer() {
        return rootContainer;
    }

    public void setRootContainer(StronglyConnectedComponent rootContainer) {
        this.rootContainer = rootContainer;
    }

    public void addEdge(Vertex vertex) {
        edges.add(vertex);
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public int getLowlink() {
        return lowlink;
    }

    public void setLowlink(int lowlink) {
        this.lowlink = lowlink;
    }

    public String getName() {
        return name;
    }

    public List<Vertex> getEdges() {
        return edges;
    }

    public boolean isOnStack() {
        return onStack;
    }

    public void setOnStack(boolean onStack) {
        this.onStack = onStack;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
