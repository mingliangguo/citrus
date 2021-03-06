/*
 * Copyright 2006-2016 the original author or authors.
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

package com.consol.citrus.mail.config.annotation;

import java.util.Properties;

import com.consol.citrus.TestActor;
import com.consol.citrus.config.annotation.AnnotationConfigParser;
import com.consol.citrus.endpoint.EndpointAdapter;
import com.consol.citrus.mail.message.MailMessageConverter;
import com.consol.citrus.mail.model.MailMarshaller;
import com.consol.citrus.mail.server.MailServer;
import com.consol.citrus.mail.server.MailServerBuilder;
import com.consol.citrus.spi.ReferenceResolver;
import org.springframework.util.StringUtils;

/**
 * @author Christoph Deppisch
 * @since 2.5
 */
public class MailServerConfigParser implements AnnotationConfigParser<MailServerConfig, MailServer> {

    @Override
    public MailServer parse(MailServerConfig annotation, ReferenceResolver referenceResolver) {
        MailServerBuilder builder = new MailServerBuilder();

        builder.autoStart(annotation.autoStart());
        builder.timeout(annotation.timeout());
        builder.debugLogging(annotation.debugLogging());

        if (StringUtils.hasText(annotation.endpointAdapter())) {
            builder.endpointAdapter(referenceResolver.resolve(annotation.endpointAdapter(), EndpointAdapter.class));
        }

        if (StringUtils.hasText(annotation.actor())) {
            builder.actor(referenceResolver.resolve(annotation.actor(), TestActor.class));
        }

        builder.port(annotation.port());

        builder.autoAccept(annotation.autoAccept());
        builder.splitMultipart(annotation.splitMultipart());

        if (StringUtils.hasText(annotation.messageConverter())) {
            builder.messageConverter(referenceResolver.resolve(annotation.messageConverter(), MailMessageConverter.class));
        }

        if (StringUtils.hasText(annotation.marshaller())) {
            builder.marshaller(referenceResolver.resolve(annotation.marshaller(), MailMarshaller.class));
        }

        if (StringUtils.hasText(annotation.javaMailProperties())) {
            builder.javaMailProperties(referenceResolver.resolve(annotation.javaMailProperties(), Properties.class));
        }

        return builder.initialize().build();
    }
}
