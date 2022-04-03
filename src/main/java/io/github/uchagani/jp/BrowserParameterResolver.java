package io.github.uchagani.jp;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class BrowserParameterResolver implements ParameterResolver {
    static final String browserId = ".browser.";

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Class<?> parameterType = parameterContext.getParameter().getType();
        return PlaywrightParameterResolver.injectPlaywrightAnnotationPresent(extensionContext) && parameterType.equals(Browser.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return getBrowser(extensionContext);
    }

    public static Browser getBrowser(ExtensionContext extensionContext) {
        String id = extensionContext.getUniqueId() + browserId;
        Browser browser = extensionContext.getStore(PlaywrightParameterResolver.junitPlaywrightNamespace).get(id, Browser.class);

        if (browser == null) {
            Playwright playwright = PlaywrightParameterResolver.getPlaywright(extensionContext);
            BrowserConfig config = PlaywrightParameterResolver.getPlaywrightConfig(extensionContext).getBrowserConfig();
            BrowserChoice browserChoice = config.getBrowser();
            BrowserCreateMethod browserCreateMethod = config.getCreateMethod();
            BrowserType browserType = null;

            switch (browserChoice) {
                case CHROMIUM:
                    browserType = playwright.chromium();
                    break;
                case FIREFOX:
                    browserType = playwright.firefox();
                    break;
                case WEBKIT:
                    browserType = playwright.webkit();
                    break;
            }

            switch (browserCreateMethod) {
                case LAUNCH:
                    BrowserType.LaunchOptions launchOptions = config.getLaunchOptions();
                    if (launchOptions == null) {
                        browser = browserType.launch();
                    } else {
                        browser = browserType.launch(launchOptions);
                    }
                    break;
                case CONNECT:
                    BrowserType.ConnectOptions connectOptions = config.getConnectOptions();
                    if (connectOptions == null) {
                        browser = browserType.connect(config.getWsEndpoint());
                    } else {
                        browser = browserType.connect(config.getWsEndpoint(), connectOptions);
                    }
                    break;
                case CONNECT_OVER_CDP:
                    BrowserType.ConnectOverCDPOptions connectOverCDPOptions = config.getConnectOverCDPOptions();
                    if (connectOverCDPOptions == null) {
                        browser = browserType.connectOverCDP(config.getEndpointUrl());
                    } else {
                        browser = browserType.connectOverCDP(config.getEndpointUrl(), connectOverCDPOptions);
                    }
                    break;
                case LAUNCH_PERSISTENT_CONTEXT:
                    BrowserType.LaunchPersistentContextOptions launchPersistentContextOptions = config.getLaunchPersistentContextOptions();
                    BrowserContext browserContext;
                    if (launchPersistentContextOptions == null) {
                        browserContext = browserType.launchPersistentContext(config.getUserDataDir());
                    } else {
                        browserContext = browserType.launchPersistentContext(config.getUserDataDir(), launchPersistentContextOptions);
                    }
                    browser = browserContext.browser();
                    BrowserContextParameterResolver.saveBrowserContextInStore(extensionContext, browserContext);
                    break;
            }

            saveBrowserInStore(extensionContext, browser);
        }

        return browser;
    }

    public static void saveBrowserInStore(ExtensionContext extensionContext, Browser browser) {
        extensionContext.getStore(PlaywrightParameterResolver.junitPlaywrightNamespace).put(extensionContext.getUniqueId() + browserId, browser);
    }
}
