package io.github.uchagani.jp;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Tracing;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PlaywrightTestWatcher implements TestWatcher {
    @Override
    public void testSuccessful(ExtensionContext extensionContext) {
        BrowserConfig browserConfig = PlaywrightParameterResolver.getPlaywrightConfig(extensionContext).getBrowserConfig();

        if (browserConfig.getEnableTracing()) {
            if (!browserConfig.getSaveTraceOnlyOnFailure()) {
                stopTrace(extensionContext);
            }
        }

        PlaywrightParameterResolver.closePlaywright(extensionContext);
    }

    @Override
    public void testAborted(ExtensionContext extensionContext, Throwable cause) {
        BrowserConfig browserConfig = PlaywrightParameterResolver.getPlaywrightConfig(extensionContext).getBrowserConfig();

        if (browserConfig.getEnableTracing()) {
            if (!browserConfig.getSaveTraceOnlyOnFailure()) {
                stopTrace(extensionContext);
            }
        }

        PlaywrightParameterResolver.closePlaywright(extensionContext);
    }

    @Override
    public void testFailed(ExtensionContext extensionContext, Throwable cause) {
        BrowserConfig browserConfig = PlaywrightParameterResolver.getPlaywrightConfig(extensionContext).getBrowserConfig();

        if (browserConfig.getEnableTracing()) {
            stopTrace(extensionContext);
        }

        PlaywrightParameterResolver.closePlaywright(extensionContext);
    }

    private void stopTrace(ExtensionContext extensionContext) {
        BrowserConfig config = PlaywrightParameterResolver.getPlaywrightConfig(extensionContext).getBrowserConfig();
        BrowserContext browserContext = BrowserContextParameterResolver.getBrowserContext(extensionContext);

        Path tracePath = Paths.get(String.valueOf(config.getOutputDirectory()), getSafeTestName(extensionContext));

        Tracing.StopOptions stopOptions = new Tracing.StopOptions()
                .setPath(tracePath);

        browserContext.tracing().stop(stopOptions);
    }

    private String getSafeTestName(ExtensionContext extensionContext) {
        return extensionContext.getRequiredTestClass().getName() + "." + extensionContext.getRequiredTestMethod().getName() + ".zip";
    }
}
