/*
 * Copyright 2006-2010 ConSol* Software GmbH.
 * 
 * This file is part of Citrus.
 * 
 * Citrus is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Citrus is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Citrus. If not, see <http://www.gnu.org/licenses/>.
 */

package com.consol.citrus.ws.config.xml;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * Namespace handler for test action components in Citrus ws namespace.
 * 
 * @author Christoph Deppisch
 */
public class CitrusWSTestcaseNamespaceHandler extends NamespaceHandlerSupport {

    public void init() {
        registerBeanDefinitionParser("assert", new AssertSoapFaultParser());
        registerBeanDefinitionParser("send", new SendSoapMessageActionParser());
        registerBeanDefinitionParser("send-fault", new SendSoapFaultActionParser());
        registerBeanDefinitionParser("receive", new ReceiveSoapMessageActionParser());
    }

}