package at.irian.ankor.system;

import at.irian.ankor.context.AnkorContextFactory;
import at.irian.ankor.context.SingletonInstanceAnkorContextFactory;
import at.irian.ankor.el.BeanResolverELResolver;
import at.irian.ankor.el.StandardELContext;
import at.irian.ankor.event.ListenersHolder;
import at.irian.ankor.event.UnsynchronizedListenersHolder;
import at.irian.ankor.messaging.*;
import at.irian.ankor.messaging.json.JsonMessageMapper;
import at.irian.ankor.rmi.ELRemoteMethodActionEventListener;
import at.irian.ankor.rmi.RemoteMethodActionEventListener;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * @author MGeiler (Manfred Geiler)
 */
public class SocketAnkorSystem extends AnkorSystem {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(SocketAnkorSystem.class);

    private final MessageLoop<String> messageLoop;

    protected SocketAnkorSystem(MessageFactory messageFactory,
                                MessageLoop<String> messageLoop,
                                ListenersHolder listenersHolder,
                                AnkorContextFactory ankorContextFactory,
                                String name,
                                RemoteMethodActionEventListener remoteMethodActionEventListener) {
        super(name, messageFactory, messageLoop.getMessageBus(), listenersHolder, ankorContextFactory,
              remoteMethodActionEventListener);
        this.messageLoop = messageLoop;
    }


    public static SocketAnkorSystem create(String name, Class<?> modelType, BeanResolver beanResolver,
                                           String remoteHost, int remotePort, int localPort) {
        MessageFactory messageFactory = new MessageFactory();

        MessageLoop<String> messageLoop = new SocketMessageLoop<String>(name, new JsonMessageMapper(),
                remoteHost, remotePort, localPort);

        UnsynchronizedListenersHolder globalEventBus = new UnsynchronizedListenersHolder();

        StandardELContext elContext = new StandardELContext();
        if (beanResolver != null) {
            elContext = elContext.withAdditional(new BeanResolverELResolver(beanResolver));
        }

        Config config = ConfigFactory.load();

        AnkorContextFactory ankorContextFactory
                = new SingletonInstanceAnkorContextFactory(modelType, globalEventBus, elContext, config,
                                                           messageLoop.getMessageBus());

        return new SocketAnkorSystem(messageFactory, messageLoop, globalEventBus, ankorContextFactory, name, null);
    }

    public static SocketAnkorSystem create(String name, Class<?> modelType) {
        return create(name, modelType, null, null);
    }

    public SocketAnkorSystem withRemoteMethodActionListenerEnabled() {
        return new SocketAnkorSystem(getMessageFactory(),
                                     messageLoop,
                                     getGlobalListenersHolder(),
                                     getAnkorContextFactory(),
                                     getName(),
                                     new ELRemoteMethodActionEventListener());
    }

    @SuppressWarnings("UnusedDeclaration")
    public static SocketAnkorSystem create(String name, Class<?> modelType,
                                     final String singletonBeanName, final Object singletonBean) {
        return create(name, modelType, new BeanResolver() {
            @Override
            public Object resolveByName(String beanName) {
                if (beanName.equals(singletonBeanName)) {
                    return singletonBean;
                } else {
                    return null;
                }
            }
        }, null, 0, 0);
    }

    @Override
    public void start() {
        if (!messageLoop.isConnected()) {
            throw new IllegalStateException("message loop is not connected");
        }
        super.start();
        messageLoop.start();
    }

    @SuppressWarnings("unchecked")
    @Override
    public LoopbackMessageBus<String> getMessageBus() {
        return (LoopbackMessageBus<String>) super.getMessageBus();
    }

}