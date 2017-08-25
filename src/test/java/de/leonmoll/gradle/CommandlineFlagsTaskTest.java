package de.leonmoll.gradle;

import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.jetbrains.annotations.NotNull;
import org.junit.*;

import java.io.*;
import java.util.*;

import static org.gradle.internal.impldep.org.codehaus.plexus.util.FileUtils.mkdir;
import static org.junit.Assert.*;

public class CommandlineFlagsTaskTest {

    private static Project project;

    @BeforeClass
    public static void classSetup() {
        project = ProjectBuilder.builder().build();
        project.getTasks().create("assemble", DefaultTask.class);

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("plugin", TaskCachePlugin.class);
        project.apply(map);

        mkdir(project.getBuildDir().toString()); //create build-directory
    }

    @Test
    public void should_read_commands_to_string() throws IOException {
        CommandlineFlagsTask flagsTask = (CommandlineFlagsTask) project.getTasks().getByName("cacheCommandlineFlags");
        String input = "--help, --something";
        ByteArrayOutputStream cmdOutput = new ByteArrayOutputStream();
        cmdOutput.write(input.getBytes());

        List<String> flags = flagsTask.parseHelpOutput(cmdOutput);

        assertEquals(Arrays.asList("--help", "--something"), flags);
    }

    @Test
    public void should_read_complete_help_output() throws IOException {
        ByteArrayOutputStream bos = readFileAsByteArray();

        CommandlineFlagsTask flagsTask = (CommandlineFlagsTask) project.getTasks().getByName("cacheCommandlineFlags");

        List<String> flags = flagsTask.parseHelpOutput(bos);

        assertEquals(Arrays.asList("-?","-h","--help","-a","--no-rebuild","-b","--build-file","-c","--settings-file","--configure-on-demand","--console","--continue","-D","--system-prop","-d","--debug","--daemon","--foreground","-g","--gradle-user-home","--gui","-I","--init-script","-i","--info","--include-build","-m","--dry-run","--max-workers","--no-daemon","--offline","-P","--project-prop","-p","--project-dir","--parallel","--profile","--project-cache-dir","-q","--quiet","--recompile-scripts","--refresh-dependencies","--rerun-tasks","-S","--full-stacktrace","-s","--stacktrace","--status","--stop","-t","--continuous","-u","--no-search-upward","-v","--version","-x","--exclude-task"), flags);
    }

    @NotNull
    private ByteArrayOutputStream readFileAsByteArray() throws IOException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classloader.getResourceAsStream("flags.txt");

        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        int bytesRead;
        byte[] dataByteArray = new byte[inputStream.available()];
        while((bytesRead = inputStream.read(dataByteArray, 0, dataByteArray.length)) != -1) {
            byteOutputStream.write(dataByteArray, 0, bytesRead);
        }
        return byteOutputStream;
    }

}
