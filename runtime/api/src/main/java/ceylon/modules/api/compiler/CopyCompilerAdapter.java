/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package ceylon.modules.api.compiler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.eclipse.ceylon.common.FileUtil;

/**
 * Plain copy compiler adapter.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
class CopyCompilerAdapter extends AbstractCompilerAdapter {
    CopyCompilerAdapter() {
        super("");
    }

    public File compile(File source, String name, File classesRoot) throws IOException {
        File copy = new File(classesRoot, name);
        File parent = copy.getParentFile();
        FileUtil.mkdirs(parent);

        FileInputStream fis = new FileInputStream(source);
        FileOutputStream fos = new FileOutputStream(copy);
        try {
            int b;
            while ((b = fis.read()) >= 0)
                fos.write(b);
        } finally {
            safeClose(fis);
            safeClose(fos);
        }

        return copy;
    }
}
