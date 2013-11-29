package at.irian.ankorsamples.todosample.fxclient.task;

import at.irian.ankor.action.Action;
import at.irian.ankor.fx.binding.fxref.FxRef;
import at.irian.ankor.ref.Ref;
import at.irian.ankorsamples.todosample.viewmodel.TaskModel;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"PointlessBooleanExpression", "UnusedDeclaration"})
public class TaskPane extends AnchorPane {

    private Ref itemRef;
    private TaskModel model;
    private int index;

    @FXML public ToggleButton completedButton;
    @FXML public Button deleteButton;
    @FXML public TextField titleTextField;

    private Property<String> title;
    private Property<Boolean> completed;
    private Property<Boolean> editing;
    private SimpleStringProperty helper = new SimpleStringProperty();

    public TaskPane(FxRef itemRef) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("task.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.itemRef = itemRef;
        title = itemRef.appendPath("title").fxProperty();
        completed = itemRef.appendPath("completed").fxProperty();
        editing = itemRef.appendPath("editing").fxProperty();

        titleTextField.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() > 1) {
                    setEditable(true);
                    titleTextField.selectAll();
                }
            }
        });

        titleTextField.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    setEditable(false);
                }
            }
        });

        titleTextField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean newValue, Boolean oldValue) {
                if (newValue == false) {       // todo  newValue <--> oldValue !
                    setEditable(false);
                }
            }
        });

        Bindings.bindBidirectional(completedButton.selectedProperty(), titleTextField.disableProperty());
        completedButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean newValue, Boolean oldValue) {
                if (newValue == false) {        // todo  newValue <--> oldValue !
                    titleTextField.getStyleClass().remove("default");
                    titleTextField.getStyleClass().add("strike-through");
                } else {
                    titleTextField.getStyleClass().remove("strike-through");
                    titleTextField.getStyleClass().add("default");
                }
            }
        });
    }

    private void setIndex(int index) {
        this.index = index;
    }

    private void setModel(TaskModel model) {
        this.model = model;
    }

    @SuppressWarnings("UnusedParameters")
    @FXML
    public void delete(ActionEvent actionEvent) {
        Map<String, Object> params = new HashMap<>();
        params.put("index", index);
        itemRef.root().appendPath("model").fire(new Action("deleteTask", params));
    }

    public StringProperty textProperty() {
        return titleTextField.textProperty();
    }

    public BooleanProperty selectedProperty() {
        return completedButton.selectedProperty();
    }

    public BooleanProperty editableProperty() {
        return titleTextField.editableProperty();
    }

    public boolean isEditable() {
        return titleTextField.isEditable();
    }

    public void setEditable(boolean editable) {
        titleTextField.setEditable(editable);
    }

    public String getText() {
        return textProperty().get();
    }

    public void setText(String value) {
        textProperty().set(value);
    }

    public void setSelected(boolean selected) {
        this.completedButton.setSelected(selected);
    }

    public boolean isSelected() {
        return this.completedButton.isSelected();
    }

    public TaskModel getModel() {
        return model;
    }

    public void updateContent(TaskModel model, int index) {
        setIndex(index);
        setModel(model);

        helper.unbindBidirectional(textProperty());
        title.unbindBidirectional(helper);
        completed.unbindBidirectional(selectedProperty());
        editing.unbindBidirectional(editableProperty());

//        title = new ViewModelProperty<>(itemRef, "title");
//        completed = new ViewModelProperty<>(itemRef, "completed");
//        editing = new ViewModelProperty<>(itemRef, "editing");

        helper = new SimpleStringProperty();
        helper.bindBidirectional(title);

        textProperty().bindBidirectional(helper);
        selectedProperty().bindBidirectional(completed);
        editableProperty().bindBidirectional(editing);
    }
}
