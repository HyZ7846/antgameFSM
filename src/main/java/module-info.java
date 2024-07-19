module assign2.ant {
    requires javafx.controls;
    requires javafx.fxml;


    opens assign2.ant to javafx.fxml;
    exports assign2.ant;
}