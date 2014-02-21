### Reacting to Actions

In this step we will deal with Actions that are sent from the client.

#### Actions

Actions are a core concept of Ankor.
An [`Action`][4] is generally used to make user interaction explicit.

In this case we will deal with the `"newTask"` action.
This `Action` is triggered when the users creates a new todo.
It contains the title of the new task as a parameter.

#### Before we start

Let's set the initial state of our view model properties based on the database:

    :::java
    itemsLeft = taskRepository.getActiveTasks().size();
    itemsLeftText = itemsLeftText(itemsLeft);
    footerVisibility = taskRepository.getTasks().size() != 0;

The `itemsLeftText` helper method is simply:

    :::java
    private String itemsLeftText(int itemsLeft) {
        return (itemsLeft == 1) ? "item left" : "items left";
    }

#### Adding an ActionListener

##### Client: Firing Actions

You can skip this section
For reference, the code for firing an Action in the JavaFX client looks like this:

    :::java
    Map<String, Object> params = new HashMap<>();
    params.put("title", title);
    modelRef.fire(new Action("newTask", params));

It looks similar for other platforms.
Anyway, what the server receives will look like this:

    {
        "senderId": "...",
        "modelId": "...",
        "messageId": "...",
        "property": "root.model",
        "action": {
            "name": "newTask",
            "params": {
                "title": "test"
            }
        }
    }

An `Action` always has a name.
Optionally it can have parameters.
If so, each of the parameters mast have a name as well.

##### Server: Reacting to Actions

Instead of adding event listeners ourselves we will use Ankor's support for annotations.
We can turn a method into an action listener by annotating it with `@ActionListener`.
However there are a few things to consider:

1. The name of the method must be the same as the name of the Action.
2. If the Action has parameters, they will become call parameters and need to be annotated as well.

Let's see how this looks for the `newTask` Action:

    :::java
    @ActionListener
    public void newTask(@Param("title") final String title) {
        // ...
    }

Note the `@Param` annotation on the method parameter.

##### Implementing the newTask method

In the body of the method we create a new task and add it to the task repository.

    :::java
    Task task = new Task(title);
    taskRepository.saveTask(task);

However, this alone will not trigger any change in the UI.
We want to change the `itemsLeft` property to reflect the actual number of tasks in the repository.
Simply setting the property will not trigger any events though.

    :::java
    int itemsLeft = taskRepository.getActiveTasks().size();

    // Ankor will not notice this
    this.itemsLeft = itemsLeft;

Instead we set the new value via the `Ref` that points at the `itemsLeft` property.
We can obtain this Ref by appending `"itemsLeft"` to our `modelRef`.

    :::java
    int itemsLeft = taskRepository.getActiveTasks().size();
    modelRef.appendPath("itemsLeft").setValue(itemsLeft);

This will send a change event to the client and trigger any events there.
It will also update the local variable, so that `(this.itemsLeft == itemsLeft)` evaluates to `true`.

Unfortunately we won't be able to see the changes in the UI yet.
This is due to the `footerVisibility`, which is set to `false` when there are no todos in the list.
In the next step we will use a `ChangeListener` to keep the `footerVisibility` updated based on the number of todos.

[4]: #TODOLinkToDocumentationAction