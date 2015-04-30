package com.redhat.ceylon.compiler.loader;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import util.ModelUtils;

import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.ceylon.CeylonUtils;
import com.redhat.ceylon.cmr.impl.JSUtils;
import com.redhat.ceylon.compiler.js.JsCompiler;
import com.redhat.ceylon.compiler.js.util.Options;
import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.TypeCheckerBuilder;
import com.redhat.ceylon.model.typechecker.model.Method;
import com.redhat.ceylon.model.typechecker.model.SiteVariance;

public class TestSiteVariance {

    private Options options(String srcdir) {
        return new Options().outRepo("modules").addRepo("build/runtime")
                .optimize(true).generateSourceArchive(false)
                .addSrcDir(srcdir);
    }

    private TypeChecker typeChecker(Options opts) {
        final RepositoryManager repoman = CeylonUtils.repoManager()
                .userRepos(opts.getRepos())
                .outRepo(opts.getOutRepo())
                .buildManager();
        final TypeCheckerBuilder builder = new TypeCheckerBuilder()
            .moduleManagerFactory(new JsModuleManagerFactory("UTF-8"));
        for (File sd : opts.getSrcDirs()) {
            builder.addSrcDirectory(sd);
        }
        builder.setRepositoryManager(repoman);
        final TypeChecker tc = builder.getTypeChecker();
        JsModuleManagerFactory.setVerbose(true);
        tc.process();
        return tc;
    }

    @Test @SuppressWarnings("unchecked")
    public void testSiteVariance() throws IOException {
        //Typecheck
        Options opts = options("src/test/resources/variance/phase1");
        TypeChecker tc = typeChecker(opts);
        //Compile
        JsCompiler jsc = new JsCompiler(tc, opts);
        jsc.generate();
        Map<String, Object> model = JSUtils.readJsonModel(new File("modules/phase1/0.1/phase1-0.1-model.js"));
        Assert.assertNotNull("Model not found", model);
        model = (Map<String,Object>)model.get("phase1");
        Assert.assertNotNull("Default package not found", model);
        model = (Map<String,Object>)model.get("m2");
        Assert.assertNotNull("Method m2 not found", model);
        Map<String,Object> type = (Map<String,Object>)model.get(MetamodelGenerator.KEY_TYPE);
        List<Map<String,Object>> targs = (List<Map<String,Object>>)type.get(MetamodelGenerator.KEY_TYPE_PARAMS);
        type = targs.get(0);
        List<List<Map<String,Object>>> paramLists = (List<List<Map<String,Object>>>)model.get(MetamodelGenerator.KEY_PARAMS);
        Map<String,Object> parm = paramLists.get(0).get(0);
        parm = (Map<String,Object>)parm.get(MetamodelGenerator.KEY_TYPE);
        targs = (List<Map<String,Object>>)parm.get(MetamodelGenerator.KEY_TYPE_PARAMS);
        parm = targs.get(0);
        Assert.assertEquals(SiteVariance.OUT.ordinal(), type.get(MetamodelGenerator.KEY_US_VARIANCE));
        Assert.assertEquals(SiteVariance.IN.ordinal(), parm.get(MetamodelGenerator.KEY_US_VARIANCE));

        //Typecheck phase2
        opts = options("src/test/resources/variance/phase2");
        tc = typeChecker(opts);
        Method m2 = (Method)tc.getPhasedUnits().getPhasedUnits().get(0).getPackage().getModule().getPackage("phase1").getDirectMember("m2", null, false);
        Assert.assertNotNull("phase1::m2 missing", m2);
        Assert.assertFalse("Missing variance overrides in return type",
                m2.getType().getVarianceOverrides().isEmpty());
        Assert.assertFalse("Missing variance overrides in parameter",
                m2.getParameterLists().get(0).getParameters().get(0).getType()
                    .getVarianceOverrides().isEmpty());
        jsc = new JsCompiler(tc, opts);
        jsc.generate();
        Assert.assertTrue("Should compile without errors", jsc.getErrors().isEmpty());
    }

    @After
    public void cleanup() {
        ModelUtils.deleteRecursively(new File("modules"));
    }

}
