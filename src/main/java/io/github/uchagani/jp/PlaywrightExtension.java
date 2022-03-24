package io.github.uchagani.jp;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.extension.*;

import java.lang.reflect.InvocationTargetException;

public class PlaywrightExtension implements ParameterResolver, AfterEachCallback {
    private static final ExtensionContext.Namespace browserProviderNamespace = ExtensionContext.Namespace.create(PlaywrightExtension.class);
    private static final String playwrightId = ".playwright.";
    private static final String browserId = ".browser.";
    private static final String browserContextId = ".browserContext.";
    private static final String pageId = ".page.";

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Class<?> parameterType = parameterContext.getParameter().getType();
        boolean annotationPresent = extensionContext.getRequiredTestClass().isAnnotationPresent(InjectPlaywright.class) ||
                extensionContext.getRequiredTestMethod().isAnnotationPresent(InjectPlaywright.class);

        return annotationPresent &&
                ((parameterType.equals(Playwright.class)) ||
                        (parameterType.equals(Browser.class)) ||
                        (parameterType.equals(BrowserContext.class)) ||
                        (parameterType.equals(Page.class)));
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Class<?> parameterType = parameterContext.getParameter().getType();

        if (parameterType == Playwright.class) {
            return getPlaywright(extensionContext);
        }

        if (parameterType == Browser.class) {
            return getBrowser(extensionContext);
        }

        if (parameterType == BrowserContext.class) {
            return getBrowserContext(extensionContext);
        }

        if (parameterType == Page.class) {
            return getPage(extensionContext);
        }

        return null;
    }

    private Page getPage(ExtensionContext extensionContext) {
        String id = extensionContext.getUniqueId() + pageId;
        Page page = extensionContext.getStore(browserProviderNamespace).get(id, Page.class);

        if (page == null) {
            BrowserContext browserContext = getBrowserContext(extensionContext);
            page = browserContext.newPage();
            savePageInStore(extensionContext, page);
        }

        return page;
    }

    private BrowserContext getBrowserContext(ExtensionContext extensionContext) {
        String id = extensionContext.getUniqueId() + browserContextId;
        BrowserContext browserContext = extensionContext.getStore(browserProviderNamespace).get(id, BrowserContext.class);

        if (browserContext == null) {
            Browser browser = getBrowser(extensionContext);
            PlaywrightConfig config = getPlaywrightConfig(extensionContext);

            if (config.getBrowserConfig().getCreateMethod() == BrowserCreateMethod.LAUNCH_PERSISTENT_CONTEXT) {
                return getBrowserContext(extensionContext);
            }

            Browser.NewContextOptions newContextOptions = config.getBrowserConfig().getNewContextOptions();

            if (newContextOptions == null) {
                browserContext = browser.newContext();
            } else {
                browserContext = browser.newContext(newContextOptions);
            }

            saveBrowserContextInStore(extensionContext, browserContext);
        }

        return browserContext;
    }

    private Browser getBrowser(ExtensionContext extensionContext) {
        String id = extensionContext.getUniqueId() + browserId;
        Browser browser = extensionContext.getStore(browserProviderNamespace).get(id, Browser.class);

        if (browser == null) {
            Playwright playwright = getPlaywright(extensionContext);
            BrowserConfig config = getPlaywrightConfig(extensionContext).getBrowserConfig();
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
                    saveBrowserContextInStore(extensionContext, browserContext);
                    break;
            }

            saveBrowserInStore(extensionContext, browser);
        }

        return browser;
    }

    private void savePageInStore(ExtensionContext extensionContext, Page page) {
        extensionContext.getStore(browserProviderNamespace).put(extensionContext.getUniqueId() + pageId, page);
    }

    private void saveBrowserContextInStore(ExtensionContext extensionContext, BrowserContext browserContext) {
        extensionContext.getStore(browserProviderNamespace).put(extensionContext.getUniqueId() + browserContextId, browserContext);
    }

    private void saveBrowserInStore(ExtensionContext extensionContext, Browser browser) {
        extensionContext.getStore(browserProviderNamespace).put(extensionContext.getUniqueId() + browserId, browser);
    }

    private void savePlaywrightInStore(ExtensionContext extensionContext, Playwright playwright) {
        extensionContext.getStore(browserProviderNamespace).put(extensionContext.getUniqueId() + playwrightId, playwright);
    }

    private Playwright getPlaywright(ExtensionContext extensionContext) {
        String id = extensionContext.getUniqueId() + playwrightId;
        Playwright playwright = extensionContext.getStore(browserProviderNamespace).get(id, Playwright.class);

        if (playwright == null) {
            PlaywrightConfig config = getPlaywrightConfig(extensionContext);
            Playwright.CreateOptions options = config.getBrowserConfig().getPlaywrightCreateOptions();
            if (options == null) {
                playwright = Playwright.create();
            } else {
                playwright = Playwright.create(options);
            }
            savePlaywrightInStore(extensionContext, playwright);
        }

        return playwright;
    }

    private PlaywrightConfig getPlaywrightConfig(ExtensionContext extensionContext) {
        PlaywrightConfig config = null;

        if (extensionContext.getRequiredTestClass().isAnnotationPresent(InjectPlaywright.class)) {
            Class<? extends PlaywrightConfig> clazz = extensionContext.getRequiredTestClass().getAnnotation(InjectPlaywright.class).value();
            if (clazz != null) {
                try {
                    config = clazz.getConstructor().newInstance();
                } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
                    throw new RuntimeException("Unable to create an instance of " + clazz.getName(), ex);
                }
            }
        }

        if (extensionContext.getRequiredTestMethod().isAnnotationPresent(InjectPlaywright.class)) {
            Class<? extends PlaywrightConfig> clazz = extensionContext.getRequiredTestMethod().getAnnotation(InjectPlaywright.class).value();
            if (clazz != null) {
                try {
                    config = clazz.getConstructor().newInstance();
                } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
                    throw new RuntimeException("Unable to create an instance of " + clazz.getName(), ex);
                }
            }
        }

        return config;
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) {
        String id = extensionContext.getUniqueId() + playwrightId;
        Playwright playwright = extensionContext.getStore(browserProviderNamespace).get(id, Playwright.class);

        if (playwright != null) {
            playwright.close();
        }
    }
}


