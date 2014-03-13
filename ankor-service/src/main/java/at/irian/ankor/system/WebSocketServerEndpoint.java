package at.irian.ankor.system;

import at.irian.ankor.annotation.AnnotationBeanMetadataProvider;
import at.irian.ankor.application.Application;
import at.irian.ankor.switching.connector.websocket.WebSocketEndpoint;
import at.irian.ankor.viewmodel.proxy.CglibProxyBeanFactory;

import javax.websocket.Endpoint;
import javax.websocket.server.ServerApplicationConfig;
import javax.websocket.server.ServerEndpointConfig;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Manfred Geiler
 */
public abstract class WebSocketServerEndpoint extends WebSocketEndpoint implements ServerApplicationConfig {
    //private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(WebSocketServerEndpoint.class);

    private static final Map<Class<? extends WebSocketServerEndpoint>, AnkorSystem> SYSTEM_MAP
            = new ConcurrentHashMap<Class<? extends WebSocketServerEndpoint>, AnkorSystem>();

    @Override
    protected AnkorSystem getAnkorSystem() {
        AnkorSystem ankorSystem = SYSTEM_MAP.get(this.getClass());
        if (ankorSystem == null) {
            synchronized (SYSTEM_MAP) {
                ankorSystem = SYSTEM_MAP.get(this.getClass());
                if (ankorSystem == null) {
                    ankorSystem = createAnkorSystem();
                    SYSTEM_MAP.put(this.getClass(), ankorSystem);
                }
            }
        }
        return ankorSystem;
    }

    protected AnkorSystem createAnkorSystem() {
        AnnotationBeanMetadataProvider beanMetadataProvider = new AnnotationBeanMetadataProvider();
        return new AnkorSystemBuilder()
                .withApplication(createApplication())
                .withActorSystemEnabled()
                .withBeanMetadataProvider(beanMetadataProvider)
                .withBeanFactory(new CglibProxyBeanFactory(beanMetadataProvider))
                .withConfigValue("at.irian.ankor.switching.connector.websocket.WebSocketConnector.enabled", true)
                .createServer();
    }

    @Override
    public final Set<ServerEndpointConfig> getEndpointConfigs(Set<Class<? extends Endpoint>> endpointClasses) {
        return Collections.singleton(ServerEndpointConfig.Builder.create(this.getClass(),
                                                                         this.getPath())
                                                                 .build());
    }

    @Override
    public final Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> scanned) {
        return Collections.emptySet();
    }

    protected abstract Application createApplication();

    protected abstract String getPath();

}
