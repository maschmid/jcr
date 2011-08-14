/*
  JBoss, Home of Professional Open Source
  Copyright [2010], Red Hat, Inc., and individual contributors
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

import javax.inject.Inject;
import javax.jcr.Repository;
import javax.jcr.Session;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.jcr.annotations.JcrConfiguration;
import org.jboss.seam.jcr.repository.RepositoryResolverImpl;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.jboss.seam.jcr.ConfigParams.JACKRABBIT_REPOSITORY_HOME;
import static org.junit.Assert.assertNotNull;

/**
 * Test case for {@link RepositorySessionProducer}
 *
 * @author George Gastaldi
 */
@RunWith(Arquillian.class)
public class RepositorySessionProducerTest {

    @Inject
    @JcrConfiguration(name = JACKRABBIT_REPOSITORY_HOME, value = "target")
    private Repository repository;

    @Inject
    @JcrConfiguration(name = JACKRABBIT_REPOSITORY_HOME, value = "target")
    private Session session;

    @Deployment
    public static JavaArchive createArchive() {
        return ShrinkWrap.create(JavaArchive.class).addPackage(RepositoryResolverImpl.class.getPackage()).addAsManifestResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"));
    }

    @Test
    public void testProduceJcrRepository() {
        assertNotNull("JCR Repository should have been injected", repository);
    }

    @Test
    public void testProduceSession() {
        assertNotNull("JCR Session should have been injected", session);
    }

    @After
    public void tearDown() {
        session.logout();
    }
}
