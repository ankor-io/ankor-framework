package at.irian.ankor.core.test;

import at.irian.ankor.core.action.ModelAction;
import at.irian.ankor.core.listener.ActionListener;
import at.irian.ankor.core.ref.Ref;
import at.irian.ankor.core.test.animal.AnimalSearchContainer;

/**
 * @author MGeiler (Manfred Geiler)
 */
public class NewContainerActionListener implements ActionListener {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(InitActionListener.class);

    @Override
    public void processAction(Ref actionContext, ModelAction action) {
        if (action.name().equals("newAnimalSearchContainer")) {
            LOG.info("Adding new Animal Search Container");
            actionContext.setValue(new AnimalSearchContainer());
        }
    }

}
