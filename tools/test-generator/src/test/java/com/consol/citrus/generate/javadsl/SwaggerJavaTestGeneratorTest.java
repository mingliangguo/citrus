/*
 * Copyright 2006-2018 the original author or authors.
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

package com.consol.citrus.generate.javadsl;

import java.io.File;
import java.io.IOException;

import com.consol.citrus.CitrusSettings;
import com.consol.citrus.generate.TestGenerator;
import com.consol.citrus.generate.UnitFramework;
import com.consol.citrus.util.FileUtils;
import org.springframework.core.io.FileSystemResource;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Christoph Deppisch
 * @since 2.7.4
 */
public class SwaggerJavaTestGeneratorTest {

    @Test
    public void testCreateTestAsClient() throws IOException {
        SwaggerJavaTestGenerator generator = new SwaggerJavaTestGenerator();

        generator.withAuthor("Christoph")
                .withDescription("This is a sample test")
                .usePackage("com.consol.citrus")
                .withFramework(UnitFramework.TESTNG);

        generator.withNamePrefix("UserLoginClient_");
        generator.withSpec("com/consol/citrus/swagger/user-login-api.json");

        generator.create();

        verifyTest("UserLoginClient_createUser_IT");
        verifyTest("UserLoginClient_loginUser_IT");
        verifyTest("UserLoginClient_logoutUser_IT");
        verifyTest("UserLoginClient_getUserByName_IT");
    }

    @Test
    public void testCreateTestAsServer() throws IOException {
        SwaggerJavaTestGenerator generator = new SwaggerJavaTestGenerator();

        generator.withAuthor("Christoph")
                .withDescription("This is a sample test")
                .usePackage("com.consol.citrus")
                .withFramework(UnitFramework.TESTNG);

        generator.withMode(TestGenerator.GeneratorMode.SERVER);
        generator.withSpec("com/consol/citrus/swagger/user-login-api.json");

        generator.create();

        verifyTest("UserLoginService_createUser_IT");
        verifyTest("UserLoginService_loginUser_IT");
        verifyTest("UserLoginService_logoutUser_IT");
        verifyTest("UserLoginService_getUserByName_IT");
    }

    private void verifyTest(String name) throws IOException {
        File javaFile = new File(CitrusSettings.DEFAULT_TEST_SRC_DIRECTORY + "java/com/consol/citrus/" + name + ".java");
        Assert.assertTrue(javaFile.exists());

        String javaContent = FileUtils.readToString(new FileSystemResource(javaFile));
        Assert.assertTrue(javaContent.contains("@author Christoph"));
        Assert.assertTrue(javaContent.contains("public class " + name));
        Assert.assertTrue(javaContent.contains("* This is a sample test"));
        Assert.assertTrue(javaContent.contains("package com.consol.citrus;"));
        Assert.assertTrue(javaContent.contains("extends TestNGCitrusSupport"));
    }

}
