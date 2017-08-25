package de.leonmoll.gradle;

import org.gradle.api.Project;
import org.gradle.api.internal.project.ProjectTaskLister;
import org.gradle.api.tasks.diagnostics.AbstractReportTask;
import org.gradle.api.tasks.diagnostics.internal.AggregateMultiProjectTaskReportModel;
import org.gradle.api.tasks.diagnostics.internal.DefaultGroupTaskReportModel;
import org.gradle.api.tasks.diagnostics.internal.ReportRenderer;
import org.gradle.api.tasks.diagnostics.internal.SingleProjectTaskReportModel;
import org.gradle.api.tasks.diagnostics.internal.TaskDetails;
import org.gradle.api.tasks.diagnostics.internal.TaskDetailsFactory;
import org.gradle.api.tasks.diagnostics.internal.TaskReportRenderer;

import javax.inject.Inject;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TaskCacheTask extends AbstractReportTask {
    private TaskReportRenderer renderer = new TaskReportRenderer();

    private AggregateMultiProjectTaskReportModel aggregateModel;
    private TaskDetailsFactory taskDetailsFactory;

    public void apply(Project project) {
    }

    @Override
    public ReportRenderer getRenderer() {
        return new TaskReportRenderer(); // Sure, this is violates LSP, but i'm not really rendering any reports anyway
    }

    @Override
    public void generate(Project project) throws IOException {
        List<String> taskList = new ArrayList<String>();

        DefaultGroupTaskReportModel model = buildReportModelForAllTasks(project);

        for (String group : model.getGroups()) {
            for (TaskDetails task : model.getTasksForGroup(group)) {
                taskList.add(task.getPath().toString());
            }
        }

        File outFile = new File(project.getBuildDir(), "tasks.txt");
        writeStringToFile(outFile, String.join(" ", taskList));

        //TODO: Completion for rules probably needs more complex logic, implement if actually needed
//        for (Rule rule : project.getTasks().getRules()) {
//            project.getLogger().lifecycle(rule.getDescription());
//        }
    }

    private DefaultGroupTaskReportModel buildReportModelForAllTasks(Project project) {
        //Create task model in which those for all projects are combinde
        aggregateModel = new AggregateMultiProjectTaskReportModel(false, true);
        taskDetailsFactory = new TaskDetailsFactory(project);

        aggregateTasksFromProject(project);

        for (Project subproject : project.getSubprojects()) {
            aggregateTasksFromProject(subproject);
        }

        aggregateModel.build();

        DefaultGroupTaskReportModel model = new DefaultGroupTaskReportModel();
        model.build(aggregateModel);
        return model;
    }

    private void aggregateTasksFromProject(Project project) {
        SingleProjectTaskReportModel projectTaskModel = new SingleProjectTaskReportModel(taskDetailsFactory);
        projectTaskModel.build(getProjectTaskLister().listProjectTasks(project));
        aggregateModel.add(projectTaskModel);
    }

    private void writeStringToFile(File outFile, String fileContentString) {
        BufferedWriter bw = null;
        FileWriter fw = null;

        try {
            fw = new FileWriter(outFile);
            bw = new BufferedWriter(fw);
            bw.write(fileContentString);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) bw.close();
                if (fw != null) fw.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Inject
    protected ProjectTaskLister getProjectTaskLister() {
        throw new UnsupportedOperationException();
    }
}
