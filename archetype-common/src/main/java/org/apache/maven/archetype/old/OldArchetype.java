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
package org.apache.maven.archetype.old;

import java.io.File;

import org.apache.maven.archetype.ArchetypeGenerationRequest;
import org.apache.maven.archetype.common.Constants;
import org.apache.maven.archetype.exception.InvalidPackaging;
import org.apache.maven.archetype.exception.UnknownArchetype;
import org.apache.maven.artifact.repository.ArtifactRepository;

/**
 * Create a Maven project from an old archetype.
 *
 * @author <a href="mailto:jason@maven.org">Jason van Zyl</a>
 * @version $Id$
 */
public interface OldArchetype {
    String ARCHETYPE_DESCRIPTOR = Constants.OLD_ARCHETYPE_DESCRIPTOR;

    String ARCHETYPE_OLD_DESCRIPTOR = Constants.OLDER_ARCHETYPE_DESCRIPTOR;

    String ARCHETYPE_RESOURCES = "archetype-resources";

    // TODO: delete this, it probably should be project.getFile instead
    String ARCHETYPE_POM = "pom.xml";

    /**
     * Download an archetype then create a project from it.
     */
    void createArchetype(ArchetypeGenerationRequest request, ArtifactRepository archetypeRepository)
            throws UnknownArchetype, ArchetypeNotFoundException, ArchetypeDescriptorException,
                    ArchetypeTemplateProcessingException, InvalidPackaging;

    /**
     * Create a project from an archetype file.
     */
    void createArchetype(ArchetypeGenerationRequest request, File archetypeFile)
            throws ArchetypeDescriptorException, ArchetypeTemplateProcessingException, InvalidPackaging;
}
