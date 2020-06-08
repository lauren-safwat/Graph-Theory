package GraphRepresentation;

import java.util.ArrayList;
import java.util.LinkedList;

import static GraphRepresentation.EdgeType.UNDIRECTED_EDGE;

public class Graph {

    public Vertex source;
    public Vertex sink;
    public ArrayList<Edge> edges = new ArrayList<>();
    public static ArrayList<Vertex> vertices = new ArrayList<>();
    public ArrayList<ArrayList<Edge>> maxFlowSteps = new ArrayList<>();
    public ArrayList<String> maxFlowPaths = new ArrayList<>();
    public static ArrayList<ArrayList<Edge>> dijkstraSteps = new ArrayList<>();
    public static ArrayList<String> shortestSteps = new ArrayList<>();
    public String[] shortestPaths;
    public int[] distance;
    public static ArrayList<int[]> distances = new ArrayList<>();

    public Graph() {}

    public Graph(ArrayList<Vertex> vertices, ArrayList<Edge> edges) {
        this.vertices = vertices;
        this.edges = edges;
    }

    private int[][] buildAdjacencyMatrix() {
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

    public void restartProgram() {
        vertices.clear();
        edges.clear();
        maxFlowPaths.clear();
        maxFlowSteps.clear();
        dijkstraSteps.clear();
        shortestSteps.clear();
        distances.clear();
    }

    //-----------------------------------------------------------------------

    private boolean BFS(int[][] residualGraph, int[] parent) {
        boolean[] visited = new boolean[vertices.size()];
        LinkedList<Integer> queue = new LinkedList<>();
        queue.add(vertices.indexOf(source));
        visited[vertices.indexOf(source)] = true;
        parent[vertices.indexOf(source)] = -1;

        while (!queue.isEmpty()) {
            int s = queue.poll();
            for (int t = 0; t < vertices.size(); t++) {
                if (!visited[t] && residualGraph[s][t] > 0) {
                    queue.add(t);
                    parent[t] = s;
                    visited[t] = true;
                }
            }
        }
        return (visited[vertices.indexOf(sink)]);
    }

    public int fordFulkerson() {
        int[][] residualGraph = buildAdjacencyMatrix();
        int[] parent = new int[vertices.size()];
        int maxFlow = 0;

        ArrayList<Edge> allEdges = new ArrayList<>();
        for (Edge edge : edges)
            allEdges.add(new Edge(edge));

        maxFlowSteps.add(edges);

        while (BFS(residualGraph, parent)) {

            int pathFlow = Integer.MAX_VALUE;
            for (int t = vertices.indexOf(sink); t != vertices.indexOf(source); t = parent[t]) {
                int s = parent[t];
                pathFlow = Math.min(pathFlow, residualGraph[s][t]);
            }

            String path = vertices.get(vertices.indexOf(sink)).symbol;
            for (int t = vertices.indexOf(sink); t != vertices.indexOf(source); t = parent[t]) {
                int s = parent[t];
                residualGraph[s][t] -= pathFlow;
                residualGraph[t][s] += pathFlow;
                for (Edge edge : allEdges) {
                    if( s == vertices.indexOf(edge.vertex_First) && t == vertices.indexOf(edge.vertex_Second) ){
                        edge.weight = residualGraph[s][t];
                        edge.flow += pathFlow;
                        path = " -> " + path;
                        path = edge.vertex_First + path;
                    }
                    else if((s == vertices.indexOf(edge.vertex_Second) && t == vertices.indexOf(edge.vertex_First) && edge.type == UNDIRECTED_EDGE)){
                        edge.weight = residualGraph[t][s];
                        edge.flow += pathFlow;
                        path = " -> " + path;
                        path = edge.vertex_Second + path;
                    }
                    else if (s == vertices.indexOf(edge.vertex_Second) && t == vertices.indexOf(edge.vertex_First)){
                        edge.weight = residualGraph[t][s];
                    }
                }
            }

            path += (",   Path flow = " + pathFlow);

            ArrayList<Edge> temp = new ArrayList<>();
            for (Edge edge : allEdges) {
                temp.add(new Edge(edge));
            }

            maxFlowSteps.add(temp);
            maxFlowPaths.add(path);

            maxFlow += pathFlow;
        }

        return maxFlow;
    }

    //-----------------------------------------------------------------------

    private int getNearestNode(int distance[], boolean inSet[]) {
        int index = -1;
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < vertices.size(); i++) {
            if (!inSet[i] && distance[i] <= min) {
                min = distance[i];
                index = i;
            }
        }
        return index;
    }

    public void dijkstra() {
        int s = 0;
        int[][] graph = buildAdjacencyMatrix();
        int[] parent = new int[vertices.size()];
        boolean[] added = new boolean[vertices.size()];
        distance = new int[vertices.size()];
        shortestPaths = new String[vertices.size()];
        ArrayList<Edge> tree = new ArrayList<>();

        for (int i = 0; i < distance.length; i++)
            distance[i] = Integer.MAX_VALUE;

        distance[vertices.indexOf(source)] = 0;
        parent[vertices.indexOf(source)] = -1;

        for (int i = 0; i < vertices.size(); i++) {
            s = getNearestNode(distance, added);
            added[s] = true;

            String path = vertices.get(s).symbol;
            for (int y = s; y != vertices.indexOf(source); y = parent[y]) {
                int x = parent[y];
                path = " -> " + path;
                path = vertices.get(x).symbol + path;
                for (Edge edge : edges) {
                    if ((edge.vertex_First == vertices.get(x) && edge.vertex_Second == vertices.get(y)) || (edge.vertex_First == vertices.get(y) && edge.vertex_Second == vertices.get(x) && edge.type == UNDIRECTED_EDGE))
                        if (!tree.contains(edge))
                            tree.add(edge);
                }
            }

            int dist[] = new int[vertices.size()];
            for (int j = 0; j < dist.length; j++) {
                dist[j] = distance[j];
            }
            distances.add(dist);
            shortestPaths[s] = path;
            shortestSteps.add(path);

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
}
