package com.example.apigateway.filter;

import com.google.common.util.concurrent.RateLimiter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SERVLET_DETECTION_FILTER_ORDER;

/**
 *
 */
@Component
public class RateLimiterFilter extends ZuulFilter {

    public static final RateLimiter RATE_LIMITER = RateLimiter.create(1, 2, TimeUnit.SECONDS);

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return SERVLET_DETECTION_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext context = RequestContext.getCurrentContext();

        boolean flag = RATE_LIMITER.tryAcquire();
        if (!flag) {
            context.setSendZuulResponse(false);
            context.setResponseStatusCode(400);
        }

        return null;
    }
}
