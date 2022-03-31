package io.github.uchagani.jp;

import org.junit.jupiter.api.Test;
import org.junit.platform.testkit.engine.EngineTestKit;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

public class InjectPlaywrightTest {
    @Test
    void injectPlaywrightTestRunsSuccessfully() {
        EngineTestKit
                .engine("junit-jupiter")
                .selectors(selectClass(InjectPlaywrightTestCase.class))
                .execute()
                .testEvents()
                .assertStatistics(stat -> stat.succeeded(1));
    }

    @Test
    void injectPlaywrightWithOptionsTestRunsFails() {
        EngineTestKit
                .engine("junit-jupiter")
                .selectors(selectClass(InjectPlaywrightWithOptionsTestCase.class))
                .execute()
                .testEvents()
                .assertStatistics(stat -> stat.failed(1));
    }
}
