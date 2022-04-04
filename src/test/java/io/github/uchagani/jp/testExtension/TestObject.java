package io.github.uchagani.jp.testExtension;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class TestObject {
    public final Playwright playwright;
    public final Browser browser;
    public final BrowserContext browserContext;
    public final Page page;

    public TestObject(Playwright playwright, Browser browser, BrowserContext browserContext, Page page) {
        this.playwright = playwright;
        this.browser = browser;
        this.browserContext = browserContext;
        this.page = page;
    }
}
