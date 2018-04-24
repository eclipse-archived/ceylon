/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.tools.maven.export;

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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.eclipse.ceylon.cmr.api.ModuleQuery;
import org.eclipse.ceylon.cmr.ceylon.loader.ModuleGraph;
import org.eclipse.ceylon.cmr.ceylon.loader.ModuleGraph.Module;
import org.eclipse.ceylon.cmr.impl.ShaSigner;
import org.eclipse.ceylon.common.FileUtil;
import org.eclipse.ceylon.common.ModuleSpec;
import org.eclipse.ceylon.common.ModuleUtil;
import org.eclipse.ceylon.common.Versions;
import org.eclipse.ceylon.common.tool.Argument;
import org.eclipse.ceylon.common.tool.Description;
import org.eclipse.ceylon.common.tool.Option;
import org.eclipse.ceylon.common.tool.OptionArgument;
import org.eclipse.ceylon.common.tool.Summary;
import org.eclipse.ceylon.common.tool.ToolUsageError;
import org.eclipse.ceylon.model.cmr.ArtifactResult;
import org.eclipse.ceylon.model.cmr.RepositoryException;
import org.eclipse.ceylon.tools.moduleloading.ModuleLoadingTool;

@Summary("Generate a Maven repository for a given module")
@Description("Generate Maven repository which contains the given module and all its run-time"
        + " dependencies, including the Ceylon runtime, which makes that repository usable by"
        + " Maven as a regular Maven repository.\n\n"
        + "Alternatively, running with `--for-import` creates a special repository set up suitable"
        + " for importing the Ceylon distribution into Maven Central. This is mostly useful for the"
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
    private boolean forSdkImport;

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

    @Option(longName="for-sdk-import")
    @Description("Special set up to create a set of folders to import the Ceylon SDK.")
    public void setForSdkImport(boolean forSdkImport) {
        this.forSdkImport = forSdkImport;
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
        String firstModuleName = null;
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
            }
            System.err.println("Doing "+module);
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
        final Set<String> directImports = new HashSet<>();
        loader.visitModules(new ModuleGraph.Visitor() {
            @Override
            public void visit(ModuleGraph.Module module) {
                if(module.artifact == null || module.artifact.artifact() == null)
                    return;
                // FIXME: skip Maven modules?
                if(forImport || forSdkImport){
                    if(forImport && !module.artifact.groupId().equals("org.ceylon-lang")){
                        externalDependencies.add(module.artifact);
                        return;
                    }
                    if(forSdkImport && !directlyListed(module.artifact.name())){
                        if(!module.artifact.groupId().equals("org.ceylon-lang")){
                            externalDependencies.add(module.artifact);
                        }
                        return;
                    }
                    makeMavenImportFolder(module, outputFolder, directImports);
                    writtenModules.add(module.artifact);
                }else
                    makeMavenModule(module, outputFolder, directImports);
            }
        });
        if(forImport || forSdkImport){
            makeMavenImportSpecialFolders(writtenModules, externalDependencies, outputFolder, directImports);
        }
        flush();
    }

    private void makeMavenImportSpecialFolders(List<ArtifactResult> writtenModules, 
            List<ArtifactResult> externalDependencies, File outputFolder, Set<String> directImports) {
        if(forImport){
            File pomAllFile = makePomFile(outputFolder, "ceylon-all");
            generatePomForAll(pomAllFile, writtenModules, directImports);

            File pomSystemFile = makePomFile(outputFolder, "ceylon-system");
            generatePomForSystem(pomSystemFile, writtenModules, directImports);

            File pomCompleteFile = makePomFile(outputFolder, "ceylon-complete");
            generatePomForComplete(pomCompleteFile, writtenModules, externalDependencies);

            File pomPartsFile = new File(outputFolder, "pom-parts-distrib.xml");
            generatePomParts(pomPartsFile, writtenModules, externalDependencies, directImports);
        }else if(forSdkImport){
            File pomSdkFile = makePomFile(outputFolder, "ceylon-sdk");
            generatePomForSdk(pomSdkFile, writtenModules, directImports);

            File pomPartsFile = new File(outputFolder, "pom-parts-sdk.xml");
            generatePomParts(pomPartsFile, writtenModules, externalDependencies, directImports);
        }
    }

    private File makePomFile(File outputFolder, String allArtifactId) {
        File folder = new File(outputFolder, allArtifactId);
        FileUtil.mkdirs(folder);
        return new File(folder, "pom.xml");
    }

    private void generatePomForAll(File pomFile, List<ArtifactResult> writtenModules,
            Set<String> directImports) {
        try (OutputStream os = new FileOutputStream(pomFile)){
            XMLStreamWriter out = XMLOutputFactory.newInstance().createXMLStreamWriter(
                    new OutputStreamWriter(os, "utf-8"));

            // FIXME: what to do with the default module?
            
            writePomHeader(out);

            writePomParent(out, writtenModules.get(0));

            writeElement(out, "artifactId", "ceylon-all");
            writeNewline(out);
            writeElement(out, "name", "ceylon-all");

            writePomDependencies(out, writtenModules, directImports);

            writePomFooter(out);
        }
        catch (IOException | XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    private void generatePomForSdk(File pomFile, List<ArtifactResult> writtenModules,
            Set<String> directImports) {
        try (OutputStream os = new FileOutputStream(pomFile)){
            XMLStreamWriter out = XMLOutputFactory.newInstance().createXMLStreamWriter(
                    new OutputStreamWriter(os, "utf-8"));

            // FIXME: what to do with the default module?
            
            writePomHeader(out);

            writePomParent(out, writtenModules.get(0));

            writeElement(out, "artifactId", "ceylon-sdk");
            writeNewline(out);
            writeElement(out, "name", "ceylon-sdk");

            writePomDependencies(out, writtenModules, directImports);

            writePomFooter(out);
        }
        catch (IOException | XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    private void generatePomForSystem(File pomFile, List<ArtifactResult> writtenModules,
            Set<String> directImports) {
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

            writePomDependencies(out, writtenModules, directImports);

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

    private void generatePomParts(File pomFile, List<ArtifactResult> writtenModules, 
            List<ArtifactResult> externalDependencies, Set<String> directImports) {
        try (OutputStream os = new FileOutputStream(pomFile)){
            XMLStreamWriter out = XMLOutputFactory.newInstance().createXMLStreamWriter(
                    new OutputStreamWriter(os, "utf-8"));

            // FIXME: what to do with the default module?
            
            writePomHeader(out);

            writePomParent(out, writtenModules.get(0));

            writeElement(out, "artifactId", "ceylon-parts");
            writeNewline(out);
            writeElement(out, "name", "ceylon-parts");
            writeNewline(out);

            writeOpen(out, "properties");
            {
                SortedMap<String, String> props = new TreeMap<>();
                for (ArtifactResult dep : externalDependencies) {
                    String name = getDependencyPropertyName(dep.name());
                    if(directImports.contains(name))
                        props.put(name, dep.version());
                }
                for (Entry<String, String> entry : props.entrySet()) {
                    writeElement(out, entry.getKey(), entry.getValue());
                }
            }
            writeClose(out);
            writeNewline(out);
            
            writeOpen(out, "modules");
            {
                List<ArtifactResult> sortedImports = new ArrayList<>(writtenModules);
                Collections.sort(sortedImports, ImportComparator);
                for(ArtifactResult dep : sortedImports){
                    String dependencyName = dep.name();
                    writeElement(out, "module", dependencyName);
                }
                if(forImport){
                    writeElement(out, "module", "ceylon-all");
                    writeElement(out, "module", "ceylon-complete");
                    writeElement(out, "module", "ceylon-system");
                }else{
                    writeElement(out, "module", "ceylon-sdk");
                }
            }
            writeClose(out);
            writeNewline(out);

            writeOpen(out, "dependencyManagement");
            {
                writePomDependencies(out, writtenModules, directImports, true);
            }
            writeClose(out);
            
            writeNewline(out);

            writePomFooter(out);
        }
        catch (IOException | XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    protected void makeMavenImportFolder(Module module, File outputFolder, Set<String> directImports) {
        File pomFile = makePomFile(outputFolder, module.artifact.artifactId());
        generatePomFromModule(pomFile, module.artifact, directImports);
    }

    protected void makeMavenModule(Module module, File outputFolder, Set<String> directImports) {
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
                    generatePomFromModule(pomFile, module.artifact, directImports);
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

    private void generatePomFromModule(File pomFile, ArtifactResult artifact,
            Set<String> directImports) {
        try (OutputStream os = new FileOutputStream(pomFile)){
            XMLStreamWriter out = XMLOutputFactory.newInstance().createXMLStreamWriter(
                    new OutputStreamWriter(os, "utf-8"));

            // FIXME: what to do with the default module?
            
            writePomHeader(out);

            if(forImport || forSdkImport){
                writePomParent(out, artifact);
            }else{
                writeElement(out, "groupId", artifact.groupId());
            }

            writeElement(out, "artifactId", artifact.artifactId());
            writeNewline(out);
            writeElement(out, "name", artifact.name());


            if(forImport || forSdkImport){
                writeElement(out, "packaging", "jar");
                writeNewline(out);
                
                writeOpen(out, "properties");
                {
                    String root = forImport 
                            ? "${ceylon.home}/repo/"
                            : "${ceylon.sdk}/";
                    writeElement(out, "jarFile", root+artifact.name().replace('.', '/')
                            +"/${ceylon.version}/"+artifact.name()+"-${ceylon.version}."
                            +(artifact.artifact().getName().endsWith(".jar") ? "jar" : "car"));

                    writeElement(out, "sourcesFile", root+artifact.name().replace('.', '/')
                            +"/${ceylon.version}/"+artifact.name()+"-${ceylon.version}.src");
                }
                writeClose(out);
            }else{
                writeElement(out, "version", artifact.version());
            }

            List<ArtifactResult> imports = artifact.dependencies();
            writePomDependencies(out, imports, directImports);

            if(forImport || forSdkImport){
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

    private boolean directlyListed(String name){
        for (ModuleSpec spec : modules) {
            if(spec.getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    private void writePomDependencies(XMLStreamWriter out, List<ArtifactResult> imports,
            Set<String> directImports) throws XMLStreamException {
        writePomDependencies(out, imports, directImports, false);
    }
    
    private void writePomDependencies(XMLStreamWriter out, List<ArtifactResult> imports, 
            Set<String> directImports, boolean forceVersion) throws XMLStreamException {
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
                        mavenCoordinates[2] = moduleArtifact.classifier();
                    }

                    writeOpen(out, "dependency");
                    {
                        writeElement(out, "groupId", mavenCoordinates[0]);
                        writeElement(out, "artifactId", mavenCoordinates[1]);
                        if (mavenCoordinates[2]!=null) {
                            writeElement(out, "classifier", mavenCoordinates[2]);
                        }

                        if(forceVersion
                                || !(forImport || forSdkImport) 
                                || !mavenCoordinates[0].equals("org.ceylon-lang")){
                            String version;
                            if(forceVersion){
                                version = "${ceylon.version}";
                            }else if(forImport || forSdkImport){
                                version = "${";
                                String name = getDependencyPropertyName(dep.name());
                                directImports.add(name);
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

    private String getDependencyPropertyName(String name) {
        if(name.startsWith("org.eclipse.ceylon.aether."))
            return "org.eclipse.ceylon.aether";
        else
            return name.replace(':', '.');
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
        return super.shouldExclude(moduleName, version) 
            || this.excludedModules.contains(moduleName);
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
