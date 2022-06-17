## Rate Limiter

API Rate Limit Component in Java

#### Description

Based on the standard design of spring annotation, developers * (= = or even Xiaobai = =) * are quite easy to use

#### How to use
First, load the following annotation on the 'service' that needs current restriction:
```java
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
```
1. Write your service class and declare `@ratelimit`  on it
2. Mark `@qpsrate'on the method you want to restrict. In order to restrict, you need to give a 'rate' value` The rate` value controls the total number of executions in one second
3. Add the `@qpsrate 'comment to a method parameter. You do not need to give a' rate '. It will be calculated automatically。:

     *1.* If the parameter is a number, the rate is that value。<br/>
     *2.* If the parameter is an array, the rate is its size。

no use spring framework 
=======
```java
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
```
use spring framework
======
```xml 
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:rate="http://www.w3.org/schema/ratelimit"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
                           http://www.w3.org/schema/ratelimit http://www.w3.org/schema/ratelimit.xsd">

    <rate:annotation-driven packages="com.thorinchen.rate.limiter"/>

</beans>
```
- Note the reference in the spring context `xmlns:rate="http://www.w3.org/schema/ratelimit`
Add schemalocation in spring context`xmlns:rate="http://www.w3.org/schema/ratelimit.xsd` ，and  `annotation-driven`Scan specified `package` ，Then it can be used normally

```java
    @Resource
    private TestService service;
```
Use 'DI' of spring framework to inject your 'service'`

### How to reference
- **Introduced through maven, the dependencies are as follows** 

```
      <groupId>com.thorinchen.rate.limiter</groupId>
      <artifactId>rate-limiter</artifactId>
      <version>1.1</version>
```