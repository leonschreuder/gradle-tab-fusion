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

import org.gradle.api.Project;
import org.gradle.api.internal.project.ProjectTaskLister;
import org.gradle.api.tasks.diagnostics.AbstractReportTask;
import org.gradle.api.tasks.diagnostics.internal.*;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TaskCacheTask extends AbstractReportTask {
    //Reference: github.com/gradle/gradle -> TaskReportTask.java

    private AggregateMultiProjectTaskReportModel aggregateModel;
    private TaskDetailsFactory taskDetailsFactory;

    public void apply(Project project) {
    }

    @Override
    public ReportRenderer getRenderer() {
        return new EmptyReportRenderer(); // Sure, this is violates LSP, but i'm not really rendering any reports anyway
    }

    @Override
    public void generate(Project project) throws IOException {
        List<String> taskList = new ArrayList<>();

        TaskReportModel model = buildReportModelForAllTasks(project);

        for (String group : model.getGroups()) {
            for (TaskDetails task : model.getTasksForGroup(group)) {
                taskList.add(task.getPath().toString());
            }
        }

        File outFile = new File(project.getBuildDir(), "tasks.txt");
        FileUtils.writeStringToFile(outFile, String.join(" ", taskList));

        //TODO: Completion for rules probably needs more complex logic, implement if actually needed
        //for (Rule rule : project.getTasks().getRules()) {
        //    project.getLogger().lifecycle(rule.getDescription());
        //}
    }

    private TaskReportModel buildReportModelForAllTasks(Project project) {
        //Create task model in which those for all projects are combinde
        aggregateModel = new AggregateMultiProjectTaskReportModel(false, true);
        taskDetailsFactory = new TaskDetailsFactory(project);

        aggregateTasksFromProject(project);

        for (Project subproject : project.getSubprojects()) {
            aggregateTasksFromProject(subproject);
        }

        aggregateModel.build();

        return aggregateModel;
    }

    private void aggregateTasksFromProject(Project project) {
        SingleProjectTaskReportModel projectTaskModel = new SingleProjectTaskReportModel(taskDetailsFactory);
        projectTaskModel.build(getProjectTaskLister().listProjectTasks(project));
        aggregateModel.add(projectTaskModel);
    }

    @Inject
    protected ProjectTaskLister getProjectTaskLister() {
        throw new UnsupportedOperationException();
    }

}
