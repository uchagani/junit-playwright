package io.github.uchagani.jp;

import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/*
These tests need to be run serially because I couldn't figure out a good way to get
the pid of the correct cli.js process when run in parallel.
 */
@Execution(ExecutionMode.SAME_THREAD)
@InjectPlaywright(DefaultConfig.class)
public class InjectPlaywrightTests {

    @Test
    public void shouldInject_Playwright_definedAtClassScope(Playwright playwright) throws IOException {
        assertThat(playwright).isNotNull();
        var processPid = ProcessUtils.getDriverProcessPid();
        var command = ProcessUtils.getCommandsUsedToRunProcess(processPid);

        DefaultConfig.playwrightOptions.forEach((k, v) -> assertThat(command).contains(k + "=" + v));
    }

    @Test
    @InjectPlaywright(OverrideConfig.class)
    public void shouldInject_Playwright_definedAtMethodScope(Playwright playwright) throws IOException {
        assertThat(playwright).isNotNull();
        var processPid = ProcessUtils.getDriverProcessPid();
        var command = ProcessUtils.getCommandsUsedToRunProcess(processPid);

        OverrideConfig.playwrightOptions.forEach((k, v) -> assertThat(command).contains(k + "=" + v));
        DefaultConfig.playwrightOptions.forEach((k, v) -> assertThat(command).doesNotContain(k + "=" + v));    }
}
