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
import org.gradle.api.tasks.diagnostics.internal.ReportRenderer;
import org.gradle.initialization.BuildClientMetaData;
import org.gradle.internal.logging.text.StyledTextOutput;

import java.io.File;
import java.io.IOException;

/**
 * This class is just an empty implementation. Other ReportRenerers print some sort of header to stdout.
 */
class EmptyReportRenderer implements ReportRenderer {
    @Override
    public void setClientMetaData(BuildClientMetaData buildClientMetaData) {
    }

    @Override
    public void setOutput(StyledTextOutput styledTextOutput) {
    }

    @Override
    public void setOutputFile(File file) throws IOException {
    }

    @Override
    public void startProject(Project project) {
    }

    @Override
    public void completeProject(Project project) {
    }

    @Override
    public void complete() {
    }
}
