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

package com.redhat.ceylon.tools.p2;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import com.redhat.ceylon.common.tools.ModuleSpec;

class ModuleInfo {
        private static final String[] PropertyNames = new String[]{
            "Bundle-Name",
            "Bundle-Vendor",
            "Bundle-Description",
            "Bundle-DocUrl",
        };
        final String name, version;
        final File jar;
        Attributes osgiAttributes;
        String osgiVersion;

        public ModuleInfo(String name, String version, File jar) throws IOException {
            this.name = name;
            this.version = version;
            this.jar = jar;
            loadOsgiVersion();
        }

        private void loadOsgiVersion() throws IOException {
            JarFile jarFile = new JarFile(jar);
            Manifest manifest = jarFile.getManifest();
            osgiVersion = version;
            if(manifest != null){
                osgiAttributes = manifest.getMainAttributes();
                if(osgiAttributes != null){
                    String tmp = osgiAttributes.getValue("Bundle-Version");
                    if(tmp != null)
                        osgiVersion = tmp;
                }
            }
            jarFile.close();
            osgiVersion = CeylonP2Tool.fixOsgiVersion(osgiVersion);
        }

        public List<ModuleSpec> getExportedPackages() {
            String value = osgiAttributes.getValue("Export-Package");
            List<ModuleSpec> ret = new LinkedList<>();
            if(value != null){
                for(String pkg : value.split(",")){
                    String[] details = pkg.split(";");
                    String name = details[0];
                    String version = "";// GRRR: API of ModuleInfo
                    for(int i=1;i<details.length;i++){
                        if(details[i].startsWith("version=")){
                            version = unquote(details[i].substring(8));
                            break;
                        }
                    }
                    ret.add(new ModuleSpec(name, version));
                }
            }
            return ret;
        }

        private String unquote(String quotedVersion) {
            if(quotedVersion == null || quotedVersion.isEmpty())
                return quotedVersion;
            char first = quotedVersion.charAt(0);
            char last = quotedVersion.charAt(quotedVersion.length()-1);
            if(first == '"' && last == '"')
                return quotedVersion.substring(1, quotedVersion.length()-1);
            if(first == '\'' && last == '\'')
                return quotedVersion.substring(1, quotedVersion.length()-1);
            return quotedVersion;
        }

        public List<ModuleSpec> getImportedPackages() {
            String value = osgiAttributes.getValue("Import-Package");
            List<ModuleSpec> ret = new LinkedList<>();
            if(value != null){
                for(String pkg : value.split(",")){
                    String[] details = pkg.split(";");
                    String name = details[0];
                    String version = "";// GRRR: API of ModuleInfo
                    for(int i=1;i<details.length;i++){
                        if(details[i].startsWith("version=")){
                            version = unquote(details[i].substring(8));
                            break;
                        }
                    }
                    ret.add(new ModuleSpec(name, version));
                }
            }
            return ret;
        }

        public List<ModuleSpec> getImportedModules() {
            String value = osgiAttributes.getValue("Require-Bundle");
            List<ModuleSpec> ret = new LinkedList<>();
            if(value != null){
                for(String pkg : value.split(",")){
                    String[] details = pkg.split(";");
                    String name = details[0];
                    // skip some modules
                    if(CeylonP2Tool.skipModule(name))
                        continue;
                    String version = "";// GRRR: API of ModuleInfo
                    for(int i=1;i<details.length;i++){
                        if(details[i].startsWith("bundle-version=")){
                            version = unquote(details[i].substring(15));
                            break;
                        }
                    }
                    ret.add(new ModuleSpec(name, version));
                }
            }
            return ret;
        }

        public String getAttribute(String name) {
            return osgiAttributes.getValue(name);
        }

        public SortedMap<String,String> getOsgiProperties() {
            SortedMap<String,String> ret = new TreeMap<>();
            for(String name : PropertyNames){
                String attribute = getAttribute(name);
                if(attribute != null){
                    ret.put(name, attribute);
                }
            }
            return ret;
        }
}