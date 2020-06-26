package GraphTheory;

import java.util.ArrayList;

import static GraphTheory.EdgeType.UNDIRECTED_EDGE;

public class Graph {

    public static ArrayList<Vertex> vertices = new ArrayList<>();
    public static ArrayList<Edge> edges = new ArrayList<>();

    /**=============================================================================**/

    public Graph() {}

    public Graph(ArrayList<Vertex> vertices, ArrayList<Edge> edges) {
        Graph.vertices = vertices;
        Graph.edges = edges;
    }

    public static int[][] buildAdjacencyMatrix() {
        int[][] graph = new int[vertices.size()][vertices.size()];
        for (Edge edge : edges) {
            int s = vertices.indexOf(edge.vertex_First);
            int t = vertices.indexOf(edge.vertex_Second);
            graph[s][t] = edge.weight;
            if (edge.type == UNDIRECTED_EDGE)
                graph[t][s] = edge.weight;
        }
        return graph;
    }

    public static int searchVertex(String vertex, ArrayList<Vertex> verticesList) {
        for (int i = 0; i < verticesList.size(); i++) {
            if (verticesList.get(i).getSymbol().equalsIgnoreCase(vertex))
                return i;
        }
        return -1;
    }

    public void restartProgram() {
        vertices.clear();
        edges.clear();
    }
}
