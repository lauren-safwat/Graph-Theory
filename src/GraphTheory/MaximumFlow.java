package GraphTheory;

import java.util.ArrayList;
import java.util.LinkedList;
import static GraphTheory.EdgeType.UNDIRECTED_EDGE;

public class MaximumFlow {

    public int maxFlow;
    public Vertex source;
    public Vertex sink;

    public ArrayList<Vertex> vertices = new ArrayList<>();
    public ArrayList<ArrayList<Edge>> maxFlowSteps = new ArrayList<>();
    public ArrayList<String> maxFlowPaths = new ArrayList<>();

    /**=============================================================================**/

    private boolean BFS(int[][] residualGraph, int[] parent) {
        boolean[] visited = new boolean[Graph.vertices.size()];
        LinkedList<Integer> queue = new LinkedList<>();
        queue.add(Graph.vertices.indexOf(source));
        visited[Graph.vertices.indexOf(source)] = true;
        parent[Graph.vertices.indexOf(source)] = -1;

        while (!queue.isEmpty()) {
            int s = queue.poll();
            for (int t = 0; t < Graph.vertices.size(); t++) {
                if (!visited[t] && residualGraph[s][t] > 0) {
                    queue.add(t);
                    parent[t] = s;
                    visited[t] = true;
                }
            }
        }
        return (visited[Graph.vertices.indexOf(sink)]);
    }

    public void fordFulkerson() {
        int[][] residualGraph = Graph.buildAdjacencyMatrix();
        int[] parent = new int[Graph.vertices.size()];
        maxFlow = 0;

        for (Vertex vertex : Graph.vertices)
            vertices.add(new Vertex(vertex.symbol));

        ArrayList<Edge> allEdges = new ArrayList<>();
        ArrayList<Edge> originalEdges = new ArrayList<>();
        for (Edge edge : Graph.edges){
            allEdges.add(new Edge(edge));
            originalEdges.add(new Edge(edge));
        }

        maxFlowSteps.add(originalEdges);

        while (BFS(residualGraph, parent)) {

            int pathFlow = Integer.MAX_VALUE;
            for (int t = Graph.vertices.indexOf(sink); t != Graph.vertices.indexOf(source); t = parent[t]) {
                int s = parent[t];
                pathFlow = Math.min(pathFlow, residualGraph[s][t]);
            }

            StringBuilder path = new StringBuilder(sink.symbol);
            for (int t = Graph.vertices.indexOf(sink); t != Graph.vertices.indexOf(source); t = parent[t]) {
                int s = parent[t];
                residualGraph[s][t] -= pathFlow;
                residualGraph[t][s] += pathFlow;
                for (Edge edge : allEdges) {
                    if( s == Graph.vertices.indexOf(edge.vertex_First) && t == Graph.vertices.indexOf(edge.vertex_Second) ){
                        edge.weight = residualGraph[s][t];
                        edge.flow += pathFlow;
                        path.insert(0, " -> ");
                        path.insert(0, edge.vertex_First);
                    }
                    else if((s == Graph.vertices.indexOf(edge.vertex_Second) && t == Graph.vertices.indexOf(edge.vertex_First) && edge.type == UNDIRECTED_EDGE)){
                        edge.weight = residualGraph[t][s];
                        edge.flow += pathFlow;
                        path.insert(0, " -> ");
                        path.insert(0, edge.vertex_Second);
                    }
                    else if (s == Graph.vertices.indexOf(edge.vertex_Second) && t == Graph.vertices.indexOf(edge.vertex_First)){
                        edge.weight = residualGraph[t][s];
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
