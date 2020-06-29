package GraphTheory.GraphModels;

public class Vertex {

    private String symbol;

    public Vertex(String symbol) {
        this.symbol = symbol;
    }

    public Vertex(int symbol) {
        this.symbol = "" + symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }
}
