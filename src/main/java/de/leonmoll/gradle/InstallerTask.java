package de.leonmoll.gradle;

import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.tasks.TaskAction;

import javax.swing.*;
import java.io.*;
import java.net.URL;

public class InstallerTask extends DefaultTask {

    //TODO: Ask user for install location?
    //    -> http://mrhaki.blogspot.de/2010/09/gradle-goodness-get-user-input-values.html

    static final String SCRIPT_URL = "https://raw.githubusercontent.com/meonlol/gradle-tab-fusion/master/src/main/bash/gradle-tab-completion.bash";
    private static final String SCRIPT_NAME = "gradle-tab-completion.bash";
    private static final String INSTALLATION_DIRECTORY = "/etc/bash_completion.d/";

    @TaskAction
    void taskAction() {


        try {
            File tmpDir = createTempDirectory();
            wget(SCRIPT_URL, tmpDir + "/" + SCRIPT_NAME);
            install(tmpDir + "/" + SCRIPT_NAME, INSTALLATION_DIRECTORY);
            sourceCompletionScript();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void wget(String uri, String fileName) {
        OutputStream outputStream = null;
        try {
            URL url = new URL(uri);

            outputStream = new FileOutputStream(fileName);
            byte[] buffer = new byte[10*1024];

            InputStream in = url.openStream();
            for(int length; (length = in.read(buffer)) != -1;) {
                outputStream.write(buffer, 0, length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void install(String fromPath, String toPath) {
        String pass = new String(askUserPassword());
        if (! new File(toPath).exists()) {
            getProject().exec(exec -> {
                exec.setStandardInput(new ByteArrayInputStream((pass + "\n").getBytes()));
                exec.commandLine("sudo", "-S", "mkdir", "-p", toPath);
            });
        }

        getProject().exec(exec -> {
            exec.setStandardInput(new ByteArrayInputStream((pass + "\n").getBytes()));
            exec.commandLine("sudo", "-S", "mv", fromPath, toPath);
        });

        getProject().exec(exec -> {
            exec.setStandardInput(new ByteArrayInputStream((pass + "\n").getBytes()));
            exec.commandLine("sudo", "-S", "chmod", "-x", INSTALLATION_DIRECTORY);
        });
    }

    private char[] askUserPassword() {
        System.out.println("--- Please input password to install ---");
        Console console = System.console();
        if (console != null) {
            return console.readPassword();
        } else {
            JPasswordField passwordField = new JPasswordField(10);
            int action = JOptionPane.showConfirmDialog(null, passwordField, "Please input password to install.", JOptionPane.OK_CANCEL_OPTION);
            if (action > 0) {
                throw new GradleException("Unable to install without system password.");
            }
            return passwordField.getPassword();
        }
    }

    public static File createTempDirectory() throws IOException {
        final File temp;

        temp = File.createTempFile("temp", Long.toString(System.nanoTime()));

        if(!(temp.delete())) {
            throw new IOException("Could not delete temp file: " + temp.getAbsolutePath());
        }

        if(!(temp.mkdir())) {
            throw new IOException("Could not create temp directory: " + temp.getAbsolutePath());
        }

        return (temp);
    }

    private void sourceCompletionScript() {
        getProject().getLogger().quiet("Installation complete. However I cannot 'source' the file for you.");
        getProject().getLogger().quiet("To use the command now, execute the following line:");
        getProject().getLogger().quiet("");
        getProject().getLogger().quiet("source " + INSTALLATION_DIRECTORY);
    }
}
