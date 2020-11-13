package com.adus.predictivecache.framework.components;

import com.adus.predictivecache.client.domains.MockPageServer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class CachableResourceRegistrarTest {

    @Autowired
    private CachableResourceRegistrar cachableResourceRegistrar;

    @Test
    void shouldInitializeRegistry() {
        assertResourceElement(MockPageServer.class.getName(), "page1", "page-1");

        assertResourceElement(MockPageServer.class.getName(), "page4WithAutoResourceId",
                MockPageServer.class.getName() + "_page4WithAutoResourceId");

        assertNull(cachableResourceRegistrar.getResourceElement("uncachablePage5"));
    }

    private void assertResourceElement(String className, String methodName, String resourceId) {
        ResourceElement resourceElement = cachableResourceRegistrar.getResourceElement(resourceId);
        assertEquals(className, resourceElement.getClassName());
        assertEquals(methodName, resourceElement.getMethodName());

        assertEquals(resourceId, cachableResourceRegistrar.getResourceId(resourceElement));
    }
}