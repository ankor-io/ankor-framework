package at.irian.ankorman.sample2.fxclient.task;

import at.irian.ankor.action.Action;
import at.irian.ankor.ref.Ref;
import at.irian.ankor.ref.listener.RefChangeListener;
import at.irian.ankor.ref.listener.RefListeners;
import at.irian.ankorman.sample2.fxclient.BaseTabController;
import at.irian.ankorman.sample2.fxclient.TaskComponent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;

import static at.irian.ankor.fx.binding.ValueBindingsBuilder.bindValue;
import static at.irian.ankorman.sample2.fxclient.App.refFactory;

public class TasksController extends BaseTabController {

    public TextField newTodo;
    public HBox todoCount;
    public Label todoCountNum;
    public Button clearCompleted;
    public ListView tasksList;

    private Ref modelRef;

    //private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(TasksController.class);

    public TasksController(String tabId) {
        super(tabId);
    }

    @Override
    public void initialize() {
        modelRef = getTabRef().append("model");

        bindValue(modelRef.append("itemsLeft"))
                .toLabel(todoCountNum)
                .createWithin(bindingContext);

        bindValue(modelRef.append("tasks.rows"))
                .toList(tasksList)
                .createWithin(bindingContext);

        // XXX: Is there a better way to do this? Something like a "visibility variable" maybe?
        RefListeners.addPropChangeListener(modelRef.append("itemsLeft"), new RefChangeListener() {
            @Override
            public void processChange(Ref changedProperty) {
                String prop = changedProperty.getValue();
                int itemsLeft = Integer.parseInt(prop);
                todoCount.setVisible(itemsLeft != 0);
            }
        });

        /*
        TaskComponent test = new TaskComponent();
        test.setText("Dynamically created Task");
        test.getCompleted().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("The button was clicked! 2");
            }
        });

        tasks.getItems().add(test);
        tasks.getItems().add(new TaskComponent());
        tasks.getItems().add(new TaskComponent());
        getTabRef().append("model").append("itemsLeft").setValue(String.valueOf(tasks.getItems().size()));
        */
    }

    @FXML
    public void newTodo(ActionEvent actionEvent) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("title", newTodo.getText());
        getTabRef().append("model").fireAction(new Action("newTodo", params));
        newTodo.clear();

        // XXX: fixed weired type bug by changing type from integer to string
        int currItemsLeft = Integer.parseInt(modelRef.append("itemsLeft").<String>getValue());
        getTabRef().append("model").append("itemsLeft").setValue(String.valueOf(currItemsLeft + 1));
    }

    // XXX
    @Override
    public Ref getTabRef() {
        Ref rootRef = refFactory().rootRef();
        return rootRef.append(String.format("tabsTask.%s", tabId));
    }
}