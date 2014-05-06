package com.redhat.ceylon.compiler.js;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.redhat.ceylon.compiler.loader.ModelEncoder;
import com.redhat.ceylon.compiler.typechecker.model.Module;

/** A container for things we need to keep per-module. */
public class JsOutput {
    private File outfile;
    private Writer writer;
    private final Set<String> s = new HashSet<String>();
    final Map<String,String> requires = new HashMap<String,String>();
    final MetamodelVisitor mmg;
    final String encoding;
    protected JsOutput(Module m, String encoding) throws IOException {
        this.encoding = encoding == null ? "UTF-8" : encoding;
        mmg = new MetamodelVisitor(m);
    }
    protected Writer getWriter() throws IOException {
        if (writer == null) {
            outfile = File.createTempFile("jsout", ".tmp");
            writer = new OutputStreamWriter(new FileOutputStream(outfile), encoding);
        }
        return writer;
    }
    protected File close() throws IOException {
        if (writer != null) {
            writer.close();
        }
        return outfile;
    }
    void addSource(String src) {
        s.add(src);
    }
    Set<String> getSources() { return s; }

    public void encodeModel() throws IOException {
        new ModelEncoder(mmg.getModel()).encode(writer);
    }

}