package org.example.server;

import org.apache.catalina.LifecycleException;

/**
 * @author puppy(陶江航 taojianghangdsjb @ smegz.cn)
 * @since 2020/9/23 13:50
 */
public interface WebServer {

    void start() throws LifecycleException;

    void stop();

    int getPort();
}
