# junit-playwright

`junit-playwright` allows you to run your JUnit 5 Playwright tests in parallel easily.  `junit-playwright` provides
isolated environments for each test and exposes Playwright-related objects as test parameters for you to use in your
tests.

## Installation

```xml

<dependency>
    <groupId>io.github.uchagani</groupId>
    <artifactId>junit-playwright</artifactId>
    <version>1.1</version>
</dependency>
```

## Getting Started

`junit-playwright` will inject Playwright objects in your tests.

### Create a config class

Create a class and implement the `PlaywrightConfig` interface

```java
public class DefaultConfig implements PlaywrightConfig {

    @Override
    public BrowserConfig getBrowserConfig() {
        return new BrowserConfig()
                .chromium()
                .launch();
    }
}
```

`PlaywrightConfig` has one method: `getBrowserConfig`. Through the `BrowserConfig` object you can specify your
playwright-related config. The API is similar to playwright-java. All the options that you would specify to initialize
Playwright, Browser, BrowserContext, or Page you can do via `BrowserConfig` object.

### Writing tests

To inject Playwright objects into your test there are two parts:

1. Add the `@InjectPlaywright` annotation to your test class and specify your config class.
2. Add the Playwright object that you need to interact with in your test as a test parameter.

```java

@InjectPlaywright(DefaultConfig.class)
public class InjectBrowserTests {

    @Test
    public void someTest(Page page) {
        page.navigate("https://playwright.dev/java/");
    }

}
```

`junit-playwright` gives you the following Playwright-related test parameters:

* [Playwright](https://playwright.dev/java/docs/api/class-playwright)
* [Browser](https://playwright.dev/docs/api/class-browser)
* [BrowserContext](https://playwright.dev/java/docs/api/class-browsercontext)
* [Page](https://playwright.dev/java/docs/api/class-page)

## Running tests in parallel

`playwright-junit` makes it easy to run tests in parallel. Each test will get an isolated Playwright environment. All
you have to do is enable parallel tests in junit. The easiest way to do this is to create a file in your classpath
called `junit-platform.properties`. For example:
`src/test/resources/junit-platform.properties` and enable parallel tests:

```properties
junit.jupiter.execution.parallel.enabled=true
junit.jupiter.execution.parallel.mode.default=concurrent
junit.jupiter.execution.parallel.mode.classes.default=concurrent
```

You can read more about running junit tests in parallel
in [their documentation](https://junit.org/junit5/docs/current/user-guide/#writing-tests-parallel-execution). With the
above configuration tests in the same class will be run in parallel.

## Examples

Please take a look at the tests located int `src/test/java/io/github/uchagani/jp` for more information on how to create
configs and create tests.

## Advanced

You can override the config for a particular test method by adding the `@InjectPlaywright` annotation over a test
method:

```java

@InjectPlaywright(DefaultConfig.class)
public class InjectBrowserTests {
    @Test
    public void createAChromeBrowser(Browser browser) {
        //Browser is configured with the `DefaultConfig` specified at the class level
    }

    @Test
    @InjectPlaywright(OverrideConfig.class)
    public void createAFirefoxBrowser(Browser browser) {
        //For this test, use the browser configured in the `OverrideConfig` class
    }
}
```
