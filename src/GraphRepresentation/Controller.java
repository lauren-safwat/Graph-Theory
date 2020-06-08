package GraphRepresentation;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static GraphRepresentation.EdgeType.DIRECTED_EDGE;
import static GraphRepresentation.EdgeType.UNDIRECTED_EDGE;


public class Controller implements Initializable {

    @FXML Pane capGraph;
    @FXML Pane flowGraph;
    @FXML Pane shortestPathGraph;

    @FXML ComboBox graphType;

    @FXML Label maxFlowStepNum;
    @FXML Label shortestPathStepNum;
    @FXML Label sourceAndSink;

    @FXML Button addVertex;
    @FXML Button deleteVertex;
    @FXML Button finishVertex;
    @FXML Button addEdge;
    @FXML Button updateEdge;
    @FXML Button deleteEdge;
    @FXML Button finishEdge;
    @FXML Button addSource;
    @FXML Button restartProgram;
    @FXML Button nextStep;
    @FXML Button previousStep;
    @FXML Button nextStep1;
    @FXML Button previousStep1;

    @FXML TextField edgeFrom;
    @FXML TextField edgeTo;
    @FXML TextField edgeName;
    @FXML TextField edgeWeight;
    @FXML TextField vSymbol;
    @FXML TextField sourceVertex;
    @FXML TextField sinkVertex;
    @FXML TextField maxFlowResult;
    @FXML TextField maxFlowPath;
    @FXML TextField shortestPath;

    @FXML TableView<Edge> dataTable;
    @FXML TableColumn<Edge, String> edgeNameCol = new TableColumn<>("Edge Name");
    @FXML TableColumn<Edge, String> edgeFromCol = new TableColumn<>("From");
    @FXML TableColumn<Edge, String> edgeToCol = new TableColumn<>("To");
    @FXML TableColumn<Edge, String> edgeWeightCol = new TableColumn<>("Weight");

    @FXML TableView<List<StringProperty>> shortestPathData;
    @FXML TableColumn<List<StringProperty>, String> vertexCol = new TableColumn<>("Vertex");
    @FXML TableColumn<List<StringProperty>, String> distanceCol = new TableColumn<>("Distance from Source");
    @FXML TableColumn<List<StringProperty>, String> pathCol = new TableColumn<>("Path");

    ObservableList<Edge> edgeData;
    ObservableList<List<StringProperty>> pathData = FXCollections.observableArrayList();

    Alert alert = new Alert(Alert.AlertType.ERROR);

    public static int index = 0, index1 = 0;
    public static String chosen;
    public static boolean flag, input, update = false;

    Graph G = new Graph();

    public Controller() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        edgeWeight.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    edgeWeight.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        edgeNameCol.setCellValueFactory(new PropertyValueFactory<>("edgeName"));
        edgeFromCol.setCellValueFactory(new PropertyValueFactory<>("vertex_First"));
        edgeToCol.setCellValueFactory(new PropertyValueFactory<>("vertex_Second"));
        edgeWeightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));
        fillTable();

        alert.setTitle("Wrong Input");
        alert.setHeaderText("Kindly, check your input.");

        dataTable.setTooltip(new Tooltip("Displays entire graph's data you entered"));
        graphType.setTooltip(new Tooltip("You should specify graph's type if directed or undirected"));
        addVertex.setTooltip(new Tooltip("Press this button to add the vertex you entered"));
        deleteVertex.setTooltip(new Tooltip("Press this button to delete the vertex you entered"));
        finishVertex.setTooltip(new Tooltip("Press this button to finish adding vertices"));
        addEdge.setTooltip(new Tooltip("Press this button after filling Edge's data above to add edge"));
        updateEdge.setTooltip(new Tooltip("Press this button after filling Edge's data above to update edge"));
        deleteEdge.setTooltip(new Tooltip("Press this button after filling Edge's start and end to delete edge"));
        finishEdge.setTooltip(new Tooltip("Press this button to finish adding edges and display results"));
        restartProgram.setTooltip(new Tooltip("To restart entire program and delete previous results"));
        edgeFrom.setTooltip(new Tooltip("Name of vertex where edge starts from"));
        edgeTo.setTooltip(new Tooltip("Name of vertex where edge ends at"));
        edgeName.setTooltip(new Tooltip("Name of the edge"));
        edgeWeight.setTooltip(new Tooltip("Enter weight of the edge"));
        vSymbol.setTooltip(new Tooltip("Here you enter the name of vertex"));
        nextStep.setTooltip(new Tooltip("Go to next step"));
        previousStep.setTooltip(new Tooltip("Go to previous step"));
        sourceVertex.setTooltip(new Tooltip("Type the name of source vertex"));
        sinkVertex.setTooltip(new Tooltip("Type the name of sink (destination) vertex"));
        maxFlowResult.setTooltip(new Tooltip("Maximum flow for graph appears here"));
        addSource.setTooltip(new Tooltip("When done adding source and sink vertices"));
        nextStep1.setTooltip(new Tooltip("Go to next step"));
        previousStep1.setTooltip(new Tooltip("Go to previous step"));
        shortestPath.setTooltip(new Tooltip("Displays shortest path from Source vertex to all other vertices"));
    }

    public void fillTable() {
        edgeData = FXCollections.observableArrayList();
        edgeData.addAll(G.edges);
        dataTable.setItems(edgeData);
    }

    public void fillTable2() {
        vertexCol.setCellValueFactory(data -> data.getValue().get(0));
        distanceCol.setCellValueFactory(data -> data.getValue().get(1));
        pathCol.setCellValueFactory(data -> data.getValue().get(2));

        for (int i = 0; i < Graph.vertices.size(); i++) {
            List<StringProperty> row = new ArrayList<>();
            row.add(new SimpleStringProperty(Graph.vertices.get(i).symbol));
            row.add(new SimpleStringProperty(G.distance[i] + ""));
            row.add(new SimpleStringProperty(G.shortestPaths[i]));
            pathData.add(row);
        }
        shortestPathData.setItems(pathData);
    }

    public void enableAddVertex() {
        if (!vSymbol.getText().isEmpty()){
            addVertex.setDisable(false);
            if(Graph.vertices.size() != 0)
                deleteVertex.setDisable(false);
        }
        else {
            addVertex.setDisable(true);
            deleteVertex.setDisable(true);
        }
    }

    public void addVertex() {
        if(searchVertex(vSymbol.getText()) != -1){
            alert.setContentText("This vertex is already added to the graph!");
            alert.showAndWait();
        }
        else{
            Graph.vertices.add(new Vertex(vSymbol.getText()));
        }
        vSymbol.clear();
        if(Graph.vertices.size() > 1)
            finishVertex.setDisable(false);
        deleteVertex.setDisable(true);
        addVertex.setDisable(true);
    }

    public void deleteVertex() {
        int index = searchVertex(vSymbol.getText());
        if(index == -1){
            alert.setContentText("No such vertex with this name is found!");
            alert.showAndWait();
        }
        else{
            Graph.vertices.remove(index);
        }
        vSymbol.clear();
        if(Graph.vertices.size() > 1)
            finishVertex.setDisable(false);
        else
            finishVertex.setDisable(true);
        addVertex.setDisable(true);
        deleteVertex.setDisable(true);
    }

    public void finishVertexButton() {
        vSymbol.setDisable(true);
        finishVertex.setDisable(true);
        addVertex.setDisable(true);
        deleteVertex.setDisable(true);

        sourceVertex.setDisable(false);
        sinkVertex.setDisable(false);
    }

    public void enableAddEdge() {
        if(edgeTo.getText().isEmpty() || edgeFrom.getText().isEmpty())
            deleteEdge.setDisable(true);
        else if(G.edges.size() != 0)
            deleteEdge.setDisable(false);

        if (edgeTo.getText().isEmpty() || edgeFrom.getText().isEmpty() || edgeName.getText().isEmpty() || edgeWeight.getText().isEmpty() || graphType.getSelectionModel().isEmpty()){
            addEdge.setDisable(true);
            updateEdge.setDisable(true);
        }
        else{
            addEdge.setDisable(false);
            if(G.edges.size() != 0)
                updateEdge.setDisable(false);
        }
    }

    public void addEdge() {
        input = true;
        if (manageInput()) {
            if (!flag) {
                chosen = graphType.getSelectionModel().getSelectedItem().toString();
                graphType.setDisable(true);
            }
            if (chosen.equals("Undirected"))
                flag = true;
            else
                flag = false;

            int idx = searchVertex(edgeFrom.getText());
            int idx2 = searchVertex(edgeTo.getText());
            int cost = Integer.parseInt(edgeWeight.getText());
            if (idx != -1 || idx2 != -1) {
                if (flag)
                    G.edges.add(new Edge(G.vertices.get(idx), G.vertices.get(idx2), UNDIRECTED_EDGE, edgeName.getText(), cost));
                else
                    G.edges.add(new Edge(G.vertices.get(idx), G.vertices.get(idx2), DIRECTED_EDGE, edgeName.getText(), cost));
            }
            resetEdge();
            finishEdge.setDisable(false);
        } else {
            alert.showAndWait();
            resetEdge();
        }
    }

    public void updateEdge() {
        update = true;
        addEdge();
        update = false;
    }

    public void deleteEdge() {
        for (Edge edge : G.edges) {
            if(edge.vertex_First.symbol.equalsIgnoreCase(edgeFrom.getText()) && edge.vertex_Second.symbol.equalsIgnoreCase(edgeTo.getText())){
                G.edges.remove(edge);
                resetEdge();
                return;
            }
            if(edge.vertex_First.symbol.equalsIgnoreCase(edgeTo.getText()) && edge.vertex_Second.symbol.equalsIgnoreCase(edgeFrom.getText()) && flag){
                G.edges.remove(edge);
                resetEdge();
                return;
            }
        }
        alert.setContentText("No such edge exists");
        alert.showAndWait();
        resetEdge();
    }

    public void enableAddSource() {
        if (sourceVertex.getText().isEmpty() || sinkVertex.getText().isEmpty())
            addSource.setDisable(true);
        else
            addSource.setDisable(false);
    }

    public void addSourceButton() {
        input = false;
        if (manageInput()) {
            G.source = Graph.vertices.get(searchVertex(sourceVertex.getText()));
            G.sink = Graph.vertices.get(searchVertex(sinkVertex.getText()));
            sourceVertex.clear();
            sinkVertex.clear();
            addSource.setDisable(true);

            edgeTo.setDisable(false);
            edgeFrom.setDisable(false);
            edgeName.setDisable(false);
            graphType.setDisable(false);
            edgeWeight.setDisable(false);
            vSymbol.setDisable(true);
            finishVertex.setDisable(true);
            addVertex.setDisable(true);
            sourceVertex.setDisable(true);
            sinkVertex.setDisable(true);
            addSource.setDisable(true);
        } else {
            alert.showAndWait();
        }
    }

    public void maxFlowGraph(int ind) {
        maxFlowStepNum.setText("Step #" + (ind + 1) + " :");

        if (ind == 0)
            maxFlowPath.setText("Initial state...");
        else
            maxFlowPath.setText("Path #" + ind + ":  " + G.maxFlowPaths.get(ind - 1));

        capGraph.getChildren().clear();
        flowGraph.getChildren().clear();
        GraphDrawer pl = new GraphDrawer();
        GraphDrawer pl2 = new GraphDrawer();
        pl.start(capGraph, G.maxFlowSteps.get(ind), ind, true, false);
        pl2.start(flowGraph, G.maxFlowSteps.get(ind), ind, false, false);
    }

    public void maxFlowPrevStep() {
        index--;
        maxFlowGraph(index);
        nextStep.setDisable(false);
        if (index == 0)
            previousStep.setDisable(true);
    }

    public void maxFlowNextStep() {
        index++;
        maxFlowGraph(index);
        previousStep.setDisable(false);
        if (index == (G.maxFlowSteps.size() - 1))
            nextStep.setDisable(true);
    }

    public void shortestPathGraph(int ind) {
        shortestPathStepNum.setText("Step #" + (ind + 1) + " :");
        if(ind == 0)
            shortestPath.setText("Initial state..");
        else
            shortestPath.setText("Path #" + ind + ": " + Graph.shortestSteps.get(ind));

        shortestPathGraph.getChildren().clear();
        GraphDrawer pl3 = new GraphDrawer();
        pl3.start(shortestPathGraph, G.edges, ind, false, true);
    }

    public void shortestPathPrevStep() {
        index1--;
        shortestPathGraph(index1);
        nextStep1.setDisable(false);
        if (index1 == 0)
            previousStep1.setDisable(true);
    }

    public void shortestPathNextStep() {
        index1++;
        shortestPathGraph(index1);
        previousStep1.setDisable(false);
        if (index1 == (Graph.dijkstraSteps.size()))
            nextStep1.setDisable(true);
    }

    public void resetEdge() {
        edgeFrom.clear();
        edgeTo.clear();
        edgeName.clear();
        edgeWeight.clear();
        addEdge.setDisable(true);
        updateEdge.setDisable(true);
        deleteEdge.setDisable(true);
    }

    public int searchVertex(String vertex) {
        for (int i = 0; i < Graph.vertices.size(); i++) {
            if (Graph.vertices.get(i).getSymbol().equalsIgnoreCase(vertex))
                return i;
        }
        return -1;
    }

    public void displayResults() {
        addEdge.setDisable(true);
        updateEdge.setDisable(true);
        deleteEdge.setDisable(true);
        finishEdge.setDisable(true);
        edgeFrom.setDisable(true);
        edgeTo.setDisable(true);
        edgeName.setDisable(true);
        edgeWeight.setDisable(true);
        fillTable();
        dataTable.setEditable(true);
        restartProgram.setDisable(false);
        sourceAndSink.setText("Source: " + G.source.symbol + "\nSink: " + G.sink.symbol);
        int maxFlow = G.fordFulkerson();
        maxFlowResult.setText("Maximum flow = " + maxFlow);
        maxFlowGraph(0);
        nextStep.setDisable(false);
        G.dijkstra();
        shortestPathGraph(0);
        nextStep1.setDisable(false);
        fillTable2();

    }

    public void restart() {
        index = 0;
        index1 = 0;
        flag = false;
        vSymbol.setDisable(false);
        previousStep.setDisable(true);
        nextStep.setDisable(true);
        previousStep1.setDisable(true);
        nextStep1.setDisable(true);
        dataTable.getItems().clear();
        capGraph.getChildren().clear();
        flowGraph.getChildren().clear();
        maxFlowPath.clear();
        maxFlowResult.clear();
        maxFlowStepNum.setText("");
        shortestPathData.getItems().clear();
        shortestPathStepNum.setText("");
        shortestPathGraph.getChildren().clear();
        shortestPath.clear();
        sourceAndSink.setText("");
        graphType.getSelectionModel().clearSelection();
        G.restartProgram();
        G = new Graph();
    }

    public boolean manageInput() {
        alert.setContentText("Input contains vertices not already added, double check it.");

        if (!input) { //check for source and sink data entry
            if ((searchVertex(sourceVertex.getText()) == -1) || (searchVertex(sinkVertex.getText()) == -1))
                return false;
            if(sourceVertex.getText().equalsIgnoreCase(sinkVertex.getText())){
                alert.setContentText("Source and sink can not be the same vertex!");
                return false;
            }
        }
        else { //check for edge data entry
            if ((searchVertex(edgeTo.getText()) == -1) || (searchVertex(edgeFrom.getText()) == -1))
                return false;

            if(edgeFrom.getText().equalsIgnoreCase(edgeTo.getText())){
                alert.setContentText("There can't be an edge from a vertex to itself!");
                return false;
            }

            for (int j = 0; j < G.edges.size(); j++) {
                if (G.edges.get(j).vertex_First.symbol.equalsIgnoreCase(edgeTo.getText())
                        && G.edges.get(j).vertex_Second.symbol.equalsIgnoreCase(edgeFrom.getText()) && flag){
                    if(update){
                        G.edges.remove(j);
                        return true;
                    }
                    alert.setContentText("There is already an edge between " + edgeTo.getText() + " and " + edgeFrom.getText());
                    return false;
                }
                if (G.edges.get(j).vertex_First.symbol.equalsIgnoreCase(edgeFrom.getText())
                        && G.edges.get(j).vertex_Second.symbol.equalsIgnoreCase(edgeTo.getText())){
                    if(update){
                        G.edges.remove(j);
                        return true;
                    }
                    alert.setContentText("There is already an edge between " + edgeFrom.getText() + " and " + edgeTo.getText());
                    return false;
                }

            }
            if(update){
                alert.setContentText("No such edge is found!");
                return false;
            }

        }

        return true;
    }

}
