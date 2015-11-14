/*
 * Copyright 2011 Red Hat inc. and third party contributors as noted 
 * by the author tags.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ceylon.modules.api.compiler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

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
        parent.mkdirs();

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
