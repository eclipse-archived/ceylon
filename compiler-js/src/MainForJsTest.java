import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import com.redhat.ceylon.compiler.js.JsCompiler;
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
    
    private static final class JsModuleCompiler extends JsCompiler {
        private final Map<Package, PrintWriter> output;

        private JsModuleCompiler(TypeChecker tc, Map<Package, PrintWriter> output) {
            super(tc);
            this.output = output;
        }

        @Override
        protected Writer getWriter(PhasedUnit pu) {
            Package pkg = pu.getPackage();
            PrintWriter writer = output.get(pkg);
            if (writer==null) {
                try {
                    File file = new File("build/test/node_modules/"+
                            toOutputPath(pkg));
                    file.getParentFile().mkdirs();
                    if (file.exists()) file.delete();
                    file.createNewFile();
                    FileWriter fileWriter = new FileWriter(file);
                    writer = new PrintWriter(fileWriter);
                    output.put(pkg, writer);
                }
                catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
            return writer;
        }

        @Override
        protected void finish() {
            for (PrintWriter writer: output.values()) {
                writer.flush();
                writer.close();
            }
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
        
        final Map<Package,PrintWriter> output = new HashMap<Package, PrintWriter>();

        TypeChecker typeChecker = new TypeCheckerBuilder()
                .verbose(false)
                .addSrcDirectory(new File("test"))
                .addSrcDirectory(new File("../ceylon.language/test"))
                .getTypeChecker();
        typeChecker.process();
        if (typeChecker.getErrors() > 0) {
            System.exit(1);
        }
        new JsModuleCompiler(typeChecker, output)
                .optimize(opt).stopOnErrors(false)
                .generate();
        validateOutput(typeChecker);
    }

    static void validateOutput(TypeChecker typeChecker)
            throws FileNotFoundException, IOException {
        int count=0;
        for (PhasedUnit pu: typeChecker.getPhasedUnits().getPhasedUnits()) {
            Package pkg = pu.getPackage();
            File generated = new File("build/test/node_modules/" + toOutputPath(pkg));
            File test = new File("test/"+ toTestPath(pkg));
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
        System.out.println("ran " + count + " tests");
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
