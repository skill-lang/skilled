package de.unistuttgart.iste.ps.skilled.service.Tarjan;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


/**
 * Class for execute Tarjan's Algorithm.
 * 
 * @author Marco Link
 *
 */
public class TarjanAlgorithm {

    private Stack<Vertex> stack;
    private List<Vertex> vertexList;
    private List<StronglyConnectedComponent> connectedComponentsList;
    private int index = 0;

    public TarjanAlgorithm(List<Vertex> vertexList) {
        this.vertexList = vertexList;
        stack = new Stack<>();
        connectedComponentsList = new ArrayList<>();
    }

    public void runAlgorithm() {
        for (Vertex vertex : vertexList) {
            if (!vertex.isVisited()) {
                strongconnect(vertex);
            }
        }
    }

    private void strongconnect(Vertex vertex) {
        vertex.setIndex(index);
        vertex.setLowlink(index++);
        vertex.setVisited(true);
        stack.push(vertex);
        vertex.setOnStack(true);
        boolean isComponentRoot = true;

        for (Vertex v : vertex.getEdges()) {

            if (!v.isVisited()) {
                strongconnect(v);
            }
            if (vertex.getLowlink() > v.getLowlink()) {
                vertex.setLowlink(v.getLowlink());
                isComponentRoot = false;
            }
        }

        if (isComponentRoot) {
            StronglyConnectedComponent component = new StronglyConnectedComponent();
            component.setRootVertex(vertex);

            while (true) {
                Vertex actualVertex = stack.pop();
                actualVertex.setOnStack(false);
                actualVertex.setRootContainer(component);
                component.addVertices(actualVertex);
                if (actualVertex.getName().equals(vertex.getName())) {
                    break;
                }
            }
            connectedComponentsList.add(component);
        }

    }

    public List<StronglyConnectedComponent> getConnectedComponentsList() {
        return connectedComponentsList;
    }
}
