/*
  JBoss, Home of Professional Open Source
  Copyright [2011], Red Hat, Inc., and individual contributors
  by the @authors tag. See the copyright.txt in the distribution for a
  full listing of individual contributors.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  http://www.apache.org/licenses/LICENSE-2.0
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package org.jboss.seam.jcr.producers;

import static org.jboss.seam.jcr.ConfigParams.MODESHAPE_URL;

import java.util.Collections;
import java.util.Map;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

import org.jboss.seam.jcr.ConfigParams;
import org.jboss.solder.core.ExtensionManaged;


/**
 * 
 * @author maschmid
 *
 */
public class ModeshapeRepositoryResolverProducer {

    @Produces
    @ExtensionManaged
    @Named(ConfigParams.JCR_REPOSITORY_CONFIG_MAP)
    public Map<String, String> producesDefaultConfig() {
        return Collections.singletonMap(MODESHAPE_URL, "file:target/test-classes/modeshape.xml?repositoryName=CarRepo");
    }
}
