package io.github.uchagani.jp;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Tracing;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.github.uchagani.jp.APIRequestContextParameterResolver.closeAPIRequestContext;
import static io.github.uchagani.jp.ExtensionUtils.getBrowserConfig;
import static io.github.uchagani.jp.PlaywrightParameterResolver.closePlaywright;

public class PlaywrightTestWatcher implements TestWatcher {
    @Override
    public void testSuccessful(ExtensionContext extensionContext) {
        BrowserConfig browserConfig = getBrowserConfig(extensionContext);
        if (browserConfig.getEnableTracing()) {
            if (!browserConfig.getSaveTraceOnlyOnFailure()) {
                stopTrace(extensionContext);
            }
        }
        cleanup(extensionContext);
    }

    @Override
    public void testAborted(ExtensionContext extensionContext, Throwable cause) {
        BrowserConfig browserConfig = getBrowserConfig(extensionContext);
        if (browserConfig.getEnableTracing()) {
            if (!browserConfig.getSaveTraceOnlyOnFailure()) {
                stopTrace(extensionContext);
            }
        }
        cleanup(extensionContext);
    }

    @Override
    public void testFailed(ExtensionContext extensionContext, Throwable cause) {
        BrowserConfig browserConfig = getBrowserConfig(extensionContext);
        if (browserConfig.getEnableTracing()) {
            stopTrace(extensionContext);
        }
        cleanup(extensionContext);
    }

    private void stopTrace(ExtensionContext extensionContext) {
        BrowserConfig config = getBrowserConfig(extensionContext);
        BrowserContext browserContext = BrowserContextParameterResolver.getBrowserContext(extensionContext);
        Path tracePath = Paths.get(String.valueOf(config.getOutputDirectory()), getSafeTestName(extensionContext));
        Tracing.StopOptions stopOptions = new Tracing.StopOptions().setPath(tracePath);
        browserContext.tracing().stop(stopOptions);
    }

    private String getSafeTestName(ExtensionContext extensionContext) {
        Pattern regex = Pattern.compile("\\[class:(.*)\\]\\/\\[method:(.*)\\]");
        Matcher matcher = regex.matcher(extensionContext.getUniqueId());
        matcher.find();
        return matcher.group(1) + "." + matcher.group(2) + ".zip";
    }

    private void cleanup(ExtensionContext extensionContext) {
        closeAPIRequestContext(extensionContext);
        closePlaywright(extensionContext);
    }
}
