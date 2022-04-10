package io.github.uchagani.jp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.platform.testkit.engine.EngineTestKit;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

@Execution(ExecutionMode.SAME_THREAD)
public class TraceTests {
    @Test
    void verifyTraceTestStats() {
        EngineTestKit
                .engine("junit-jupiter")
                .selectors(selectClass(TraceTestCase.class))
                .execute();

        List<Method> testMethods = Arrays.stream(TraceTestCase.class.getMethods())
                .filter(m -> m.isAnnotationPresent(Test.class)).collect(Collectors.toList());

        for (Method test : testMethods) {
            BrowserConfig config = getBrowserConfig(test.getAnnotation(UseBrowserConfig.class).value());
            Path outputDir = config.getOutputDirectory();

            if (test.getName().endsWith("isCreated")) {
                assertThat(outputDir).isDirectoryContaining(getTraceFileName(test.getName()));
            } else if (test.getName().endsWith("isNotCreated")) {
                assertThat(outputDir).isDirectoryNotContaining(getTraceFileName(test.getName()));
            }
        }
    }

    private String getTraceFileName(String testName) {
        return "glob:**" + TraceTestCase.class.getName() + "." + testName + ".zip";
    }

    private BrowserConfig getBrowserConfig(Class<? extends PlaywrightBrowserConfig> clazz) {
        try {
            return clazz.getConstructor().newInstance().getBrowserConfig();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
