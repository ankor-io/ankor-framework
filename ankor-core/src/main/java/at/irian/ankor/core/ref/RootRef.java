package at.irian.ankor.core.ref;

import at.irian.ankor.core.action.ModelAction;
import at.irian.ankor.core.application.ModelActionBus;
import at.irian.ankor.core.application.ModelChangeNotifier;
import at.irian.ankor.core.application.ModelHolder;

/**
 * @author Manfred Geiler
 */
class RootRef implements Ref {
    //private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(PropertyRef.class);

    private final RefFactory refFactory;
    private final ModelHolder modelHolder;
    private final ModelChangeNotifier modelChangeNotifier;

    RootRef(RefFactory refFactory,
            ModelHolder modelHolder,
            ModelChangeNotifier modelChangeNotifier) {
        this.refFactory = refFactory;
        this.modelHolder = modelHolder;
        this.modelChangeNotifier = modelChangeNotifier;
    }

    @Override
    public Ref root() {
        return this;
    }

    @Override
    public Ref parent() {
        return null;
    }

    @Override
    public Ref unwatched() {
        return refFactory.unwatchedRootRef();
    }

    public void setValue(Object newValue) {
        this.modelHolder.setModel(newValue);
        if (this.modelChangeNotifier != null) {
            this.modelChangeNotifier.notifyLocalListeners(this);
        }
    }

    @SuppressWarnings("RedundantCast")
    public <T> T getValue() {
        return (T)this.modelHolder.getModel();
    }

    @Override
    public void fire(ModelAction action) {
        ModelActionBus modelActionBus = refFactory.modelActionBus();
        if (modelActionBus != null) {
            modelActionBus.broadcastAction(this, action);
        }
    }

    @Override
    public boolean equals(Object o) {
        return this == o || !(o == null || getClass() != o.getClass());
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        return refFactory.toString(this);
    }

    @Override
    public Ref sub(String subPath) {
        return refFactory.ref(subPath, modelChangeNotifier);
    }

    @Override
    public String path() {
        return refFactory.pathOf(this);
    }

    @Override
    public boolean isDescendantOf(Ref ref) {
        return false;
    }

    @Override
    public boolean isAncestorOf(Ref ref) {
        return true;
    }
}
