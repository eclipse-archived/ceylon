import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.ceylon.CeylonUtils;
import com.redhat.ceylon.cmr.impl.JULLogger;
import com.redhat.ceylon.compiler.Options;
import com.redhat.ceylon.compiler.js.JsCompiler;
import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.TypeCheckerBuilder;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.model.Module;

/**
 * Some hack before a proper unit test harness is put in place
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class MainForJsTest {
    
    static boolean opt = false;

    public static void main(String[] args) throws Exception {
        for (String arg: args) { 
            if (arg.equals("optimize")) {
                System.out.println("performance optimized code");
                opt=true; 
            }
        }

        Options opts = Options.parse(new ArrayList<String>(Arrays.asList(opt ? "-optimize" : "", "-out", "build/test/node_modules", "-module")));
        final RepositoryManager repoman = CeylonUtils.makeRepositoryManager(Collections.<String>emptyList(), opts.getOutDir(), new JULLogger());
        TypeCheckerBuilder tcb = new TypeCheckerBuilder().verbose(false)
            .addSrcDirectory(new File("src/test/ceylon"))
            .addSrcDirectory(new File("../ceylon.language/test"));
        tcb.setRepositoryManager(repoman);
        TypeChecker typeChecker = tcb.getTypeChecker();
        typeChecker.process();
        if (typeChecker.getErrors() > 0) {
            System.exit(1);
        }
        JsCompiler jsc = new JsCompiler(typeChecker, opts).stopOnErrors(false);
        if (jsc.generate()) {
            validateOutput(typeChecker);
        } else {
            jsc.printErrors(System.out);
            System.out.println("Skipping output validation.");
        }
    }

    static void validateOutput(TypeChecker typeChecker)
            throws FileNotFoundException, IOException {
        int count=0;
        HashSet<Module> tested = new HashSet<Module>();
        for (PhasedUnit pu: typeChecker.getPhasedUnits().getPhasedUnits()) {
            Module mod = pu.getPackage().getModule();
            if (!tested.contains(mod)) {
                File generated = new File("build/test/node_modules/" + toOutputPath(mod));
                File test = new File("src/test/ceylon/"+ toTestPath(mod));
                if (test.exists()) {
                    BufferedReader reader = new BufferedReader(new FileReader(test));
                    BufferedReader outputReader = new BufferedReader(new FileReader(generated));
                    int i=0;
                    while (reader.ready() && outputReader.ready()) {
                        i++;
                        String actual = outputReader.readLine();
                        String expected = reader.readLine();
                        if (!expected.equals(actual) && !expected.trim().startsWith("//")) {
                            System.err.println("error at " + test.getPath() + ":" + i); 
                            System.err.println("expected: " + expected);
                            System.err.println("  actual: " + actual);
                            break;
                        }
                    }
                    count++;
                }
                tested.add(mod);
            }
        }
        System.out.printf("ran %d tests%n", count);
    }

    private static String toOutputPath(Module mod) {
        String modname = mod.isDefault() ? "default" : mod.getNameAsString();
        return modname.replace('.', '/') +
                (mod.isDefault() ? "/" : "/" + mod.getVersion() ) + "/" +
                modname + (mod.isDefault() ? "" : "-" + mod.getVersion() ) +
                ".js";
    }

    private static String toTestPath(Module mod) {
        String modname = mod.isDefault() ? "default" : mod.getNameAsString();
        return modname.replace('.', '/') + "/" + modname + (opt? ".jsopt" : "") + ".js";
    }
}
