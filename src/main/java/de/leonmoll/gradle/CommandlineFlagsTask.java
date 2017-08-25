package de.leonmoll.gradle;

import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskAction;

import java.io.*;

/**
 *
 */
public class CommandlineFlagsTask extends DefaultTask {

    @TaskAction
    void taskAction() {
        ByteArrayOutputStream cmdOutput = new ByteArrayOutputStream();

        getProject().exec(execSpec -> {
            execSpec.executable("./gradlew");
            execSpec.args("--help");
            execSpec.setStandardOutput(cmdOutput);
        });

        File outFile = new File(getProject().getBuildDir(), "flags.txt");
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(outFile);
            cmdOutput.writeTo(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }  finally {
            if (outputStream != null) try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
