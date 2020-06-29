package GraphTheory;

import GraphTheory.GraphModels.Edge;
import GraphTheory.GraphModels.Graph;
import GraphTheory.GraphModels.Vertex;

import java.util.ArrayList;
import java.util.Arrays;
import static GraphTheory.GraphModels.EdgeType.UNDIRECTED_EDGE;

public class ShortestPath {

    private Vertex source;

    private ArrayList<Vertex> vertices = new ArrayList<>();
    private ArrayList<Edge> edges = new ArrayList<>();

    private static ArrayList<ArrayList<Edge>> dijkstraSteps = new ArrayList<>();
    private static ArrayList<String> shortestSteps = new ArrayList<>();
    private String[] shortestPaths;
    private int[] distance;
    private static ArrayList<int[]> distances = new ArrayList<>();

    /**=============================================================================**/

    public Vertex getSource() {
        return source;
    }

    public void setSource(Vertex source) {
        this.source = source;
    }

    public ArrayList<Vertex> getVertices() {
        return vertices;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public static ArrayList<ArrayList<Edge>> getDijkstraSteps() {
        return dijkstraSteps;
    }

    public static ArrayList<String> getShortestSteps() {
        return shortestSteps;
    }

    public String[] getShortestPaths() {
        return shortestPaths;
    }

    public int[] getDistance() {
        return distance;
    }

    public static ArrayList<int[]> getDistances() {
        return distances;
    }

    /**=============================================================================**/

    private int getNearestNode(boolean[] inSet) {
        int index = -1;
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < Graph.getVertices().size(); i++) {
            if (!inSet[i] && (distance[i] <= min)) {
                min = distance[i];
                index = i;
            }
        }
        return index;
    }

    public void dijkstra() {
        int s;
        int[][] graph = Graph.buildWeightAdjacencyMatrix();
        int[] parent = new int[Graph.getVertices().size()];
        boolean[] added = new boolean[Graph.getVertices().size()];
        distance = new int[Graph.getVertices().size()];
        shortestPaths = new String[Graph.getVertices().size()];
        ArrayList<Edge> tree = new ArrayList<>();

        for (Vertex vertex : Graph.getVertices())
            vertices.add(new Vertex(vertex.getSymbol()));
        for (Edge edge : Graph.getEdges())
            edges.add(new Edge(edge));

        Arrays.fill(distance, Integer.MAX_VALUE);

        distance[Graph.getVertices().indexOf(source)] = 0;
        parent[Graph.getVertices().indexOf(source)] = -1;

        for (int i = 0; i < vertices.size(); i++) {
            s = getNearestNode(added);
            added[s] = true;

            StringBuilder path = new StringBuilder(vertices.get(s).getSymbol());
            for (int y = s; y != Graph.getVertices().indexOf(source); y = parent[y]) {
                int x = parent[y];
                path.insert(0, " -> ");
                path.insert(0, vertices.get(x).getSymbol());
                for (Edge edge : Graph.getEdges()) {
                    if ((edge.getVertex_First() == Graph.getVertices().get(x) && edge.getVertex_Second() == Graph.getVertices().get(y)) || (edge.getVertex_First() == Graph.getVertices().get(y) && edge.getVertex_Second() == Graph.getVertices().get(x) && edge.getType() == UNDIRECTED_EDGE))
                        if (!tree.contains(edge))
                            tree.add(edge);
                }
            }

            int[] dist = new int[vertices.size()];
            System.arraycopy(distance, 0, dist, 0, dist.length);

            distances.add(dist);
            shortestPaths[s] = path.toString();
            shortestSteps.add(path.toString());

            if (!tree.isEmpty()) {
                ArrayList<Edge> temp = new ArrayList<>();
                for (Edge edge : tree) {
                    temp.add(new Edge(edge));
                }
                dijkstraSteps.add(temp);
            }

            if (i == vertices.size() - 1)
                break;

            for (int t = 0; t < vertices.size(); t++) {
                if ((!added[t]) && (graph[s][t] != 0) && (distance[s] != Integer.MAX_VALUE) && ((distance[s] + graph[s][t]) < distance[t])) {
                    distance[t] = distance[s] + graph[s][t];
                    parent[t] = s;
                }
            }
        }
    }

    public void clear() {
        dijkstraSteps.clear();
        shortestSteps.clear();
        distances.clear();
        vertices.clear();
        edges.clear();
    }
}
