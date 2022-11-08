package io.github.uchagani.jp;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.platform.testkit.engine.EngineTestKit;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

// This needs to be run serially until https://github.com/microsoft/playwright-java/issues/878 is fixed
@Tag("playwrightCreate")
@Execution(ExecutionMode.SAME_THREAD)
public class PlaywrightTests {
    @Test
    void injectPlaywrightTestRunsSuccessfully() {
        EngineTestKit
                .engine("junit-jupiter")
                .selectors(selectClass(PlaywrightTestCase.class))
                .execute()
                .testEvents()
                .assertStatistics(stat -> stat.succeeded(1));
    }

    @Tag("flakey")
    @Test
    void injectPlaywrightWithOptionsTestRunsFails() {
        EngineTestKit
                .engine("junit-jupiter")
                .selectors(selectClass(PlaywrightWithOptionsTestCase.class))
                .execute()
                .testEvents()
                .assertStatistics(stat -> stat.failed(1));
    }
}
