

package org.eclipse.ceylon.tools.p2;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.eclipse.ceylon.common.ModuleSpec;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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
            dependencies.add(new ModuleSpec(null, plugin.getAttribute("id"), plugin.getAttribute("version")));
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