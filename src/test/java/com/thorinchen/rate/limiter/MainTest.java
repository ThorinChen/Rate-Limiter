package com.thorinchen.rate.limiter;

import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test.xml")
public class MainTest {


    @Resource
    private TestService service;

    ExecutorService executorService = Executors.newCachedThreadPool();

    @org.junit.Test
     public void testSayPer2Second() throws Exception {
        for (int i = 0; i < 5; i++) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    service.sayPer2Second();
                }
            });
        }
    }

    @org.junit.Test
    public void testSayPerSecond() throws Exception {
        for (int i = 0; i < 5; i++) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    service.sayPerSecond();
                }
            });
        }
    }

    @org.junit.Test
    public void testSayData() throws Exception {
        final byte[] data = "hello".getBytes();
        System.out.println("size:"+data.length);
        for (int i = 0; i < 5; i++) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    service.sayData(data);
                }
            });
        }
    }

    @org.junit.Test
    public void testSayTwicePerSecond() throws Exception {
        for (int i = 0; i < 5; i++) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    service.sayTwicePerSecond();
                }
            });
        }
    }

    @After
    public void tearDown() throws Exception {
        executorService.shutdown();

        while (!executorService.isTerminated()) {
            Thread.sleep(100);
        }

    }
}