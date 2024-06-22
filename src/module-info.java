module sudoku {
    requires transitive javafx.controls;
    requires javafx.fxml;

    opens sudoku to javafx.fxml;
    exports sudoku;
}
