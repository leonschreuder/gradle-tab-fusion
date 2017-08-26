package de.leonmoll.gradle;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import java.io.*;
import java.net.URL;

public class InstallerTask extends DefaultTask {

    //TODO: Ask user for install location?
    //    -> http://mrhaki.blogspot.de/2010/09/gradle-goodness-get-user-input-values.html

    static final String SCRIPT_URL = "https://raw.githubusercontent.com/meonlol/gradle-tab-fusion/master/src/main/bash/gradle-tab-completion.bash";
    private static final String INSTALLATION_DIRECTORY = "/usr/local/etc/bash_completion.d/gradle-tab-completion.bash";

    @TaskAction
    void taskAction() {
        wget(SCRIPT_URL, INSTALLATION_DIRECTORY);
        makeExecutable();
        sourceCompletionScript();
    }

    protected void wget(String uri, String fileName) {
        try {
            URL url = new URL(uri);

            OutputStream outputStream = new FileOutputStream(fileName);
            byte[] buffer = new byte[10*1024];

            InputStream in = url.openStream();
            for(int length; (length = in.read(buffer)) != -1;) {
                outputStream.write(buffer, 0, length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void makeExecutable() {
        getProject().exec(exec -> {
            exec.executable("chmod");
            exec.args("-x", INSTALLATION_DIRECTORY);
        });
    }

    private void sourceCompletionScript() {
        getProject().getLogger().quiet("Installation complete. However I cannot 'source' the file for you.");
        getProject().getLogger().quiet("To use the command now, execute the following line:");
        getProject().getLogger().quiet("");
        getProject().getLogger().quiet("source " + INSTALLATION_DIRECTORY);
    }
}
