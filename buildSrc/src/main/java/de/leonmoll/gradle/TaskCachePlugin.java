package de.leonmoll.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;


import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.internal.plugins.DslObject;
import org.gradle.api.plugins.ProjectReportsPluginConvention;
import org.gradle.api.plugins.ReportingBasePlugin;
import org.gradle.api.reporting.dependencies.HtmlDependencyReportTask;
import org.gradle.api.tasks.diagnostics.DependencyReportTask;
import org.gradle.api.tasks.diagnostics.PropertyReportTask;
import org.gradle.api.tasks.diagnostics.TaskReportTask;
import java.io.File;
import java.util.concurrent.Callable;

public class TaskCachePlugin implements Plugin<Project> {

    //public static final String TASK_REPORT = "taskReport";
    //public static final String PROPERTY_REPORT = "propertyReport";
    //public static final String DEPENDENCY_REPORT = "dependencyReport";
    //public static final String HTML_DEPENDENCY_REPORT = "htmlDependencyReport";
    //public static final String PROJECT_REPORT = "projectReport";

    public void apply(Project project) {
        project.getTasks().create("writeTasks", TaskCacheTask.class, (task) -> {

            project.getPluginManager().apply(ReportingBasePlugin.class);
            final ProjectReportsPluginConvention convention = new ProjectReportsPluginConvention(project);
            project.getConvention().getPlugins().put("projectReports", convention);

            //TaskReportTask taskReportTask = project.getTasks().create(TASK_REPORT, TaskReportTask.class);
            //taskReportTask.setDescription("Generates a report about your tasks.");
            //taskReportTask.conventionMapping("outputFile", new Callable<Object>() {
            //    public Object call() throws Exception {
            //        return new File(convention.getProjectReportDir(), "tasks.txt");
            //    }
            //});
            //taskReportTask.conventionMapping("projects", new Callable<Object>() {
            //    public Object call() throws Exception {
            //        return convention.getProjects();
            //    }
            //});

            //PropertyReportTask propertyReportTask = project.getTasks().create(PROPERTY_REPORT, PropertyReportTask.class);
            //propertyReportTask.setDescription("Generates a report about your properties.");
            //propertyReportTask.conventionMapping("outputFile", new Callable<Object>() {
            //    public Object call() throws Exception {
            //        return new File(convention.getProjectReportDir(), "properties.txt");
            //    }
            //});
            //propertyReportTask.conventionMapping("projects", new Callable<Object>() {
            //    public Object call() throws Exception {
            //        return convention.getProjects();
            //    }
            //});

            //DependencyReportTask dependencyReportTask = project.getTasks().create(DEPENDENCY_REPORT,
            //        DependencyReportTask.class);
            //dependencyReportTask.setDescription("Generates a report about your library dependencies.");
            //dependencyReportTask.conventionMapping("outputFile", new Callable<Object>() {
            //    public Object call() throws Exception {
            //        return new File(convention.getProjectReportDir(), "dependencies.txt");
            //    }
            //});
            //dependencyReportTask.conventionMapping("projects", new Callable<Object>() {
            //    public Object call() throws Exception {
            //        return convention.getProjects();
            //    }
            //});

            //HtmlDependencyReportTask htmlDependencyReportTask = project.getTasks().create(HTML_DEPENDENCY_REPORT,
            //        HtmlDependencyReportTask.class);
            //htmlDependencyReportTask.setDescription("Generates an HTML report about your library dependencies.");
            //new DslObject(htmlDependencyReportTask.getReports().getHtml()).getConventionMapping().map("destination", new Callable<Object>() {
            //    public Object call() throws Exception {
            //        return new File(convention.getProjectReportDir(), "dependencies");
            //    }
            //});
            //htmlDependencyReportTask.conventionMapping("projects", new Callable<Object>() {
            //    public Object call() throws Exception {
            //        return convention.getProjects();
            //    }
            //});

            //Task projectReportTask = project.getTasks().create(PROJECT_REPORT);
            //projectReportTask.dependsOn(TASK_REPORT, PROPERTY_REPORT, DEPENDENCY_REPORT, HTML_DEPENDENCY_REPORT);
            //projectReportTask.setDescription("Generates a report about your project.");
            //projectReportTask.setGroup("reporting");
        });
    }
}
