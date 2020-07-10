package com.ae.zuul.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class PostElapsedTimeFilter extends ZuulFilter {

    private final static String TYPE_FILTER_PRE = "pre";
    private final static String START_TIME_ATTRIBUTE = "startTime";

    private static final Logger log = LoggerFactory.getLogger(PostElapsedTimeFilter.class);

    @Override
    public String filterType() {
        return TYPE_FILTER_PRE;
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        log.info(String.format("%s request routed to %s", getRequest().getMethod(), getRequest().getRequestURL().toString()));

        Long startTime = System.currentTimeMillis();
        getRequest().setAttribute(START_TIME_ATTRIBUTE, startTime);

        return null;
    }

    private HttpServletRequest getRequest() {
        RequestContext context = RequestContext.getCurrentContext();
        return context.getRequest();
    }

}
