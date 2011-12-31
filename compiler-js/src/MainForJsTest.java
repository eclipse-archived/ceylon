import java.io.BufferedReader;
import java.io.File;
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
    /**
     * Files that are not under a proper module structure are placed under a <nomodule> module.
     */
    public static void main(String[] args) throws Exception {
        
        final Map<Package,PrintWriter> output = new HashMap<Package, PrintWriter>();

        TypeChecker typeChecker = new TypeCheckerBuilder()
                .verbose(false)
                .addSrcDirectory(new File("test"))
                .getTypeChecker();
        typeChecker.process();
        new JsCompiler(typeChecker) { 
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
        }.generate();
        
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
            }
            count++;
        }
        System.out.println("ran " + count + " tests");
    }
    
    private static String toOutputPath(Package pkg) {
        return pkg.getModule().getNameAsString().replace('.', '/') + "/" +
                pkg.getNameAsString() + ".js";
    }

    private static String toTestPath(Package pkg) {
        return pkg.getNameAsString().replace('.', '/') + "/" +
                pkg.getNameAsString() + ".js";
    }
}
