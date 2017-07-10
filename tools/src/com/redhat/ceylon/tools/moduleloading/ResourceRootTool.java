package com.redhat.ceylon.tools.moduleloading;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

import com.redhat.ceylon.cmr.impl.IOUtils;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.OptionArgument;

public abstract class ResourceRootTool extends ModuleLoadingTool {

    private String resourceRoot;
    private final List<EntrySpec> entrySpecs = new ArrayList<>();
    
    @OptionArgument(shortName='R', argumentName="directory")
    @Description("Sets the special resource directory whose files will " +
            "end up in the root of the resulting WAR file (default: web-content).")
    public void setResourceRoot(String root) {
        this.resourceRoot = root;
    }
    
    protected List<EntrySpec> getEntrySpecs() {
        return entrySpecs;
    }
    
    /** 
     * Copies resources from the {@link #resourceRoot} to the WAR.
     */
    protected void addResources() throws MalformedURLException {
        final File root;
        if (this.resourceRoot == null) {
            File defaultRoot = applyCwd(new File("web-content"));
            if (!defaultRoot.exists()) {
                return;
            }
            root = defaultRoot;
        } else {
            root = applyCwd(new File(this.resourceRoot));
        }
        if (!root.exists()) {
            usageError("resourceRoot.missing", root.getAbsolutePath());
        }
        if (!root.isDirectory()) {
            usageError("resourceRoot.nondir", root.getAbsolutePath());
        }
        debug("adding.resources", root.getAbsolutePath());
        
        addResources(root, "");
    }
    
    private void addResources(File root, String prefix) throws MalformedURLException {
        for (File f : root.listFiles()) {
            if (f.isDirectory()) {
                addResources(f, prefix + f.getName() + "/");
            } else {
                addSpec(new URLEntrySpec(f.toURI().toURL(), prefix + f.getName()));
            }
        }
    }
    
    protected void addSpec(EntrySpec spec) {
        debug("adding.entry", spec.name);
        this.entrySpecs.add(spec);
    }
    
    protected abstract void debug(String key, Object... args);
    
    protected abstract void usageError(String key, Object... args);
    
    protected abstract class EntrySpec {
        EntrySpec(final String name) {
            this.name = name;
        }
        
        public void write(final JarOutputStream out) throws IOException {
            out.putNextEntry(new ZipEntry(this.name));
            IOUtils.copyStream(openStream(), out, true, false);
        }
        
        abstract InputStream openStream() throws IOException;
        
        final String name; 
    }
    
    protected class URLEntrySpec extends EntrySpec {
        public URLEntrySpec(final URL url, final String name) {
            super(name);
            this.url = url;
        }
        
        InputStream openStream() throws IOException {
            return this.url.openStream();
        }
        
        final private URL url;
    }
    
    protected class StringEntrySpec extends EntrySpec {
        public StringEntrySpec(final String content, final String name) {
            super(name);
            this.content = content;
        }
        
        InputStream openStream() throws IOException {
            return new ByteArrayInputStream(this.content.getBytes());
        }
    
        final private String content;
    }

    protected class PropertiesEntrySpec extends EntrySpec {

        public PropertiesEntrySpec(final Properties properties, final String name) {
            super(name);
            this.properties = properties;
        }
        
        public void write(final JarOutputStream out) throws IOException {
            out.putNextEntry(new ZipEntry(this.name));
            this.properties.store(out, "");
        }
        
        InputStream openStream() throws IOException {
            //unused
            return null;
        }
        
        final private Properties properties;
    }
    
}
