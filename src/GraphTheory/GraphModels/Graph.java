package GraphTheory.GraphModels;

import java.util.*;

import static GraphTheory.GraphModels.EdgeType.UNDIRECTED_EDGE;

public class Graph {

    private static ArrayList<Vertex> vertices = new ArrayList<>();
    private static ArrayList<Edge> edges = new ArrayList<>();

    String[][] adjacencyMatrix;
    String[][] incidenceMatrix;
    String[][] representationMatrix;
    Map<Vertex, List<Vertex>> adjacencyList = new HashMap<>();

    /**=============================================================================**/

    public static ArrayList<Vertex> getVertices() {
        return vertices;
    }

    public static ArrayList<Edge> getEdges() {
        return edges;
    }

    /**=============================================================================**/

    public Graph() {}

    public Graph(ArrayList<Vertex> vertices, ArrayList<Edge> edges) {
        Graph.vertices = vertices;
        Graph.edges = edges;
    }

    public void buildAdjacencyList() {
        for (Vertex vertex : vertices)
            adjacencyList.put(vertex, new ArrayList<>());

        for (Edge edge : edges) {
            adjacencyList.get(edge.getVertex_First()).add(edge.getVertex_Second());
            if (edge.getType() == UNDIRECTED_EDGE)
                adjacencyList.get(edge.getVertex_Second()).add(edge.getVertex_First());
        }
    }

    private void matrixSetup(String[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {

            for (int j = 0; j < matrix.length; j++) {

                if (i == 0 && j == 0) {
                    matrix[i][j] = "  ";
                } else if (i == 0 && j != 0) {
                    matrix[i][j] = vertices.get(j - 1).getSymbol();
                } else if (j == 0 && i != 0) {
                    matrix[i][j] = vertices.get(i - 1).getSymbol();
                } else {
                    matrix[i][j] = "" + 0;
                }
            }
        }
    }

    private void incidenceMatrixSetup() {
        for (int i = 0; i < incidenceMatrix.length; i++) {
            for (int j = 0; j < incidenceMatrix[0].length; j++) {
                if (i == 0 && j == 0) {
                    incidenceMatrix[i][j] = "  ";
                } else if (i == 0) {
                    incidenceMatrix[i][j] = edges.get(j - 1).getEdgeName();
                } else if (j == 0 && i != 0) {
                    incidenceMatrix[i][j] = vertices.get(i - 1).getSymbol();
                } else {
                    incidenceMatrix[i][j] = "" + 0;
                }
            }
        }
    }

    public void buildIncidenceMatrix() {
        incidenceMatrix = new String[vertices.size() + 1][edges.size() + 1];
        incidenceMatrixSetup();

        for (int i = 0; i < edges.size(); i++) {
            int start = vertices.indexOf(edges.get(i).getVertex_First()) + 1;
            int end = vertices.indexOf(edges.get(i).getVertex_Second()) + 1;
            incidenceMatrix[start][i+1] = 1 + "";
            if (edges.get(i).getType() == UNDIRECTED_EDGE)
                incidenceMatrix[end][i+1] = 1 + "";
            else
                incidenceMatrix[end][i+1] = -1 + "";
        }
    }

    public void buildAdjacencyMatrix() {
        adjacencyMatrix = new String[vertices.size() + 1][vertices.size() + 1];
        matrixSetup(adjacencyMatrix);

        for (Edge edge : edges) {
            int start = vertices.indexOf(edge.getVertex_First()) + 1;
            int end = vertices.indexOf(edge.getVertex_Second()) + 1;
            adjacencyMatrix[start][end] = 1 + "";
            if (edge.getType() == UNDIRECTED_EDGE)
                adjacencyMatrix[end][start] = 1 + "";
        }
    }

    public void buildRepresentationMatrix() {
        representationMatrix = new String[vertices.size() + 1][vertices.size() + 1];
        matrixSetup(representationMatrix);

        for (Edge edge : edges) {
            int start = vertices.indexOf(edge.getVertex_First()) + 1;
            int end = vertices.indexOf(edge.getVertex_Second()) + 1;
            int value = Integer.parseInt(representationMatrix[start][end]);
            representationMatrix[start][end] = (value + 1) + "";
            if (edge.getType() == UNDIRECTED_EDGE) {
                value = Integer.parseInt(representationMatrix[end][start]);
                representationMatrix[end][start] = (value + 1) + "";
            }
        }
    }

    public static int[][] buildWeightAdjacencyMatrix() {
        int[][] graph = new int[vertices.size()][vertices.size()];
        for (Edge edge : edges) {
            int s = vertices.indexOf(edge.getVertex_First());
            int t = vertices.indexOf(edge.getVertex_Second());
            graph[s][t] = edge.getWeight();
            if (edge.getType() == UNDIRECTED_EDGE)
                graph[t][s] = edge.getWeight();
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
