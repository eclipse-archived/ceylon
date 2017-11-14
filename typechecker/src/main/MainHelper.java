/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package main;
import java.io.File;
import java.util.Arrays;

import org.eclipse.ceylon.compiler.typechecker.io.ClosableVirtualFile;
import org.eclipse.ceylon.compiler.typechecker.io.VFS;

/**
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class MainHelper {

    public static final ClosableVirtualFile getLatestZippedLanguageSourceFile() {
        VFS vfs = new VFS();
        File langDir = new File("../dist/dist/repo/ceylon/language");
        if (!langDir.exists()) {
            System.err.println("Unable to test language module, not found in repository: " + langDir);
            System.exit(-1);
        }
        String[] versions = langDir.list();
        Arrays.sort(versions);
        String version = versions[versions.length-1]; //last
        return vfs.getFromZipFile( new File(langDir, version + "/ceylon.language-" + version + ".src") );
    }
}
