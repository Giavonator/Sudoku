package sudoku;

import javafx.application.Application;
import javafx.stage.Stage;
import sudoku.buildlogic.SudokuBuildLogic;
import sudoku.userinterface.IUserInterfaceContract;
import sudoku.userinterface.UserInterfaceImpl;

import java.io.IOException;

/**
 * JavaFX App
 */
public class SudokuApplication extends Application {
    private IUserInterfaceContract.View uiImpl;

    @Override
    public void start(Stage primaryStage) throws Exception {
        uiImpl = new UserInterfaceImpl(primaryStage);
        if(uiImpl.getDifficulty() != null) {
            try {
                SudokuBuildLogic.build(uiImpl);
            } 
            catch (IOException e) {
                e.printStackTrace();
                throw e;
            }
        }
        else {
            System.out.println("Not ready.");
        }
        
    }

    public static void main(String[] args) {
        launch();
    }

}