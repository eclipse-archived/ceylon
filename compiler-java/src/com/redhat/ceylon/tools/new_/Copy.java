/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package com.redhat.ceylon.tools.new_;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;

public class Copy {

    private static final class CopyingVisitor extends SimpleFileVisitor<Path> {
        private final Environment env;
        private final Path basedir;
        private final Path root;
        private final PathMatcher pathMatcher;
        private final boolean substitute;

        private CopyingVisitor(Environment env, Path root, PathMatcher pathMatcher, Path basedir, boolean substitute) {
            this.env = env;
            this.root = root;
            this.pathMatcher = pathMatcher;
            this.basedir = basedir;
            this.substitute = substitute;
        }

        @Override
        public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
            Path rel = root.relativize(path);
            if (pathMatcher.matches(rel)) {
                Path dstPath = basedir.resolve(rel);
                try {
                    copy(path, dstPath);
                } catch (Exception e) {
                    throw new RuntimeException("Error while copying " + path + " to " + dstPath, e);
                }
            }
            return FileVisitResult.CONTINUE;
        }

        private void copy(Path path, Path dstPath) throws IOException {
            File dstParent = dstPath.toFile().getParentFile();
            if (!dstParent.exists()) {
                if (!dstParent.mkdirs()) {
                    throw new IOException("Unable to mkdir " + dstParent);
                }
            }
            if (substitute) {
                Files.copy(substitute(path, env), 
                        dstPath, 
                        StandardCopyOption.REPLACE_EXISTING);
            } else {
                Files.copy(path, dstPath, 
                        StandardCopyOption.REPLACE_EXISTING);
            }
        }
        
        private InputStream substitute(Path path, Environment env) throws IOException {
            File file = path.toFile();
            long length = file.length();
            if (length > Integer.MAX_VALUE) {
                throw new RuntimeException("File way too big for in-memory substitution");
            }
            Template.SimpleSubstitution subs = new Template.SimpleSubstitution(env);
            try (FileChannel channel = new FileInputStream(file).getChannel()) {
                // could use channel.map() if the file was large...
                ByteBuffer byteBuffer = ByteBuffer.allocate((int)length);
                channel.read(byteBuffer);
                byteBuffer.flip();
                Template template = new Template(Charset.forName("UTF-8").decode(byteBuffer));
                String substituted = template.eval(subs);
                return new ByteArrayInputStream(substituted.getBytes("UTF-8"));
            }
        }
    }


    private final PathMatcher pathMatcher;
    private final boolean substitute;
    private final File from;
    private final File basedir;

    public Copy(File from, File basedir, PathMatcher pathMatcher, String dst, boolean substitute) {
        this.from = from;
        this.basedir = new File(basedir, dst);
        this.pathMatcher = pathMatcher;
        this.substitute = substitute;
    }
    
    public void run(final Environment env) throws IOException {
        Files.walkFileTree(from.toPath(), new CopyingVisitor(env, 
                from.toPath(), pathMatcher, basedir.toPath(), substitute));
    }
    
    public String toString() {
        return "Copy from=" + from + ", basedir=" + basedir + ", matching=" + pathMatcher + ", substituting=" + substitute;
    }

}
