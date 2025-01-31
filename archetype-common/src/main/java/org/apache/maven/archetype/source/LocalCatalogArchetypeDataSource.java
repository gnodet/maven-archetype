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
package org.apache.maven.archetype.source;

import javax.inject.Named;
import javax.inject.Singleton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import org.apache.maven.archetype.catalog.Archetype;
import org.apache.maven.archetype.catalog.ArchetypeCatalog;
import org.apache.maven.project.ProjectBuildingRequest;
import org.codehaus.plexus.util.ReaderFactory;

@Named("catalog")
@Singleton
public class LocalCatalogArchetypeDataSource extends CatalogArchetypeDataSource {

    @Override
    public File updateCatalog(ProjectBuildingRequest buildingRequest, Archetype archetype)
            throws ArchetypeDataSourceException {
        File localRepo =
                buildingRequest.getRepositorySession().getLocalRepository().getBasedir();

        File catalogFile = new File(localRepo, ARCHETYPE_CATALOG_FILENAME);

        getLogger().debug("Catalog to be used for update: " + catalogFile.getAbsolutePath());

        ArchetypeCatalog catalog;
        if (catalogFile.exists()) {
            try {
                getLogger().debug("Reading catalog to be updated: " + catalogFile);
                catalog = readCatalog(ReaderFactory.newXmlReader(catalogFile));
            } catch (FileNotFoundException ex) {
                getLogger().debug("Catalog file don't exist");
                catalog = new ArchetypeCatalog();
            } catch (IOException e) {
                throw new ArchetypeDataSourceException("Error reading archetype catalog.", e);
            }
        } else {
            getLogger().debug("Catalog file don't exist");
            catalog = new ArchetypeCatalog();
        }

        Iterator<Archetype> archetypes = catalog.getArchetypes().iterator();
        boolean found = false;
        Archetype newArchetype = archetype;
        while (!found && archetypes.hasNext()) {
            Archetype a = archetypes.next();
            if (a.getGroupId().equals(archetype.getGroupId())
                    && a.getArtifactId().equals(archetype.getArtifactId())) {
                newArchetype = a;
                found = true;
            }
        }
        if (!found) {
            catalog.addArchetype(newArchetype);
        }

        newArchetype.setVersion(archetype.getVersion());
        newArchetype.setRepository(archetype.getRepository());
        newArchetype.setDescription(archetype.getDescription());
        newArchetype.setProperties(archetype.getProperties());
        newArchetype.setGoals(archetype.getGoals());

        writeLocalCatalog(catalog, catalogFile);
        return catalogFile;
    }

    @Override
    public ArchetypeCatalog getArchetypeCatalog(ProjectBuildingRequest buildingRequest)
            throws ArchetypeDataSourceException {
        File localRepo =
                buildingRequest.getRepositorySession().getLocalRepository().getBasedir();

        File catalogFile = new File(localRepo, ARCHETYPE_CATALOG_FILENAME);

        if (catalogFile.exists() && catalogFile.isDirectory()) {
            catalogFile = new File(catalogFile, ARCHETYPE_CATALOG_FILENAME);
        }
        getLogger().debug("Getting archetypes from catalog: " + catalogFile);

        if (catalogFile.exists()) {
            try {
                return readCatalog(ReaderFactory.newXmlReader(catalogFile));
            } catch (FileNotFoundException e) {
                throw new ArchetypeDataSourceException("The specific archetype catalog does not exist.", e);
            } catch (IOException e) {
                throw new ArchetypeDataSourceException("Error reading archetype catalog.", e);
            }
        } else {
            return new ArchetypeCatalog();
        }
    }
}
