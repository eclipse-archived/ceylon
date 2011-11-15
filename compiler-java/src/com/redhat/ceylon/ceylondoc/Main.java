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

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if ("-d".equals(arg)) {
                destDir = args[++i];
            } else if ("-sourcepath".equals(arg)) {
                srcDir = args[++i];
            } else if ("-private".equals(arg)) {
                showPrivate = true;
            }
        }
        if (destDir == null) {
            System.err.println("-d <dest-dir>: option required");
            System.exit(1);
        }
        if (srcDir == null) {
            System.err.println("-sourcepath <src-dir>: option required");
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
        ceylonDocTool.makeDoc();
    }
}
