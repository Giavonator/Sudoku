package sudoku.userinterface;

import java.util.HashMap;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sudoku.constants.GameDifficulty;
import sudoku.constants.GameState;
import sudoku.problemdomain.Coordinates;
import sudoku.problemdomain.SudokuGame;

public class UserInterfaceImpl implements IUserInterfaceContract.View,
        EventHandler<KeyEvent> {

    private final Stage stage;
    private final Group root;

    private HashMap<Coordinates, SudokuTextField> textFieldCoordinates;

    private IUserInterfaceContract.EventListener listener;

    private final static double WINDOW_X = 679;
    private final static double WINDOW_Y = 732;
    private final static double BOARD_PADDING = 50;
    private final static double BOARD_X_AND_Y = 576;

    private static final Color WINDOW_BACKGROUND_COLOR = Color.rgb(12, 89, 15);
    private static final Color BOARD_BACKGROUND_COLOR = Color.rgb(224, 242, 241);
    private static final String SUDOKU = "Sudoku";
    private static GameDifficulty GAME_DIFFICULTY;


    public UserInterfaceImpl(Stage stage) throws InterruptedException {
        this.stage = stage;
        this.root = new Group();
        this.textFieldCoordinates = new HashMap<>();
        intializeWelcomeScreen();
    }

    private void intializeWelcomeScreen() {
        drawBackground(root);
        drawWelcomeTitle(root);
        drawDifficultyButtons(root);
        stage.show();
    }

    private void drawBackground(Group root) {
        Scene scene = new Scene(root, WINDOW_X, WINDOW_Y);
        scene.setFill(WINDOW_BACKGROUND_COLOR);
        stage.setScene(scene);
    }

    private void drawWelcomeTitle(Group root) {
        Text welcomeText = new Text(70, 200, "Welcome To");
        welcomeText.setFill(Color.WHITE);
        Text welcomeTitle = new Text(160, 330, "Sudoku!");
        welcomeTitle.setFill(Color.WHITE);
        stage.setTitle(SUDOKU);

        Font welcomeFont = new Font(100);
        welcomeText.setFont(welcomeFont);
        welcomeTitle.setFont(welcomeFont);
        root.getChildren().addAll(welcomeText, welcomeTitle);
    }

    private void drawDifficultyButtons(Group root) {
        String[] BUTTON_STR = {"Beginner", "Amateur", "Expert"};
        GameDifficulty[] BUTTON_DIFFICULTIES = {GameDifficulty.BEGINNER, GameDifficulty.AMATEUR, GameDifficulty.EXPERT};
        int[] buttonCoordinates = {110, 430, 360, 430, 235, 520};
        Button[] welcomeButtons = new Button[3];
        Font buttonFont = new Font(30);
        
        for (int i = 0; i < 3; ++i) {
            welcomeButtons[i] = new Button("");
            welcomeButtons[i].setText(BUTTON_STR[i]);
            welcomeButtons[i].setTranslateX(buttonCoordinates[(i*2)]);
            welcomeButtons[i].setTranslateY(buttonCoordinates[(i*2 + 1)]);
            welcomeButtons[i].setFont(buttonFont);
            welcomeButtons[i].setStyle("-fx-background-radius: 10px;" +
                                    "-fx-min-width: 200px;");

            final int index = i;
            welcomeButtons[i].setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    GAME_DIFFICULTY = BUTTON_DIFFICULTIES[index];
                    switchToSudokuBoard(root);
                }
            });
        }

        root.getChildren().addAll(welcomeButtons[0], welcomeButtons[1], welcomeButtons[2]);
    }

    private void switchToSudokuBoard(Group root) {
        root.getChildren().removeAll(root.getChildren());
        initializeUserInterface();
    }

    private void initializeUserInterface() {
        drawBoardTitle(root);
        drawSudokuBoard(root);
        drawTextFields(root);
        drawGridLines(root);
        stage.show();
    }

    private void drawBoardTitle(Group root) {
        Text title = new Text(275, 690, SUDOKU);
        title.setFill(Color.WHITE);
        Font titleFont = new Font(43);
        title.setFont(titleFont);
        root.getChildren().addAll(title);
    }

    private void drawSudokuBoard(Group root) {
        Rectangle boardBackground = new Rectangle();
        boardBackground.setX(BOARD_PADDING);
        boardBackground.setY(BOARD_PADDING);
        boardBackground.setWidth(BOARD_X_AND_Y);
        boardBackground.setHeight(BOARD_X_AND_Y);

        boardBackground.setFill(BOARD_BACKGROUND_COLOR);

        root.getChildren().addAll(boardBackground);
    }
    
    private void drawTextFields(Group root) {
        final int xOrigin = 50;
        final int yOrigin = 50;

        final int xAndYDelta = 64;

        for(int xIndex = 0; xIndex < 9; ++xIndex){
            for(int yIndex = 0; yIndex < 9; ++yIndex){
                int x = xOrigin + xIndex * xAndYDelta;
                int y = yOrigin + yIndex * xAndYDelta;

                SudokuTextField tile = new SudokuTextField(xIndex, yIndex);
    
                styleSudokuTile(tile, x, y);

                tile.setOnKeyPressed(this);

                textFieldCoordinates.put(new Coordinates(xIndex, yIndex), tile);

                root.getChildren().add(tile);
            }
        }
    }

    private void styleSudokuTile(SudokuTextField tile, double x, double y) {
        Font numberFont = new Font(32);

        tile.setFont(numberFont);
        tile.setAlignment(Pos.CENTER);

        tile.setLayoutX(x);
        tile.setLayoutY(y);
        tile.setPrefHeight(64);
        tile.setPrefWidth(64);

        tile.setBackground(Background.EMPTY);
    }

    private void drawGridLines(Group root) {
        int xAndY = 114;
        int index = 0;
        while(index < 8) {
            int thickness;
            if(index % 3 == 2){
                thickness = 3;
            }
            else {
                thickness = 2;
            }

            Rectangle verticalLine = getLine(
                xAndY + 64 * index,
                BOARD_PADDING,
                BOARD_X_AND_Y,
                thickness
            );

            Rectangle horizontalLine = getLine(
                BOARD_PADDING,
                xAndY + 64 * index,
                thickness,
                BOARD_X_AND_Y
            );

            root.getChildren().addAll(
                verticalLine,
                horizontalLine
            );

            index++;
        }
    }

    private Rectangle getLine(double x, 
                              double y, 
                              double height, 
                              double width) {

        Rectangle line = new Rectangle();
        line.setX(x);
        line.setY(y);
        line.setHeight(height);
        line.setWidth(width);

        line.setFill(Color.BLACK);
        return line;
    }

    public GameDifficulty getGameDifficulty() {
        return GAME_DIFFICULTY;
    }

    @Override
    public void setListener(IUserInterfaceContract.EventListener listener) {
        this.listener = listener;
    }

    @Override
    public void updateSquare(int x, int y, int input) {

        String value = Integer.toString(input);
        if(value.equals("0")) value = "";

        SudokuTextField tile = textFieldCoordinates.get(new Coordinates(x, y));
        tile.textProperty().setValue(value);
    }

    @Override
    public void updateBoard(SudokuGame game) {
        for(int xIndex = 0; xIndex < 9; ++xIndex){
            for(int yIndex = 0; yIndex < 9; ++yIndex){

                String value = Integer.toString(game.getCopyOfGridState()[xIndex][yIndex]);
                if(value.equals("0")) value = "";

                TextField tile = textFieldCoordinates.get(new Coordinates(xIndex, yIndex));
                tile.setText(value);

                if(game.getGameState() == GameState.NEW){
                    if(value.equals("")) {
                        tile.setStyle("-fx-opacity: 1;");
                        tile.setDisable(false);
                    } else {
                        tile.setStyle("-fx-opacity: 0.8;");
                        tile.setDisable(true);
                    }
                }
            }
        }
    }

    @Override
    public void showDialog(String message) {
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.OK);
        dialog.showAndWait();

        if(dialog.getResult() == ButtonType.OK) listener.onDialogClick();
    }

    @Override
    public void showError(String message) {
        Alert dialog = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        dialog.showAndWait();
    }

    @Override
    public GameDifficulty getDifficulty() {
        return GAME_DIFFICULTY;
    }

    @Override
    public void handle(KeyEvent event) {
        if(event.getEventType() == KeyEvent.KEY_PRESSED) {
            if(event.getText().matches("[0-9]")) {
                int value = Integer.parseInt(event.getText());
                handleInput(value, event.getSource());
            } 
            else if (event.getCode() == KeyCode.BACK_SPACE) {
                handleInput(0, event.getSource());
            }
            else {
                ((TextField) event.getSource()).setText("");
            }
        }
        event.consume();
    }

    private void handleInput(int value, Object source) {
        listener.onSudokuInput(
            ((SudokuTextField) source).getX(),
            ((SudokuTextField) source).getY(),
            value
        );
    }
}
