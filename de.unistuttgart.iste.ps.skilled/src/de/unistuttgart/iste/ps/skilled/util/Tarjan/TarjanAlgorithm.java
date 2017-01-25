package de.unistuttgart.iste.ps.skilled.util.Tarjan;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


/**
 * @author Marco Link
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

    private void strongconnect(Vertex from) {
        from.setIndex(index);
        from.setLowlink(index++);
        from.setVisited(true);
        stack.push(from);
        from.setOnStack(true);

        for (Vertex to : from.getEdges()) {
            if (!to.isVisited()) {
                strongconnect(to);
                from.setLowlink(Math.min(from.getLowlink(), to.getLowlink()));
            } else if (to.isOnStack()) {
                from.setLowlink(Math.min(from.getLowlink(), to.getLowlink()));
            }
        }

        if (from.getLowlink() == from.getIndex()) {
            StronglyConnectedComponent component = new StronglyConnectedComponent();
            component.setRootVertex(from);

            while (true) {
                Vertex actualVertex = stack.pop();
                actualVertex.setOnStack(false);
                actualVertex.setRootContainer(component);
                component.addVertices(actualVertex);
                if (actualVertex.getName().equals(from.getName())) {
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
