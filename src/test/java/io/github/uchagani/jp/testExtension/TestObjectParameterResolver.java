package io.github.uchagani.jp.testExtension;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import io.github.uchagani.jp.BrowserContextParameterResolver;
import io.github.uchagani.jp.BrowserParameterResolver;
import io.github.uchagani.jp.PageParameterResolver;
import io.github.uchagani.jp.PlaywrightParameterResolver;
import org.junit.jupiter.api.extension.*;

public class TestObjectParameterResolver implements ParameterResolver {

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(TestObject.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Playwright playwright = PlaywrightParameterResolver.getPlaywright(extensionContext);
        Browser browser = BrowserParameterResolver.getBrowser(extensionContext);
        BrowserContext browserContext = BrowserContextParameterResolver.getBrowserContext(extensionContext);
        Page page = PageParameterResolver.getPage(extensionContext);

        return new TestObject(playwright, browser, browserContext, page);
    }
}
