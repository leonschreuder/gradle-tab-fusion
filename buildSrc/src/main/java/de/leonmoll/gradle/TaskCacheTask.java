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
    private TaskReportRenderer renderer = new TaskReportRenderer();

    private boolean detail;

    public void apply(Project project) {
    }

    @Override
    public ReportRenderer getRenderer() {
        return renderer;
    }

    public void setRenderer(TaskReportRenderer renderer) {
        this.renderer = renderer;
    }

    @Option(option = "all", description = "Show additional tasks and detail.")
    public void setShowDetail(boolean detail) {
        this.detail = detail;
    }

    @Console
    public boolean isDetail() {
        return detail;
    }

    @Override
    public void generate(Project project) throws IOException {
        renderer.showDetail(isDetail());
        renderer.addDefaultTasks(project.getDefaultTasks());

        AggregateMultiProjectTaskReportModel aggregateModel = new AggregateMultiProjectTaskReportModel(!isDetail(), isDetail());
        TaskDetailsFactory taskDetailsFactory = new TaskDetailsFactory(project);

        SingleProjectTaskReportModel projectTaskModel = new SingleProjectTaskReportModel(taskDetailsFactory);
        projectTaskModel.build(getProjectTaskLister().listProjectTasks(project));
        aggregateModel.add(projectTaskModel);

        for (Project subproject : project.getSubprojects()) {
            SingleProjectTaskReportModel subprojectTaskModel = new SingleProjectTaskReportModel(taskDetailsFactory);
            subprojectTaskModel.build(getProjectTaskLister().listProjectTasks(subproject));
            aggregateModel.add(subprojectTaskModel);
        }

        aggregateModel.build();

        DefaultGroupTaskReportModel model = new DefaultGroupTaskReportModel();
        model.build(aggregateModel);

        for (String group : model.getGroups()) {
            renderer.startTaskGroup(group);
            for (TaskDetails task : model.getTasksForGroup(group)) {
                renderer.addTask(task);
            }
        }
        renderer.completeTasks();

        for (Rule rule : project.getTasks().getRules()) {
            renderer.addRule(rule);
        }
    }

    @Inject
    protected ProjectTaskLister getProjectTaskLister() {
        throw new UnsupportedOperationException();
    }
}
