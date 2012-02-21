package com.redhat.ceylon.compiler.js;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.model.Package;

/** Compiler all the specified units into a single javascript file, packaged as a CommonJS module.
 * 
 * @author Enrique Zamudio
 */
public abstract class JsModuleCompiler extends JsCompiler {

    private final Map<Package, Writer> output = new HashMap<Package, Writer>();

    public JsModuleCompiler(TypeChecker tc) {
        super(tc);
    }

    @Override
    protected Writer getWriter(PhasedUnit pu) throws IOException {
        Package pkg = pu.getPackage();
        Writer writer = output.get(pkg);
        if (writer==null) {
            writer = getModuleWriter(pu);
            beginWrapper(writer);
            output.put(pkg, writer);
        }
        return writer;
    }

    /** Returns a Writer for the phased unit, based on its package. This method is called only once
     * per package. */
    protected abstract Writer getModuleWriter(PhasedUnit pu) throws IOException;

    @Override
    protected void finish() throws IOException {
        for (Writer writer: output.values()) {
            endWrapper(writer);
            writer.flush();
            writer.close();
        }
    }
}
