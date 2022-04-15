# junit-playwright

`junit-playwright` allows you to run your JUnit 5 Playwright tests in parallel easily.  `junit-playwright` provides
isolated environments for each test and exposes Playwright-related objects as test parameters for you to use in your
tests.

## Note

`junit-playwright` has recently been updated to v2.0. This brings some breaking changes. Please see the documentation in
the wiki for v1 docs.  It is recommended to upgrade to v2.0.  Migration help can be found at the end of this readme.

## Installation

```xml

<dependency>
    <groupId>io.github.uchagani</groupId>
    <artifactId>junit-playwright</artifactId>
    <version>2.2.5</version>
</dependency>
```

## Getting Started

`junit-playwright` will inject Playwright objects in your tests.  There are two different interfaces that you can implement based on the type of testing that you want to do:  Browser or API.

## Browser Testing

### Create a config class

Create a class and implement the `PlaywrightBrowserConfig` interface

```java
public class DefaultBrowserConfig implements PlaywrightBrowserConfig {

    @Override
    public BrowserConfig getBrowserConfig() {
        return new BrowserConfig()
                .chromium()
                .launch();
    }
}
```

`PlaywrightBrowserConfig` has one method: `getBrowserConfig`. Through the `BrowserConfig` object you can specify your
playwright-related config. The API is similar to playwright-java. All the options that you would specify to initialize
Playwright, Browser, BrowserContext, or Page you can do via `BrowserConfig` object.

### Writing tests

To inject Playwright objects into your test there are two parts:

1. Add the `@UseBrowserConfig` annotation to your test class and specify your config class.
2. Add the Playwright object that you need to interact with in your test as a test parameter.

```java

@UseBrowserConfig(DefaultBrowserConfig.class)
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

## API Testing

Two use Playwright's [APIRequestContext](https://playwright.dev/java/docs/api/class-apirequestcontext) you need to implement the `PlaywrightRestConfig` interface:

```java
public class DefaultRestConfig implements PlaywrightRestConfig {
    @Override
    public RestConfig getRestConfig() {
        return new RestConfig();
    }
}
```

Then you can use this config in your tests:

```java
@UseRestConfig(DefaultRestConfig.class)
public class APIRequestContextTests {
    @Test
    public void someAPITest(APIRequestContext request) {
        request.get("https://api.coindesk.com/v1/bpi/currentprice.json");
    }
}
```

`@UseBrowserConfig` annotation can be used either at the class level, method level.  
`@UseRestConfig` annotation can be used either at the class level, method leve, or parameter level.

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

You can override the config for a particular test method by adding the `@UseBrowserConfig` annotation over a test
method:

### Overriding config

```java

@UseBrowserConfig(DefaultConfig.class)
public class InjectBrowserTests {
    @Test
    public void createAChromeBrowser(Browser browser) {
        //Browser is configured with the `DefaultConfig` specified at the class level
    }

    @Test
    @UseBrowserConfig(OverrideConfig.class)
    public void createAFirefoxBrowser(Browser browser) {
        //For this test, use the browser configured in the `OverrideConfig` class
    }
}
```

### Testing multiple APIs in one test

```java
@Test
public void useDifferentConfigsInTheSameTest(@UseRestConfig(
        SpecificRestConfig.class) APIRequestContext specificRequest, @UseRestConfig(
        OverrideRestConfig.class) APIRequestContext overrideRequest) {
    
    assertThat(specificRequest).isNotNull();
    assertThat(overrideRequest).isNotNull();
    assertThat(specificRequest.get("/foo").url()).contains("google.com/foo");
    assertThat(overrideRequest.get("/foo").url()).contains("bing.com/foo");
}
```


## Requirements

* Java 8+
* Playwright 1.18.0+
* JUnit 5.6+

## Migrating from v1

To migrate to v2.x from v1.x, there are a couple of changes that you need to make:

1.  Change `PlaywrightConfig` to `PlaywrightBrowserConfig`.
2.  Change `@InjectPlaywright` to `@UseBrowserConfig`.
