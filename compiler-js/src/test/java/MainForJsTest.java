import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;

import com.redhat.ceylon.compiler.Options;
import com.redhat.ceylon.compiler.js.JsCompiler;
import com.redhat.ceylon.compiler.js.JsModuleCompiler;
import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.TypeCheckerBuilder;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.model.Package;

/**
 * Some hack before a proper unit test harness is put in place
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class MainForJsTest {
    
    private static final class JsTestCompiler extends JsModuleCompiler {

        private JsTestCompiler(TypeChecker tc, Options options) {
            super(tc, options);
        }

        @Override
        protected Writer getModuleWriter(PhasedUnit pu) throws IOException {
            Package pkg = pu.getPackage();
            File file = new File("build/test/node_modules", toOutputPath(pkg));
            file.getParentFile().mkdirs();
            if (file.exists()) file.delete();
            file.createNewFile();
            return new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
        }

    }

    static boolean opt = false;

    public static void main(String[] args) throws Exception {
        for (String arg: args) { 
            if (arg.equals("optimize")) {
                System.out.println("performance optimized code");
                opt=true; 
            }
        }

        TypeChecker typeChecker = new TypeCheckerBuilder()
                .verbose(false)
                .addSrcDirectory(new File("src/test/ceylon"))
                .addSrcDirectory(new File("../ceylon.language/test"))
                .getTypeChecker();
        typeChecker.process();
        if (typeChecker.getErrors() > 0) {
            System.exit(1);
        }
        Options opts = Options.parse(new ArrayList<String>(Arrays.asList(opt ? "-optimize" : "")));
        JsCompiler jsc = new JsTestCompiler(typeChecker, opts).stopOnErrors(false);
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
        for (PhasedUnit pu: typeChecker.getPhasedUnits().getPhasedUnits()) {
            Package pkg = pu.getPackage();
            File generated = new File("build/test/node_modules/" + toOutputPath(pkg));
            File test = new File("src/test/ceylon/"+ toTestPath(pkg));
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
        }
        System.out.printf("ran %d tests%n", count);
    }
    
    private static String toOutputPath(Package pkg) {
        String pkgName = pkg.getNameAsString();
        if (pkgName.isEmpty()) pkgName = "default";
        String modName = pkg.getModule().getNameAsString();
        return modName.replace('.', '/') +
                (pkg.getModule().isDefault() ? 
                        "/" : "/" + pkg.getModule().getVersion() ) +
                pkgName + ".js";
    }

    private static String toTestPath(Package pkg) {
        String pkgName = pkg.getNameAsString();
        if (pkgName.isEmpty()) pkgName = "default";
        return pkgName.replace('.', '/') + "/" + pkgName + (opt? ".jsopt" : "") + ".js";
    }
}
