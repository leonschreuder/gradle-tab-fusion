package de.leonmoll.gradle;

import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.gradle.internal.impldep.org.codehaus.plexus.util.FileUtils.mkdir;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.HashMap;

public class InstallerTaskTest {

    private final String tmpFileName = "actual_file.txt";
    private Project project;

    @Before
    public void setup() {
        project = ProjectBuilder.builder().build();
        project.getTasks().create("assemble", DefaultTask.class);

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("plugin", TaskCachePlugin.class);
        project.apply(map);

        mkdir(project.getBuildDir().toString()); //create build-directory
    }

    @After
    public void tearDown() {
        new File(tmpFileName).delete();
    }

    @Test
    public void should_be_able_to_download_a_file() {
        InstallerTask installerTask = (InstallerTask) project.getTasks().getByName("installTabCompletion");

        installerTask.wget(InstallerTask.SCRIPT_URL, tmpFileName);

        assertTrue(new File(tmpFileName).exists());
        installerTask.taskAction();
    }

}
