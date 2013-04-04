package com.redhat.ceylon.compiler.js;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.redhat.ceylon.compiler.typechecker.model.Module;

/** A container for things we need to keep per-module. */
class JsOutput {
    private File outfile;
    private Writer writer;
    private final Set<String> s = new HashSet<String>();
    final Map<String,String> requires = new HashMap<String,String>();
    final MetamodelVisitor mmg;
    protected JsOutput(Module m) throws IOException {
        mmg = new MetamodelVisitor(m);
    }
    protected Writer getWriter() throws IOException {
        if (writer == null) {
            outfile = File.createTempFile("jsout", ".tmp");
            writer = new FileWriter(outfile);
        }
        return writer;
    }
    protected File close() throws IOException {
        writer.close();
        return outfile;
    }
    void addSource(String src) {
        s.add(src);
    }
    Set<String> getSources() { return s; }
}