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
import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.Option;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tool.ToolUsageError;
import com.redhat.ceylon.compiler.java.tools.MavenPomUtil;
import com.redhat.ceylon.model.cmr.ArtifactResult;
import com.redhat.ceylon.model.cmr.RepositoryException;
import com.redhat.ceylon.tools.moduleloading.ModuleLoadingTool;

@Summary("Generate a Ceylon executable jar for a given module")
@Description("Gerate an executable _fat jar_ which contains the given module and all its run-time"
        + " dependencies, including the Ceylon run-time, which makes that jar self-sufficient and"
        + " executable by `java` as if the Ceylon module was run by `ceylon run`."
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
    @Description("Excludes modules from the resulting far jat. Can be a module name or " + 
            "a file containing module names. Can be specified multiple times. Note that "+
            "this excludes the module from the resulting fat jar, but if your modules require that "+
            "module to be present at runtime it will still be required and may cause your "+
            "application to fail to start if it is not provided at runtime.")
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
        loader.visitModules(new ModuleGraph.Visitor() {
            @Override
            public void visit(ModuleGraph.Module module) {
                if(module.artifact == null || module.artifact.artifact() == null)
                    return;
                // FIXME: skip Maven modules?
                if(forImport){
                    if(!module.artifact.groupId().equals("org.ceylon-lang"))
                        return;
                    makeMavenImportFolder(module, outputFolder);
                }else
                    makeMavenModule(module, outputFolder);
            }
        });
        flush();
    }

    protected void makeMavenImportFolder(Module module, File outputFolder) {
        String groupId = module.artifact.groupId();
        String artifactId = module.artifact.artifactId();
        File folder = new File(outputFolder, artifactId);
        FileUtil.mkdirs(folder);
        File pomFile = new File(folder, "pom.xml");
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

            out.writeStartDocument("UTF-8", "1.0");
            out.writeCharacters("\n");
            
            // FIXME: what to do with the default module?
            
            out.writeStartElement("project");
            out.writeAttribute("xmlns", "http://maven.apache.org/POM/4.0.0");
            out.writeAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            out.writeAttribute("xsi:schemaLocation", "http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd");
            
            out.writeCharacters("\n  ");
            out.writeStartElement("modelVersion");
            out.writeCharacters("4.0.0");
            out.writeEndElement();
            out.writeCharacters("\n");

            if(forImport){
                out.writeCharacters("\n  ");
                out.writeStartElement("parent");

                out.writeCharacters("\n    ");
                out.writeStartElement("groupId");
                out.writeCharacters(artifact.groupId());
                out.writeEndElement();

                out.writeCharacters("\n    ");
                out.writeStartElement("artifactId");
                out.writeCharacters("ceylon-parent");
                out.writeEndElement();

                out.writeCharacters("\n    ");
                out.writeStartElement("version");
                out.writeCharacters(artifact.version());
                out.writeEndElement();

                out.writeCharacters("\n  ");
                out.writeEndElement();
                out.writeCharacters("\n");
            }else{
                out.writeCharacters("\n  ");
                out.writeStartElement("groupId");
                out.writeCharacters(artifact.groupId());
                out.writeEndElement();
            }

            out.writeCharacters("\n  ");
            out.writeStartElement("artifactId");
            out.writeCharacters(artifact.artifactId());
            out.writeEndElement();

            out.writeCharacters("\n\n  ");
            out.writeStartElement("name");
            out.writeCharacters(artifact.name());
            out.writeEndElement();

            if(forImport){
                out.writeCharacters("\n  ");
                out.writeStartElement("packaging");
                out.writeCharacters("jar");
                out.writeEndElement();
                
                out.writeCharacters("\n\n  ");
                out.writeStartElement("properties");

                out.writeCharacters("\n    ");
                out.writeStartElement("jarFile");
                out.writeCharacters("${ceylon.home}/repo/"+artifact.name().replace('.', '/')
                        +"/${ceylon.version}/"+artifact.name()+"-${ceylon.version}."
                        +(artifact.artifact().getName().endsWith(".jar") ? "jar" : "car"));
                out.writeEndElement();

                out.writeCharacters("\n    ");
                out.writeStartElement("sourcesFile");
                out.writeCharacters("${ceylon.home}/repo/"+artifact.name().replace('.', '/')
                        +"/${ceylon.version}/"+artifact.name()+"-${ceylon.version}.src");
                out.writeEndElement();

                out.writeCharacters("\n  ");
                out.writeEndElement();
            }else{
                out.writeCharacters("\n  ");
                out.writeStartElement("version");
                out.writeCharacters(artifact.version());
                out.writeEndElement();
            }

            List<ArtifactResult> imports = artifact.dependencies();
            if(!imports.isEmpty()){
                out.writeCharacters("\n\n  ");
                out.writeStartElement("dependencies");

                List<ArtifactResult> sortedImports = new ArrayList<>(imports);
                Collections.sort(sortedImports, ImportComparator);
                for(ArtifactResult dep : sortedImports){
                    String dependencyName = dep.name();
                    ArtifactResult moduleArtifact = loader.getModuleArtifact(dependencyName);
                    
                    // skip jdk
                    if(jdkProvider.isJDKModule(dependencyName))
                        continue;
                    
                    // get the real values from the module
                    String[] mavenCoordinates = MavenPomUtil.getMavenCoordinates(dependencyName);
                    if(moduleArtifact != null){
                        mavenCoordinates[0] = moduleArtifact.groupId();
                        mavenCoordinates[1] = moduleArtifact.artifactId();
                    }
                    
                    out.writeCharacters("\n    ");
                    out.writeStartElement("dependency");
                    
                    out.writeCharacters("\n      ");
                    out.writeStartElement("groupId");
                    out.writeCharacters(mavenCoordinates[0]);
                    out.writeEndElement();

                    out.writeCharacters("\n      ");
                    out.writeStartElement("artifactId");
                    out.writeCharacters(mavenCoordinates[1]);
                    out.writeEndElement();

                    if(!forImport || !mavenCoordinates[0].equals("org.ceylon-lang")){
                        out.writeCharacters("\n      ");
                        out.writeStartElement("version");
                        if(forImport){
                            out.writeCharacters("${");
                            out.writeCharacters(dep.name());
                            out.writeCharacters("}");
                        }else
                            out.writeCharacters(dep.version());
                        out.writeEndElement();
                    }
                    
                    if(dep.optional()){
                        out.writeCharacters("\n      ");
                        out.writeStartElement("optional");
                        out.writeCharacters("true");
                        out.writeEndElement();
                    }
                    
                    out.writeCharacters("\n    ");
                    out.writeEndElement();
                }

                out.writeCharacters("\n  ");
                out.writeEndElement();
            }

            if(forImport){
                out.writeCharacters("\n\n  ");
                out.writeStartElement("build");

                out.writeCharacters("\n    ");
                out.writeStartElement("plugins");

                out.writeCharacters("\n      ");
                out.writeStartElement("plugin");

                out.writeCharacters("\n        ");
                out.writeStartElement("groupId");
                out.writeCharacters("com.coderplus.maven.plugins");
                out.writeEndElement();

                out.writeCharacters("\n        ");
                out.writeStartElement("artifactId");
                out.writeCharacters("copy-rename-maven-plugin");
                out.writeEndElement();

                out.writeCharacters("\n      ");
                out.writeEndElement();

                out.writeCharacters("\n      ");
                out.writeStartElement("plugin");

                out.writeCharacters("\n        ");
                out.writeStartElement("groupId");
                out.writeCharacters("org.codehaus.mojo");
                out.writeEndElement();

                out.writeCharacters("\n        ");
                out.writeStartElement("artifactId");
                out.writeCharacters("build-helper-maven-plugin");
                out.writeEndElement();

                out.writeCharacters("\n      ");
                out.writeEndElement();

                out.writeCharacters("\n    ");
                out.writeEndElement();

                out.writeCharacters("\n  ");
                out.writeEndElement();
            }
            
            out.writeCharacters("\n\n");
            out.writeEndElement();
            out.writeCharacters("\n");
            out.writeEndDocument();
            
            out.flush();
        }
        catch (IOException | XMLStreamException e) {
            throw new RuntimeException(e);
        }
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
