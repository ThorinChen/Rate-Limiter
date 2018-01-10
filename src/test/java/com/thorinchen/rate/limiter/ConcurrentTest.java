
package com.thorinchen.rate.limiter;



import org.junit.After;
import org.junit.Before;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ConcurrentTest {

    protected ExecutorService executorService = Executors.newCachedThreadPool();


    @Before
    public void setUp() throws Exception {


    }

    @After
    public void tearDown() throws Exception {
        while (!executorService.isTerminated()) {
            Thread.sleep(100);
        }

    }
}