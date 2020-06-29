package GraphTheory.GraphModels;

public class Edge {

    private Vertex vertex_First;
    private Vertex vertex_Second;
    private EdgeType type;
    private String edgeName;
    private int weight;
    private int flow = 0;

    /**=============================================================================**/

    public Edge(Vertex vertex_First, Vertex vertex_Second, EdgeType type, String edgeName, int weight) {
        this.vertex_First = vertex_First;
        this.vertex_Second = vertex_Second;
        this.type = type;
        this.edgeName = edgeName;
        this.weight = weight;
    }

    public Edge(Edge edge) {
        vertex_First = edge.vertex_First;
        vertex_Second = edge.vertex_Second;
        type = edge.type;
        edgeName = edge.edgeName;
        weight = edge.weight;
        flow = edge.flow;
    }

    /**=============================================================================**/

    public Vertex getVertex_First() {
        return vertex_First;
    }

    public Vertex getVertex_Second() {
        return vertex_Second;
    }

    public EdgeType getType() {
        return type;
    }

    public String getEdgeName() {
        return edgeName;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getFlow() {
        return flow;
    }

    public void setFlow(int flow) {
        this.flow = flow;
    }
}
