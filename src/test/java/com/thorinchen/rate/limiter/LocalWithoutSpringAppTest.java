package com.thorinchen.rate.limiter;

import org.junit.Test;

public class LocalWithoutSpringAppTest extends ConcurrentTest {

    @Test
    public void testSimple() throws Exception {
        RateLimitContext context = new RateLimitContext();
        RateLimitRuntime rateLimitRuntime = new RateLimitRuntime(context);
        context.addRateLimiters(TestService.class);
        final TestService service = rateLimitRuntime.create(TestService.class);
        for (int i = 0; i < 5; i++) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    service.sayPerSecond();
                }
            });
        }
        executorService.shutdown();
    }
}