/*
 * Copyright 2006-2017 the original author or authors.
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

package com.consol.citrus.junit.jupiter.integration;

import com.consol.citrus.GherkinTestActionRunner;
import com.consol.citrus.TestActionRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.junit.jupiter.CitrusSupport;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.consol.citrus.actions.CreateVariablesAction.Builder.createVariable;
import static com.consol.citrus.actions.EchoAction.Builder.echo;

/**
 * @author Christoph Deppisch
 */
@ExtendWith(CitrusSupport.class)
public class EchoActionJUnit5JavaIT {

    @Test
    @CitrusTest
    void echoJavaTest(@CitrusResource TestActionRunner runner) {
        runner.run(createVariable("time", "citrus:currentDate()"));

        runner.run(echo("Hello Citrus!"));

        runner.run(echo("CurrentTime is: ${time}"));
    }

    @Test
    @CitrusTest(name = "EchoSampleTest")
    void echoTest(@CitrusResource GherkinTestActionRunner runner) {
        runner.given(createVariable("time", "citrus:currentDate()"));

        runner.when(echo("Hello Citrus!"));

        runner.then(echo("CurrentTime is: ${time}"));
    }
}
