package org.example.servlet;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author puppy(陶江航 taojianghangdsjb @ smegz.cn)
 * @since 2020/9/23 14:04
 */
public class CharsetEncodingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("设置编码");
        chain.doFilter(request, response);
    }
}
