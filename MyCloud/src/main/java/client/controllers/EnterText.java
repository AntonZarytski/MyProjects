package client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EnterText {

    public TextField newName;

    @FXML
    private void initialize() {
    }

    public String getEditableData() {
        return newName.getText();
    }

    public void ok(ActionEvent actionEvent) {
        cancel(actionEvent);
    }

    public void cancel(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.hide();
    }
}
