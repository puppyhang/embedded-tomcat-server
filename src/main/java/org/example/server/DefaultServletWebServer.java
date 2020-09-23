package org.example.server;

import org.apache.catalina.*;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.example.servlet.DefaultServletContainerInitializer;
import org.example.servlet.HttpRequestValve;

import javax.servlet.ServletContainerInitializer;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author puppy(陶江航 taojianghangdsjb @ smegz.cn)
 * @since 2020/9/23 13:50
 */
public class DefaultServletWebServer implements WebServer {
    public static final String DEFAULT_PROTOCOL = "org.apache.coyote.http11.Http11NioProtocol";
    private final AtomicInteger containerCounter = new AtomicInteger(0);
    private final List<Valve> engineValves = new ArrayList<>();
    private final static Integer PORT = 8080;
    private final Tomcat tomcat;


    private final static ServletContainerInitializer INITIALIZER = new DefaultServletContainerInitializer();

    public DefaultServletWebServer() {
        tomcat = new Tomcat();
        Connector connector = new Connector(DEFAULT_PROTOCOL);
        connector.setThrowOnFailure(true);
        tomcat.getService().addConnector(connector);
        customizeConnector(connector);
        tomcat.setConnector(connector);
        tomcat.getHost().setAutoDeploy(false);
        configureEngine(tomcat.getEngine());
        registryServletFilter(tomcat.getHost(), INITIALIZER);
        System.out.println("Initializing the tomcat server " + tomcat.toString());
    }

    private void registryServletFilter(Host host, ServletContainerInitializer initializer) {
        Context context = new StandardContext();
        context.setName("default-context");
        context.setDisplayName("默认Context");
        context.setPath("/");
        File docBase = new File(System.getProperty("user.dir"));
        context.setDocBase(docBase.getAbsolutePath());
        context.addLifecycleListener(new Tomcat.FixContextListener());
        context.setParentClassLoader(getClass().getClassLoader());
        context.addServletContainerInitializer(initializer, Collections.emptySet());
        host.addChild(context);
    }

    private void configureEngine(Engine engine) {
        engine.setBackgroundProcessorDelay(3);
        engineValves.add(new HttpRequestValve());
        for (Valve valve : this.engineValves) {
            engine.getPipeline().addValve(valve);
        }
    }

    // Needs to be protected so it can be used by subclasses
    protected void customizeConnector(Connector connector) {
        int port = Math.max(getPort(), 0);
        connector.setPort(port);
        // Don't bind to the socket prematurely if ApplicationContext is slow to start
        // 在spring中如何容器启动太慢了，先不要让tomcat过早的可以用
        // connector.setProperty("bindOnInit", "false");
    }

    @Override
    public void start() throws LifecycleException {
        System.out.println("Starting tomcat server " + tomcat.toString());
        tomcat.start();
        startDaemonAwaitThread();
        System.out.println("Started tomcat server " + tomcat.toString() + " wait for request. \r\n" +
                "You can get service by access http://" + tomcat.getServer().getAddress() + ":"
                + getPort());
    }

    private void startDaemonAwaitThread() {
        Thread awaitThread = new Thread("container-" + (containerCounter.get())) {

            @Override
            public void run() {
                DefaultServletWebServer.this.tomcat.getServer().await();
            }

        };
        awaitThread.setContextClassLoader(getClass().getClassLoader());
        awaitThread.setDaemon(false);
        awaitThread.start();
    }

    @Override
    public void stop() {

    }

    @Override
    public int getPort() {
        return PORT;
    }
}
