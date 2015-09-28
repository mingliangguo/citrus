/*
 * Copyright 2006-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.consol.citrus.docker.command;

import com.consol.citrus.context.TestContext;
import com.consol.citrus.docker.client.DockerClient;
import com.github.dockerjava.api.command.PullImageCmd;
import com.github.dockerjava.api.model.PullResponseItem;
import com.github.dockerjava.core.command.PullImageResultCallback;

/**
 * @author Christoph Deppisch
 * @since 2.4
 */
public class ImagePull extends AbstractDockerCommand<PullResponseItem> {

    /**
     * Default constructor initializing the command name.
     */
    public ImagePull() {
        super("docker:pull");
    }

    @Override
    public void execute(DockerClient dockerClient, TestContext context) {
        final PullImageCmd command = dockerClient.getDockerClient().pullImageCmd(getImageId(context));
        PullImageResultCallback imageResult = new PullImageResultCallback() {
            @Override
            public void onNext(PullResponseItem item) {
                setCommandResult(item);
                super.onNext(item);
            }
        };

        if (hasParameter("registry")) {
            command.withRegistry(getParameter("registry", context));
        }

        if (hasParameter("repository")) {
            command.withRepository(getParameter("repository", context));
        }

        if (hasParameter("tag")) {
            command.withTag(getParameter("tag", context));
        }

        command.exec(imageResult);

        imageResult.awaitSuccess();
    }
}
