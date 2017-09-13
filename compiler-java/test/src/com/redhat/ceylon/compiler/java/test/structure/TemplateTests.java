package com.redhat.ceylon.compiler.java.test.structure;

import org.junit.Test;

import com.redhat.ceylon.compiler.java.test.CompilerTests;

public class TemplateTests extends CompilerTests {
    @Override
    protected String transformDestDir(String name) {
        return name + "-templ";
    }
    
    @Test
    public void testKlsSerializableClass() {
        compile("template/play.template.ceylon");
    }
}
