package com.redhat.ceylon.ceylondoc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.Modules;

public class CeylonDocTool {

    private List<PhasedUnit> phasedUnits;
    private Modules modules;
    private String destDir;

    public CeylonDocTool(List<PhasedUnit> phasedUnits, Modules modules) {
        this.phasedUnits = phasedUnits;
        this.modules = modules;
    }
    
    public void setDestDir(String destDir){
        this.destDir = destDir;
    }

    public void makeDoc() throws IOException{
        for (PhasedUnit pu : phasedUnits) {
            for(Declaration decl : pu.getUnit().getDeclarations()){
                doc(decl);
            }
        }
        for(Module module : modules.getListOfModules()){
            for(Package pkg : module.getPackages()){
                doc(pkg);
            }
        }
        doc(modules);
        copyResource("resources/style.css");
    }

    private void doc(Modules modules) throws IOException {
        new SummaryDoc(destDir, modules).generate();
    }

    private void doc(Package pkg) throws IOException {
        new PackageDoc(destDir, pkg).generate();
    }

    private void copyResource(String path) throws IOException {
        InputStream resource = getClass().getResourceAsStream(path);
        OutputStream os = new FileOutputStream(new File(destDir, "style.css"));
        byte[] buf = new byte[1024];
        int read;
        while((read = resource.read(buf)) > -1){
            os.write(buf, 0, read);
        }
        os.flush();
        os.close();
    }

    private void doc(Declaration decl) throws IOException {
        if(decl instanceof ClassOrInterface){
            new ClassDoc(destDir, (ClassOrInterface)decl).generate();
        }
    }

}
