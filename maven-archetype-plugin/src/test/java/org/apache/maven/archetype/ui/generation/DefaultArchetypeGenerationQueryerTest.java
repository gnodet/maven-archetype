/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.maven.archetype.ui.generation;

import java.util.regex.Pattern;

import org.codehaus.plexus.ContainerConfiguration;
import org.codehaus.plexus.PlexusTestCase;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;
import org.easymock.EasyMock;

public class DefaultArchetypeGenerationQueryerTest extends PlexusTestCase {

    private DefaultArchetypeGenerationQueryer queryer;

    @Override
    protected void customizeContainerConfiguration(ContainerConfiguration configuration) {
        configuration.setClassPathScanning("index");
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        queryer = (DefaultArchetypeGenerationQueryer) lookup(ArchetypeGenerationQueryer.class.getName());
    }

    public void testPropertyRegexValidationRetry() throws PrompterException {
        Prompter prompter = EasyMock.createMock(Prompter.class);

        EasyMock.expect(prompter.prompt(EasyMock.<String>anyObject())).andReturn("invalid-answer");
        EasyMock.expect(prompter.prompt(EasyMock.<String>anyObject())).andReturn("valid-answer");

        EasyMock.replay(prompter);
        queryer.setPrompter(prompter);

        String value = queryer.getPropertyValue("custom-property", null, Pattern.compile("^valid-.*"));

        assertEquals("valid-answer", value);
    }
}
