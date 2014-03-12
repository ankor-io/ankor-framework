package at.irian.ankorsamples.todosample.fxclient;

/**
 * @author Manfred Geiler
 */
public class TodoSocketFxClientCollab2Starter extends TodoSocketFxClientStarter {
    //private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(TodoSocketFxClientCollab1Starter.class);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    protected String getApplicationName() {
        return super.getApplicationName() + " 2";
    }

    @Override
    protected String getClientAddress() {
        return "//localhost:9092";
    }

    @Override
    protected String getModelInstanceIdToConnect() {
        return "collabTest";
    }
}
