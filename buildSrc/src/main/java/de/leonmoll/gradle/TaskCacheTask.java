package de.leonmoll.gradle;

import org.gradle.api.Project;
import org.gradle.api.Rule;
import org.gradle.api.internal.project.ProjectTaskLister;
import org.gradle.api.internal.tasks.options.Option;
import org.gradle.api.tasks.Console;
import org.gradle.api.tasks.diagnostics.AbstractReportTask;
import org.gradle.api.tasks.diagnostics.internal.AggregateMultiProjectTaskReportModel;
import org.gradle.api.tasks.diagnostics.internal.DefaultGroupTaskReportModel;
import org.gradle.api.tasks.diagnostics.internal.ReportRenderer;
import org.gradle.api.tasks.diagnostics.internal.SingleProjectTaskReportModel;
import org.gradle.api.tasks.diagnostics.internal.TaskDetails;
import org.gradle.api.tasks.diagnostics.internal.TaskDetailsFactory;
import org.gradle.api.tasks.diagnostics.internal.TaskReportRenderer;

import javax.inject.Inject;
import java.io.IOException;

public class TaskCacheTask extends AbstractReportTask {
//    private TaskReportRenderer renderer = new TaskReportRenderer();

//    private boolean detail;
    private AggregateMultiProjectTaskReportModel aggregateModel;
    private TaskDetailsFactory taskDetailsFactory;

    public void apply(Project project) {
    }

    @Override
    public ReportRenderer getRenderer() {
        return new TaskReportRenderer(); // Sure, this is violates LSP, but i'm not really rendering any reports anyway
    }

//    public void setRenderer(TaskReportRenderer renderer) {
//        this.renderer = renderer;
//    }

//    @Option(option = "all", description = "Show additional tasks and detail.")
//    public void setShowDetail(boolean detail) {
//        this.detail = detail;
//    }

//    @Console
//    public boolean isDetail() {
//        return detail;
//    }

    @Override
    public void generate(Project project) throws IOException {
        //setShowDetail(true);
        //renderer.showDetail(isDetail());
        //renderer.addDefaultTasks(project.getDefaultTasks()); //defaultTasks is empty

        DefaultGroupTaskReportModel model = buildModelForAllTasks(project);

        for (String group : model.getGroups()) {
            //renderer.startTaskGroup(group); //only prints headers
            for (TaskDetails task : model.getTasksForGroup(group)) {
                project.getLogger().lifecycle(task.getPath().toString());
                //renderer.addTask(task);
            }
        }
        //renderer.completeTasks();

        //for (Rule rule : project.getTasks().getRules()) {
        //    renderer.addRule(rule);
        //}
    }

    private DefaultGroupTaskReportModel buildModelForAllTasks(Project project) {
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

    @Inject
    protected ProjectTaskLister getProjectTaskLister() {
        throw new UnsupportedOperationException();
    }
}
