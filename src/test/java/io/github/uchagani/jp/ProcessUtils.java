package io.github.uchagani.jp;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ProcessUtils {
    public static long getDriverProcessPid() {
        var cli = ProcessHandle.allProcesses().filter(p -> {
            var args = p.info().arguments().orElse(new String[0]);
            for (var a : args) {
                return a.contains("cli");
            }
            return false;
        }).findFirst().orElse(null);

        assertThat(cli).isNotNull();
        return cli.pid();
    }

    public static String getCommandsUsedToRunProcess(long pid) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("ps", "eww", String.valueOf(pid));
        return IOUtils.toString(pb.start().getInputStream(), String.valueOf(StandardCharsets.UTF_8));
    }
}
