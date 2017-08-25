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
import org.gradle.api.Task;
import org.gradle.api.tasks.Exec;
import org.gradle.api.tasks.TaskContainer;

import java.io.*;

public class TaskCachePlugin implements Plugin<Project> {
    //Reference: github.com/gradle/gradle -> ProjectReportsPlugin.java

    public void apply(Project project) {
        TaskContainer taskContainer = project.getTasks();

        TaskCacheTask cacheTask = taskContainer.create("cacheTaskList", TaskCacheTask.class);
        taskContainer.getByName("assemble").dependsOn(cacheTask);


        taskContainer.create("cacheCommandlineFlags", CommandlineFlagsTask.class);
//        taskContainer.create("cacheCommandlineFlags", task -> {
//            ByteArrayOutputStream cmdOutput = new ByteArrayOutputStream();
//            project.exec(execSpec -> {
//                execSpec.executable("./gradlew");
//                execSpec.args("--help");
//                execSpec.setStandardOutput(cmdOutput);
//            });
//
//            File outFile = new File(project.getBuildDir(), "flags.txt");
//            OutputStream outputStream = null;
//            try {
//                outputStream = new FileOutputStream(outFile);
//                cmdOutput.writeTo(outputStream);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }  finally {
//                if (outputStream != null) try {
//                    outputStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });



//        System.out.println("TESTESTES2");
//        CommandLineActionFactory f = new org.gradle.launcher.cli.CommandLineActionFactory();
//        CommandLineParser cliParser = new CommandLineParser();
//        cliParser.parse("-h");
//        cliParser.printUsage(System.out);
    }
}
