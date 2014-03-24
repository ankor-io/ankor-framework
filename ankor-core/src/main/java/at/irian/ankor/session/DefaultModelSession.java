package at.irian.ankor.session;

import at.irian.ankor.application.Application;
import at.irian.ankor.event.ArrayListEventListeners;
import at.irian.ankor.event.EventListeners;
import at.irian.ankor.event.dispatch.DispatchThreadAware;
import at.irian.ankor.event.dispatch.EventDispatcher;
import at.irian.ankor.event.dispatch.EventDispatcherFactory;
import at.irian.ankor.ref.RefContext;
import at.irian.ankor.ref.RefContextFactory;
import com.google.common.collect.ImmutableMap;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Manfred Geiler
 */
class DefaultModelSession implements ModelSession, DispatchThreadAware {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(ModelSession.class);

    private final String id;
    private final EventListeners eventListeners;
    private final Application application;
    private RefContext refContext;
    private EventDispatcher eventDispatcher;
    private volatile Map<String, Object> modelRootMap;
    private volatile Map<String, Object> attributes;

    private volatile Thread dispatchThread;

    DefaultModelSession(String modelSessionId,
                        EventListeners eventListeners,
                        Application application) {
        this.id = modelSessionId;
        this.eventListeners = eventListeners;
        this.application = application;
        this.modelRootMap = Collections.emptyMap();
        this.attributes = Collections.emptyMap();
        this.refContext = null;
    }

    public static ModelSession create(EventDispatcherFactory eventDispatcherFactory,
                                      EventListeners defaultEventListeners,
                                      RefContextFactory refContextFactory,
                                      Application application) {
        EventListeners eventListeners = new ArrayListEventListeners(defaultEventListeners);
        DefaultModelSession modelSession = new DefaultModelSession(createModelSessionId(),
                                                                   eventListeners,
                                                                   application);
        modelSession.refContext = refContextFactory.createRefContextFor(modelSession); // ugly, but we have to init it this way, because we have a bi-directional relation here
        modelSession.eventDispatcher = eventDispatcherFactory.createFor(modelSession);
        return modelSession;
    }

    protected static String createModelSessionId() {
        return UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    @Override
    public EventListeners getEventListeners() {
        return eventListeners;
    }

    @Override
    public void setEventDispatcher(EventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

    @Override
    public EventDispatcher getEventDispatcher() {
        return eventDispatcher;
    }

    @Override
    public void close() {
        eventDispatcher.close();

        for (Map.Entry<String, Object> entry : modelRootMap.entrySet()) {
            String modelName = entry.getKey();
            Object modelRoot = entry.getValue();
            refContext.closeModelConnection(modelName);
            application.releaseModel(modelName, modelRoot);
        }

        modelRootMap = Collections.emptyMap();
        attributes = Collections.emptyMap();
    }

    @Override
    public String toString() {
        return "DefaultModelSession{" +
               "id='" + id + '\'' +
               '}';
    }

    @Override
    public void setCurrentDispatchThread(Thread dispatchThread) {
        this.dispatchThread = dispatchThread;
        LOG.trace("Current dispatch thread for {} is now: {}", this, dispatchThread);
    }

    @Override
    public Thread getCurrentDispatchThread() {
        return dispatchThread;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getAttribute(String key) {
        return (T)attributes.get(key);
    }

    @Override
    public void setAttribute(String key, Object value) {
        attributes = ImmutableMap.<String, Object>builder()
                                 .putAll(attributes)
                                 .put(key, value)
                                 .build();
    }

    @Override
    public RefContext getRefContext() {
        return refContext;
    }

    @Override
    public void setModelRoot(String modelName, Object modelRoot) {
        Map<String, Object> mutableMap = new HashMap<String, Object>(modelRootMap);
        mutableMap.put(modelName, modelRoot);
        modelRootMap = ImmutableMap.copyOf(mutableMap);
    }

    @Override
    public Object getModelRoot(String modelName) {
        Object modelRoot = modelRootMap.get(modelName);
        if (modelRoot == null) {
            throw new IllegalArgumentException("No model with name " + modelName);
        }
        return modelRoot;
    }

    @Override
    public Map<String, Object> getModels() {
        return Collections.unmodifiableMap(modelRootMap);
    }
}
