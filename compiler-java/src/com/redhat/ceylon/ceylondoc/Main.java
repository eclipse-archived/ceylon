package com.redhat.ceylon.ceylondoc;

import java.io.File;
import java.io.IOException;

import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.TypeCheckerBuilder;

public class Main {
    public static void main(String[] args) throws IOException {
        String destDir = null;
        String srcDir = null;
        boolean showPrivate = false;
        boolean omitSource = false;
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if ("-d".equals(arg)) {
                System.err.println("-d: option not yet supported (though perhaps you meant -dest-dir?)");
                System.exit(1);
            } else if ("-dest-dir".equals(arg)) {
                destDir = args[++i];
            } else if ("-src".equals(arg)) {
                srcDir = args[++i];
            } else if ("-rep".equals(arg)) {
                System.err.println("-rep: option not yet supported");
                System.exit(1);
            } else if ("-private".equals(arg)) {
                showPrivate = true;
            } else if ("-omit-source".equals(arg)) {
                omitSource = true;
            } else {
                System.err.println("Processing modules by name is not supported yet");                
            }
            
        }
        if (destDir == null) {
            System.err.println("-dest-dir <dest-dir>: option required");
            System.exit(1);
        }
        if (srcDir == null) {
            System.err.println("-src <src-dir>: option required");
            System.exit(1);
        }

        File file = new File(srcDir);
        if (file.exists() == false) {
            System.err.println(srcDir + " is not a file or directory");
            System.exit(1);
        }

        TypeChecker typeChecker = new TypeCheckerBuilder().addSrcDirectory(file).getTypeChecker();
        typeChecker.process();
        CeylonDocTool ceylonDocTool = new CeylonDocTool(typeChecker.getPhasedUnits().getPhasedUnits(), typeChecker.getContext().getModules(), showPrivate);
        ceylonDocTool.setDestDir(destDir);
        ceylonDocTool.setSrcDir(srcDir);
        ceylonDocTool.setOmitSource(omitSource);
        ceylonDocTool.makeDoc();
    }
}
