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
import org.gradle.api.tasks.TaskAction;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandlineFlagsTask extends DefaultTask {

    private static final String COMMANDLINEFLAGS_CACHE_FILE = "cmdlineflags.cache";

    @TaskAction
    void taskAction() {
        ByteArrayOutputStream gradleHelpOutput = requestCommandlineHelpOutput();

        List<String> flags = parseHelpOutput(gradleHelpOutput);

        File outFile = new File(getProject().getBuildDir(), COMMANDLINEFLAGS_CACHE_FILE);
        FileUtils.writeStringToFile(outFile, String.join(" ", flags));
    }

    @NotNull
    private ByteArrayOutputStream requestCommandlineHelpOutput() {
        ByteArrayOutputStream cmdOutput = new ByteArrayOutputStream();
        getProject().exec(execSpec -> {
            execSpec.executable("./gradlew"); //TODO: support native gradle distribution
            execSpec.args("--help");
            execSpec.setStandardOutput(cmdOutput);
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
