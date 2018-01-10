package com.thorinchen.rate.limiter;

import com.thorinchen.rate.limiter.annotation.DPSRate;
import com.thorinchen.rate.limiter.annotation.QPSRate;
import com.thorinchen.rate.limiter.annotation.RateLimit;


@RateLimit
public class TestService {

    @QPSRate(rate = 1)
    public void sayPerSecond() {
        System.out.println("say one per second");
    }

    @QPSRate(rate = 0.5)
    public void sayPer2Second() {
        System.out.println("say one per 2 seconds");
    }

    public void sayData(@DPSRate(rate = 1) byte[] data) {
        System.out.println("say data ");
    }

    @QPSRate(key = "sayTwicePerSecond",rate = 2)
    public void sayTwicePerSecond() {
        System.out.println("say Twice Per Second");
    }

    @QPSRate(key = "sayTwicePerSecond")
    public void alsoSayTwicePerSecond() {
        System.out.println("also Say Twice Per Second");
    }
}