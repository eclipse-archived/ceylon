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

import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.redhat.ceylon.common.tools.ModuleSpec;

class Feature {
    final String name, version;
    final long manifestSize, jarSize;
    private String label;
    private String provider;
    private String description;
    private List<ModuleSpec> dependencies = new LinkedList<>();
    private String copyright;
    private String license;
    private Category category;

    public Feature(String name, String version, long manifestSize, long jarSize, Element root) {
        this.name = name;
        this.version = version;
        this.manifestSize = manifestSize;
        this.jarSize = jarSize;
        parseXml(root);
    }

    private void parseXml(Element root) {
        this.label = root.getAttribute("label");
        this.provider = root.getAttribute("provider-name");
        this.description = CeylonP2Tool.getContent(root, "description");
        this.license = CeylonP2Tool.getContent(root, "license");
        this.copyright = CeylonP2Tool.getContent(root, "copyright");
        NodeList nodes = root.getElementsByTagName("plugin");
        for(int i=0;i<nodes.getLength();i++){
            Element plugin = (Element) nodes.item(i);
            dependencies.add(new ModuleSpec(plugin.getAttribute("id"), plugin.getAttribute("version")));
        }
    }

    public SortedMap<String, String> getProperties() {
        SortedMap<String,String> ret = new TreeMap<>();
        ret.put("org.eclipse.equinox.p2.name", label);
        ret.put("org.eclipse.equinox.p2.description", description);
        ret.put("org.eclipse.equinox.p2.provider", provider);
        return ret;
    }

    public List<ModuleSpec> getImportedModules() {
        return dependencies;
    }

    public String getLicense() {
        return license;
    }

    public String getCopyright() {
        return copyright;
    }
}