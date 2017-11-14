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
package org.eclipse.ceylon.compiler.java.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class MetaInfServices  {
    /** 
     * Parse the contents of a {@code META-INF/services/...} 
     * file being read from the given stream, returning the
     * class names in a set.
     */
    public static Set<String> parseMetaInfServices(InputStream is)
            throws UnsupportedEncodingException, IOException {
        Set<String> impls = new HashSet<String>(1);
        BufferedReader inputStream = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        try {
            String line = inputStream.readLine();
            while (line != null) {
                int index = line.indexOf('#');
                if (index != -1) {
                    line = line.substring(0, index);
                }
                if (!line.isEmpty()) {
                    impls.add(line);
                }
                line = inputStream.readLine();
            }
        } finally {
            inputStream.close();
        }
        return impls;
    }
    
    public static Map<String, Set<String>> parseAllServices(File carFile) throws IOException, UnsupportedEncodingException {
        Map<String, Set<String>> result;
        result = new HashMap<String, Set<String>>();
        JarFile jarFile = new JarFile(carFile);
        try {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().startsWith("META-INF/services/")
                        && !entry.isDirectory()) {
                    String serviceBinaryName = entry.getName().substring("META-INF/services/".length());
                    result.put(serviceBinaryName, MetaInfServices.parseMetaInfServices(jarFile.getInputStream(entry)));
                }
            }
        } finally {
            jarFile.close();
        }
        return result;
    }
    

    public static void writeAllServices(File outputFolder, Map<String, Set<String>> previousServices) {
        for (Map.Entry<String, Set<String>> serviceEntry : previousServices.entrySet()) {
            String serviceInterface = serviceEntry.getKey();
            Set<String> serviceClasses = serviceEntry.getValue();
            try (Writer writer = new OutputStreamWriter(new FileOutputStream(
                    new File(outputFolder, "META-INF/services/"+serviceInterface)),
                    "UTF-8")){
                for (String impl : serviceClasses) {
                    writer.append(impl).append('\n');
                }
                writer.flush();
            }
            catch(IOException e) {
                // TODO : log to the right place
            }
        }
    }
    
}
