package de.leonmoll.gradle;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.gradle.internal.impldep.com.google.common.annotations.VisibleForTesting;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class CommandlineFlagsTask extends DefaultTask {

    @TaskAction
    void taskAction() {
        ByteArrayOutputStream cmdOutput = getCommandlineHelpOutput();
        List<String> flags = parseOutput(cmdOutput);
        File outFile = new File(getProject().getBuildDir(), "cmdlineflags.cache");
        FileUtils.writeStringToFile(outFile, String.join(" ", flags));
    }

    @NotNull
    private ByteArrayOutputStream getCommandlineHelpOutput() {
        ByteArrayOutputStream cmdOutput = new ByteArrayOutputStream();
        getProject().exec(execSpec -> {
            execSpec.executable("./gradlew");
            execSpec.args("--help");
            execSpec.setStandardOutput(cmdOutput);
        });
        return cmdOutput;
    }

    protected List<String> parseOutput(ByteArrayOutputStream bos) {
        BufferedReader bufferedReader;
        ArrayList<String> result  = new ArrayList<>();

        try {
            bufferedReader = new BufferedReader(new StringReader(new String(bos.toByteArray())));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.startsWith("-")) {
                    String[] splitCmdsAndDescription = line.split("\\s\\s+");
                    result.addAll(Arrays.asList(splitCmdsAndDescription[0].split(",\\s")));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
