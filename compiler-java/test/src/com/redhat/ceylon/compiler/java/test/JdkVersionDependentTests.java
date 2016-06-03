package com.redhat.ceylon.compiler.java.test;

import java.util.Arrays;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class JdkVersionDependentTests extends CompilerTests {

    @Parameters(name="-target {0}")
    public static Iterable<Object[]> testParameters() {
        return Arrays.asList(
                new Object[]{"7", "7"}, 
                new Object[]{"8", "8"});
    }
    
    private final String target;
    private final String source;
    
    public JdkVersionDependentTests(String target, String source) {
        super();
        this.target = target;
        this.source = source;
        if (target != null) {
            defaultOptions.add("-target");
            defaultOptions.add(target);
        }
        if (source != null) {
            defaultOptions.add("-source");
            defaultOptions.add(source);
        }
    }
    @Override
    protected String getSrcName(String name) {
        String src = name+".src";
        if ("8".equals(target)) {
            src = name+".src8";
        } else {
            src = name+".src";
        }
        return src;
    }
}
