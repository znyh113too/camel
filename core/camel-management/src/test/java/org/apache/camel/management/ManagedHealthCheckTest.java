/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.management;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.openmbean.TabularData;
import java.util.Collection;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.health.HealthCheckRegistry;
import org.apache.camel.impl.health.DefaultHealthCheckRegistry;
import org.junit.Test;

public class ManagedHealthCheckTest extends ManagementTestSupport {

    @Override
    protected CamelContext createCamelContext() throws Exception {
        CamelContext context = super.createCamelContext();

        // install health check manually
        HealthCheckRegistry registry = new DefaultHealthCheckRegistry();
        registry.setCamelContext(context);
        Object hc = registry.resolveById("context");
        registry.register(hc);
        context.setExtension(HealthCheckRegistry.class, registry);

        return context;
    }

    @Test
    public void testHealthCheck() throws Exception {
        // JMX tests dont work well on AIX CI servers (hangs them)
        if (isPlatform("aix")) {
            return;
        }

        getMockEndpoint("mock:result").expectedMessageCount(1);
        template.sendBody("direct:start", "Hello World");
        assertMockEndpointsSatisfied();

        MBeanServer mbeanServer = getMBeanServer();
        ObjectName on = ObjectName.getInstance("org.apache.camel:context=camel-1,type=health,name=DefaultHealthCheck");
        assertTrue(mbeanServer.isRegistered(on));

        Boolean up = (Boolean) mbeanServer.getAttribute(on, "Healthy");
        assertTrue(up);

        TabularData data = (TabularData) mbeanServer.invoke(on, "details", null, null);
        assertEquals(1, data.size());

        Collection<String> ids = (Collection) mbeanServer.invoke(on, "getHealthChecksIDs", null, null);
        assertEquals(1, ids.size());

        assertEquals("context", ids.iterator().next());
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start").to("mock:result");
            }
        };
    }

}