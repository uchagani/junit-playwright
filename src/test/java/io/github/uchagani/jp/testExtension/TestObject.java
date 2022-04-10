package io.github.uchagani.jp.testExtension;

import com.microsoft.playwright.*;

public class TestObject {
    public final Playwright playwright;
    public final Browser browser;
    public final BrowserContext browserContext;
    public final Page page;
    public final APIRequestContext apiRequestContext;

    public TestObject(Playwright playwright, Browser browser, BrowserContext browserContext, Page page, APIRequestContext apiRequestContext) {
        this.playwright = playwright;
        this.browser = browser;
        this.browserContext = browserContext;
        this.page = page;
        this.apiRequestContext = apiRequestContext;
    }
}
