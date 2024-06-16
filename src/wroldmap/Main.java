package wroldmap;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Main extends Application {

    static ArrayList<Country> Countrys;
    static Country sourceCity = null;
    static Country destinationCity = null;
    Pane root = new Pane();
    ComboBox<Label> source = new ComboBox<Label>();
    ComboBox<Label> Target = new ComboBox<Label>();
    static float mapHeight = 1080;
    static float mapWidth = 1550;
    static double clickedX = 0;
    static double clickedY = 0;

    @Override

    public void start(Stage stage) throws FileNotFoundException {

        Stage primaryStage = new Stage();
        primaryStage.setTitle("Wrold Map Travel");
        primaryStage.setMaximized(true);
        Scene scene = new Scene(root);
        root.setStyle("-fx-background-color:#064273;");
        // root.setStyle("-fx-background-image: url('download (3).jpg');");
        initialize();
        Label names[] = new Label[Countrys.size()];
        Label LabelSorce = new Label("Sorce:");
        LabelSorce.setFont(new Font(30));
        LabelSorce.setTextFill(Color.BEIGE);
        Label LabelTarget = new Label("Target:");
        LabelTarget.setFont(new Font(30));
        LabelTarget.setTextFill(Color.BEIGE);
        source.setStyle("-fx-background-color: #87CEFA;\r\n");
        source.setPromptText("Source City");
        Target.setStyle("-fx-background-color: #87CEFA;\r\n");
        Target.setPromptText("Target City");

        for (int i = 0, j = 0; i < names.length; i++, j++) {

            if (Countrys.get(i).isStreet) {
                continue;
            }
            names[i] = new Label();
            names[i].setFont(new Font(20));
            names[i].setTextFill(Color.BLACK);
            names[i].setText(Countrys.get(i).name);
            source.getItems().addAll(names[i]);

            names[j] = new Label();
            names[j].setFont(new Font(20));
            names[j].setTextFill(Color.BLACK);
            names[j].setText(Countrys.get(j).name);
            Target.getItems().add(names[j]);

        }

        source.setTranslateX(1000); // * position in TextFiled in CompoBox
        source.setTranslateY(180);
        source.setPrefSize(180, 50);
        Target.setTranslateX(1000);
        Target.setTranslateY(250);
        Target.setPrefSize(180, 50);

        source.setOnAction(e -> {

            sourceCity = Dijkstra.allNodes.get(source.getValue().getText());
            if (sourceCity != null) {
                sourceCity.getTest()
                        .setStyle("-fx-background-color: #FF0000;\r\n" + "        -fx-background-radius:100;\r\n");

            }
        });
        Target.setOnAction(i -> {
            destinationCity = Dijkstra.allNodes.get(Target.getValue().getText());
            if (destinationCity != null) {

                destinationCity.getTest()
                        .setStyle("-fx-background-color: #FF0000;\r\n" + "        -fx-background-radius:100;\r\n");

            }

        });

        Button run = new Button("Run");
        run.setFont(new Font(30));
        run.setTranslateX(1050);
        run.setTranslateY(320);
        run.setMinWidth(170);
        run.setMinHeight(50);
        run.setAlignment(Pos.CENTER);
        run.setBackground(new Background(new BackgroundFill(Color.SKYBLUE, new CornerRadii(25), Insets.EMPTY)));

        TextArea TextAreapath = new TextArea();
        TextAreapath.setTranslateX(1050);
        TextAreapath.setTranslateY(420);
        TextAreapath.setMinSize(270, 220);
        TextAreapath.setMaxSize(270, 220);
        TextAreapath.setEditable(false);
        TextAreapath.setStyle("-fx-background-color: #87CEEB;");

        TextField t1 = new TextField();
        t1.setTranslateX(1050);
        t1.setTranslateY(670);
        t1.setPrefSize(190, 50);
        t1.setEditable(false);
        t1.setFont(new Font(20));
        t1.setStyle("-fx-background-color: #87CEEB;");

        run.setOnAction(e -> {
            int v = 0, w = 0;
            for (int i = 0; i < Countrys.size(); i++) {
                if (sourceCity.getFullName().equals(Countrys.get(i).getFullName())) {
                  //  System.out.println("sourceCity Full Name? " + Countrys.get(i).getFullName());
                    v = i;
                }
                if (destinationCity.getFullName().equals(Countrys.get(i).getFullName())) {
                   // System.out.println(" destinationCity Full Name? " + Countrys.get(i).getFullName());
                    w = i;
                }
            }
            if (sourceCity != null && destinationCity != null) {
                Dijkstra graph = new Dijkstra(Countrys, Countrys.get(v), Countrys.get(w));
                System.out.println("Countrys.get(v)" + Countrys.get(v) + "Countrys.get(w)" + Countrys.get(w));
             //   graph.printQueue();
                graph.generateDijkstra();

                drawPathOnMap(graph.pathTo(Countrys.get(w)));

                root.getChildren().add(group);
                TextAreapath.setText(graph.getPathString());
                t1.setText(graph.distanceString + " KM");
            }
        });

        Button reset = new Button("Reset");
        reset.setPrefSize(150, 60);
        reset.setAlignment(Pos.CENTER);
        reset.setTranslateX(1000);
        reset.setTranslateY(760);
        reset.setStyle("-fx-background-color: #FF0000;\r\n" + "        -fx-background-radius:100;\r\n");
        reset.setFont(new Font(30));

        reset.setOnAction(action -> {
            if (sourceCity != null && destinationCity != null) {
                sourceCity.getTest()
                        .setStyle("-fx-background-color: #000000;\r\n" + "        -fx-background-radius:100;\r\n");
                destinationCity.getTest()
                        .setStyle("-fx-background-color: #000000;\r\n" + "        -fx-background-radius:100;\r\n");
                sourceCity = new Country();
                destinationCity = new Country();
                group.getChildren().clear();
                root.getChildren().remove(group);
                source.setValue(new Label(""));
                Target.setValue(new Label(""));
                TextAreapath.setText(null);
                t1.setText(null);
            } else if (source != null && Target == null) {
                group.getChildren().clear();
                root.getChildren().remove(group);

            } else if (Target != null && source == null) {
                group.getChildren().clear();
                root.getChildren().remove(group);
            }

            // System.out.println("rami");
        });

        root.getChildren().addAll(source, Target, run, TextAreapath, reset,t1);
        // Set positions dynamically based on stage width
        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
            double sceneWidth = newVal.doubleValue();
            double maxRightX = sceneWidth - 300; // Adjust as needed based on element width and padding

            source.setTranslateX(maxRightX);
            Target.setTranslateX(maxRightX);
            LabelSorce.setTranslateX(maxRightX - 100); // Adjust label position relative to ComboBox
            LabelTarget.setTranslateX(maxRightX - 100);
            run.setTranslateX(maxRightX);
            TextAreapath.setTranslateX(maxRightX);
            //  labelpath.setTranslateX(maxRightX - 100);
            t1.setTranslateX(maxRightX + 50); // Adjust position relative to the label
            // t.setTranslateX(maxRightX - 100);
            reset.setTranslateX(maxRightX);
        });
        primaryStage.setScene(scene);// set the scene
        primaryStage.show();
    }

    public void initialize() {
        Image image1 = new Image("file:///C:/Users/zaid7/Downloads/physical-world-map2.png");
        ImageView imageView1 = new ImageView(image1);
        imageView1.setFitHeight(mapHeight);
        imageView1.setFitWidth(mapWidth);
        imageView1.setVisible(true);
        root.getChildren().add(imageView1);

        imageView1.setOnMouseClicked(event -> {

            double clickedX = event.getX();
            double clickedY = event.getY();

            System.out.println("Mouse Click Coordinates:");
            System.out.println("Image Coordinates: X=" + clickedX + ", Y=" + clickedY);

        });

        for (int i = 0; i < Countrys.size(); i++) {
            Button ButtoninMap = new Button();
            Countrys.get(i).setTest(ButtoninMap);
            ButtoninMap.setUserData(Countrys.get(i));
            ButtoninMap.setTranslateX(getX(Countrys.get(i).y)); // Use longitude for X
            ButtoninMap.setTranslateY(getY(Countrys.get(i).x)); // Use latitude for Y
//                System.out.println("X_________"+getX(Countrys.get(i).y));
//                System.out.println("Y_________"+getY(Countrys.get(i).x));

            ButtoninMap.setMinWidth(9);
            ButtoninMap.setMinHeight(9);
            ButtoninMap.setMaxWidth(9);
            ButtoninMap.setMaxHeight(9);
            ButtoninMap.setStyle("-fx-background-color: #000000;\r\n" + "        -fx-background-radius:100;\r\n");
            ButtoninMap.setOnAction(event -> {
                ButtoninMap.setStyle("-fx-background-color: #006400;\r\n" + "        -fx-background-radius:100;\r\n");
                if (sourceCity == null) {
                    sourceCity = (Country) ButtoninMap.getUserData();
                    Label l = new Label();
                    l.setFont(new Font(30));
                    l.setTextFill(Color.BLACK);

                    l.setText(sourceCity.name);
                    source.setValue(l);

                } else if (destinationCity == null && sourceCity != null) {
                    destinationCity = (Country) ButtoninMap.getUserData();
                    Label l = new Label();
                    l.setFont(new Font(20));
                    l.setTextFill(Color.BLACK);
                    l.setText(destinationCity.name);
                    Target.setValue(l);

                }
            });

            Label labelinButton = new Label(Countrys.get(i).name);
            labelinButton.setFont(Font.font("Arial", FontWeight.BOLD, 14)); // Set font to bold and size to 14
            labelinButton.setTextFill(Color.WHITE); // Change font color
            labelinButton.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5); -fx-padding: 2;"); // Add background and padding
            labelinButton.setTranslateX(getX(Countrys.get(i).y) - 10); // Adjust X position
            labelinButton.setTranslateY(getY(Countrys.get(i).x) - 20); // Adjust Y position to avoid overlap

//                // Create the border label
//                Label labelBorder = new Label(Countrys.get(i).name);
//                labelBorder.setFont(Font.font("Arial", FontWeight.BOLD, 14)); // Set font to bold and size to 14
//                labelBorder.setTextFill(Color.BLACK); // Border color
//                labelBorder.setStyle("-fx-padding: 2; -fx-effect: dropshadow(gaussian, white, 1, 1, 0, 0);"); // Create a shadow effect to simulate border
//                labelBorder.setTranslateX(getX(Countrys.get(i).y) - 10); // Adjust X position
//                labelBorder.setTranslateY(getY(Countrys.get(i).x) - 20); // Adjust Y position to avoid overlap
            root.getChildren().add(ButtoninMap);
            root.getChildren().add(labelinButton);
        }
    }

    Group group = new Group();

    private void drawPathOnMap(Country[] cities) {
        for (int i = 0; i < cities.length - 1; i++) {
            Line line = new Line(getX(cities[i].y) + 5, getY(cities[i].x) + 5, getX(cities[i + 1].y) + 5,
                    getY(cities[i + 1].x) + 5);
            line.setStroke(Color.RED);
            line.setStrokeWidth(1);
            line.setStrokeWidth(1);
            line.setStrokeLineCap(StrokeLineCap.ROUND);
            line.setStrokeLineJoin(StrokeLineJoin.ROUND);
            line.setStrokeType(StrokeType.CENTERED);

            line.setStrokeWidth(1);
            line.setStrokeLineCap(StrokeLineCap.ROUND);
            line.setStrokeLineJoin(StrokeLineJoin.ROUND);
            line.setStrokeType(StrokeType.CENTERED);

            if (i == 0) {
                // First line is solid
                line.setStroke(Color.RED);
            } else {
                // Rest of the lines are dashed
                line.setStroke(Color.RED);
                line.getStrokeDashArray().addAll(5d, 5d);
            }

            // Add arrowhead
            addArrowHead(line, 10, 5);
            group.getChildren().add(line);

        }

    }

    private void addArrowHead(Line line, double arrowSize, double arrowWidth) {
        double endX = line.getEndX();
        double endY = line.getEndY();

        double angle = Math.atan2((endY - line.getStartY()), (endX - line.getStartX()));
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);

        // Creating arrowhead shape
        double x1 = endX - arrowSize * cos + arrowWidth * sin;
        double y1 = endY - arrowSize * sin - arrowWidth * cos;
        double x2 = endX - arrowSize * cos - arrowWidth * sin;
        double y2 = endY - arrowSize * sin + arrowWidth * cos;

        Polygon arrowhead = new Polygon(endX, endY, x1, y1, x2, y2);
        arrowhead.setFill(line.getStroke());

        group.getChildren().add(arrowhead);
    }

    private static double getX(double longitude) {
        // Calculate X position based on the longitude
        // Adjust this calculation based on the actual dimensions of your world map image
        return (longitude + 180) * (mapWidth / 360);
    }

    private static double getY(double latitude) {
        // Calculate Y position based on the latitude
        // Adjust this calculation based on the actual dimensions of your world map image
        return (90 - latitude) * (mapHeight / 180);
    }

    public static void main(String[] args) throws FileNotFoundException {
        Countrys = Dijkstra.readFile();
        launch(args);
    }
}
