package GraphTheory;

import GraphTheory.GraphModels.Edge;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

import static GraphTheory.GraphModels.Graph.getVertices;
import static GraphTheory.GraphModels.Graph.getEdges;

public class MinimumSpanningTree {

    private ArrayList<Edge> MST = new ArrayList<>();

    private ArrayList<String> steps = new ArrayList<>();
    private static ArrayList<ArrayList<Edge>> mstSteps = new ArrayList<>();

    /**=============================================================================**/

    public ArrayList<Edge> getMST() {
        return MST;
    }

    public ArrayList<String> getSteps() {
        return steps;
    }

    public static ArrayList<ArrayList<Edge>> getMstSteps() {
        return mstSteps;
    }

    /**=============================================================================**/

    public void runKruskal() {
        PriorityQueue<Edge> pq = new PriorityQueue<>(getEdges().size(), Comparator.comparingInt(Edge::getWeight));
        pq.addAll(getEdges());

        int[] parent = new int[getVertices().size()];

        makeSet(parent);

        while (MST.size() < getVertices().size() - 1) {
            if (pq.isEmpty())
                break;


            Edge edge = pq.remove();

            StringBuilder step = new StringBuilder();
            step.append("Pick edge ").append(edge.getEdgeName()).append(" : ");

            int source = (getVertices().indexOf(edge.getVertex_First()));
            int x_set = find(parent, source);

            int dest = (getVertices().indexOf(edge.getVertex_Second()));
            int y_set = find(parent, dest);

            if (x_set != y_set) {
                step.append("No cycle is formed, include it in the minimum spanning tree.");
                MST.add(edge);
                union(parent, x_set, y_set);
            }
            else
                step.append("Including this edge in the minimum spanning tree results in a cycle, discard it.");

            steps.add(step.toString());

            ArrayList<Edge> temp = new ArrayList<>();
            for (Edge includedEdge : MST)
                temp.add(new Edge(includedEdge));
            mstSteps.add(temp);
        }

    }

    private void makeSet(int[] parent) {
        for (int i = 0; i < getVertices().size(); i++)
            parent[i] = i;
    }

    private int find(int[] parent, int vertex) {
        if (parent[vertex] != vertex)
            return find(parent, parent[vertex]);
        return vertex;
    }

    private void union(int[] parent, int x, int y) {
        int x_set_parent = find(parent, x);
        int y_set_parent = find(parent, y);
        parent[y_set_parent] = x_set_parent;
    }
}
