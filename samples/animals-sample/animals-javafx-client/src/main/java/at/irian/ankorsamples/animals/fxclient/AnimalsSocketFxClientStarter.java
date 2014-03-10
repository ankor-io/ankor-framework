package at.irian.ankorsamples.animals.fxclient;

import at.irian.ankor.system.SocketFxClientApplication;
import at.irian.ankor.fx.binding.fxref.FxRefs;
import at.irian.ankor.fx.controller.AnkorFXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Starts the Animals JavaFX Sample with socket connection support.
 * <p>
 * Examples vor starting:
 * <ul>
 *     <li><pre>java at.irian.ankorsamples.animals.fxclient.AnimalsSocketFxClientStarter</pre></li>
 *     <li><pre>java at.irian.ankorsamples.animals.fxclient.AnimalsSocketFxClientStarter --client=//localhost:9091</pre></li>
 *     <li><pre>java at.irian.ankorsamples.animals.fxclient.AnimalsSocketFxClientStarter --client=//localhost:9092 --appInstanceId=collaborationTest</pre></li>
 * </ul>
 * </p>
 *
 * @author Manfred Geiler
 */
public class AnimalsSocketFxClientStarter extends SocketFxClientApplication {
    //private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(AnimalsSocketFxClientStarter.class);

    private static final String APPLICATION_NAME = "Animals FX Client";
    private static final String MODEL_NAME = "root";

    public static void main(String[] args) {
        launch(args);
    }

    public AnimalsSocketFxClientStarter() {
        super(APPLICATION_NAME, MODEL_NAME);
    }

    @Override
    public void startFx(Stage stage) throws Exception {
        stage.setTitle("Ankor Animals FX Sample");

        AnkorFXMLLoader fxmlLoader = new AnkorFXMLLoader();
        fxmlLoader.setLocation(getClass().getClassLoader().getResource("main.fxml"));
        fxmlLoader.setResourcesRef(FxRefs.refFactory().ref("root.resources"));
        Pane myPane = (Pane) fxmlLoader.load();

        Scene myScene = new Scene(myPane);
        myScene.getStylesheets().add("style.css");
        stage.setScene(myScene);
        stage.show();
    }

}