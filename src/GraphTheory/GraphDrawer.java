package GraphTheory;

import GraphTheory.GraphModels.Edge;
import GraphTheory.GraphModels.EdgeType;
import GraphTheory.GraphModels.Graph;
import GraphTheory.GraphModels.Vertex;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.util.ArrayList;

import static GraphTheory.GraphModels.Graph.getAdjacency;
import static GraphTheory.GraphModels.Graph.getVertices;
import static GraphTheory.ShortestPath.getDijkstraSteps;
import static java.lang.Math.PI;
import static java.lang.Math.random;


public class GraphDrawer {

    public GraphDrawer() {
    }

    public static int i;
    public double sceneX, sceneY, layoutX, layoutY;
    public ArrayList<StackPane> dots = new ArrayList<>();
    public StackPane weightAB;

    public void start(Pane pane, ArrayList<Vertex> vertices, ArrayList<Edge> edges, int idx, boolean capacity, boolean shortPath) {

        for (i = 0; i < vertices.size(); i++) {
            StackPane dotA = getDot("skyblue", vertices.get(i).getSymbol());
            if (shortPath) {
                String dotName = vertices.get(i).getSymbol() + "/";
                if(ShortestPath.getDistances().get(idx)[i] == Integer.MAX_VALUE)
                    dotName += "∞";
                else
                    dotName += ShortestPath.getDistances().get(idx)[i];

                dotA = getDot("skyblue", dotName);

                for (int j = 0; j <= idx; j++) {
                    if (ShortestPath.getShortestSteps().get(j).contains(vertices.get(i).getSymbol()))
                        dotA = getDot("yellow", dotName);
                }
            }
            dotA.setLayoutX(295 + 155 * Math.cos(2 * PI / vertices.size() * i));
            dotA.setLayoutY(150 + 155 * Math.sin(2 * PI / vertices.size() * i));
            dots.add(dotA);
        }
        for (i = 0; i < edges.size(); i++) {
            StackPane dotA = dots.get(Graph.searchVertex(edges.get(i).getVertex_First().getSymbol(), vertices));
            StackPane dotB = dots.get(Graph.searchVertex(edges.get(i).getVertex_Second().getSymbol(), vertices));

            boolean directed = false;
            if (edges.get(i).getType().equals(EdgeType.DIRECTED_EDGE))
                directed = true;

            if (directed) {
                int index = isBidirectional(edges.get(i), edges);
                if (index == -1)
                    buildSingleDirectionalLine(dotA, dotB, pane, true, edges, capacity, idx, shortPath);
                else {
                    buildBiDirectionalLine(dotA, dotB, pane, edges, capacity, idx, shortPath);
                }
            } else
                buildSingleDirectionalLine(dotA, dotB, pane, false, edges, capacity, idx, shortPath);

            if (!pane.getChildren().contains(dotA) && !pane.getChildren().contains(dotB))
                pane.getChildren().addAll(dotA, dotB);
            else if (!pane.getChildren().contains(dotA))
                pane.getChildren().add(dotA);
            else if (!pane.getChildren().contains(dotB))
                pane.getChildren().add(dotB);
            dotA.toFront();
            dotB.toFront();
            weightAB.toFront();
        }
        for (StackPane dot : dots) {
            if (!pane.getChildren().contains(dot)) {
                pane.getChildren().add(dot);
            }
        }
    }

    private int isBidirectional(Edge edge, ArrayList<Edge> edges) {
        for (int j = 0; j < edges.size(); j++)
            if (edges.get(j).getVertex_First() == edge.getVertex_Second() && edges.get(j).getVertex_Second() == edge.getVertex_First())
                return j;
        return -1;
    }

    private void buildSingleDirectionalLine(StackPane startDot, StackPane endDot, Pane parent, boolean hasEndArrow, ArrayList<Edge> edges, boolean capacity, int idx, boolean shortPath) {
        boolean contained = false;
        if (shortPath && (idx != 0)) {
            for (int j = 0; j < getDijkstraSteps().get(idx - 1).size(); j++) {
                if (getDijkstraSteps().get(idx - 1).get(j).getEdgeName().equals(edges.get(i).getEdgeName())) {
                    contained = true;
                    break;
                }
            }
        }
        Line line = getLine(startDot, endDot, contained, shortPath);
        StackPane arrowAB = getArrow(true, line, startDot, endDot);
        if (!hasEndArrow) {
            arrowAB.setOpacity(0);
        }
        StackPane arrowBA = getArrow(false, line, startDot, endDot);
        arrowBA.setOpacity(0);

        weightAB = getWeight(line, edges, capacity, shortPath);
        weightAB.toFront();
        parent.getChildren().addAll(line, weightAB, arrowBA, arrowAB);
    }

    private void buildBiDirectionalLine(StackPane startDot, StackPane endDot, Pane parent, ArrayList<Edge> edges, boolean capacity, int idx, boolean shortPath) {
        boolean contained = false;
        if (shortPath && (idx != 0)) {
            for (int j = 0; j < getDijkstraSteps().get(idx - 1).size(); j++) {
                if (getDijkstraSteps().get(idx - 1).get(j).getEdgeName().equals(edges.get(i).getEdgeName())) {
                    contained = true;
                    break;
                }
            }
        }
        Line virtualCenterLine = getLine(startDot, endDot, contained, shortPath);
        virtualCenterLine.setOpacity(0);
        StackPane centerLineArrowAB = getArrow(true, virtualCenterLine, startDot, endDot);
        centerLineArrowAB.setOpacity(0);
        StackPane centerLineArrowBA = getArrow(false, virtualCenterLine, startDot, endDot);
        centerLineArrowBA.setOpacity(0);

        Line directedLine = new Line();
        directedLine.setStroke(Color.ROYALBLUE);
        directedLine.setStrokeWidth(2);

        if (shortPath && contained) {
            directedLine.setStroke(Color.YELLOWGREEN);
            directedLine.setStrokeWidth(5);
        }

        double diff = -centerLineArrowAB.getPrefWidth() / 2;
        final ChangeListener<Number> listener = (obs, old, newVal) -> {
            Rotate r = new Rotate();
            r.setPivotX(virtualCenterLine.getStartX());
            r.setPivotY(virtualCenterLine.getStartY());
            r.setAngle(centerLineArrowAB.getRotate());
            Point2D point = r.transform(new Point2D(virtualCenterLine.getStartX(), virtualCenterLine.getStartY() + diff));
            directedLine.setStartX(point.getX());
            directedLine.setStartY(point.getY());

            Rotate r2 = new Rotate();
            r2.setPivotX(virtualCenterLine.getEndX());
            r2.setPivotY(virtualCenterLine.getEndY());
            r2.setAngle(centerLineArrowBA.getRotate());
            Point2D point2 = r2.transform(new Point2D(virtualCenterLine.getEndX(), virtualCenterLine.getEndY() - diff));
            directedLine.setEndX(point2.getX());
            directedLine.setEndY(point2.getY());
        };
        centerLineArrowAB.rotateProperty().addListener(listener);
        centerLineArrowBA.rotateProperty().addListener(listener);
        virtualCenterLine.startXProperty().addListener(listener);
        virtualCenterLine.startYProperty().addListener(listener);
        virtualCenterLine.endXProperty().addListener(listener);
        virtualCenterLine.endYProperty().addListener(listener);

        StackPane mainArrow = getArrow(true, directedLine, startDot, endDot);
        weightAB = getWeight(directedLine, edges, capacity, shortPath);
        weightAB.toFront();
        parent.getChildren().addAll(virtualCenterLine, centerLineArrowAB, centerLineArrowBA, weightAB, directedLine, mainArrow);
    }

    private Line getLine(StackPane startDot, StackPane endDot, boolean contained, boolean shortPath) {
        Line line = new Line();
        line.setStroke(Color.ROYALBLUE);
        line.setStrokeWidth(2);
        line.startXProperty().bind(startDot.layoutXProperty().add(startDot.translateXProperty()).add(startDot.widthProperty().divide(2)));
        line.startYProperty().bind(startDot.layoutYProperty().add(startDot.translateYProperty()).add(startDot.heightProperty().divide(2)));
        line.endXProperty().bind(endDot.layoutXProperty().add(endDot.translateXProperty()).add(endDot.widthProperty().divide(2)));
        line.endYProperty().bind(endDot.layoutYProperty().add(endDot.translateYProperty()).add(endDot.heightProperty().divide(2)));

        if (shortPath && contained) {
            line.setStroke(Color.YELLOWGREEN);
            line.setStrokeWidth(5);
        }

        return line;
    }

    private StackPane getArrow(boolean toLineEnd, Line line, StackPane startDot, StackPane endDot) {
        double size = 12; // Arrow size
        StackPane arrow = new StackPane();
        arrow.setStyle("-fx-background-color:#333333;-fx-border-width:1px;-fx-border-color:black;-fx-shape: \"M0,-4L4,0L0,4Z\"");//
        arrow.setPrefSize(size, size);
        arrow.setMaxSize(size, size);
        arrow.setMinSize(size, size);

        // Determining the arrow visibility unless there is enough space between dots.
        DoubleBinding xDiff = line.endXProperty().subtract(line.startXProperty());
        DoubleBinding yDiff = line.endYProperty().subtract(line.startYProperty());
        BooleanBinding visible = (xDiff.lessThanOrEqualTo(size).and(xDiff.greaterThanOrEqualTo(-size)).and(yDiff.greaterThanOrEqualTo(-size)).and(yDiff.lessThanOrEqualTo(size))).not();
        arrow.visibleProperty().bind(visible);

        // Determining the x point on the line which is at a certain distance.
        DoubleBinding tX = Bindings.createDoubleBinding(() -> {
            double xDiffSqu = (line.getEndX() - line.getStartX()) * (line.getEndX() - line.getStartX());
            double yDiffSqu = (line.getEndY() - line.getStartY()) * (line.getEndY() - line.getStartY());
            double lineLength = Math.sqrt(xDiffSqu + yDiffSqu);
            double dt;
            if (toLineEnd) {
                // When determining the point towards end, the required distance is total length minus (radius + arrow half width)
                dt = lineLength - (endDot.getWidth() / 2) - (arrow.getWidth() / 2);
            } else {
                // When determining the point towards start, the required distance is just (radius + arrow half width)
                dt = (startDot.getWidth() / 2) + (arrow.getWidth() / 2);
            }

            double t = dt / lineLength;
            return ((1 - t) * line.getStartX()) + (t * line.getEndX());
        }, line.startXProperty(), line.endXProperty(), line.startYProperty(), line.endYProperty());

        // Determining the y point on the line which is at a certain distance.
        DoubleBinding tY = Bindings.createDoubleBinding(() -> {
            double xDiffSqu = (line.getEndX() - line.getStartX()) * (line.getEndX() - line.getStartX());
            double yDiffSqu = (line.getEndY() - line.getStartY()) * (line.getEndY() - line.getStartY());
            double lineLength = Math.sqrt(xDiffSqu + yDiffSqu);
            double dt;
            if (toLineEnd) {
                dt = lineLength - (endDot.getHeight() / 2) - (arrow.getHeight() / 2);
            } else {
                dt = (startDot.getHeight() / 2) + (arrow.getHeight() / 2);
            }
            double t = dt / lineLength;
            return ((1 - t) * line.getStartY()) + (t * line.getEndY());
        }, line.startXProperty(), line.endXProperty(), line.startYProperty(), line.endYProperty());

        arrow.layoutXProperty().bind(tX.subtract(arrow.widthProperty().divide(2)));
        arrow.layoutYProperty().bind(tY.subtract(arrow.heightProperty().divide(2)));

        DoubleBinding endArrowAngle = Bindings.createDoubleBinding(() -> {
            double stX = toLineEnd ? line.getStartX() : line.getEndX();
            double stY = toLineEnd ? line.getStartY() : line.getEndY();
            double enX = toLineEnd ? line.getEndX() : line.getStartX();
            double enY = toLineEnd ? line.getEndY() : line.getStartY();
            double angle = Math.toDegrees(Math.atan2(enY - stY, enX - stX));
            if (angle < 0) {
                angle += 360;
            }
            return angle;
        }, line.startXProperty(), line.endXProperty(), line.startYProperty(), line.endYProperty());
        arrow.rotateProperty().bind(endArrowAngle);

        return arrow;
    }

    private StackPane getWeight(Line line, ArrayList<Edge> edges, boolean capacity, boolean shortestPath) {
        StackPane weight = new StackPane();
        weight.setStyle("-fx-background-color:transparent;");
        /*double size = 30;
        weight.setStyle("-fx-background-color:grey;-fx-border-width:1px;-fx-border-color:black;");
        weight.setPrefSize(size * 3, size);
        weight.setMaxSize(size * 3, size);
        weight.setMinSize(size * 3, size);*/

        Label lb;
        if (capacity)
            lb = new Label(edges.get(i).getEdgeName() + ", c = " + edges.get(i).getWeight());
        else if(shortestPath)
            lb = new Label(edges.get(i).getEdgeName() + ", d = " + edges.get(i).getWeight());
        else
            lb = new Label(edges.get(i).getEdgeName() + ", f = " + edges.get(i).getFlow());
        lb.setStyle("-fx-font-size:18px;-fx-font-weight:bold;");
        lb.setTextFill(Color.BLACK);
        DoubleBinding wgtSqrHalfWidth = weight.widthProperty().divide(2);
        DoubleBinding wgtSqrHalfHeight = weight.heightProperty().divide(2);
        DoubleBinding lineXHalfLength = line.endXProperty().subtract(line.startXProperty()).divide(1.5);
        DoubleBinding lineYHalfLength = line.endYProperty().subtract(line.startYProperty()).divide(1.5);

        weight.layoutXProperty().bind(line.startXProperty().add(lineXHalfLength.subtract(wgtSqrHalfWidth)));
        weight.layoutYProperty().bind(line.startYProperty().add(lineYHalfLength.subtract(wgtSqrHalfHeight)));
        weight.setId(edges.get(i).getEdgeName());
        weight.getChildren().addAll(lb);
        weight.setAccessibleText(edges.get(i).getEdgeName());
        weight.setAccessibleRoleDescription(edges.get(i).getEdgeName());

        return weight;
    }

    private StackPane getDot(String color, String text) {
        double radius = 30;
        double paneSize = 2 * radius;
        StackPane dotPane = new StackPane();
        Circle dot = new Circle();
        dot.setRadius(radius);
        dot.setStyle("-fx-fill:" + color + ";-fx-stroke-width:2px;-fx-stroke:black;");
        Label txt = new Label(text);
        txt.setStyle("-fx-font-size:20px;-fx-font-weight:bold;");
        dotPane.getChildren().addAll(dot, txt);
        dotPane.setPrefSize(paneSize, paneSize);
        dotPane.setMaxSize(paneSize, paneSize);
        dotPane.setMinSize(paneSize, paneSize);
        dotPane.setOnMousePressed(e -> {
            sceneX = e.getSceneX();
            sceneY = e.getSceneY();
            layoutX = dotPane.getLayoutX();
            layoutY = dotPane.getLayoutY();
        });

        EventHandler<MouseEvent> dotOnMouseDraggedEventHandler = e -> {
            // Offset of drag
            double offsetX = e.getSceneX() - sceneX;
            double offsetY = e.getSceneY() - sceneY;

            // Taking parent bounds
            Bounds parentBounds = dotPane.getParent().getLayoutBounds();

            // Drag node bounds
            double currPaneLayoutX = dotPane.getLayoutX();
            double currPaneWidth = dotPane.getWidth();
            double currPaneLayoutY = dotPane.getLayoutY();
            double currPaneHeight = dotPane.getHeight();

            if ((currPaneLayoutX + offsetX < parentBounds.getWidth() - currPaneWidth) && (currPaneLayoutX + offsetX > -1)) {
                // If the dragNode bounds is within the parent bounds, then you can set the offset value.
                dotPane.setTranslateX(offsetX);
            } else if (currPaneLayoutX + offsetX < 0) {
                // If the sum of your offset and current layout position is negative, then you ALWAYS update your translate to negative layout value
                // which makes the final layout position to 0 in mouse released event.
                dotPane.setTranslateX(-currPaneLayoutX);
            } else {
                // If your dragNode bounds are outside parent bounds,ALWAYS setting the translate value that fits your node at end.
                dotPane.setTranslateX(parentBounds.getWidth() - currPaneLayoutX - currPaneWidth);
            }

            if ((currPaneLayoutY + offsetY < parentBounds.getHeight() - currPaneHeight) && (currPaneLayoutY + offsetY > -1)) {
                dotPane.setTranslateY(offsetY);
            } else if (currPaneLayoutY + offsetY < 0) {
                dotPane.setTranslateY(-currPaneLayoutY);
            } else {
                dotPane.setTranslateY(parentBounds.getHeight() - currPaneLayoutY - currPaneHeight);
            }
        };
        dotPane.setOnMouseDragged(dotOnMouseDraggedEventHandler);
        dotPane.setOnMouseReleased(e -> {
            // Updating the new layout positions
            dotPane.setLayoutX(layoutX + dotPane.getTranslateX());
            dotPane.setLayoutY(layoutY + dotPane.getTranslateY());

            // Resetting the translate positions
            dotPane.setTranslateX(0);
            dotPane.setTranslateY(0);
        });
        return dotPane;
    }

    public void drawColoredGraph() {

        int color_numbers = 0;
        char[] colors = new char[getVertices().size()];
        char label_color = 'A';

        for (int i = 0; i < getVertices().size(); i++) {
            if (colors[i] == 0) {
                colors[i] = label_color;
                color_numbers++;
                label_color = (char) ((int) label_color + 1);
            }
            for (int j = 0; j < getVertices().size(); j++) {
                if (colors[j] == 0 && getAdjacency()[i][j] == 0) {
                    boolean adj = false;
                    for (int k = 0; k < getVertices().size(); k++) {
                        if (colors[k] == colors[i]) {
                            if (getAdjacency()[k][j] == 1) {
                                adj = true;
                                break;
                            }
                        }
                    }
                    if (!adj)
                        colors[j] = colors[i];
                }
            }
        }

        Pane root = new Pane();
        Stage stage = new Stage();
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Graph Coloring");
        Color[] fills = new Color[color_numbers];
        Circle[] nodes = new Circle[getVertices().size()];
        for (int i = 0; i < color_numbers; i++) {
            fills[i] = new Color(random(), random(), random(), 1);
        }
        for (int i = 0; i < getVertices().size(); i++) {
            nodes[i] = new Circle();
            nodes[i].setCenterX(400 + 200 * Math.cos(2 * PI / getVertices().size() * i));
            nodes[i].setCenterY(300 + 200 * Math.sin(2 * PI / getVertices().size() * i));
            nodes[i].setRadius(20);
            nodes[i].setFill(fills[(int) (colors[i]) - 65]);
            nodes[i].setStroke(Color.BLACK);
            Label t = new Label("" + getVertices().get(i).getSymbol());
            t.layoutXProperty().bind(nodes[i].centerXProperty().add(-5));
            t.layoutYProperty().bind(nodes[i].centerYProperty().add(-5));
            root.getChildren().addAll(nodes[i], t);
            int finalI = i;
            nodes[i].setOnMouseDragged(new EventHandler<MouseEvent>() {
                Circle c = nodes[finalI];
                @Override
                public void handle(MouseEvent event) {
                    c.setCenterX(event.getX());
                    c.setCenterY(event.getY());
                }
            });
        }
        for (int i = 0; i < getVertices().size(); i++) {
            for (int j = 0; j < getVertices().size(); j++) {
                if (getAdjacency()[i][j] == 1) {
                    Line l = new Line();
                    l.startXProperty().bind(nodes[i].centerXProperty());
                    l.startYProperty().bind(nodes[i].centerYProperty());
                    l.endXProperty().bind(nodes[j].centerXProperty());
                    l.endYProperty().bind(nodes[j].centerYProperty());
                    root.getChildren().add(0, l);
                }
            }
        }
        stage.show();
    }
}
