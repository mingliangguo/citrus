/*
 * Copyright 2006-2010 the original author or authors.
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

package com.consol.citrus.validation.json;

import java.util.HashMap;
import java.util.Map;

import com.consol.citrus.actions.SendMessageAction;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.context.TestContextFactory;
import com.consol.citrus.endpoint.Endpoint;
import com.consol.citrus.endpoint.EndpointConfiguration;
import com.consol.citrus.functions.DefaultFunctionLibrary;
import com.consol.citrus.message.DefaultMessage;
import com.consol.citrus.message.Message;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.messaging.Producer;
import com.consol.citrus.testng.AbstractTestNGUnitTest;
import com.consol.citrus.validation.DefaultMessageHeaderValidator;
import com.consol.citrus.validation.builder.PayloadTemplateMessageBuilder;
import com.consol.citrus.validation.context.HeaderValidationContext;
import com.consol.citrus.validation.matcher.DefaultValidationMatcherLibrary;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

/**
 * @author Christoph Deppisch
 */
public class SendMessageActionTest extends AbstractTestNGUnitTest {

    private Endpoint endpoint = Mockito.mock(Endpoint.class);
    private Producer producer = Mockito.mock(Producer.class);
    private EndpointConfiguration endpointConfiguration = Mockito.mock(EndpointConfiguration.class);

    @Override
    protected TestContextFactory createTestContextFactory() {
        TestContextFactory factory = super.createTestContextFactory();
        factory.getFunctionRegistry().addFunctionLibrary(new DefaultFunctionLibrary());
        factory.getValidationMatcherRegistry().addValidationMatcherLibrary(new DefaultValidationMatcherLibrary());
        return factory;
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void testSendMessageOverwriteMessageElementsJsonPath() {
        PayloadTemplateMessageBuilder messageBuilder = new PayloadTemplateMessageBuilder();
        messageBuilder.setPayloadData("{ \"TestRequest\": { \"Message\": \"?\" }}");

        Map<String, Object> overwriteElements = new HashMap<>();
        overwriteElements.put("$.TestRequest.Message", "Hello World!");

        JsonPathMessageProcessor processor = new JsonPathMessageProcessor.Builder()
                .expressions(overwriteElements)
                .build();

        final Message controlMessage = new DefaultMessage("{\"TestRequest\":{\"Message\":\"Hello World!\"}}");

        reset(endpoint, producer, endpointConfiguration);
        when(endpoint.createProducer()).thenReturn(producer);
        when(endpoint.getEndpointConfiguration()).thenReturn(endpointConfiguration);

        doAnswer(invocation -> {
            validateMessageToSend(invocation.getArgument(0), controlMessage);
            return null;
        }).when(producer).send(any(Message.class), any(TestContext.class));

        when(endpoint.getActor()).thenReturn(null);

        SendMessageAction sendAction = new SendMessageAction.Builder()
                .endpoint(endpoint)
                .messageType(MessageType.JSON)
                .message(messageBuilder)
                .process(processor)
                .build();
        sendAction.execute(context);

    }

    private void validateMessageToSend(Message toSend, Message controlMessage) {
        Assert.assertEquals(toSend.getPayload(String.class).trim(), controlMessage.getPayload(String.class).trim());
        DefaultMessageHeaderValidator validator = new DefaultMessageHeaderValidator();
        validator.validateMessage(toSend, controlMessage, context, new HeaderValidationContext());
    }
}
