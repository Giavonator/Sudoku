module com.sudoku {
    requires transitive javafx.controls;
    requires javafx.fxml;

    opens com.sudoku to javafx.fxml;
    exports com.sudoku;
}
