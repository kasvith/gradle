/*
 * Copyright 2018 the original author or authors.
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

package org.gradle.cache.internal;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import org.apache.commons.io.FileUtils;
import org.gradle.api.Action;
import org.gradle.api.UncheckedIOException;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.gradle.util.GradleVersion;

import java.io.File;
import java.io.IOException;

public class WrapperDistributionCleanupAction implements Action<GradleVersion> {

    @VisibleForTesting static final String WRAPPER_DISTRIBUTION_FILE_PATH = "wrapper/dists";
    private static final Logger LOGGER = Logging.getLogger(WrapperDistributionCleanupAction.class);
    private static final ImmutableList<String> DISTRIBUTION_TYPES = ImmutableList.of("bin", "all");

    private final File distsDir;

    public WrapperDistributionCleanupAction(File gradleUserHomeDirectory) {
        this.distsDir = new File(gradleUserHomeDirectory, WRAPPER_DISTRIBUTION_FILE_PATH);
    }

    @Override
    public void execute(GradleVersion version) {
        try {
            deleteDistributions(version);
        } catch (IOException e) {
            throw new UncheckedIOException("Error deleting distribution for " + version, e);
        }
    }

    private void deleteDistributions(GradleVersion version) throws IOException {
        for (String distributionType : DISTRIBUTION_TYPES) {
            File dir = new File(distsDir, "gradle-" + version.getVersion() + "-" + distributionType);
            if (dir.isDirectory()) {
                LOGGER.debug("Deleting Gradle distribution at {}", dir);
                FileUtils.deleteDirectory(dir);
            }
        }
    }
}
