package org.example;

import org.apache.catalina.LifecycleException;
import org.example.server.DefaultServletWebServer;
import org.example.server.WebServer;

/**
 * 自定义Tomcat的启动
 *
 * @author puppy(陶江航 taojianghangdsjb @ smegz.cn)
 * @since 2020/9/23 13:49
 */
public class EmbeddedTomcatServer {

    public static void main(String[] args) {
        WebServer webServer = new DefaultServletWebServer();
        try {
            webServer.start();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }
    }
}
