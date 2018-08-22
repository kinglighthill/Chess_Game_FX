package sample;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.prefs.Preferences;

import org.json.*;

public class Main extends Application {
    private Stage context;
    private Scene scene;

    private String title = "King Chess Game";
    private String url = "www.google.com";
    private String path = "/sample/res/images/icon.jpg";
    private String coverPath = "/sample/res/images/cover.jpg";
    private String preferenceKey = "PlayerName";

    private Color blue;

    private Board board = Board.getInstance(this);
    private MenuBar menuBar = new MenuBar();
    private Menu gameMenu, playerMenu, helpMenu;
    private MenuItem newGameWH, newGameWC, restartGame, exit, undo, redo, multiPlayer, save, load, endGame, threeD, addPlayer,
            changePlayer, deletePlayer, statistics, viewHelp, about;

    public  static BorderPane ROOT = new BorderPane();
    private Window owner;
    private StackPane centerPanel;
    private VBox leftPanel;
    private VBox rightPanel;
    private VBox timerVBoxPerMove;
    private VBox timerVBoxPerPlayer;

    private Slider timeSlider;
    private Label timerMoveLabel;
    private Label timerPlayerLabel;
//    private Label timerColonLabel;
    private Label playerLabel;
    private Label playerNameLabel;

    private Image image;
    private BackgroundImage view;
    private BackgroundSize imageSize;
    private Background imageBackground;

    private String whitePosition;
    private String blackPosition;

    private double sceneWidth;
    private double sceneHeight;
    private double centerPanelWidth;
    private double leftPanelWidth;
    private double rightPanelWidth;

    private ArrayList<Player> players;
    private ArrayList<String> namesList = new ArrayList<>();
    private String name = null,  winner = null;
    private Player white, black, tempPlayer;
    private String[] namesArray = {};

    private Time timer;
    private Time timer2;

    public static int TIME_REMAINING;
    public static int TIME_REMAINING_2;
    public boolean hasEnded = false;
    public boolean isTimerOn = false;
    private boolean isGameOn = false;
    private boolean isPerMove = false;
    private boolean isRule1 = false;
    private boolean isNew = false;

    private Preferences preferences;

    @Override
    public void start(Stage primaryStage) throws Exception{
        preferences = Preferences.userNodeForPackage(this.getClass());
        context = primaryStage;
        blue = new Color(0, 0.3059, 1, 1);
        sceneWidth = 1100;
        sceneHeight = 690;
        centerPanelWidth = 750;
        leftPanelWidth = 250;
        rightPanelWidth = 150;

        ROOT.setStyle("-fx-background-color:transparent");

        image = new Image(coverPath);
        imageSize = new BackgroundSize(centerPanelWidth, sceneHeight, true, true, true,
                true);
        view = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,BackgroundPosition.CENTER,
                imageSize);
        imageBackground = new Background(view);

        centerPanel = new StackPane();
        centerPanel.setPrefWidth(centerPanelWidth);
        centerPanel.setBackground(imageBackground);

        leftPanel = new VBox(20);
        leftPanel.setStyle("-fx-background-color:#A9A9A9");
        leftPanel.setPrefWidth(leftPanelWidth);

        rightPanel = new VBox(20);
        rightPanel.setStyle("-fx-background-color:#A9A9A9");
        rightPanel.setPrefWidth(rightPanelWidth);

        setUpPlayerLabel();
        setUPMenuBar();

        ROOT.setTop(menuBar);
        ROOT.setLeft(leftPanel);
        ROOT.setRight(rightPanel);
        ROOT.setCenter(centerPanel);

        scene = new Scene(ROOT, sceneWidth, sceneHeight, Color.GRAY);

        primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream(path)));
        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.setOnCloseRequest((event)-> {
            stopAllTimer();
            preferences.put(preferenceKey, playerNameLabel.getText());
        });

        fetchPlayers();

        owner = scene.getWindow();
    }


    public void setWhitePosition(String whitePosition) {
        this.whitePosition = whitePosition;
    }

    public void setBlackPosition(String blackPosition) {
        this.blackPosition = blackPosition;
    }

    private void startGame() {
        if (isNew) {
            board = Board.getNewInstance();
            board.setMain(this);
        }
        board.setWhitePieces("DOWN");
        board.setBlackPieces("UP");
        board.setUpPieces();
        centerPanel.setBackground(null);
        centerPanel.getChildren().add(board);
        centerPanel.setAlignment(board, Pos.CENTER);
        if (!isGameOn) {
            isGameOn = true;
        }
        if (hasEnded) {
            hasEnded = false;
        }
    }

    private void fetchPlayers() {
        namesList.clear();
        players = Player.fetchPlayers();
        Iterator<Player> iterator = players.iterator();
        while(iterator.hasNext()) {
            namesList.add(iterator.next().getName());
        }
    }

    private void stopAllTimer() {
        System.exit(0);
    }

    public void stopTimer() {
        timer.stop();
    }

    public void changeChance(int player) {
        if(!hasEnded && timer != null) {
            if(timerPlayerLabel.getText() == "White") {
                timerPlayerLabel.setTextFill(blue);
                timerPlayerLabel.setText("Black");
            }
            else {
                timerPlayerLabel.setTextFill(Color.WHITE);
                timerPlayerLabel.setText("White");
            }
            if (isPerMove) {
                timer.reset();
                timer.start();
            }
            else {
                if (timer2 != null) {
                    if  (board.getMoves() == 40) {
                        timer2.increment(1800);
                        timer.increment(1800);
                    }
                    if (player != 0) {
                        timer.pause();
                        timer2.play();
                        if (isRule1) {
                            timer.increment(30);
                            delayTimer(timer);
                        }
                        else {
                            if (board.getMoves() > 40) {
                                timer.increment(30);
                                delayTimer(timer);
                            }
                        }
                    }
                    else {
                        timer.play();
                        timer2.pause();
                        if (isRule1) {
                            timer2.increment(30);
                            delayTimer(timer2);
                        }
                        else {
                            if (board.getMoves() > 40) {
                                timer2.increment(30);
                                delayTimer(timer2);
                            }
                            if (board.getMoves() == 40) {
                                delayTimer(timer2);
                            }
                        }
                    }
                }
            }
        }
        else if (hasEnded) {
            timer.stop();
            if (timer2 != null) {
                timer2.stop();
            }
        }
    }

    private void delayTimer(Time time) {
        time.play();
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        time.pause();
                    }
                }, 1000
        );
    }

    public void resetTimerLabel() {
        timer.reset();
//        delayTimer(timer);
        if (timer2 != null) {
            timer2.reset();
            delayTimer(timer2);
        }
        timer.play();
        timerPlayerLabel.setTextFill(Color.WHITE);
        timerPlayerLabel.setText("White");
        board.setMoves(0);
    }

    private void setUpPlayerLabel() {
        playerLabel = new Label("Player");
        playerLabel.setPrefWidth(leftPanelWidth/2);
        playerLabel.setFont(Font.font("SERIF", FontWeight.BOLD, 20));
        playerLabel.setTextFill(Color.AQUAMARINE);

        playerNameLabel = new Label();
        playerNameLabel.setText(preferences.get(preferenceKey, "Anonymous"));
        playerNameLabel.setPrefWidth(leftPanelWidth/2);
        playerNameLabel.setFont(Font.font("SERIF", FontWeight.BOLD, 20));
        playerNameLabel.setTextFill(Color.ANTIQUEWHITE);

        GridPane playerGridPane = new GridPane();
        playerGridPane.setMaxHeight(30);
        playerGridPane.setPrefWidth(leftPanelWidth);
        playerGridPane.add(playerLabel, 0, 0);
        playerGridPane.add(playerNameLabel, 1, 0);
        /*GridPane.setHalignment(playerLabel, HPos.CENTER);
        GridPane.setHalignment(playerNameLabel, HPos.CENTER);
        playerGridPane.setAlignment(Pos.CENTER);*/

        leftPanel.getChildren().add(playerGridPane);
    }

    private  void setUpMoveLabel() {
        timerMoveLabel = new Label("Move");
        timerMoveLabel.setFont(Font.font("SERIF", FontWeight.BOLD, 20));
        timerMoveLabel.setTextFill(Color.AQUA);
        timerMoveLabel.setPrefWidth((leftPanelWidth / 2) - 5);
        /*timerColonLabel = new Label(":");
        timerColonLabel.setFont(Font.font("SERIF", FontWeight.BOLD, 20));
        timerColonLabel.setTextFill(Color.DEEPSKYBLUE);
        timerColonLabel.setPrefWidth(10);*/
        timerPlayerLabel = new Label("White");
        timerPlayerLabel.setFont(Font.font("SERIF", FontWeight.BOLD, 20));
        timerPlayerLabel.setTextFill(Color.WHITE);
        timerPlayerLabel.setPrefWidth(leftPanelWidth / 2);
//        timerPlayerLabel.setPrefWidth((leftPanelWidth / 2) - 5);

        isTimerOn = true;
    }

    private void setUpTimerLabel() {
        setUpMoveLabel();

        Label timerLabel = new Label();
        timerLabel.setFont(Font.font("SERIF", FontWeight.BOLD, 25));
        timerLabel.setTextFill(Color.GREEN);
        timer = new Time(timerLabel, true, true);

        GridPane timerGridPane = new GridPane();
        timerGridPane.setMaxHeight(30);
        timerGridPane.setPrefWidth(leftPanelWidth);
        timerGridPane.add(timerMoveLabel, 0, 0);
//        timerGridPane.add(timerColonLabel, 0, 0);
        timerGridPane.add(timerPlayerLabel, 1, 0);
        timerGridPane.add(timerLabel, 0, 1, 2,  1);
        /*GridPane.setHalignment(timerMoveLabel, HPos.CENTER);
        GridPane.setHalignment(timerColonLabel, HPos.CENTER);
        GridPane.setHalignment(timerPlayerLabel, HPos.CENTER);
        GridPane.setHalignment(timerLabel, HPos.CENTER);*/
        timerGridPane.setAlignment(Pos.CENTER);

        timerVBoxPerMove = new VBox(5);
        timerVBoxPerMove.setPrefWidth(leftPanelWidth);
        timerVBoxPerMove.getChildren().addAll(new Separator(), timerGridPane, new Separator());

        leftPanel.getChildren().remove(timerVBoxPerPlayer);
        leftPanel.getChildren().addAll(timerVBoxPerMove);
    }

    private void setUpTimerLabel2() {
        setUpMoveLabel();

        Label whiteLabel = new Label("White");
        whiteLabel.setFont(Font.font("SERIF", FontWeight.BOLD, 20));
        whiteLabel.setTextFill(Color.WHITE);
        whiteLabel.setMaxWidth(leftPanelWidth);
        Label whiteTimeLabel = new Label("Time Remaining :  ");
        whiteTimeLabel.setFont(Font.font("SERIF", FontWeight.BOLD, 20));
        whiteTimeLabel.setTextFill(Color.AQUA);
        whiteTimeLabel.setMaxWidth(leftPanelWidth * 2 / 3);
        Label whiteTimerLabel = new Label();
        whiteTimerLabel.setFont(Font.font("SERIF", FontWeight.BOLD, 20));
        whiteTimerLabel.setTextFill(Color.GREEN);
        whiteTimerLabel.setMaxWidth(leftPanelWidth / 3);
        timer = new Time(whiteTimerLabel, true, false);

        GridPane whiteGridPane = new GridPane();
        whiteGridPane.setMaxHeight(30);
        whiteGridPane.add(whiteLabel, 0, 0,2,1);
        whiteGridPane.add(whiteTimeLabel, 0, 1);
        whiteGridPane.add(whiteTimerLabel, 1, 1);

        Label blackLabel = new Label("Black");
        blackLabel.setFont(Font.font("SERIF", FontWeight.BOLD, 20));
        blackLabel.setTextFill(blue);
        blackLabel.setMaxWidth(leftPanelWidth);
        Label blackTimeLabel = new Label("Time Remaining :  ");
        blackTimeLabel.setFont(Font.font("SERIF", FontWeight.BOLD, 20));
        blackTimeLabel.setTextFill(Color.AQUA);
        blackTimeLabel.setMaxWidth(leftPanelWidth * 2 / 3);
        Label blackTimerLabel = new Label();
        blackTimerLabel.setFont(Font.font("SERIF", FontWeight.BOLD, 20));
        blackTimerLabel.setTextFill(Color.GREEN);
        blackTimerLabel.setMaxWidth(leftPanelWidth / 3);
        timer2 = new Time(blackTimerLabel, false, false);

        GridPane blackGridPane = new GridPane();
        blackGridPane.setMaxHeight(30);
        blackGridPane.add(blackLabel, 0, 0,2,1);
        blackGridPane.add(blackTimeLabel, 0, 1);
        blackGridPane.add(blackTimerLabel, 1, 1);

        GridPane moveGridPane = new GridPane();
        moveGridPane.setMaxHeight(15);
        moveGridPane.add(timerMoveLabel, 0, 0);
        moveGridPane.add(timerPlayerLabel, 1, 0);

        timerVBoxPerPlayer = new VBox(20);
        timerVBoxPerPlayer.getChildren().addAll(new Separator(), moveGridPane, new Separator(), whiteGridPane,
                new Separator(), blackGridPane, new Separator());

        leftPanel.getChildren().remove(timerVBoxPerMove);
        leftPanel.getChildren().addAll(timerVBoxPerPlayer);

        isTimerOn = true;
    }

    private void setUpSlider() {
        timeSlider = new Slider();
        timeSlider.setMin(0);
        timeSlider.setMax(8);
        timeSlider.setValue(1);
        timeSlider.setMajorTickUnit(1);
        timeSlider.setMinorTickCount(15);
        timeSlider.setBlockIncrement(1);
        timeSlider.setShowTickLabels(true);
        timeSlider.setShowTickMarks(true);
        timeSlider.setMinWidth(600);
    }

    private void setUPMenuBar() {
        newGameWH = new MenuItem("New Game with Human");
        newGameWH.setOnAction((event) -> {
            if (!isGameOn) {
                Dialog dialog = new Dialog();
                dialog.initOwner(context);
                dialog.setTitle(title);
                dialog.setContentText("Do you want to play with timer ON");
                dialog.getDialogPane().getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
                Button yesButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.YES);
                    yesButton.addEventFilter(
                        ActionEvent.ACTION, yesEvent -> {
                            int spacing = 10;
                            Dialog sliderDialog = new Dialog();
                            sliderDialog.initOwner(context);
                            sliderDialog.setTitle(title);
                            sliderDialog.getDialogPane().getButtonTypes().add( ButtonType.OK);
                            setUpSlider();

                            final Tooltip perMoveToolTip = new Tooltip("If you run out of time the next player moves");
                            final Tooltip perPlayerToolTip = new Tooltip("If you run out of time you lose the game");
                            final Tooltip rule1ToolTip = new Tooltip("30 seconds is added after each move");
                            final Tooltip rule2ToolTip = new Tooltip("After the first 40 moves, " +
                                    "30 minutes is added to each player plus 30 seconds after each move");

                            ToggleGroup radioButtonGroup = new ToggleGroup();
                            final RadioButton perMove = new RadioButton("Set time per move");
                            perMove.setTooltip(perMoveToolTip);
                            perMove.setToggleGroup(radioButtonGroup);
                            final RadioButton perPlayer = new RadioButton("Set time per player");
                            perPlayer.setTooltip(perPlayerToolTip);
                            perPlayer.setToggleGroup(radioButtonGroup);
                            perPlayer.setSelected(true);

                            ToggleGroup toggleGroup = new ToggleGroup();
                            RadioButton rule1 = new RadioButton("Rule 1");
                            rule1.setTooltip(rule1ToolTip);
                            rule1.setToggleGroup(toggleGroup);
                            RadioButton rule2 = new RadioButton("Rule 2");
                            rule2.setTooltip(rule2ToolTip);
                            rule2.setToggleGroup(toggleGroup);
                            rule2.setSelected(true);
                            VBox ruleVBox = new VBox(spacing);
                            ruleVBox.getChildren().addAll(rule1, rule2);

                            TreeItem<RadioButton> root = new TreeItem<>(new RadioButton());
                            TreeItem<RadioButton> perMoveTreeItem = new TreeItem<>(perMove);
                            TreeItem<RadioButton> perPlayerTreeItem = new TreeItem<>(perPlayer);
                            TreeItem<RadioButton> rule1TreeItem = new TreeItem<>(rule1);
                            TreeItem<RadioButton> rule2TreeItem = new TreeItem<>(rule2);

                            perPlayerTreeItem.setExpanded(true);
                            root.getChildren().addAll(perMoveTreeItem, perPlayerTreeItem);

                            perPlayer.selectedProperty().addListener((observable, oldValue, newValue)-> {
                                if(perPlayer.isSelected()) {
                                    perPlayerTreeItem.getChildren().addAll(rule1TreeItem, rule2TreeItem);
                                }
                                else {
                                    perPlayerTreeItem.getChildren().clear();
                                }
                            });

                            if(perPlayer.isSelected()) {
                                perPlayerTreeItem.getChildren().addAll(rule1TreeItem, rule2TreeItem);
                            }
                            else {
                                perPlayerTreeItem.getChildren().clear();
                            }

                            TreeView<RadioButton> treeView = new TreeView<>(root);
                            treeView.setShowRoot(false);
                            treeView.setMaxHeight(100);
                            treeView.setStyle("-fx-background-color:transparent");

                            Label timeLabel = new Label("60 minutes");
                            Label hintLabel = new Label("hold the mouse still for a moment over any option " +
                                    "to show tooltip");
                            hintLabel.setFont(Font.font("times new roman", FontPosture.ITALIC, 12));
                            hintLabel.setPadding(new Insets(0, 0, 0, 8));
                            VBox selectRuleVBox = new VBox();
                            selectRuleVBox.getChildren().addAll(new Label("Select Timing Rule "), hintLabel);

                            VBox sliderDialogVBox = new VBox(spacing);
                            sliderDialogVBox.getChildren().addAll(new Label("Set Timer (in hours)"), new Separator(),
                                    timeLabel, timeSlider, new Separator(), selectRuleVBox, treeView);

                            sliderDialog.getDialogPane().setContent(sliderDialogVBox);

                            TIME_REMAINING = (int) timeSlider.getValue() * 3600;
                            timeSlider.valueProperty().addListener(
                                    (ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
                                        DecimalFormat decimalFormat = new DecimalFormat("#.##");
                                        double selectedTime = Double.valueOf(decimalFormat.format(newValue.doubleValue())) * 3600;
                                        int time = (int)(selectedTime/60);
                                        TIME_REMAINING = time * 60;
                                        timeLabel.setText(String.valueOf(time)+ " minutes");
                                    }
                            );

                            Button ok = (Button) sliderDialog.getDialogPane().lookupButton(ButtonType.OK);
                            ok.addEventFilter(
                                    ActionEvent.ACTION, okEvent -> {
                                        if (TIME_REMAINING != 0) {
                                            startGame();
                                            if (perMove.isSelected()) {
                                                isPerMove = true;
                                                setUpTimerLabel();
                                            }
                                            if (perPlayer.isSelected()) {
                                                isPerMove = false;
                                                TIME_REMAINING_2 = TIME_REMAINING;
                                                if (rule1.isSelected()) {
                                                    isRule1 = true;
                                                    setUpTimerLabel2();
                                                }
                                                else {
                                                    isRule1 = false;
                                                    setUpTimerLabel2();
                                                }
                                            }
                                        }
                                        else {
                                            Alert alert = new Alert(Alert.AlertType.NONE, "Time cannot be zero",
                                                    ButtonType.OK);
                                            alert.setTitle("Invalid Time");
                                            alert.show();
                                            okEvent.consume();
                                        }
                                    }
                            );
                            sliderDialog.getDialogPane().getScene().getWindow().setOnCloseRequest(
                                            windowEvent -> {}
                            );
                            sliderDialog.show();
                        }
                );
                Button noButton  = (Button) dialog.getDialogPane().lookupButton(ButtonType.NO);
                noButton.addEventFilter(
                        ActionEvent.ACTION, noEvent -> startGame()
                );
                dialog.getDialogPane().getScene().getWindow().setOnCloseRequest(
                        windowEvent -> {}
                );
                dialog.show();
            }
        });
        newGameWH.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));

        newGameWC = new MenuItem("New Game with Computer");
        newGameWC.setOnAction((event) -> {

        });

        restartGame = new MenuItem("Restart");
        restartGame.setOnAction((event) -> {
            board.refreshBoard(whitePosition, blackPosition);
        });
        restartGame.setDisable(true);

        exit = new MenuItem("Exit");
        exit.setOnAction((event) -> {
            if(board.isChanged) {

            }
            else {
                stopAllTimer();
                context.close();
                preferences.put(preferenceKey, playerNameLabel.getText());
            }
        });

        undo = new MenuItem("Undo");
        undo.setOnAction((event) -> {

        });
        undo.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCodeCombination.CONTROL_DOWN));
        undo.setDisable(true);

        redo = new MenuItem("Redo");
        redo.setOnAction((event) -> {

        });
        redo.setAccelerator(new KeyCodeCombination(KeyCode.Y, KeyCodeCombination.CONTROL_DOWN));
        redo.setDisable(true);

        multiPlayer = new MenuItem("Multi Player Mode");
        multiPlayer.setOnAction((event) -> {

        });

        save = new MenuItem("Save Game");
        save.setOnAction((event) -> {
            /*FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(
                    "JSON files (*.json)", "*.json");
            fileChooser.getExtensionFilters().add(extensionFilter);
            File file = fileChooser.showSaveDialog(ROOT.getScene().getWindow());*/
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Hey", "Charlie");
//            jsonObject.
            System.out.println(jsonObject.toString());
        });
        save.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        save.setDisable(true);

        load = new MenuItem("Load Game");
        load.setOnAction((event) -> {

        });
        load.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));

        endGame = new MenuItem("Quit Game");
        endGame.setOnAction((event) -> quit());
        endGame.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN));

        threeD = new MenuItem("3D Mode");
        threeD.setOnAction((event) -> {

        });

        addPlayer = new MenuItem("New Player");
        addPlayer.setOnAction((event) -> {
            Dialog dialog = new Dialog();
            dialog.initOwner(context);
            dialog.setTitle("New Player");
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            TextField textField = new TextField();
            VBox vBox = new VBox(5);
            vBox.getChildren().addAll(new Label("Enter your name"), textField);

            dialog.getDialogPane().setPrefWidth(300);
            dialog.getDialogPane().setContent(vBox);
            dialog.show();

            Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
            okButton.addEventFilter(ActionEvent.ACTION, (okEvent) -> {
                if (!textField.getText().trim().equals("")) {
                    String name = textField.getText();
                    ArrayList<Player> names = Player.fetchPlayers();
                    Iterator<Player> iterator = names.iterator();

                    while(iterator.hasNext()) {
                        if(iterator.next().getName().equals(name)) {
                            Alert alert = new Alert(Alert.AlertType.NONE, "Player exists", ButtonType.OK);
                            alert.initOwner(context);
                            alert.setTitle("Error");
                            alert.show();
                            okEvent.consume();
                            return;
                        }
                    }

                    Player tempPlayer = new Player(name);
                    tempPlayer.updatePlayer();
                    white = tempPlayer;
                    playerNameLabel.setText(white.getName());
                }
                else {
                    okEvent.consume();
                }
            });
            Button cancelButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
            cancelButton.addEventFilter(ActionEvent.ACTION, (cancelEvent) ->
                dialog.getDialogPane().getScene().getWindow().hide()
            );
        });
        addPlayer.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));

        changePlayer = new MenuItem("Change Player");
        changePlayer.setOnAction((event) -> {
            fetchPlayers();

            Dialog dialog = new Dialog();
            dialog.initOwner(context);
            dialog.setTitle("Change Player");
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.APPLY);

            ComboBox comboBox = new ComboBox();
            comboBox.setPrefWidth(300);
            comboBox.getItems().addAll(namesList);
            comboBox.setValue(namesList.get(0));
            comboBox.setVisibleRowCount(5);
            VBox vBox = new VBox(5);
            vBox.getChildren().addAll(new Label("Select Player"), comboBox);

            dialog.getDialogPane().setPrefWidth(300);
            dialog.getDialogPane().setContent(vBox);
            dialog.show();

            Button applyButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.APPLY);
            applyButton.addEventFilter(ActionEvent.ACTION, (applyEvent) -> {
                for (Player player: players) {
                    if(comboBox.getValue().equals(player.getName())) {
                        white = player;
                        playerNameLabel.setText((String) comboBox.getValue());
                    }
                }
            });
        });
        changePlayer.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN));

        deletePlayer = new MenuItem("Delete Player");
        deletePlayer.setOnAction((event) -> {
            fetchPlayers();

            Dialog dialog = new Dialog();
            dialog.initOwner(context);
            dialog.setTitle("Delete Player");
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.APPLY);

            ComboBox comboBox = new ComboBox();
            comboBox.setPrefWidth(300);
            comboBox.getItems().addAll(namesList);
            comboBox.setValue(namesList.get(0));
            comboBox.setVisibleRowCount(5);
            VBox vBox = new VBox(5);
            vBox.getChildren().addAll(new Label("Enter your name"), comboBox);

            dialog.getDialogPane().setPrefWidth(300);
            dialog.getDialogPane().setContent(vBox);
            dialog.show();

            Button applyButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.APPLY);
            applyButton.addEventFilter(ActionEvent.ACTION, (applyEvent) -> {
                Alert alert = new Alert(Alert.AlertType.NONE, "Are you sure you want to delete player", ButtonType.YES);
                alert.initOwner(context);
                alert.setTitle("Confirm Delete");
                alert.show();
                applyEvent.consume();

                Button yesButton = (Button) alert.getDialogPane().lookupButton(ButtonType.YES);
                yesButton.addEventFilter(ActionEvent.ACTION, (yesEvent) -> {
                    for (Player player: players) {
                        if(comboBox.getValue().equals(player.getName())) {
                            player.deletePlayer();
                            fetchPlayers();
                            playerNameLabel.setText(namesList.get(0));
                        }
                    }
                    dialog.getDialogPane().getScene().getWindow().hide();
                });
            });
        });
        deletePlayer.setAccelerator(new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN));

        statistics = new MenuItem("Statistics");
        statistics.setOnAction((event) -> {

        });
        statistics.setAccelerator(new KeyCodeCombination(KeyCode.F12));

        viewHelp = new MenuItem("View Help");
        viewHelp.setOnAction((event) ->
                getHostServices().showDocument(url)
        );

        about = new MenuItem("About");
        about.setOnAction((event) -> new AboutDialog(ROOT.getScene().getWindow())
        );

        gameMenu = new Menu("Game");
        playerMenu = new Menu("Player");
        helpMenu = new Menu("Help");

        gameMenu.getItems().addAll(newGameWH, newGameWC, new SeparatorMenuItem(), save, load, new SeparatorMenuItem(),
                restartGame, endGame, new SeparatorMenuItem(), undo, redo, new SeparatorMenuItem(), threeD,
                new SeparatorMenuItem(), multiPlayer, new SeparatorMenuItem(), exit);
        gameMenu.setOnShowing((event -> {
            if (board.isMoved) {
                restartGame.setDisable(false);
            }
            else {
                restartGame.setDisable(true);
            }
            if(namesList.size() > 0) {
                statistics.setDisable(false);
            }
            else {
                statistics.setDisable(true);
            }
            if (isGameOn) {
                save.setDisable(false);
                endGame.setDisable(false);
            }
            else {
                save.setDisable(true);
                endGame.setDisable(true);
            }
        }));

        playerMenu.getItems().addAll(addPlayer, changePlayer, deletePlayer, new SeparatorMenuItem(), statistics);
        playerMenu.setOnShowing((event) -> {
            fetchPlayers();
            if (namesList.size() > 1) {
                changePlayer.setDisable(false);
                deletePlayer.setDisable(false);
            }
            else {
                changePlayer.setDisable(true);
                deletePlayer.setDisable(true);
            }
        });

        helpMenu.getItems().addAll(viewHelp, about);

        menuBar.getMenus().addAll(gameMenu, playerMenu, helpMenu);
    }

    public void quit() {
        centerPanel.setBackground(imageBackground);
        centerPanel.getChildren().clear();
        if (isTimerOn) {
            timer.stop();
            leftPanel.getChildren().remove(timerVBoxPerMove);
            leftPanel.getChildren().remove(timerVBoxPerPlayer);
            isTimerOn = false;
        }
        if (!hasEnded) {
            hasEnded = true;
        }
        if (isGameOn) {
            isGameOn = false;
        }
        if (!isNew) {
            isNew = true;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
