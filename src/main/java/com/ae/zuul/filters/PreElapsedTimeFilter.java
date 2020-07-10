package com.ae.zuul.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class PreElapsedTimeFilter extends ZuulFilter {

    private final static String TYPE_FILTER_POST = "post";
    private final static String START_TIME_ATTRIBUTE = "startTime";
    private static final Logger log = LoggerFactory.getLogger(PreElapsedTimeFilter.class);

    @Override
    public String filterType() {
        return TYPE_FILTER_POST;
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
        log.info("Enter to POST");

        Long startTime = (Long) getRequest().getAttribute(START_TIME_ATTRIBUTE);
        Long endTime = System.currentTimeMillis();
        Long elapsedTime = endTime - startTime;

        log.info(String.format("Elapsed time in seconds: %s", elapsedTime.doubleValue()/1000.00));

        return null;
    }

    private HttpServletRequest getRequest() {
        RequestContext context = RequestContext.getCurrentContext();
        return context.getRequest();
    }

}
