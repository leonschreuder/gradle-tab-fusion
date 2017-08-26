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

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskContainer;

public class TaskCachePlugin implements Plugin<Project> {
    //Reference: github.com/gradle/gradle -> ProjectReportsPlugin.java

    public void apply(Project project) {
        TaskContainer taskContainer = project.getTasks();

        TaskCacheTask taskCacheTask = taskContainer.create("cacheTaskList", TaskCacheTask.class);
        taskContainer.getByName("assemble").dependsOn(taskCacheTask);

        CommandlineFlagsTask flagsCacheTask = taskContainer.create("cacheCommandlineFlags", CommandlineFlagsTask.class);
        taskContainer.getByName("assemble").dependsOn(flagsCacheTask);

        taskContainer.create("installTabCompletion", InstallerTask.class);

        //TODO: Add up-to-date checks for both
    }
}
