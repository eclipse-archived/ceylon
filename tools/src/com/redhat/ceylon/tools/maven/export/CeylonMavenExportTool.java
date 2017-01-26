package com.redhat.ceylon.tools.maven.export;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.redhat.ceylon.cmr.api.ModuleQuery;
import com.redhat.ceylon.cmr.ceylon.loader.ModuleGraph;
import com.redhat.ceylon.cmr.ceylon.loader.ModuleGraph.Module;
import com.redhat.ceylon.cmr.impl.ShaSigner;
import com.redhat.ceylon.common.FileUtil;
import com.redhat.ceylon.common.ModuleSpec;
import com.redhat.ceylon.common.ModuleUtil;
import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.Option;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tool.ToolUsageError;
import com.redhat.ceylon.model.cmr.ArtifactResult;
import com.redhat.ceylon.model.cmr.RepositoryException;
import com.redhat.ceylon.tools.moduleloading.ModuleLoadingTool;

@Summary("Generate a Maven repository for a given module")
@Description("Gerate Maven repository which contains the given module and all its run-time"
        + " dependencies, including the Ceylon run-time, which makes that repository usable by"
        + " Maven as a regular Maven repository.\n\n"
        + "Alternately, running with `--for-import` creates a special repository set up suitable"
        + " for importing the Ceylon distribution to Maven Central. This is mostly useful for the"
        + " Ceylon team."
)
public class CeylonMavenExportTool extends ModuleLoadingTool {

    public class ImportComparator implements Comparator<ArtifactResult> {

        @Override
        public int compare(ArtifactResult o1, ArtifactResult o2) {
            // get'em from the root
            ArtifactResult to1 = loader.getModuleArtifact(o1.name());
            ArtifactResult to2 = loader.getModuleArtifact(o2.name());
            if(to1 == null || to2 == null)
                return o1.name().compareTo(o2.name());
            
            int ret;
            // place those first
            if(to1.groupId().equals("org.ceylon-lang")){
                if(to2.groupId().equals("org.ceylon-lang"))
                    ret = 0;
                else
                    ret = -1;
            }else{
                if(to2.groupId().equals("org.ceylon-lang"))
                    ret = 1;
                else
                    ret = to1.groupId().compareTo(to2.groupId());
            }
            if(ret != 0)
                return ret;
            return to1.artifactId().compareTo(to2.artifactId());
        }

    }

    private final Comparator<? super ArtifactResult> ImportComparator = new ImportComparator();
    private List<ModuleSpec> modules;
	private File out;
    private final List<String> excludedModules = new ArrayList<>();
    private boolean forImport;

    @Argument(order = 1, argumentName="module", multiplicity="+")
    public void setModules(List<String> modules) {
        setModuleSpecs(ModuleSpec.parseEachList(modules));
    }

    public void setModuleSpecs(List<ModuleSpec> modules) {
        this.modules = modules;
    }

    @Description("Target Maven repository folder (defaults to `maven-repository`).")
    @OptionArgument(shortName = 'o', argumentName="file")
    public void setOut(File out) {
        this.out = out;
    }

    @Option(longName="for-import")
    @Description("Special set up to create a set of folders to import the distrib.")
    public void setForImport(boolean forImport) {
        this.forImport = forImport;
    }

    @OptionArgument(argumentName="moduleOrFile", shortName='x')
    @Description("Excludes modules from the resulting Maven repository. Can be a module name or " + 
            "a file containing module names. Can be specified multiple times.")
    public void setExcludeModule(List<String> exclusions) {
        for (String each : exclusions) {
            File xFile = new File(each);
            if (xFile.exists() && xFile.isFile()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(xFile))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        this.excludedModules.add(line);
                    }
                } catch (IOException e) {
                    throw new ToolUsageError(CeylonMavenExportMessages.msg("exclude.file.failure", each), 
                            e);
                }
            } else {
                this.excludedModules.add(each);
            }
        }
    }

    @Override
    public void run() throws Exception {
        String firstModuleName = null, firstModuleVersion = null;
        for (ModuleSpec module : modules) {
            String moduleName = module.getName();
            String version = checkModuleVersionsOrShowSuggestions(
                    moduleName,
                    module.isVersioned() ? module.getVersion() : null,
                    ModuleQuery.Type.JVM,
                    Versions.JVM_BINARY_MAJOR_VERSION,
                    Versions.JVM_BINARY_MINOR_VERSION,
                    null, null, // JS binary but don't care since JVM
                    null);
            if(version == null)
                return;
            if(firstModuleName == null){
                firstModuleName = moduleName;
                firstModuleVersion = version;
            }
            loadModule(null, moduleName, version);
        }
        // FIXME: we probably want to allow exporting of multiple versions
        loader.resolve();

        final File outputFolder = applyCwd(out != null ? out : new File("maven-repository"));
        if(!outputFolder.exists()){
            FileUtil.mkdirs(outputFolder);
        }else{
            // FIXME: error if regular file?
            // FIXME: or add, don't delete?
            FileUtil.delete(outputFolder);
        }
        final List<ArtifactResult> writtenModules = new LinkedList<>();
        final List<ArtifactResult> externalDependencies = new LinkedList<>();
        loader.visitModules(new ModuleGraph.Visitor() {
            @Override
            public void visit(ModuleGraph.Module module) {
                if(module.artifact == null || module.artifact.artifact() == null)
                    return;
                // FIXME: skip Maven modules?
                if(forImport){
                    if(!module.artifact.groupId().equals("org.ceylon-lang")){
                        externalDependencies.add(module.artifact);
                        return;
                    }
                    makeMavenImportFolder(module, outputFolder);
                    writtenModules.add(module.artifact);
                }else
                    makeMavenModule(module, outputFolder);
            }
        });
        if(forImport){
            makeMavenImportSpecialFolders(writtenModules, externalDependencies, outputFolder);
        }
        flush();
    }

    private void makeMavenImportSpecialFolders(List<ArtifactResult> writtenModules, List<ArtifactResult> externalDependencies, File outputFolder) {
        File pomAllFile = makePomFile(outputFolder, "ceylon-all");
        generatePomForAll(pomAllFile, writtenModules);

        File pomSystemFile = makePomFile(outputFolder, "ceylon-system");
        generatePomForSystem(pomSystemFile, writtenModules);
    
        File pomCompleteFile = makePomFile(outputFolder, "ceylon-complete");
        generatePomForComplete(pomCompleteFile, writtenModules, externalDependencies);
    }

    private File makePomFile(File outputFolder, String allArtifactId) {
        File folder = new File(outputFolder, allArtifactId);
        FileUtil.mkdirs(folder);
        return new File(folder, "pom.xml");
    }

    private void generatePomForAll(File pomFile, List<ArtifactResult> writtenModules) {
        try (OutputStream os = new FileOutputStream(pomFile)){
            XMLStreamWriter out = XMLOutputFactory.newInstance().createXMLStreamWriter(
                    new OutputStreamWriter(os, "utf-8"));

            // FIXME: what to do with the default module?
            
            writePomHeader(out);

            writePomParent(out, writtenModules.get(0));

            writeElement(out, "artifactId", "ceylon-all");
            writeNewline(out);
            writeElement(out, "name", "ceylon-all");

            writePomDependencies(out, writtenModules);

            writePomFooter(out);
        }
        catch (IOException | XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    private void generatePomForSystem(File pomFile, List<ArtifactResult> writtenModules) {
        try (OutputStream os = new FileOutputStream(pomFile)){
            XMLStreamWriter out = XMLOutputFactory.newInstance().createXMLStreamWriter(
                    new OutputStreamWriter(os, "utf-8"));

            // FIXME: what to do with the default module?
            
            writePomHeader(out);

            writePomParent(out, writtenModules.get(0));

            writeElement(out, "artifactId", "ceylon-system");
            writeNewline(out);
            writeElement(out, "name", "ceylon-system");
            writeNewline(out);
            writeElement(out, "packaging", "pom");

            writePomDependencies(out, writtenModules);

            writePomFooter(out);
        }
        catch (IOException | XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeNewline(XMLStreamWriter out) throws XMLStreamException{
        out.writeCharacters("\n");
    }

    private int indent = 0;
    
    private void writeIndent(XMLStreamWriter out) throws XMLStreamException{
        if(indent == 0)
            return;
        out.writeCharacters("\n");
        for(int i=0;i<indent;i++)
            out.writeCharacters("  ");
    }

    private void writeOpen(XMLStreamWriter out, String element) throws XMLStreamException{
        writeIndent(out);
        out.writeStartElement(element);
        indent++;
    }

    private void writeClose(XMLStreamWriter out) throws XMLStreamException{
        indent--;
        writeIndent(out);
        out.writeEndElement();
    }

    private void writeElement(XMLStreamWriter out, String element, String text) throws XMLStreamException{
        writeIndent(out);
        out.writeStartElement(element);
        out.writeCharacters(text);
        out.writeEndElement();
    }

    
    private void generatePomForComplete(File pomFile, List<ArtifactResult> writtenModules, List<ArtifactResult> externalDependencies) {
        try (OutputStream os = new FileOutputStream(pomFile)){
            XMLStreamWriter out = XMLOutputFactory.newInstance().createXMLStreamWriter(
                    new OutputStreamWriter(os, "utf-8"));

            // FIXME: what to do with the default module?
            
            writePomHeader(out);

            writePomParent(out, writtenModules.get(0));

            writeElement(out, "artifactId", "ceylon-complete");
            writeNewline(out);
            writeElement(out, "name", "ceylon-complete");
            writeNewline(out);

            writeOpen(out, "dependencies");
            {
                writeOpen(out, "dependency");
                {
                    writeElement(out, "groupId", "org.ceylon-lang");
                    writeElement(out, "artifactId", "ceylon-all");
                }
                writeClose(out);
            }
            writeClose(out);
            
            writeNewline(out);
            writeOpen(out, "build");
            {
                writeOpen(out, "plugins");
                {
                    writeOpen(out, "plugin");
                    {
                        writeElement(out, "artifactId", "maven-shade-plugin");
                        writeElement(out, "version", "2.4.2");
                        writeOpen(out, "executions");
                        {
                            writeOpen(out, "execution");
                            {
                                writeOpen(out, "goals");
                                {
                                    writeElement(out, "goal", "shade");
                                }
                                writeClose(out);
                                
                                writeOpen(out, "configuration");
                                {
                                    writeElement(out, "createDependencyReducedPom", "true");
                                    writeElement(out, "createSourcesJar", "true");
                                    
                                    writeOpen(out, "artifactSet");
                                    {
                                        writeOpen(out, "includes");
                                        {
                                            writeElement(out, "include", "org.ceylon-lang:*");
                                            List<ArtifactResult> sortedImports = new ArrayList<>(externalDependencies);
                                            Collections.sort(sortedImports, ImportComparator);
                                            for(ArtifactResult dep : sortedImports){
                                                String dependencyName = dep.name();

                                                // skip jdk
                                                if(jdkProvider.isJDKModule(dependencyName))
                                                    continue;

                                                writeElement(out, "include", dep.groupId()+":"+dep.artifactId());
                                            }
                                        }
                                        writeClose(out);
                                    }
                                    writeClose(out);
                                }
                                writeClose(out);
                            }
                            writeClose(out);
                        }
                        writeClose(out);
                    }
                    writeClose(out);
                }
                writeClose(out);
            }
            writeClose(out);


            writePomFooter(out);
        }
        catch (IOException | XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    protected void makeMavenImportFolder(Module module, File outputFolder) {
        File pomFile = makePomFile(outputFolder, module.artifact.artifactId());
        generatePomFromModule(pomFile, module.artifact);
    }

    protected void makeMavenModule(Module module, File outputFolder) {
        String groupId = module.artifact.groupId();
        String artifactId = module.artifact.artifactId();
        String path = groupId.replace('.', '/') + "/" + artifactId + "/" + module.version;
        File folder = new File(outputFolder, path);
        FileUtil.mkdirs(folder);
        String mavenFileName = artifactId+"-"+module.version;
        try {
            File jarFile = new File(folder, mavenFileName+".jar");
            File pomFile = new File(folder, mavenFileName+".pom");
            Files.copy(module.artifact.artifact().toPath(), jarFile.toPath(), 
                    StandardCopyOption.REPLACE_EXISTING);
            // extract its pom.xml too
            try(ZipFile zf = new ZipFile(module.artifact.artifact())){
                ZipEntry pomEntry = zf.getEntry("META-INF/maven/"+groupId+"/"+artifactId+"/pom.xml");
                if(pomEntry != null){
                    try(InputStream is = zf.getInputStream(pomEntry)){
                        Files.copy(is, pomFile.toPath(), 
                                StandardCopyOption.REPLACE_EXISTING);
                    }
                }else{
                    generatePomFromModule(pomFile, module.artifact);
                }
            }
            // now sha1 them
            ShaSigner.sign(jarFile, null, false);
            ShaSigner.sign(pomFile, null, false);
        } catch (RepositoryException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void generatePomFromModule(File pomFile, ArtifactResult artifact) {
        try (OutputStream os = new FileOutputStream(pomFile)){
            XMLStreamWriter out = XMLOutputFactory.newInstance().createXMLStreamWriter(
                    new OutputStreamWriter(os, "utf-8"));

            // FIXME: what to do with the default module?
            
            writePomHeader(out);

            if(forImport){
                writePomParent(out, artifact);
            }else{
                writeElement(out, "groupId", artifact.groupId());
            }

            writeElement(out, "artifactId", artifact.artifactId());
            writeNewline(out);
            writeElement(out, "name", artifact.name());


            if(forImport){
                writeElement(out, "packaging", "jar");
                writeNewline(out);
                
                writeOpen(out, "properties");
                {
                    writeElement(out, "jarFile", "${ceylon.home}/repo/"+artifact.name().replace('.', '/')
                            +"/${ceylon.version}/"+artifact.name()+"-${ceylon.version}."
                            +(artifact.artifact().getName().endsWith(".jar") ? "jar" : "car"));

                    writeElement(out, "sourcesFile", "${ceylon.home}/repo/"+artifact.name().replace('.', '/')
                            +"/${ceylon.version}/"+artifact.name()+"-${ceylon.version}.src");
                }
                writeClose(out);
            }else{
                writeElement(out, "version", artifact.version());
            }

            List<ArtifactResult> imports = artifact.dependencies();
            writePomDependencies(out, imports);

            if(forImport){
                writeNewline(out);
                writeOpen(out, "build");
                {
                    writeOpen(out, "plugins");
                    {
                        writeOpen(out, "plugin");
                        {
                            writeElement(out, "groupId", "com.coderplus.maven.plugins");
                            writeElement(out, "artifactId", "copy-rename-maven-plugin");
                        }
                        writeClose(out);

                        writeOpen(out, "plugin");
                        {
                            writeElement(out, "groupId", "org.codehaus.mojo");
                            writeElement(out, "artifactId", "build-helper-maven-plugin");
                        }
                        writeClose(out);
                    }
                    writeClose(out);
                }
                writeClose(out);
            }

            writePomFooter(out);
        }
        catch (IOException | XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }
    
    private void writePomFooter(XMLStreamWriter out) throws XMLStreamException {
        writeNewline(out);
        writeClose(out);
        out.writeCharacters("\n");
        out.writeEndDocument();
        
        out.flush();
    }

    private void writePomDependencies(XMLStreamWriter out, List<ArtifactResult> imports) throws XMLStreamException {
        if(!imports.isEmpty()){
            writeNewline(out);
            writeOpen(out, "dependencies");
            {
                List<ArtifactResult> sortedImports = new ArrayList<>(imports);
                Collections.sort(sortedImports, ImportComparator);
                for(ArtifactResult dep : sortedImports){
                    String dependencyName = dep.name();
                    ArtifactResult moduleArtifact = loader.getModuleArtifact(dependencyName);

                    // skip jdk
                    if(jdkProvider.isJDKModule(dependencyName))
                        continue;

                    // get the real values from the module
                    String[] mavenCoordinates = ModuleUtil.getMavenCoordinates(dependencyName);
                    if(moduleArtifact != null){
                        mavenCoordinates[0] = moduleArtifact.groupId();
                        mavenCoordinates[1] = moduleArtifact.artifactId();
                    }

                    writeOpen(out, "dependency");
                    {
                        writeElement(out, "groupId", mavenCoordinates[0]);
                        writeElement(out, "artifactId", mavenCoordinates[1]);

                        if(!forImport || !mavenCoordinates[0].equals("org.ceylon-lang")){
                            String version;
                            if(forImport){
                                version = "${";
                                String name;
                                if(dep.name().startsWith("org.apache.maven."))
                                    name = "org.apache.maven";
                                else if(dep.name().startsWith("org.eclipse.aether."))
                                    name = "org.eclipse.aether";
                                else
                                    name = dep.name();
                                version += name;
                                version += "}";
                            }else
                                version = dep.version();
                            writeElement(out, "version", version);
                        }

                        if(dep.optional()){
                            writeElement(out, "optional", "true");
                        }
                    }
                    writeClose(out);
                }
            }
            writeClose(out);
        }
    }

    private void writePomParent(XMLStreamWriter out, ArtifactResult artifact) throws XMLStreamException {
        writeOpen(out, "parent");
        {
            writeElement(out, "groupId", artifact.groupId());
            writeElement(out, "artifactId", "ceylon-parent");
            writeElement(out, "version", artifact.version());
        }
        writeClose(out);

        writeNewline(out);
    }

    private void writePomHeader(XMLStreamWriter out) throws XMLStreamException {
        out.writeStartDocument("UTF-8", "1.0");
        out.writeCharacters("\n");
        
        writeOpen(out, "project");
        out.writeAttribute("xmlns", "http://maven.apache.org/POM/4.0.0");
        out.writeAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        out.writeAttribute("xsi:schemaLocation", "http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd");
        
        writeElement(out, "modelVersion", "4.0.0");
        writeNewline(out);
    }

    @Override
    protected boolean shouldExclude(String moduleName, String version) {
        return super.shouldExclude(moduleName, version) ||
                this.excludedModules.contains(moduleName);
    }
    
    @Override
    public boolean includeOptionalDependencies() {
        return forImport;
    }

    @Override
    public void cycleDetected(List<Module> path) {
        try {
            errorAppend(CeylonMavenExportMessages.msg("module.cycle", path));
            errorNewline();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
