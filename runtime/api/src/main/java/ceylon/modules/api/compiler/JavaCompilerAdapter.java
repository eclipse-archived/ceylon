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

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.Collections;

/**
 * Java source compiler adapter.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class JavaCompilerAdapter extends AbstractCompilerAdapter {
    public static CompilerAdapter INSTANCE = new JavaCompilerAdapter();

    private JavaCompilerAdapter() {
        super(".java");
    }

    public File findSource(File root, String name) {
        return super.findSource(root, toPath(name));
    }

    public File compile(File source, String name, final File classesRoot) throws IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Collections.singleton(classesRoot));
        JavaFileObject sourceJFO = new FileJavaFileObject(source);
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null, null, null, Collections.singleton(sourceJFO));
        if (task.call() == false) // start compilation
            throw new IllegalArgumentException("Cannot compile: " + source);

        return new File(classesRoot, toPath(name, ".class"));
    }

    private static class FileJavaFileObject extends SimpleJavaFileObject {
        private File source;

        private FileJavaFileObject(File source) {
            super(source.toURI(), Kind.SOURCE);
            if (source == null)
                throw new IllegalArgumentException("Null source");
            this.source = source;
        }

        public InputStream openInputStream() throws IOException {
            return new FileInputStream(source);
        }

        public OutputStream openOutputStream() throws IOException {
            return new FileOutputStream(source);
        }

        public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
            StringWriter writer = new StringWriter();
            InputStream is = openInputStream();
            try {
                int b;
                while ((b = is.read()) >= 0)
                    writer.write(b);

                writer.flush();
            } finally {
                safeClose(is);
                safeClose(writer);
            }
            return writer.getBuffer();
        }

        public long getLastModified() {
            return source.lastModified();
        }

        public boolean delete() {
            return source.delete();
        }
    }
}
