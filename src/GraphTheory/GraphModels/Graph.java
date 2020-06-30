package GraphTheory.GraphModels;

import java.util.*;

import static GraphTheory.GraphModels.EdgeType.UNDIRECTED_EDGE;

public class Graph {

    private static ArrayList<Vertex> vertices = new ArrayList<>();
    private static ArrayList<Edge> edges = new ArrayList<>();

    private static int[][] adjacency;
    private String[][] adjacencyMatrix;
    private String[][] incidenceMatrix;
    private String[][] representationMatrix;
    private Map<Vertex, List<Vertex>> adjacencyList = new HashMap<>();

    /**=============================================================================**/

    public Graph() {}

    public Graph(ArrayList<Vertex> vertices, ArrayList<Edge> edges) {
        Graph.vertices = vertices;
        Graph.edges = edges;
    }

    public static ArrayList<Vertex> getVertices() {
        return vertices;
    }

    public static ArrayList<Edge> getEdges() {
        return edges;
    }

    public static int[][] getAdjacency() {
        return adjacency;
    }

    public String[][] getAdjacencyMatrix() {
        return adjacencyMatrix;
    }

    public String[][] getIncidenceMatrix() {
        return incidenceMatrix;
    }

    public String[][] getRepresentationMatrix() {
        return representationMatrix;
    }

    public Map<Vertex, List<Vertex>> getAdjacencyList() {
        return adjacencyList;
    }

    /**=============================================================================**/

    private void matrixSetup(String[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {

            for (int j = 0; j < matrix.length; j++) {

                if (i == 0 && j == 0) {
                    matrix[i][j] = "  ";
                } else if (i == 0) {
                    matrix[i][j] = vertices.get(j - 1).getSymbol();
                } else if (j == 0) {
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
                } else if (j == 0) {
                    incidenceMatrix[i][j] = vertices.get(i - 1).getSymbol();
                } else {
                    incidenceMatrix[i][j] = "" + 0;
                }
            }
        }
    }

    public void graphRepresentation(){
        adjacency = new int[vertices.size()][vertices.size()];
        adjacencyMatrix = new String[vertices.size() + 1][vertices.size() + 1];
        incidenceMatrix = new String[vertices.size() + 1][edges.size() + 1];
        representationMatrix = new String[vertices.size() + 1][vertices.size() + 1];
        matrixSetup(adjacencyMatrix);
        incidenceMatrixSetup();
        matrixSetup(representationMatrix);

        for (Vertex vertex : vertices)
            adjacencyList.put(vertex, new ArrayList<>());

        for (int i = 0; i < edges.size(); i++) {
            int start = vertices.indexOf(edges.get(i).getVertex_First()) + 1;
            int end = vertices.indexOf(edges.get(i).getVertex_Second()) + 1;
            int value = Integer.parseInt(representationMatrix[start][end]);

            adjacency[start-1][end-1] = 1;
            adjacencyMatrix[start][end] = 1 + "";
            incidenceMatrix[start][i+1] = 1 + "";
            incidenceMatrix[end][i+1] = -1 + "";
            representationMatrix[start][end] = (value + 1) + "";
            adjacencyList.get(edges.get(i).getVertex_First()).add(edges.get(i).getVertex_Second());

            if (edges.get(i).getType() == UNDIRECTED_EDGE){
                value = Integer.parseInt(representationMatrix[end][start]);

                adjacency[end-1][start-1] = 1;
                adjacencyMatrix[end][start] = 1 + "";
                incidenceMatrix[end][i+1] = 1 + "";
                representationMatrix[end][start] = (value + 1) + "";
                adjacencyList.get(edges.get(i).getVertex_Second()).add(edges.get(i).getVertex_First());
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
