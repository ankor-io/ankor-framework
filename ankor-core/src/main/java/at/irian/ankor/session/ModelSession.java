package at.irian.ankor.session;

import at.irian.ankor.event.EventListeners;
import at.irian.ankor.event.dispatch.EventDispatcher;
import at.irian.ankor.ref.RefContext;

import java.util.Collection;

/**
 * The ModelSession provides access to all information that belongs to one specific model instance. It represents
 * the model instance during runtime.
 * Every ModelSession has a unique identifier that identifies the model. Besides that the ModelSession provides access
 * to the event listeners of the model and one or more "model roots".
 *
 * @author Manfred Geiler
 */
public interface ModelSession {

    /**
     * @return  the unique identifier of the corresponding model
     */
    String getId();

    /**
     * @return  the event listeners of this model
     */
    EventListeners getEventListeners();

    /**
     * Close this model and free all resources used by this ModelSession.
     * After a model is closed, the framework is free to cleanup all resources that might have been used by this
     * model.
     * ModelSession implementations should inform all (remotely) connected Models about the closing,
     * close the associated EventDispatcher(s), release all associated
     * model roots and omit all references to these model root instances.
     */
    void close();

    /**
     * Change a custom user attribute of this ModelSession.
     * @param key   a key
     * @param value a value
     */
    void setAttribute(String key, Object value);

    /**
     * Get the custom user attribute with the given key.
     * @param key   a key
     * @param <T>   type of value
     * @return the value associated with the given key or null if there is no such value
     */
    <T> T getAttribute(String key);

    /**
     * @return the event dispatcher that is currently used for this model
     */
    EventDispatcher getEventDispatcher();

    /**
     * Replace the currently used EventDispatcher by the given EventDispatcher. The current dispatcher is
     * put on a stack and may later be restored by a call to {@link #popEventDispatcher()}.
     * @param eventDispatcher the new EventDispatcher
     */
    void pushEventDispatcher(EventDispatcher eventDispatcher);  // todo  move this to special StackEventDispatcher impl

    /**
     * Omit the current EventDispatcher and restore the previously used EventDispatcher for this model.
     * @return the restored EventDispatcher
     * @throws java.util.NoSuchElementException if there is no previous EventDispatcher
     */
    EventDispatcher popEventDispatcher();

    /**
     * @return the RefContext for this ModelSession
     */
    RefContext getRefContext();


    void addModelRoot(String modelName, Object modelRoot);

    Object getModelRoot(String modelName);

    Collection<String> getModelNames();

}
