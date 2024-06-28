package sudoku.buildlogic;

import java.io.IOException;

import sudoku.computationlogic.GameLogic;
import sudoku.persistence.LocalStorageImpl;
import sudoku.problemdomain.IStorage;
import sudoku.problemdomain.SudokuGame;
import sudoku.userinterface.IUserInterfaceContract;
import sudoku.userinterface.logic.ControlLogic;

public class SudokuBuildLogic {

    public static void build(IUserInterfaceContract.View userInterface) throws IOException {
        SudokuGame intialState;
        IStorage storage = new LocalStorageImpl();

        try {
            intialState = storage.getGameData();
        } catch (IOException e){
            intialState = GameLogic.getNewGame();
            storage.updateGameData(intialState);
        }

        IUserInterfaceContract.EventListener uiLogic = new ControlLogic(storage, userInterface);

        userInterface.setListener(uiLogic);
        userInterface.updateBoard(intialState);
    }
}
