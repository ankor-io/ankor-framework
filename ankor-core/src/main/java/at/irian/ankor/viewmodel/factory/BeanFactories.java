package at.irian.ankor.viewmodel.factory;

import at.irian.ankor.ref.Ref;
import at.irian.ankor.viewmodel.RefAware;

/**
 * @author Manfred Geiler
 */
public final class BeanFactories {
    //private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(BeanFactories.class);

    private BeanFactories() {}

    public static <T> T newInstance(Class<T> type, Ref ref, Object... constructorArgs) {
        BeanFactory beanFactory = ref.context().beanFactory();
        return beanFactory.createAndInitializeInstance(type, ref, constructorArgs);
    }

    public static <T> T newPropertyInstance(Class<T> type, Ref beanRef, String propertyName) {
        Ref ref = beanRef.appendPath(propertyName);
        return newInstance(type, ref);
    }

    public static <T> T newPropertyInstance(Class<T> type, Object bean, String propertyName) {
        if (bean instanceof RefAware) {
            return newPropertyInstance(type, ((RefAware) bean).getRef(), propertyName);
        } else {
            throw new IllegalArgumentException("Given bean " + bean + " does not implement interface " + RefAware.class.getName());
        }
    }

}
