package GraphTheory;

import GraphTheory.GraphModels.Edge;
import GraphTheory.GraphModels.Graph;
import GraphTheory.GraphModels.Vertex;

import java.util.ArrayList;
import java.util.LinkedList;
import static GraphTheory.GraphModels.EdgeType.UNDIRECTED_EDGE;

public class MaximumFlow {

    private int maxFlow;
    private Vertex source;
    private Vertex sink;
    private ArrayList<Vertex> vertices = new ArrayList<>();
    private ArrayList<ArrayList<Edge>> maxFlowSteps = new ArrayList<>();
    private ArrayList<String> maxFlowPaths = new ArrayList<>();

    /**=============================================================================**/

    public int getMaxFlow() {
        return maxFlow;
    }

    public Vertex getSource() {
        return source;
    }

    public void setSource(Vertex source) {
        this.source = source;
    }

    public Vertex getSink() {
        return sink;
    }

    public void setSink(Vertex sink) {
        this.sink = sink;
    }

    public ArrayList<Vertex> getVertices() {
        return vertices;
    }

    public ArrayList<ArrayList<Edge>> getMaxFlowSteps() {
        return maxFlowSteps;
    }

    public ArrayList<String> getMaxFlowPaths() {
        return maxFlowPaths;
    }

    /**=============================================================================**/

    private boolean BFS(int[][] residualGraph, int[] parent) {
        boolean[] visited = new boolean[Graph.getVertices().size()];
        LinkedList<Integer> queue = new LinkedList<>();
        queue.add(Graph.getVertices().indexOf(source));
        visited[Graph.getVertices().indexOf(source)] = true;
        parent[Graph.getVertices().indexOf(source)] = -1;

        while (!queue.isEmpty()) {
            int s = queue.poll();
            for (int t = 0; t < Graph.getVertices().size(); t++) {
                if (!visited[t] && residualGraph[s][t] > 0) {
                    queue.add(t);
                    parent[t] = s;
                    visited[t] = true;
                }
            }
        }
        return (visited[Graph.getVertices().indexOf(sink)]);
    }

    public void fordFulkerson() {
        int[][] residualGraph = Graph.buildWeightAdjacencyMatrix();
        int[] parent = new int[Graph.getVertices().size()];
        maxFlow = 0;

        for (Vertex vertex : Graph.getVertices())
            vertices.add(new Vertex(vertex.getSymbol()));

        ArrayList<Edge> allEdges = new ArrayList<>();
        ArrayList<Edge> originalEdges = new ArrayList<>();
        for (Edge edge : Graph.getEdges()){
            allEdges.add(new Edge(edge));
            originalEdges.add(new Edge(edge));
        }

        maxFlowSteps.add(originalEdges);

        while (BFS(residualGraph, parent)) {

            int pathFlow = Integer.MAX_VALUE;
            for (int t = Graph.getVertices().indexOf(sink); t != Graph.getVertices().indexOf(source); t = parent[t]) {
                int s = parent[t];
                pathFlow = Math.min(pathFlow, residualGraph[s][t]);
            }

            StringBuilder path = new StringBuilder(sink.getSymbol());
            for (int t = Graph.getVertices().indexOf(sink); t != Graph.getVertices().indexOf(source); t = parent[t]) {
                int s = parent[t];
                residualGraph[s][t] -= pathFlow;
                residualGraph[t][s] += pathFlow;
                for (Edge edge : allEdges) {
                    if( s == Graph.getVertices().indexOf(edge.getVertex_First()) && t == Graph.getVertices().indexOf(edge.getVertex_Second()) ){
                        edge.setWeight(residualGraph[s][t]);
                        edge.setFlow(edge.getFlow() + pathFlow);
                        path.insert(0, " -> ");
                        path.insert(0, edge.getVertex_First());
                    }
                    else if((s == Graph.getVertices().indexOf(edge.getVertex_Second()) && t == Graph.getVertices().indexOf(edge.getVertex_First()) && edge.getType() == UNDIRECTED_EDGE)){
                        edge.setWeight(residualGraph[t][s]);
                        edge.setFlow(edge.getFlow() + pathFlow);
                        path.insert(0, " -> ");
                        path.insert(0, edge.getVertex_Second());
                    }
                    else if (s == Graph.getVertices().indexOf(edge.getVertex_Second()) && t == Graph.getVertices().indexOf(edge.getVertex_First())){
                        edge.setWeight(residualGraph[t][s]);
                    }
                }
            }

            path.append(",   Path flow = ").append(pathFlow);

            ArrayList<Edge> temp = new ArrayList<>();
            for (Edge edge : allEdges) {
                temp.add(new Edge(edge));
            }

            maxFlowSteps.add(temp);
            maxFlowPaths.add(path.toString());

            maxFlow += pathFlow;
        }
    }

    public void clear() {
        maxFlowPaths.clear();
        maxFlowSteps.clear();
        vertices.clear();
    }
}
