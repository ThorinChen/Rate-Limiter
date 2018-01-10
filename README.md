## Rate Limiter
基于Spring Annotation的标准设计,开发人员*(==甚至开发小白==)*使用起来相当容易
#### 怎样使用
首先在需要限流的`service`加载如下Annotation :
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
1. 书写你的`service`类并在之申明 `@RateLimit ` .
2. 将`@QPSRate`标注在您想要限制的方法上，为了限制您需要给出`rate`值。`rate`值控制在一秒内执行的总数。
3. 将`@QPSRate`注释添加到一个方法参数之外，您不必给出`rate`。它会自动计算。:
     *1.* 如果参数是一个数字，速率就是那个值。
     *2.* 如果参数是一个数组，那么速率就是它的大小。

### 无需 spring framework 使用
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
### 通过 spring framework 使用
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
- 注意在Spring context中引用 `xmlns:rate="http://www.w3.org/schema/ratelimit`
在Spring context中 schemaLocation 添加 `xmlns:rate="http://www.w3.org/schema/ratelimit.xsd` ，同时 `annotation-driven`扫描指定的`package` ，然后就可以正常使用起来了

```java
    @Resource
    private TestService service;
```
使用 Spring framework 的`DI` 去注入你的 `service`

### 如何引用
- **通过maven引入,依赖如下** 

```
      <groupId>com.thorinchen.rate.limiter</groupId>
      <artifactId>rate-limiter</artifactId>
      <version>1.1</version>
```