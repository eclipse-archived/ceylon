package com.redhat.ceylon.compiler.java.test.language;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.compiler.java.test.CeylonModuleRunner;
import com.redhat.ceylon.compiler.java.test.CeylonModuleRunner.TestLoader;

public class LanguageLoader implements TestLoader {

    @Override
    public Map<String, List<String>> loadTestMethods(
            CeylonModuleRunner moduleRunner, File srcDir) {
        return Collections.singletonMap("runAndAssert_", Collections.singletonList("runAndAssert"));
    }
}