package io.github.uchagani.jp;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static io.github.uchagani.jp.AnnotationUtils.isAnnotationPresentOnClassOrMethod;
import static io.github.uchagani.jp.ExtensionUtils.*;

public class BrowserParameterResolver implements ParameterResolver {
    static final String id = ".browser.";
    static final Map<BrowserChoice, BrowserType> browserChoiceMap = new HashMap<>();

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Class<?> parameterType = parameterContext.getParameter().getType();
        return isAnnotationPresentOnClassOrMethod(extensionContext, UseBrowserConfig.class) && parameterType.equals(Browser.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return getBrowser(extensionContext);
    }

    private static BrowserType getBrowserType(BrowserChoice browserChoice, Playwright playwright) {
        switch (browserChoice) {
            case CHROMIUM:
                return playwright.chromium();
            case FIREFOX:
                return playwright.firefox();
            case WEBKIT:
                return playwright.webkit();
            default:
                throw new RuntimeException("Unknown BrowserChoice");
        }
    }

    private static Browser launch(BrowserType browserType, BrowserType.LaunchOptions options) {
        if (options == null) {
            return browserType.launch();
        } else {
            return browserType.launch(options);
        }
    }

    private static Browser connect(BrowserType browserType, String wsEndpoint, BrowserType.ConnectOptions options) {
        if (options == null) {
            return browserType.connect(wsEndpoint);
        } else {
            return browserType.connect(wsEndpoint, options);
        }
    }

    private static Browser connectOverCDP(BrowserType browserType, String wsEndpointUrl, BrowserType.ConnectOverCDPOptions options) {
        if (options == null) {
            return browserType.connectOverCDP(wsEndpointUrl);
        } else {
            return browserType.connectOverCDP(wsEndpointUrl, options);
        }
    }

    private static BrowserContext launchPersistentContext(BrowserType browserType, Path userDir, BrowserType.LaunchPersistentContextOptions options) {
        if (options == null) {
            return browserType.launchPersistentContext(userDir);
        } else {
            return browserType.launchPersistentContext(userDir, options);
        }
    }

    private static Browser createBrowser(BrowserConfig config, BrowserType browserType) {
        switch (config.getCreateMethod()) {
            case LAUNCH:
                return launch(browserType, config.getLaunchOptions());
            case CONNECT:
                return connect(browserType, config.getWsEndpoint(), config.getConnectOptions());
            case CONNECT_OVER_CDP:
                return connectOverCDP(browserType, config.getEndpointUrl(), config.getConnectOverCDPOptions());
            case LAUNCH_PERSISTENT_CONTEXT:
                BrowserContext browserContext = launchPersistentContext(browserType, config.getUserDataDir(), config.getLaunchPersistentContextOptions());
                return browserContext.browser();
            default:
                throw new RuntimeException("Unknown BrowserCreateMethod");
        }
    }

    public static Browser getBrowser(ExtensionContext extensionContext) {
        Browser browser = getObjectFromStore(extensionContext, id, Browser.class);
        if (browser == null) {
            Playwright playwright = PlaywrightParameterResolver.getPlaywright(extensionContext);
            BrowserConfig config = getBrowserConfig(extensionContext);
            BrowserType browserType = getBrowserType(config.getBrowser(), playwright);
            browser = createBrowser(config, browserType);
            saveBrowserInStore(extensionContext, browser);
            if (config.getCreateMethod().equals(BrowserCreateMethod.LAUNCH_PERSISTENT_CONTEXT)) {
                BrowserContextParameterResolver.saveBrowserContextInStore(extensionContext, browser.contexts().get(0));
            }
        }
        return browser;
    }

    public static void saveBrowserInStore(ExtensionContext extensionContext, Browser browser) {
        saveObjectInStore(extensionContext, id, browser);
    }
}
