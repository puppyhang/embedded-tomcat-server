package org.example.servlet;

import com.sun.prism.impl.BaseMeshView;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author puppy(陶江航 taojianghangdsjb @ smegz.cn)
 * @since 2020/9/23 14:47
 */
public class HttpRequestValve extends ValveBase {

    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {
        System.out.println("调用请求:" + request.getRequestURI());

        // Select the Wrapper to be used for this Request
        Wrapper wrapper = request.getWrapper();
        if (wrapper == null || wrapper.isUnavailable()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        wrapper.getPipeline().getFirst().invoke(request, response);
    }
}
