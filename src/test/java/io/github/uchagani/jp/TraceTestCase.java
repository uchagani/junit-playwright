package io.github.uchagani.jp;

import com.microsoft.playwright.Page;
import io.github.uchagani.jp.configs.TraceBrowserConfig;
import io.github.uchagani.jp.configs.TraceBrowserConfigAlternateOutputDir;
import io.github.uchagani.jp.configs.TraceBrowserConfigSaveOnlyOnFailure;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import static org.assertj.core.api.Assumptions.assumeThat;
import static org.junit.jupiter.api.Assertions.fail;

@Tag("indirect")
public class TraceTestCase {
    @BeforeAll
    public static void clearTestResultsDir() {
        try {
            Files.walk(Paths.get("test-results"))
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (Exception ignored) {
        }

        try {
            Files.walk(Paths.get("test-results-2"))
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (Exception ignored) {
        }
    }

    @Test
    @UseBrowserConfig(TraceBrowserConfig.class)
    public void traceFile_alwaysCreate_onPass_isCreated(Page ignored) {
        //force pass
        return;
    }

    @Test
    @UseBrowserConfig(TraceBrowserConfig.class)
    public void traceFile_alwaysCreate_onFail_isCreated(Page ignored) {
        fail("force fail");
    }

    @Test
    @UseBrowserConfig(TraceBrowserConfig.class)
    public void traceFile_alwaysCreate_onAbort_isCreated(Page ignored) {
        assumeThat(true).isEqualTo(false);
    }

    @Test
    @UseBrowserConfig(TraceBrowserConfigSaveOnlyOnFailure.class)
    public void traceFile_onPass_isNotCreated(Page ignored) {
        //force pass
        return;
    }

    @Test
    @UseBrowserConfig(TraceBrowserConfigSaveOnlyOnFailure.class)
    public void traceFile_onAbort_isNotCreated(Page ignored) {
        assumeThat(true).isEqualTo(false);
    }

    @Test
    @UseBrowserConfig(TraceBrowserConfigSaveOnlyOnFailure.class)
    public void traceFile_onFail_isCreated(Page ignored) {
        fail("force fail");
    }

    @Test
    @UseBrowserConfig(TraceBrowserConfigAlternateOutputDir.class)
    public void traceFile_inAlternateDir_isCreated(Page ignored) {
        //force pass
        return;
    }

}
