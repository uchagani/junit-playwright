package io.github.uchagani.jp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.platform.testkit.engine.EngineTestKit;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
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

            String testName = generateFileNameFromMethod(test);
            if (test.getName().endsWith("isCreated")) {
                assertThat(outputDir).isDirectoryContaining(getTraceFileName(testName));
            } else if (test.getName().endsWith("isNotCreated")) {
                assertThat(outputDir).isDirectoryNotContaining(getTraceFileName(testName));
            }
        }
    }

    private String generateFileNameFromMethod(Method method) {
        StringBuilder testName = new StringBuilder(method.getName());
        testName.append("(");
        for(int i = 0; i < method.getParameterCount(); i++) {
            if(i > 0) {
                testName.append(", ");
            }
            testName.append(method.getParameters()[i].getType().getName());
        }
        testName.append(")");
        return testName.toString();
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
