package de.leonmoll.gradle;

import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.*;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class TaskCachePluginTest {

    @Test
    public void should_be_able_to_apply() {
        Project project = ProjectBuilder.builder().build();
        project.getTasks().create("assemble", DefaultTask.class);

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("plugin", TaskCachePlugin.class);
        project.apply(map);

        assertNotNull(project.getTasks().getByName("cacheCommandlineFlags"));
    }
}
