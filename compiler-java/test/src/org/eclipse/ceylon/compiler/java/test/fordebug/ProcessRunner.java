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
package org.eclipse.ceylon.compiler.java.test.fordebug;

import java.io.IOException;
import java.io.InputStream;


public class ProcessRunner {

    interface Handler {
        public void out(String s);
        public void err(String s);
    }
    
    public static int exec(ProcessBuilder pb) throws Exception {
        return exec(pb, new Handler() {

            @Override
            public void out(String s) {
                System.out.print(s);
            }

            @Override
            public void err(String s) {
                System.err.print(s);
            }});
    }
    
    public static int exec(ProcessBuilder pb, final Handler handler) throws Exception {
        final Process process = pb.start();
        {
            Thread errReader = new Thread() {
                public void run() {
                    try {
                        try (InputStream err = process.getErrorStream()) {
                            byte[] buffer = new byte[1024];
                            
                                int read = err.read(buffer);
                                while (read != -1) {
                                    synchronized(handler) {
                                        handler.err(new String(buffer, 0, read));
                                    }
                                    read = err.read(buffer);
                                }
                            
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            errReader.start();
        }
        {
            Thread outReader = new Thread() {
                public void run() {
                    try {
                        try (InputStream out = process.getInputStream()) {
                            byte[] buffer = new byte[1024];
                            
                                int read = out.read(buffer);
                                while (read != -1) {
                                    synchronized(handler) {
                                        handler.out(new String(buffer, 0, read));
                                    }
                                    read = out.read(buffer);
                                }
                            
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            outReader.start();
        }
        process.waitFor();
        return process.exitValue();
    }
    
}
