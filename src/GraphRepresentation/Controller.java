package GraphRepresentation;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
    @FXML Label source;

    @FXML Button addVertex;
    @FXML Button deleteVertex;
    @FXML Button addSourceAndSink;
    @FXML Button addSource;
    @FXML Button addEdge;
    @FXML Button updateEdge;
    @FXML Button deleteEdge;
    @FXML Button executeMaximumFlow;
    @FXML Button executeShortestPath;
    @FXML Button maxFlowNextStep;
    @FXML Button maxFlowPreviousStep;
    @FXML Button shortestPathNextStep;
    @FXML Button shortestPathPreviousStep;
    @FXML Button restartProgram;

    @FXML TextField vSymbol;
    @FXML TextField maxFlowSource;
    @FXML TextField maxFlowSink;
    @FXML TextField shortestPathSource;
    @FXML TextField edgeFrom;
    @FXML TextField edgeTo;
    @FXML TextField edgeName;
    @FXML TextField edgeWeight;
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

    public static int maxFlowStepIndex = 0, shortestPathStepIndex = 0;
    public static String chosen;
    public static boolean flag, delete = false, update = false;

    Graph G = new Graph();


    /** =========================================================== **/

    public Controller() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        edgeWeight.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                edgeWeight.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        edgeNameCol.setCellValueFactory(new PropertyValueFactory<>("edgeName"));
        edgeFromCol.setCellValueFactory(new PropertyValueFactory<>("vertex_First"));
        edgeToCol.setCellValueFactory(new PropertyValueFactory<>("vertex_Second"));
        edgeWeightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));

        alert.setTitle("Wrong Input");
        alert.setHeaderText("Kindly, check your input.");

        dataTable.setTooltip(new Tooltip("Displays the edges you entered"));
        graphType.setTooltip(new Tooltip("To specify graph's type if directed or undirected"));
        addVertex.setTooltip(new Tooltip("Press this button to add the vertex you entered"));
        deleteVertex.setTooltip(new Tooltip("Press this button to delete the vertex you entered"));
        addEdge.setTooltip(new Tooltip("Press this button after filling Edge's data above to add edge"));
        updateEdge.setTooltip(new Tooltip("Press this button after filling Edge's data above to update edge"));
        deleteEdge.setTooltip(new Tooltip("Press this button after filling Edge's start and end to delete edge"));
        executeShortestPath.setTooltip(new Tooltip("Execute Dijkstra's algorithm and display results"));
        executeMaximumFlow.setTooltip(new Tooltip("Execute Ford-Fulkerson algorithm and display results"));
        restartProgram.setTooltip(new Tooltip("To restart entire program and delete previous results"));
        edgeFrom.setTooltip(new Tooltip("Name of vertex where edge starts from"));
        edgeTo.setTooltip(new Tooltip("Name of vertex where edge ends at"));
        edgeName.setTooltip(new Tooltip("Name of the edge"));
        edgeWeight.setTooltip(new Tooltip("Enter weight of the edge"));
        vSymbol.setTooltip(new Tooltip("Here you enter the name of vertex"));
        maxFlowNextStep.setTooltip(new Tooltip("Go to next step"));
        maxFlowPreviousStep.setTooltip(new Tooltip("Go to previous step"));
        shortestPathSource.setTooltip(new Tooltip("Type the name of maxFlowSource vertex for shortest paths"));
        maxFlowSource.setTooltip(new Tooltip("Type the name of maxFlowSource vertex for max flow"));
        maxFlowSink.setTooltip(new Tooltip("Type the name of sink (destination) vertex for max flow"));
        maxFlowResult.setTooltip(new Tooltip("Maximum flow for graph appears here"));
        addSourceAndSink.setTooltip(new Tooltip("Press this button to add maxFlowSource and sink vertices for max flow algorithm"));
        addSource.setTooltip(new Tooltip("Press this button to add maxFlowSource vertix for shortest paths algorithm"));
        shortestPathNextStep.setTooltip(new Tooltip("Go to next step"));
        shortestPathPreviousStep.setTooltip(new Tooltip("Go to previous step"));
        shortestPath.setTooltip(new Tooltip("Displays shortest path from Source vertex to all other vertices"));
    }

    /** =========================================================== **/

    public void fillGraphDataTable() {
        edgeData = FXCollections.observableArrayList();
        edgeData.addAll(G.edges);
        dataTable.setItems(edgeData);
    }

    public void fillShortestPathsTable() {
        vertexCol.setCellValueFactory(data -> data.getValue().get(0));
        distanceCol.setCellValueFactory(data -> data.getValue().get(1));
        pathCol.setCellValueFactory(data -> data.getValue().get(2));

        for (int i = 0; i < G.vertices.size(); i++) {
            List<StringProperty> row = new ArrayList<>();
            row.add(new SimpleStringProperty(G.vertices.get(i).symbol));
            row.add(new SimpleStringProperty(G.distance[i] + ""));
            row.add(new SimpleStringProperty(G.shortestPaths[i]));
            pathData.add(row);
        }
        shortestPathData.setItems(pathData);
    }

    /** =========================================================== **/

    public void enableVertexButtons() {
        if (!vSymbol.getText().isEmpty()){
            addVertex.setDisable(false);
            if(G.vertices.size() != 0)
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
            G.vertices.add(new Vertex(vSymbol.getText()));
        }
        vSymbol.clear();
        deleteVertex.setDisable(true);
        addVertex.setDisable(true);
        enableMaxFlowExecution();
        enableShortestPathExecution();
    }

    public void deleteVertex() {
        int index = searchVertex(vSymbol.getText());
        if(index == -1){
            alert.setContentText("No such vertex with this name is found!");
            alert.showAndWait();
        }
        else{
            G.vertices.remove(index);
        }
        vSymbol.clear();
        addVertex.setDisable(true);
        deleteVertex.setDisable(true);
        enableMaxFlowExecution();
        enableShortestPathExecution();
    }

    public int searchVertex(String vertex) {
        for (int i = 0; i < G.vertices.size(); i++) {
            if (G.vertices.get(i).getSymbol().equalsIgnoreCase(vertex))
                return i;
        }
        return -1;
    }

    /** =========================================================== **/

    public void enableAddSourceAndSink() {
        if (maxFlowSource.getText().isEmpty() || maxFlowSink.getText().isEmpty())
            addSourceAndSink.setDisable(true);
        else
            addSourceAndSink.setDisable(false);
    }

    public void addSourceAndSink() {
        if (validateSourceAndSink()) {
            G.maxFlowSource = G.vertices.get(searchVertex(maxFlowSource.getText()));
            G.sink = G.vertices.get(searchVertex(maxFlowSink.getText()));

            maxFlowSource.clear();
            maxFlowSink.clear();
            addSourceAndSink.setDisable(true);

        } else {
            alert.showAndWait();
        }
        enableMaxFlowExecution();
        enableShortestPathExecution();
    }

    /** =========================================================== **/

    public void enableAddSource() {
        if (shortestPathSource.getText().isEmpty())
            addSource.setDisable(true);
        else
            addSource.setDisable(false);
    }

    public void addSource() {
        if (validateSource()) {
            G.shortestPathSource = G.vertices.get(searchVertex(shortestPathSource.getText()));

            shortestPathSource.clear();
            addSource.setDisable(true);
        } else {
            alert.showAndWait();
        }
        enableMaxFlowExecution();
        enableShortestPathExecution();
    }

    /** =========================================================== **/

    public void enableEdgeButtons() {
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
        if (validateEdgeInput()) {
            if (!flag) {
                chosen = graphType.getSelectionModel().getSelectedItem().toString();
                graphType.setDisable(true);
            }
            if (chosen.equals("Undirected"))
                flag = true;
            else
                flag = false;

            int fromIndex = searchVertex(edgeFrom.getText());
            int toIndex = searchVertex(edgeTo.getText());
            int cost = Integer.parseInt(edgeWeight.getText());
            if (flag)
                G.edges.add(new Edge(G.vertices.get(fromIndex), G.vertices.get(toIndex), UNDIRECTED_EDGE, edgeName.getText(), cost));
            else
                G.edges.add(new Edge(G.vertices.get(fromIndex), G.vertices.get(toIndex), DIRECTED_EDGE, edgeName.getText(), cost));

            fillGraphDataTable();
            resetEdge();
        }
        else {
            alert.showAndWait();
            resetEdge();
        }
        enableMaxFlowExecution();
        enableShortestPathExecution();
    }

    public void updateEdge() {
        update = true;
        addEdge();
        update = false;
    }

    public void deleteEdge() {
        delete = true;
        if(validateEdgeInput())
            fillGraphDataTable();
        else
            alert.showAndWait();
        resetEdge();
        enableMaxFlowExecution();
        enableShortestPathExecution();
        delete = false;
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

    /** =========================================================== **/

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
        pl.start(capGraph, G.maxFlowVertices, G.maxFlowSteps.get(ind), ind, true, false);
        pl2.start(flowGraph, G.maxFlowVertices, G.maxFlowSteps.get(ind), ind, false, false);
    }

    public void maxFlowPrevStep() {
        maxFlowStepIndex--;
        maxFlowGraph(maxFlowStepIndex);
        maxFlowNextStep.setDisable(false);
        if (maxFlowStepIndex == 0)
            maxFlowPreviousStep.setDisable(true);
    }

    public void maxFlowNextStep() {
        maxFlowStepIndex++;
        maxFlowGraph(maxFlowStepIndex);
        maxFlowPreviousStep.setDisable(false);
        if (maxFlowStepIndex == (G.maxFlowSteps.size() - 1))
            maxFlowNextStep.setDisable(true);
    }

    /** =========================================================== **/

    public void shortestPathGraph(int ind) {
        shortestPathStepNum.setText("Step #" + (ind + 1) + " :");
        if(ind == 0)
            shortestPath.setText("Initial state..");
        else
            shortestPath.setText("Path #" + ind + ": " + Graph.shortestSteps.get(ind));

        shortestPathGraph.getChildren().clear();
        GraphDrawer pl3 = new GraphDrawer();
        pl3.start(shortestPathGraph, G.shortestPathVertices, G.shortestPathEdges, ind, false, true);
    }

    public void shortestPathPrevStep() {
        shortestPathStepIndex--;
        shortestPathGraph(shortestPathStepIndex);
        shortestPathNextStep.setDisable(false);
        if (shortestPathStepIndex == 0)
            shortestPathPreviousStep.setDisable(true);
    }

    public void shortestPathNextStep() {
        shortestPathStepIndex++;
        shortestPathGraph(shortestPathStepIndex);
        shortestPathPreviousStep.setDisable(false);
        if (shortestPathStepIndex == (Graph.dijkstraSteps.size()))
            shortestPathNextStep.setDisable(true);
    }

    /** =========================================================== **/

    public void enableShortestPathExecution(){
        if(G.edges.size() > 0 && G.shortestPathSource != null)
            executeShortestPath.setDisable(false);
        else
            executeShortestPath.setDisable(true);
    }

    public void enableMaxFlowExecution(){
        if(G.edges.size() > 0 && G.maxFlowSource != null && G.sink != null)
            executeMaximumFlow.setDisable(false);
        else
            executeMaximumFlow.setDisable(true);
    }

    public void executeMaxFlow() {
        clearMaxFlowResults();
        sourceAndSink.setText("Source: " + G.maxFlowSource.symbol + "\nSink: " + G.sink.symbol);
        int maxFlow = G.fordFulkerson();
        maxFlowResult.setText("Maximum flow = " + maxFlow);
        maxFlowGraph(0);
        maxFlowNextStep.setDisable(false);
    }

    public void executeShortestPaths() {
        clearShortestPathResults();
        source.setText("Source: " + G.shortestPathSource.symbol);
        G.dijkstra();
        shortestPathGraph(0);
        shortestPathNextStep.setDisable(false);
        fillShortestPathsTable();
    }

    /** =========================================================== **/

    public void clearMaxFlowResults(){
        maxFlowStepIndex = 0;
        maxFlowResult.clear();
        maxFlowPath.clear();
        maxFlowStepNum.setText("");
        sourceAndSink.setText("");
        capGraph.getChildren().clear();
        flowGraph.getChildren().clear();
        maxFlowPreviousStep.setDisable(true);
        maxFlowNextStep.setDisable(true);
        G.clearMaxFlow();
    }

    public void clearShortestPathResults(){
        shortestPathStepIndex = 0;
        shortestPathData.getItems().clear();
        shortestPath.clear();
        shortestPathStepNum.setText("");
        source.setText("");
        shortestPathGraph.getChildren().clear();
        shortestPathPreviousStep.setDisable(true);
        shortestPathNextStep.setDisable(true);
        G.clearShortestPath();
    }

    public void clearInputs(){
        flag = false;
        update = false;
        delete = false;

        vSymbol.clear();
        vSymbol.setDisable(false);
        addVertex.setDisable(true);
        deleteVertex.setDisable(true);

        maxFlowSource.clear();
        maxFlowSink.clear();
        maxFlowSource.setDisable(false);
        maxFlowSink.setDisable(false);
        addSourceAndSink.setDisable(true);

        shortestPathSource.clear();
        shortestPathSource.setDisable(false);
        addSource.setDisable(true);

        graphType.getSelectionModel().clearSelection();
        graphType.setDisable(false);

        edgeFrom.clear();
        edgeTo.clear();
        edgeName.clear();
        edgeWeight.clear();
        edgeFrom.setDisable(false);
        edgeTo.setDisable(false);
        edgeName.setDisable(false);
        edgeWeight.setDisable(false);
        addEdge.setDisable(true);
        updateEdge.setDisable(true);
        deleteEdge.setDisable(true);

        dataTable.getItems().clear();
    }

    public void restart() {
        clearInputs();
        clearMaxFlowResults();
        clearShortestPathResults();

        executeMaximumFlow.setDisable(true);
        executeShortestPath.setDisable(true);

        G.restartProgram();
        G = new Graph();
    }

    /** =========================================================== **/

    public boolean validateSource() {
        if(searchVertex(shortestPathSource.getText()) == -1){
            alert.setContentText("This vertex doesn't exist in the set of vertices entered. Please re-enter it!");
            return false;
        }
        return true;
    }

    public boolean validateSourceAndSink() {
        if ((searchVertex(maxFlowSource.getText()) == -1) || (searchVertex(maxFlowSink.getText()) == -1)){
            alert.setContentText("The input contains vertices that don't exist in the set of vertices entered. Please re-enter the input!");
            return false;
        }

        if(maxFlowSource.getText().equalsIgnoreCase(maxFlowSink.getText())){
            alert.setContentText("Source and sink can not be the same vertex!");
            return false;
        }

        return true;
    }

    public boolean validateEdgeInput() {
        if ((searchVertex(edgeTo.getText()) == -1) || (searchVertex(edgeFrom.getText()) == -1)){
            alert.setContentText("The input contains vertices that don't exist in the set of vertices entered. Please re-enter the input!");
            return false;
        }

        if(edgeFrom.getText().equalsIgnoreCase(edgeTo.getText())){
            alert.setContentText("There can't be an edge from a vertex to itself!");
            return false;
        }

        for (int j = 0; j < G.edges.size(); j++) {
            if (G.edges.get(j).vertex_First.symbol.equalsIgnoreCase(edgeTo.getText())
                    && G.edges.get(j).vertex_Second.symbol.equalsIgnoreCase(edgeFrom.getText()) && flag){
                if(update || delete){
                    G.edges.remove(j);
                    return true;
                }
                alert.setContentText("There is already an edge between " + edgeTo.getText() + " and " + edgeFrom.getText());
                return false;
            }
            if (G.edges.get(j).vertex_First.symbol.equalsIgnoreCase(edgeFrom.getText())
                    && G.edges.get(j).vertex_Second.symbol.equalsIgnoreCase(edgeTo.getText())){
                if(update || delete){
                    G.edges.remove(j);
                    return true;
                }
                alert.setContentText("There is already an edge between " + edgeFrom.getText() + " and " + edgeTo.getText());
                return false;
            }

        }

        if(update || delete){
            alert.setContentText("No such edge is found!");
            return false;
        }

        return true;
    }

}
