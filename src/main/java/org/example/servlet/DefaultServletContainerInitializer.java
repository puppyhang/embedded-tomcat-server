package org.example.servlet;

import javax.servlet.*;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * @author puppy(陶江航 taojianghangdsjb @ smegz.cn)
 * @since 2020/9/23 14:26
 */
public class DefaultServletContainerInitializer implements ServletContainerInitializer {
    @Override
    public void onStartup(Set<Class<?>> c, ServletContext ctx) {
        System.out.println("初始化DispatcherServlet");
        DispatcherServlet servlet = new DispatcherServlet();
        ServletRegistration.Dynamic servletRegistration = ctx.addServlet("dispatcherServlet", servlet);
        servletRegistration.setLoadOnStartup(1);
        servletRegistration.addMapping("/*");

        System.out.println("初始化CharsetEncodingFilter");
        CharsetEncodingFilter charsetEncodingFilter = new CharsetEncodingFilter();
        FilterRegistration.Dynamic filterRegistration = ctx.addFilter("charsetEncodingFilter",
                charsetEncodingFilter);
        filterRegistration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST),
                false, "/*");
        filterRegistration.addMappingForServletNames(EnumSet.of(DispatcherType.REQUEST),
                false, "dispatcherServlet");

    }
}
