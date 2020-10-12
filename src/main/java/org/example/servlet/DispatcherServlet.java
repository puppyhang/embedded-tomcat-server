package org.example.servlet;

import com.alibaba.fastjson.JSON;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author puppy(陶江航 taojianghangdsjb @ smegz.cn)
 * @since 2020/9/23 14:05
 */
public class DispatcherServlet extends AbstractServlet {

    @Override
    public void service(ServletRequest req, ServletResponse res) throws IOException {
        HttpServletRequest request = null;
        if (req instanceof HttpServletRequest) {
            request = (HttpServletRequest) req;
        } else {
            throw new RuntimeException("不能处理该请求");
        }

        HttpServletResponse response = null;
        if (res instanceof HttpServletResponse) {
            response = (HttpServletResponse) res;
        } else {
            throw new RuntimeException("不能处理该响应");
        }

        Object handler = resolveHandler(request);

        Object adapter = resolveAdapter(request);

        HttpSession session = request.getSession(false);

        if (session == null) {
            System.out.println("session还没有创建");
            printHeader(response);
            session = request.getSession(true);
            System.out.println("session创建完毕" + session.getId());
            printHeader(response);
        }

        if (handler != null && adapter != null) {
            returnResponse(handler, adapter, response);
        } else {
            if (handler == null) {
                throw new RuntimeException("没有可用的handler");
            } else {
                throw new RuntimeException("没有可用的adapter");
            }
        }

    }

    private void printHeader(HttpServletResponse response) {
        if (response.getHeaderNames().size() == 0) {
            System.out.println("没有headers");
        } else {
            for (String headerName : response.getHeaderNames()) {
                System.out.println(headerName + ":" + response.getHeader(headerName));
            }
        }
    }

    private void returnResponse(Object handler, Object adapter, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");

        try (PrintWriter writer = response.getWriter()) {

            Map<String, Object> objectMap = new HashMap<>();
            objectMap.put("handler", handler);
            objectMap.put("adapter", adapter);
            objectMap.put("result", "恭喜你登录成功了");
            writer.write(JSON.toJSONString(objectMap));

            writer.flush();
        }

    }

    private Object resolveAdapter(HttpServletRequest request) {

        if (request.getRequestURI().equalsIgnoreCase("/user/login")) {
            return "login";
        }

        return null;
    }

    private Object resolveHandler(HttpServletRequest request) {

        if (request.getRequestURI().equalsIgnoreCase("/user/login")) {
            return "login";
        }

        return null;
    }
}
