package at.irian.ankor.switching.connector.websocket;

import at.irian.ankor.action.Action;
import at.irian.ankor.change.Change;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

/**
 * @author Thomas Spiegl
 */
public class WebSocketMessage {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String property;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Action action;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Change change;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String,Object> connectParams;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private boolean close;

    /**
     * for deserialization only
     */
    @SuppressWarnings("UnusedDeclaration")
    protected WebSocketMessage() {}

    private WebSocketMessage(String property,
                          Action action,
                          Change change,
                          Map<String, Object> connectParams,
                          boolean close) {
        this.property = property;
        this.action = action;
        this.change = change;
        this.connectParams = connectParams;
        this.close = close;
    }

    public String getProperty() {
        return property;
    }

    public Action getAction() {
        return action;
    }

    public Change getChange() {
        return change;
    }

    public Map<String, Object> getConnectParams() {
        return connectParams;
    }

    public boolean isClose() {
        return close;
    }

    public static WebSocketMessage createActionMsg(String property, Action action) {
        return new WebSocketMessage(property, action, null, null, false);
    }

    public static WebSocketMessage createChangeMsg(String property, Change change) {
        return new WebSocketMessage(property, null, change, null, false);
    }

    public static WebSocketMessage createConnectMsg(String modelName,
                                                 Map<String, Object> connectParameters) {
        return new WebSocketMessage(modelName, null, null, connectParameters, false);
    }

    public static WebSocketMessage createCloseMsg(String modelName) {
        return new WebSocketMessage(modelName, null, null, null, true);
    }

    @Override
    public String toString() {
        return "WebSocketMessage{" +
                "property='" + property + '\'' +
                ", action=" + action +
                ", change=" + change +
                ", connectParams=" + connectParams +
                ", close=" + close +
                '}';
    }
}
