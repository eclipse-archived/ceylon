package com.redhat.ceylon.common.test;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;

import com.redhat.ceylon.common.Constants;
import com.redhat.ceylon.common.config.CeylonConfig;
import com.redhat.ceylon.common.config.ConfigParser;

public abstract class AbstractConfigTest {
    protected final File testDir = new File("build/test-stores");
    protected String userDir;
    
    @Before
    public void setup() throws IOException {
        delete(testDir);
        testDir.mkdirs();
        userDir = System.getProperty(Constants.PROP_CEYLON_USER_DIR);
        System.setProperty("ceylon.user.dir", testDir.getAbsolutePath());
    }
    
    protected CeylonConfig loadConfig(String filename) throws IOException {
        return ConfigParser.loadConfigFromFile(new File(filename));
    }
    
    @After
    public void teardown() {
        if (userDir == null) {
            System.getProperties().remove(Constants.PROP_CEYLON_USER_DIR);
        } else {
            System.setProperty(Constants.PROP_CEYLON_USER_DIR, userDir);
        }
    }
    
    private void delete(File f) {
        if (f.isDirectory()) {
            for (File c : f.listFiles()) {
                delete(c);
            }
        }
        f.delete();
    }
}
