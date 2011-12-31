import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import com.redhat.ceylon.compiler.js.JsCompiler;
import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.TypeCheckerBuilder;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;

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
        
        final Map<PhasedUnit,StringBuffer> output = new HashMap<PhasedUnit, StringBuffer>();

        TypeChecker typeChecker = new TypeCheckerBuilder()
                .verbose(false)
                .addSrcDirectory(new File("test"))
                .getTypeChecker();
        typeChecker.process();
        new JsCompiler(typeChecker) { 
            @Override
            protected Writer getWriter(PhasedUnit pu) {
                StringWriter sw = new StringWriter();
                output.put(pu, sw.getBuffer());
                return sw;
            }
        }.generate();
        
        int count=0;
        for (PhasedUnit pu: typeChecker.getPhasedUnits().getPhasedUnits()) {
            String ceylonPath = pu.getPathRelativeToSrcDir();
            String jsPath = ceylonPath.replace(".ceylon", ".js");
            File file = new File("test/"+jsPath);
            File out = new File("build/test/node_modules/"+jsPath);
            out.getParentFile().mkdirs();
            if (out.exists()) out.delete();
            out.createNewFile();
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                BufferedReader outputReader = new BufferedReader(new StringReader(output.get(pu).toString()));
                PrintWriter writer = new PrintWriter(new FileWriter(out));
                int i=0;
                while (reader.ready() && outputReader.ready()) {
                    i++;
                    String expected = reader.readLine();
                    String actual = outputReader.readLine();
                    if (!expected.equals(actual)) {
                        System.err.println("error at " + jsPath + ":" + i); 
                        System.err.println("expected: " + expected);
                        System.err.println("  actual: " + actual);
                    }
                    writer.println(actual);
                }
                writer.flush();
                writer.close();
            }
            count++;
        }
        System.out.println("ran " + count + " tests");
    }
}
