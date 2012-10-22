package com.redhat.ceylon.ant;

import java.io.File;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.redhat.ceylon.common.ant.Module;

public class ModuleDescriptorReaderTest {

    @Test
    public void testReadDescriptorA() {
        ModuleDescriptorReader r = new ModuleDescriptorReader(new Module("com.redhat.ceylon.ant.modules.a"), new File("test/src"));
        Assert.assertEquals("1.0", r.getModuleVersion());
        Assert.assertEquals("com.redhat.ceylon.ant.modules.a", r.getModuleName());
        Assert.assertEquals("http://example.com/license", r.getModuleLicense());
        List<String> authors = r.getModuleAuthors();
        Assert.assertEquals(2, authors.size());
        Assert.assertEquals("Tom", authors.get(0));
        Assert.assertEquals("and Tom", authors.get(1));
    }
    
}
