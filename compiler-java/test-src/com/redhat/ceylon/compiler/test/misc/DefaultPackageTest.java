package com.redhat.ceylon.compiler.test.misc;

import org.junit.Test;

import com.redhat.ceylon.compiler.test.CompilerTest;
import com.redhat.ceylon.compiler.tools.CeyloncFileManager;
import com.redhat.ceylon.compiler.tools.CeyloncTool;

public class DefaultPackageTest extends CompilerTest {
    @Test
    public void testDefaultPackage(){
        compareWithJavaSource("defaultPackage");
    }
    
    @Override
    protected CeyloncFileManager makeFileManager(CeyloncTool compiler) {
        CeyloncFileManager fileManager = (CeyloncFileManager)compiler.getStandardFileManager(null, null, null);
        fileManager.setSourcePath(path);
        return fileManager;
    }
}
