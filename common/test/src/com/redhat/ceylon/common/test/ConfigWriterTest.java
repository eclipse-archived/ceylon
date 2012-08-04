package com.redhat.ceylon.common.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.redhat.ceylon.common.CeylonConfig;
import com.redhat.ceylon.common.ConfigParser;
import com.redhat.ceylon.common.ConfigWriter;

public class ConfigWriterTest {

    File testFile;
    CeylonConfig testConfig;
    
    @Before
    public void setup() throws IOException {
        testFile = new File("test/src/com/redhat/ceylon/common/test/test.config");
        testConfig = ConfigParser.loadConfigFromFile(testFile);
    }
    
    @Test
    public void testSimpleDuplicate() throws IOException {
        String contents = readFile(testFile);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ConfigWriter.write(testConfig, testFile, out);
        Assert.assertEquals(contents, out.toString("UTF-8"));
    }

    private String readFile(File file) {
        try{
            Reader reader = new FileReader(file);
            StringBuilder strbuf = new StringBuilder();
            try{
                char[] buf = new char[1024];
                int read;
                while((read = reader.read(buf)) > -1)
                    strbuf.append(buf, 0, read);
            }finally{
                reader.close();
            }
            return strbuf.toString();
        }catch(IOException x){
            throw new RuntimeException(x);
        }
    }
}
