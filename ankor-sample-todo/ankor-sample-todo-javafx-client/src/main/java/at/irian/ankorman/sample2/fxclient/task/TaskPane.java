package at.irian.ankorman.sample2.fxclient.task;

import at.irian.ankor.action.Action;
import at.irian.ankor.ref.Ref;
import javafx.beans.binding.Bindings;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.HashMap;

public class TaskPane extends AnchorPane {

    private Ref modelRef;
    private int index = -1;

    @FXML public ToggleButton completed;
    @FXML public Button deleteButton;
    @FXML public TextField title;

    private String oldTitle = "";

    public TaskPane(Ref modelRef) {
        this.modelRef = modelRef;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("task.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        title.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() > 1) {
                    title.setEditable(true);
                    title.selectAll();
                }
            }
        });

        title.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    title.setEditable(false);
                    edit();
                }
            }
        });

        title.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean newValue, Boolean oldValue) {
                if (newValue == false) {
                    title.setEditable(false);
                    edit();
                }
            }
        });

        Bindings.bindBidirectional(completed.selectedProperty(), title.disableProperty());
        completed.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean newValue, Boolean oldValue) {
                if (newValue == false) {
                    title.getStyleClass().remove("default");
                    title.getStyleClass().add("strike-through");
                } else {
                    title.getStyleClass().remove("strike-through");
                    title.getStyleClass().add("default");
                }
            }
        });
    }

    private void edit() {
        if (!title.getText().equals(oldTitle)) {
            HashMap params = new HashMap<String, Object>();
            params.put("index", index);
            params.put("title", title.getText());
            modelRef.fireAction(new Action("editTask", params));
            oldTitle = title.getText();
        }
    }

    @FXML
    public void complete(ActionEvent actionEvent) {
        HashMap params = new HashMap<String, Object>();
        params.put("index", index);
        modelRef.fireAction(new Action("toggleTask", params));
    }

    @FXML
    public void delete(ActionEvent actionEvent) {
        HashMap params = new HashMap<String, Object>();
        params.put("index", index);
        modelRef.fireAction(new Action("deleteTask", params));
    }

    public StringProperty textProperty() {
        return title.textProperty();
    }

    public String getText() {
        return textProperty().get();
    }

    public void setText(String value) {
        oldTitle = value;
        textProperty().set(value);
    }

    public ToggleButton getCompleted() {
        return completed;
    }

    public void setCompleted(ToggleButton completed) {
        this.completed = completed;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public void setDeleteButton(Button deleteButton) {
        this.deleteButton = deleteButton;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
