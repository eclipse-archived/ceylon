package com.redhat.ceylon.tools.p2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.ModuleQuery;
import com.redhat.ceylon.cmr.api.ModuleVersionDetails;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.ceylon.OutputRepoUsingTool;
import com.redhat.ceylon.cmr.impl.IOUtils;
import com.redhat.ceylon.cmr.impl.ShaSigner;
import com.redhat.ceylon.common.log.Logger;
import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tools.CeylonTool;
import com.redhat.ceylon.common.tools.ModuleSpec;
import com.redhat.ceylon.model.cmr.ArtifactResult;
import com.redhat.ceylon.model.cmr.JDKUtils;

@Summary("Generates p2 repository metadata suitable for Eclipse")
@Description("This is EXPERIMENTAL" +
        "\n\n" +
        "Given a list of modules, and optionally a category file and prefix, this tool " +
        "generates `content.xml` and `artifacts.xml` files in the output directory, and " +
        "packs the folders in `${output}/features/*` into jars.")
public class CeylonP2Tool extends OutputRepoUsingTool {

    private static final String KNOWN_EXPRESSION_TYPE = "providedCapabilities.exists(p | p.namespace == 'osgi.bundle')";
    private List<ModuleSpec> modules;
    private File categories;
    private String categoryPrefix;
    private String repositoryName = "P2 repository";
    
    public CeylonP2Tool() {
        super(CeylonP2Messages.RESOURCE_BUNDLE);
    }

    @Argument(argumentName="module", multiplicity="+")
    public void setModules(List<String> modules) {
        setModuleSpecs(ModuleSpec.parseEachList(modules));
    }
    
    public void setModuleSpecs(List<ModuleSpec> modules) {
        this.modules = modules;
    }

    @OptionArgument(argumentName="categories")
    @Description("Specify a `categories.xml` file to be used for the list of categories")
    public void setCategories(File categories) {
        this.categories = categories;
    }

    @OptionArgument(argumentName="category-prefix")
    @Description("Specify the prefix for categories, for example if you have a category "+
                 "named 'x' and a prefix of 'com.foo.bar' we will generate a unit named "+
                 "'com.foo.bar.x' for your category")
    public void setCategoryPrefix(String categoryPrefix) {
        this.categoryPrefix = categoryPrefix;
    }

    @OptionArgument(argumentName="repository-name")
    @Description("Specify an output repository name")
    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    @Override
    protected boolean includeJDK() {
        return true;
    }
    
    @Override
    protected boolean needsSystemRepo() {
        return false;
    }

    @Override
    public void initialize(CeylonTool mainTool) {
    }
    
    @Override
    public void run() throws Exception {
        RepositoryManager repoManager = getRepositoryManager();
        Map<String, ModuleInfo> allModules = new HashMap<>();
        for (ModuleSpec module : modules) {
            String version = findModuleVersion(module);
            msg("collecting.modules", module.toString());
            newline();
            collectModules(repoManager, module.getName(), version, allModules);
        }
        // now purge empty modules
        purgeMissingModules(allModules);
        Map<String, Feature> features = collectFeatures();
        Map<String, Category> categoriesByName = null;
        if(categories != null){
            categoriesByName = parseCategories(features);
        }
        msg("generating.artifacts");
        newline();
        printArtifacts(allModules, features);
        msg("generating.content");
        newline();
        printContent(allModules, features, categoriesByName);
    }

    private String findModuleVersion(ModuleSpec module) throws IOException {
        if(module.isVersioned())
            return module.getVersion();
        /*
         *  FIXME: we should look in the compiled repo as well, except we set our output to the output p2 distrib
         *  and the car repo is in a normal "rep" so we can't look in the output repo.
         *  Another thing that doesn't work is that the list of dependencies would include transitive ones like
         *  the distrib, even for the SDK where we only want the SDK and its dependencies that are not in the distrib,
         *  so we use the same trick as the build does for ceylon-copy to put cars in osgi/dist/plugins/ which is
         *  offline and no-default-repos on invocation.
         *  A better strategy would be to not start from a module list and just scan osgi/dist/plugins jars and skip
         *  the CMR entirely, but in the future I suspect we want this tool to also do the copying step and stuff
         *  so let's leave it like this for now.
         */
        // try from source first
        ModuleVersionDetails fromSource = getVersionFromSource(module.getName());
        if(fromSource != null){
            // is it the version we're after?
            return fromSource.getVersion();
        }else{
            Collection<ModuleVersionDetails> versions = getModuleVersions(getRepositoryManager(), module.getName(), module.getVersion(), 
                    ModuleQuery.Type.JVM, null, null);
            if (versions.isEmpty()) {
                String err = getModuleNotFoundErrorMessage(getRepositoryManager(), module.getName(), module.getVersion());
                throw new CeylonP2ToolException(err);
            }
            return versions.iterator().next().getVersion();
        }
    }

    private void purgeMissingModules(Map<String, ModuleInfo> allModules) {
        Set<String> toClean = new HashSet<String>();
        for(Entry<String, ModuleInfo> entry : allModules.entrySet()){
            if(entry.getValue() == null)
                toClean.add(entry.getKey());
        }
        for(String key : toClean)
            allModules.remove(key);
    }

    private Map<String, Category> parseCategories(Map<String, Feature> features) throws SAXException, IOException, ParserConfigurationException {
        msg("parsing.categories", categories.getPath());
        newline();
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = builder.parse(categories);
        Element root = document.getDocumentElement();
        NodeList categories = root.getElementsByTagName("category-def");
        Map<String, Category> categoriesByName = new HashMap<>();
        for(int i=0;i<categories.getLength();i++){
            Element category = (Element) categories.item(i);
            String name = category.getAttribute("name");
            String label = category.getAttribute("label");
            String description = getContent(category, "description");
            categoriesByName.put(name, new Category(name, label, description));
            msg("category.found", name);
            newline();
        }
        NodeList featureList = root.getElementsByTagName("feature");
        for(int i=0;i<featureList.getLength();i++){
            Element feature = (Element) featureList.item(i);
            String featureName = feature.getAttribute("id");
            NodeList categoryList = feature.getElementsByTagName("category");
            Element categoryElem = (Element) categoryList.item(0);
            String categoryName = categoryElem.getAttribute("name");
            Category category = categoriesByName.get(categoryName);
            category.setFeature(features.get(featureName));
        }
        NodeList iuList = root.getElementsByTagName("iu");
        for(int i=0;i<iuList.getLength();i++){
            Element iu = (Element) iuList.item(i);
            NodeList categoryList = iu.getElementsByTagName("category");
            Element categoryElem = (Element) categoryList.item(0);
            String categoryName = categoryElem.getAttribute("name");
            Category category = categoriesByName.get(categoryName);

            NodeList queryList = iu.getElementsByTagName("query");
            if(queryList.getLength() != 1)
                throw new CeylonP2ToolException("Don't know how to handle iu element with no query or more than one in category file");
            Element queryElem = (Element) queryList.item(0);

            String expression = getContent(queryElem, "expression");
            if(expression == null)
                throw new CeylonP2ToolException("Don't know how to handle iu element with no query/expression or more than one in category file");
            if(!expression.equals(KNOWN_EXPRESSION_TYPE))
                throw new CeylonP2ToolException("Don't know how to handle iu element with query/expression of: '"+expression+
                        "'. We only know how to handle: '"+KNOWN_EXPRESSION_TYPE+"'.");

            category.setAllJars();
        }
        return categoriesByName;
    }

    private Map<String,Feature> collectFeatures() throws ParserConfigurationException, SAXException, IOException {
        File featuresDir = new File(out, "features");
        msg("collecting.features", featuresDir.getPath());
        newline();
        Map<String,Feature> ret = new HashMap<>();
        if(!featuresDir.exists())
            return ret;
        for(File feature : featuresDir.listFiles()){
            if(feature.isDirectory()){
                collectFeature(ret, feature);
            }
        }
        return ret;
    }

    static String getContent(Element root, String name) {
        NodeList nodes = root.getElementsByTagName(name);
        if(nodes.getLength() != 1)
            return null;
        Node item = nodes.item(0);
        return item.getTextContent().trim();
    }

    private void collectFeature(Map<String, Feature> ret, File feature) throws ParserConfigurationException, SAXException, IOException {
        File manifest = new File(feature, "feature.xml");
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = builder.parse(manifest);
        Element root = document.getDocumentElement();

        File jar = new File(feature.getParentFile(), feature.getName()+".jar");
        if(jar.exists())
            jar.delete();
        File zipFolder = IOUtils.zipFolder(feature);
        Files.copy(zipFolder.toPath(), jar.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
        zipFolder.delete();
        Feature f = new Feature(root.getAttribute("id"), root.getAttribute("version"), alignOn4k(manifest.length()), jar.length(), root);
        ret.put(f.name, f);
        msg("feature.found", f.name, feature.getPath());
        newline();
        msg("feature.zipped", f.name, jar.getPath());
        newline();
    }

    private long alignOn4k(long length) {
        return ((long)Math.ceil(length / 4096)) * 4096;
    }

    @Override
    protected Logger createLogger() {
        return new Logger(){

            @Override
            public void error(String str) {
                print("[ERROR]: "+str);
            }

            private void print(String string) {
                if(verbose == null)
                    return;
                try {
                    errorAppend(string);
                    errorNewline();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void warning(String str) {
                print("[WARNING]: "+str);
            }

            @Override
            public void info(String str) {
                print("[INFO]: "+str);
            }

            @Override
            public void debug(String str) {
                print("[DEBUG]: "+str);
            }
        };
    }
    
    private void printContent(Map<String, ModuleInfo> allModules, Map<String, Feature> features, Map<String, Category> categoriesByName) throws XMLStreamException, IOException{
        File outFile = new File(out, "content.xml");
        Writer fileWriter = new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8");
        XMLStreamWriter stupidWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(fileWriter);
        XMLStreamWriter writer = new IndentingWriter(stupidWriter);
        writer.writeStartDocument("UTF-8", "1.0");
        writer.writeProcessingInstruction("metadataRepository", "version='1.1.0'"); // FIXME: is this supposed to be configurable?
        
        writer.writeStartElement("repository");
        writer.writeAttribute("name", repositoryName);
        writer.writeAttribute("type", "org.eclipse.equinox.internal.p2.metadata.repository.LocalMetadataRepository");
        writer.writeAttribute("version", "1.0.0");

        commonProperties(writer);
        
        {
            writer.writeStartElement("units");
            writer.writeAttribute("size", String.valueOf(allModules.size()+(features.size()*2)+categoriesByName.size()));
            
            for(Map.Entry<String, ModuleInfo> entries : allModules.entrySet()){
                ModuleInfo moduleInfo = entries.getValue();
                printUnit(writer, moduleInfo);
            }

            for(Feature feature : features.values()){
                printUnitFeature(writer, feature, true);
                printUnitFeature(writer, feature, false);
            }
            
            for(Category category : categoriesByName.values()){
                printUnitCategory(writer, category, allModules.values());
            }

            writer.writeEndElement();
        }
        
        writer.writeEndElement();
        writer.writeEndDocument();
        writer.flush();
        writer.close();
    }
    
    private void printUnitCategory(XMLStreamWriter writer, Category category, Collection<ModuleInfo> modules) throws XMLStreamException {
        writer.writeStartElement("unit");
        String name = categoryPrefix + "." + category.name;
        // the real algo is too weird to reproduce
        String versionQualifier = UUID.randomUUID().toString();
        String version = "1.0.0."+versionQualifier;
        writer.writeAttribute("id", name);
        writer.writeAttribute("version", version);
        {
            writer.writeStartElement("properties");
            writer.writeAttribute("size", "3");

            {
                writer.writeEmptyElement("property");
                writer.writeAttribute("name", "org.eclipse.equinox.p2.name");
                writer.writeAttribute("value", category.label);
            }
            {
                writer.writeEmptyElement("property");
                writer.writeAttribute("name", "org.eclipse.equinox.p2.description");
                writer.writeAttribute("value", category.description);
            }
            {
                writer.writeEmptyElement("property");
                writer.writeAttribute("name", "org.eclipse.equinox.p2.type.category");
                writer.writeAttribute("value", "true");
            }

            writer.writeEndElement();
        }

        {
            writer.writeStartElement("provides");
            writer.writeAttribute("size", "1");

            {
                writer.writeEmptyElement("provided");
                writer.writeAttribute("namespace", "org.eclipse.equinox.p2.iu");
                writer.writeAttribute("name", name);
                writer.writeAttribute("version", version);
            }

            writer.writeEndElement();
        }

        {
            writer.writeStartElement("requires");
            if(category.feature != null){
                writer.writeAttribute("size", "1");

                {
                    writer.writeEmptyElement("required");
                    writer.writeAttribute("namespace", "org.eclipse.equinox.p2.iu");
                    writer.writeAttribute("name", category.feature.name+".feature.group");
                    writer.writeAttribute("range", "["+category.feature.version+","+category.feature.version+"]");
                }
            }else if(category.allJars){
                writer.writeAttribute("size", String.valueOf(modules.size()));
                for(ModuleInfo mod : modules){
                    writer.writeEmptyElement("required");
                    writer.writeAttribute("namespace", "org.eclipse.equinox.p2.iu");
                    writer.writeAttribute("name", mod.name);
                    writer.writeAttribute("range", "["+mod.osgiVersion+","+mod.osgiVersion+"]");
                }
                
            }

            writer.writeEndElement();
        }
        
        {
            writer.writeEmptyElement("touchpoint");
            writer.writeAttribute("id", "null");
            writer.writeAttribute("version", "0.0.0");
        }

        writer.writeEndElement();
    }

    private void printUnit(XMLStreamWriter writer, ModuleInfo moduleInfo) throws XMLStreamException {
        writer.writeStartElement("unit");
        writer.writeAttribute("id", moduleInfo.name);
        writer.writeAttribute("version", moduleInfo.osgiVersion);
        writer.writeAttribute("singleton", "false");
        {
            writer.writeEmptyElement("update");
            writer.writeAttribute("id", moduleInfo.name);
            writer.writeAttribute("range", "[0.0.0,"+moduleInfo.osgiVersion+")");
            writer.writeAttribute("severity", "0");
        }
        {
            writer.writeStartElement("properties");
            SortedMap<String,String> properties = moduleInfo.getOsgiProperties();
            writer.writeAttribute("size", String.valueOf(properties.size()));

            for(Map.Entry<String,String> property : properties.entrySet()){
                writer.writeEmptyElement("property");
                writer.writeAttribute("name", getUnitPropertyName(property.getKey()));
                writer.writeAttribute("value", property.getValue());
            }
            writer.writeEndElement();
        }

        {
            writer.writeStartElement("provides");
            List<ModuleSpec> exportedPackages = moduleInfo.getExportedPackages();
            writer.writeAttribute("size", String.valueOf(3 + exportedPackages.size()));

            {
                writer.writeEmptyElement("provided");
                writer.writeAttribute("namespace", "org.eclipse.equinox.p2.iu");
                writer.writeAttribute("name", moduleInfo.name);
                writer.writeAttribute("version", moduleInfo.osgiVersion);
            }
            {
                writer.writeEmptyElement("provided");
                writer.writeAttribute("namespace", "osgi.bundle");
                writer.writeAttribute("name", moduleInfo.name);
                writer.writeAttribute("version", moduleInfo.osgiVersion);
            }

            for(ModuleSpec pkg : exportedPackages){
                writer.writeEmptyElement("provided");
                writer.writeAttribute("namespace", "java.package");
                writer.writeAttribute("name", pkg.getName());
                writer.writeAttribute("version", pkg.isVersioned() ? pkg.getVersion() : "0.0.0");
            }

            {
                writer.writeEmptyElement("provided");
                writer.writeAttribute("namespace", "org.eclipse.equinox.p2.eclipse.type");
                writer.writeAttribute("name", "bundle");
                writer.writeAttribute("version", "1.0.0");
            }
            writer.writeEndElement();
        }

        {
            writer.writeStartElement("requires");
            List<ModuleSpec> importedPackages = moduleInfo.getImportedPackages();
            List<ModuleSpec> importedModules = moduleInfo.getImportedModules();
            writer.writeAttribute("size", String.valueOf(importedModules.size() + importedPackages.size()));

            for(ModuleSpec mod : importedModules){
                writer.writeEmptyElement("required");
                writer.writeAttribute("namespace", "osgi.bundle");
                writer.writeAttribute("name", mod.getName());
                writer.writeAttribute("range", mod.getVersion());
            }

            for(ModuleSpec pkg : importedPackages){
                writer.writeEmptyElement("required");
                writer.writeAttribute("namespace", "java.package");
                writer.writeAttribute("name", pkg.getName());
                writer.writeAttribute("range", pkg.isVersioned() ? pkg.getVersion() : "0.0.0");
            }

            writer.writeEndElement();
        }

        {
            writer.writeStartElement("artifacts");
            writer.writeAttribute("size", "1");

            {
                writer.writeEmptyElement("artifact");
                writer.writeAttribute("classifier", "osgi.bundle");
                writer.writeAttribute("id", moduleInfo.name);
                writer.writeAttribute("version", moduleInfo.osgiVersion);
            }

            writer.writeEndElement();
        }

        {
            writer.writeEmptyElement("touchpoint");
            writer.writeAttribute("id", "org.eclipse.equinox.p2.osgi");
            writer.writeAttribute("version", "1.0.0");
        }

        {
            writer.writeStartElement("touchpointData");
            writer.writeAttribute("size", "1");

            {
                writer.writeStartElement("instructions");
                writer.writeAttribute("size", "2");

                {
                    writer.writeStartElement("instruction");
                    writer.writeAttribute("key", "manifest");
                    writer.writeCharacters("Bundle-SymbolicName: "+moduleInfo.getAttribute("Bundle-SymbolicName")
                    +"\nBundle-Version: "+moduleInfo.getAttribute("Bundle-Version"));
                    writer.writeEndElement();
                }

                {
                    writer.writeStartElement("instruction");
                    writer.writeAttribute("key", "zipped");
                    writer.writeCharacters("true");
                    writer.writeEndElement();
                }

                writer.writeEndElement();
            }

            writer.writeEndElement();
        }

        writer.writeEndElement();
    }

    private void printUnitFeature(XMLStreamWriter writer, Feature feature, boolean group) throws XMLStreamException {
        writer.writeStartElement("unit");
        String name = feature.name+(group ? ".feature.group" : ".feature.jar");
        writer.writeAttribute("id", name);
        writer.writeAttribute("version", feature.version);
        if(group){
            writer.writeAttribute("singleton", "false");
            {
                writer.writeEmptyElement("update");
                writer.writeAttribute("id", name);
                writer.writeAttribute("range", "[0.0.0,"+feature.version+")");
                writer.writeAttribute("severity", "0");
            }
        }
        {
            writer.writeStartElement("properties");
            SortedMap<String,String> properties = feature.getProperties();
            writer.writeAttribute("size", String.valueOf(properties.size()+(group?1:0)));

            for(Map.Entry<String,String> property : properties.entrySet()){
                writer.writeEmptyElement("property");
                writer.writeAttribute("name", property.getKey());
                writer.writeAttribute("value", property.getValue());
            }
            if(group){
                writer.writeEmptyElement("property");
                writer.writeAttribute("name", "org.eclipse.equinox.p2.type.group");
                writer.writeAttribute("value", "true");
            }

            writer.writeEndElement();
        }

        {
            writer.writeStartElement("provides");
            writer.writeAttribute("size", group ? "1" : "3");

            {
                writer.writeEmptyElement("provided");
                writer.writeAttribute("namespace", "org.eclipse.equinox.p2.iu");
                writer.writeAttribute("name", name);
                writer.writeAttribute("version", feature.version);
            }
            if(!group){
                {
                    writer.writeEmptyElement("provided");
                    writer.writeAttribute("namespace", "org.eclipse.equinox.p2.eclipse.type");
                    writer.writeAttribute("name", "feature");
                    writer.writeAttribute("version", "1.0.0");
                }
                {
                    writer.writeEmptyElement("provided");
                    writer.writeAttribute("namespace", "org.eclipse.update.feature");
                    writer.writeAttribute("name", feature.name);
                    writer.writeAttribute("version", feature.version);
                }
            }

            writer.writeEndElement();
        }

        if(group){
            writer.writeStartElement("requires");
            List<ModuleSpec> importedModules = feature.getImportedModules();
            writer.writeAttribute("size", String.valueOf(importedModules.size()+1));

            for(ModuleSpec mod : importedModules){
                writer.writeEmptyElement("required");
                writer.writeAttribute("namespace", "org.eclipse.equinox.p2.iu");
                writer.writeAttribute("name", mod.getName());
                writer.writeAttribute("range", "["+mod.getVersion()+","+mod.getVersion()+"]");
            }

            {
                writer.writeStartElement("required");
                writer.writeAttribute("namespace", "org.eclipse.equinox.p2.iu");
                writer.writeAttribute("name", feature.name+".feature.jar");
                writer.writeAttribute("range", "["+feature.version+","+feature.version+"]");

                {
                    writer.writeStartElement("filter");
                    writer.writeCharacters("(org.eclipse.update.install.features=true)");
                    writer.writeEndElement();
                }
                
                writer.writeEndElement();
            }
            
            writer.writeEndElement();
        }else{
            writer.writeStartElement("filter");
            writer.writeCharacters("(org.eclipse.update.install.features=true)");
            writer.writeEndElement();

            {
                writer.writeStartElement("artifacts");
                writer.writeAttribute("size", "1");
                
                {
                    writer.writeEmptyElement("artifact");
                    writer.writeAttribute("classifier", "org.eclipse.update.feature");
                    writer.writeAttribute("id", feature.name);
                    writer.writeAttribute("version", feature.version);
                }
                
                writer.writeEndElement();
            }
        }
        
        {
            writer.writeEmptyElement("touchpoint");
            writer.writeAttribute("id", group ? "null" : "org.eclipse.equinox.p2.osgi");
            writer.writeAttribute("version", group ? "0.0.0" : "1.0.0");
        }
        if(!group){
            writer.writeStartElement("touchpointData");
            writer.writeAttribute("size", "1");

            {
                writer.writeStartElement("instructions");
                writer.writeAttribute("size", "1");

                {
                    writer.writeStartElement("instruction");
                    writer.writeAttribute("key", "zipped");
                    writer.writeCharacters("true");
                    writer.writeEndElement();
                }

                writer.writeEndElement();
            }

            writer.writeEndElement();
        }


        {
            writer.writeStartElement("licenses");
            writer.writeAttribute("size", "1");

            {
                writer.writeStartElement("license");
                writer.writeAttribute("uri", "");
                writer.writeAttribute("url", "");

                writer.writeCharacters(feature.getLicense());

                writer.writeEndElement();
            }

            writer.writeEndElement();
        }

        {
            writer.writeStartElement("copyright");

            writer.writeCharacters(feature.getCopyright());

            writer.writeEndElement();
        }

        writer.writeEndElement();
    }

    private String getUnitPropertyName(String key) {
        switch(key){
        case "Bundle-Name": return "org.eclipse.equinox.p2.name";
        case "Bundle-Description": return "org.eclipse.equinox.p2.description";
        case "Bundle-DocUrl": return "org.eclipse.equinox.p2.doc.url";
        case "Bundle-Vendor": return "org.eclipse.equinox.p2.provider";
        }
        return key;
    }

    private void printArtifacts(Map<String, ModuleInfo> allModules, Map<String, Feature> features) 
            throws IOException, XMLStreamException, FactoryConfigurationError {
        File outFile = new File(out, "artifacts.xml");
        Writer fileWriter = new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8");
        XMLStreamWriter stupidWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(fileWriter);
        XMLStreamWriter writer = new IndentingWriter(stupidWriter);
        writer.writeStartDocument("UTF-8", "1.0");
        writer.writeProcessingInstruction("artifactRepository", "version='1.1.0'"); // FIXME: is this supposed to be configurable?
        
        writer.writeStartElement("repository");
        writer.writeAttribute("name", repositoryName);
        writer.writeAttribute("type", "org.eclipse.equinox.p2.artifact.repository.simpleRepository");
        writer.writeAttribute("version", "1");

        commonProperties(writer);

        {
            writer.writeStartElement("mappings");
            writer.writeAttribute("size", "3");
            
            {
                writer.writeEmptyElement("rule");
                writer.writeAttribute("filter", "(& (classifier=osgi.bundle))");
                writer.writeAttribute("output", "${repoUrl}/plugins/${id}_${version}.jar");
            }
            {
                writer.writeEmptyElement("rule");
                writer.writeAttribute("filter", "(& (classifier=binary))");
                writer.writeAttribute("output", "${repoUrl}/binary/${id}_${version}");
            }
            {
                writer.writeEmptyElement("rule");
                writer.writeAttribute("filter", "(& (classifier=org.eclipse.update.feature))");
                writer.writeAttribute("output", "${repoUrl}/features/${id}_${version}.jar");
            }
            
            writer.writeEndElement();
        }

        {
            writer.writeStartElement("artifacts");
            writer.writeAttribute("size", String.valueOf(allModules.size()+features.size()));
            
            for(Map.Entry<String, ModuleInfo> entries : allModules.entrySet()){
                ModuleInfo moduleInfo = entries.getValue();
                // skip missing modules
                if(moduleInfo == null)
                    continue;
                String version = moduleInfo.osgiVersion;
                
                writer.writeStartElement("artifact");
                writer.writeAttribute("classifier", "osgi.bundle");
                writer.writeAttribute("id", moduleInfo.name);
                writer.writeAttribute("version", version);
                {
                    writer.writeStartElement("properties");
                    writer.writeAttribute("size", "3");

                    {
                        writer.writeEmptyElement("property");
                        writer.writeAttribute("name", "artifact.size");
                        writer.writeAttribute("value", String.valueOf(moduleInfo.jar.length()));
                    }
                    {
                        writer.writeEmptyElement("property");
                        writer.writeAttribute("name", "download.size");
                        writer.writeAttribute("value", String.valueOf(moduleInfo.jar.length()));
                    }
                    {
                        writer.writeEmptyElement("property");
                        writer.writeAttribute("name", "download.md5");
                        writer.writeAttribute("value", ShaSigner.md5(moduleInfo.jar));
                    }

                    writer.writeEndElement();
                }
                writer.writeEndElement();
            }
            
            for(Feature feature : features.values()){
                writer.writeStartElement("artifact");
                writer.writeAttribute("classifier", "org.eclipse.update.feature");
                writer.writeAttribute("id", feature.name);
                writer.writeAttribute("version", feature.version);
                {
                    writer.writeStartElement("properties");
                    writer.writeAttribute("size", "3");

                    {
                        writer.writeEmptyElement("property");
                        writer.writeAttribute("name", "artifact.size");
                        writer.writeAttribute("value", String.valueOf(feature.manifestSize));
                    }
                    {
                        writer.writeEmptyElement("property");
                        writer.writeAttribute("name", "download.size");
                        writer.writeAttribute("value", String.valueOf(feature.jarSize));
                    }
                    {
                        writer.writeEmptyElement("property");
                        writer.writeAttribute("name", "download.contentType");
                        writer.writeAttribute("value", "application/zip");
                    }

                    writer.writeEndElement();
                }
                writer.writeEndElement();
            }
            
            writer.writeEndElement();
        }

        writer.writeEndElement();
        writer.writeEndDocument();
        writer.flush();
        writer.close();
    }

    private void commonProperties(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement("properties");
        writer.writeAttribute("size", "2");

        {
            writer.writeEmptyElement("property");
            writer.writeAttribute("name", "p2.timestamp");
            writer.writeAttribute("value", String.valueOf(new Date().getTime()));
        }
        {
            writer.writeEmptyElement("property");
            writer.writeAttribute("name", "p2.compressed");
            writer.writeAttribute("value", "false");
        }

        writer.writeEndElement();
    }

    static String fixOsgiVersion(String version) {
        int firstDot = version.indexOf('.');
        if(firstDot == -1)
            return version + ".0.0";
        int secondDot = version.indexOf('.', firstDot+1);
        if(secondDot == -1)
            return version + ".0";
        return version;
    }

    private void collectModules(RepositoryManager repoManager, String name, String version, Map<String, ModuleInfo> allModules) throws IOException {
        // ignore JDK dependencies
        if(CeylonP2Tool.skipModule(name))
            return;
        String key = name+"/"+version;
        if(allModules.containsKey(key))
            return;
        ArtifactResult artifact = repoManager.getArtifactResult(new ArtifactContext(name, version, ArtifactContext.CAR, ArtifactContext.JAR));
        File artifactJar = null;
        if(artifact == null){
            // try to find it in the plugins folder
            File pluginJar = new File(out, "plugins/"+name+"_"+version+".jar");
            if(pluginJar.exists()){
                artifactJar = pluginJar;
            }
        }else{
            artifactJar = artifact.artifact();
        }
        allModules.put(key, artifactJar != null ? new ModuleInfo(name, version, artifactJar) : null);
        if(artifact == null){
            errorMsg("module.not.found", name+"/"+version, out+"/plugins");
        }else{
            msg("module.found", name+"/"+version, artifact != null ? 
                    artifact.repositoryDisplayString() : artifactJar.getPath());
            newline();
            for(ArtifactResult dep : artifact.dependencies()){
                // FIXME: deal with optionals
                collectModules(repoManager, dep.name(), dep.version(), allModules);
            }
        }
    }

    public static boolean skipModule(String name) {
        return JDKUtils.isJDKModule(name) || JDKUtils.isOracleJDKModule(name)
                // this one is "provided"
                || "org.osgi.core".equals(name);
    }
}
