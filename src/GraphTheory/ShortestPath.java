package GraphTheory;

import java.util.ArrayList;
import java.util.Arrays;
import static GraphTheory.EdgeType.UNDIRECTED_EDGE;

public class ShortestPath {

    public Vertex source;

    public ArrayList<Vertex> vertices = new ArrayList<>();
    public ArrayList<Edge> edges = new ArrayList<>();

    public static ArrayList<ArrayList<Edge>> dijkstraSteps = new ArrayList<>();
    public static ArrayList<String> shortestSteps = new ArrayList<>();
    public String[] shortestPaths;
    public int[] distance;
    public static ArrayList<int[]> distances = new ArrayList<>();

    /**=============================================================================**/

    private int getNearestNode(boolean[] inSet) {
        int index = -1;
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < Graph.vertices.size(); i++) {
            if (!inSet[i] && (distance[i] <= min)) {
                min = distance[i];
                index = i;
            }
        }
        return index;
    }

    public void dijkstra() {
        int s;
        int[][] graph = Graph.buildAdjacencyMatrix();
        int[] parent = new int[Graph.vertices.size()];
        boolean[] added = new boolean[Graph.vertices.size()];
        distance = new int[Graph.vertices.size()];
        shortestPaths = new String[Graph.vertices.size()];
        ArrayList<Edge> tree = new ArrayList<>();

        for (Vertex vertex : Graph.vertices)
            vertices.add(new Vertex(vertex.symbol));
        for (Edge edge : Graph.edges)
            edges.add(new Edge(edge));

        Arrays.fill(distance, Integer.MAX_VALUE);

        distance[Graph.vertices.indexOf(source)] = 0;
        parent[Graph.vertices.indexOf(source)] = -1;

        for (int i = 0; i < vertices.size(); i++) {
            s = getNearestNode(added);
            added[s] = true;

            StringBuilder path = new StringBuilder(vertices.get(s).symbol);
            for (int y = s; y != Graph.vertices.indexOf(source); y = parent[y]) {
                int x = parent[y];
                path.insert(0, " -> ");
                path.insert(0, vertices.get(x).symbol);
                for (Edge edge : Graph.edges) {
                    if ((edge.vertex_First == Graph.vertices.get(x) && edge.vertex_Second == Graph.vertices.get(y)) || (edge.vertex_First == Graph.vertices.get(y) && edge.vertex_Second == Graph.vertices.get(x) && edge.type == UNDIRECTED_EDGE))
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
