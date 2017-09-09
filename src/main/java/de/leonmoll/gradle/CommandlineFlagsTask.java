/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.leonmoll.gradle;

import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;
import org.jetbrains.annotations.NotNull;
import org.gradle.internal.os.OperatingSystem;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandlineFlagsTask extends DefaultTask {

    private static final String COMMANDLINEFLAGS_CACHE_FILE = "cmdlineflags.cache";
    private static final String COMPLETION_DIR = "completion";

    @OutputDirectory
    File outputDir = new File(getProject().getBuildDir(), COMPLETION_DIR);

    @OutputFile
    File outputFile = new File(outputDir, COMMANDLINEFLAGS_CACHE_FILE);

    @TaskAction
    void taskAction() {
        ByteArrayOutputStream gradleHelpOutput = requestCommandlineHelpOutput();

        List<String> flags = parseHelpOutput(gradleHelpOutput);

        outputDir.mkdirs();
        FileUtils.writeStringToFile(outputFile, String.join(" ", flags));
    }

    @NotNull
    private ByteArrayOutputStream requestCommandlineHelpOutput() {
        ByteArrayOutputStream cmdOutput = new ByteArrayOutputStream();
        getProject().exec(exec -> {

            if (OperatingSystem.current().isWindows()) {
                if (new File(getProject().getProjectDir(), "gradlew.bat").exists()) {
                    exec.commandLine("cmd", "/c", "gradlew.bat", "--help");
                } else {
                    exec.commandLine("cmd", "/c", "gradle", "--help"); //using default installation
                }
            } else {
                if (new File(getProject().getProjectDir(), "gradlew").exists()) {
                    exec.commandLine("./gradlew", "--help");
                } else {
                    exec.commandLine("gradle", "--help");
                }
            }

            exec.setStandardOutput(cmdOutput);
        });
        return cmdOutput;
    }

    protected List<String> parseHelpOutput(ByteArrayOutputStream bos) {
        BufferedReader bufferedReader;
        BufferedWriter bw = null;
        FileWriter fw = null;
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
