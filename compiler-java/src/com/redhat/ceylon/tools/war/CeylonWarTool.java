package com.redhat.ceylon.tools.war;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

import com.redhat.ceylon.cmr.ceylon.OutputRepoUsingTool;
import com.redhat.ceylon.cmr.impl.IOUtils;
import com.redhat.ceylon.common.ModuleUtil;
import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.RemainingSections;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tool.ToolUsageError;
import com.redhat.ceylon.common.tools.CeylonTool;
import com.redhat.ceylon.model.cmr.ArtifactResult;
import com.redhat.ceylon.tools.moduleloading.ModuleLoadingTool;

@Summary("Generates a WAR file")
@Description("Generates a WAR file for the given module, suitable for " + 
		"deploying to a standard Servlet container.")
@RemainingSections( "## Overriding web.xml\n\n" +
		"If you provide a custom `WEB-INF/web.xml` file in your WAR " +
		"resource-root, you'll need to include the listener " + 
		"(ceylon.war.WarInitializer) that is " +
		"set in the default web.xml. Without that listener, the " + 
		"metamodel will not be properly initialized.\n\n" + 
		OutputRepoUsingTool.DOCSECTION_REPOSITORIES)
public class CeylonWarTool extends ModuleLoadingTool {

	static final String WAR_MODULE = "com.redhat.ceylon.war";
	
    @Argument(argumentName="module", multiplicity = "1")
    public void setModule(String module) {
        this.moduleNameOptVersion = module;
    }

    @OptionArgument(shortName='o', argumentName="dir")
    @Description("Sets the output directory for the WAR file (default: .)")
    public void setOut(String out) {
    	this.out = out;
    }
    
    @OptionArgument(shortName='n', argumentName="name")
    @Description("Sets name of the WAR file (default: moduleName-version.war)")
    public void setName(String name) {
    	this.name = name;
    }
    
    @OptionArgument(shortName='R', argumentName="folder-name")
    @Description("Sets the special resource folder name whose files will " +
            "end up in the root of the resulting WAR file (no default).")
    public void setResourceRoot(String root) {
    	this.resourceRoot = root;
    }
    
    @OptionArgument(argumentName="moduleOrFile", shortName='x')
    @Description("Excludes modules from the WAR file. Can be a module name or " + 
    		"a file containing module names. Can be specified multiple times. Note that "+
            "this excludes the module from the WAR file, but if your modules require that "+
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
    				throw new ToolUsageError(CeylonWarMessages.msg("exclude.file.failure", each), 
    						e);
    			}
    		} else {
    			this.excludedModules.add(each);
    		}
    	}
    }
    
	@Override
	public void run() throws Exception {
		final String moduleName = ModuleUtil.moduleName(this.moduleNameOptVersion);
		final String moduleVersion = moduleVersion(this.moduleNameOptVersion);
		final Properties properties = new Properties();
		
		if (!loadModule(moduleName, moduleVersion) ||
				!loadModule(WAR_MODULE, Versions.CEYLON_VERSION_NUMBER)) {
			throw new ToolUsageError(CeylonWarMessages.msg("abort.missing.modules"));
		}
		
		addLibEntries();
		
		properties.setProperty("moduleName", moduleName);
		properties.setProperty("moduleVersion", moduleVersion);
		
		addSpec(new PropertiesEntrySpec(properties, "META-INF/module.properties"));	
		
		if (!addResources(entrySpecs)) {
			debug("adding.entry", "default web.xml");
			addSpec(new URLEntrySpec(CeylonWarTool.class
							.getClassLoader()
							.getResource("com/redhat/ceylon/tools/war/resources/default-web.xml"),
							"WEB-INF/web.xml"));
		}
		
		if (this.name == null) {
			this.name = String.format("%s-%s.war", moduleName, moduleVersion);
			debug("default.name", this.name);
		}
		
		final File jarFile = applyCwd(this.out == null ? new File(this.name) : new File(this.out, this.name));
		writeJarFile(jarFile);
				
		append(CeylonWarMessages.msg("archive.created", moduleName, moduleVersion, jarFile.getAbsolutePath()));
		newline();
	}

	@Override
	protected boolean shouldExclude(String moduleName) {
		return super.shouldExclude(moduleName) ||
				this.excludedModules.contains(moduleName);
	}

	@Override
	public void initialize(CeylonTool mainTool) throws Exception {
	}
	
	protected void addSpec(EntrySpec spec) {
		debug("adding.entry", spec.name);
		this.entrySpecs.add(spec);
	}
	
	protected void debug(String key, Object... args) {
		if (this.verbose != null &&
				!this.verbose.equals("loader")) {
			try {
				append("Debug: ").append(CeylonWarMessages.msg(key, args)).newline();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	// returns true if a web.xml was added
	protected boolean addResources(List<EntrySpec> entries) throws MalformedURLException {
		if (this.resourceRoot != null) {
			final File root = applyCwd(new File(this.resourceRoot));
			if (!root.exists()) {
				throw new ToolUsageError(CeylonWarMessages.msg("resourceRoot.missing", root.getAbsolutePath()));
			}
			if (!root.isDirectory()) {
				throw new ToolUsageError(CeylonWarMessages.msg("resourceRoot.nondir", root.getAbsolutePath()));
			}
			debug("adding.resources", root.getAbsolutePath());
			
			return addResources(root, "", entries);
		}
		
		return false;
	}
	
	// returns true if a web.xml was added
	protected boolean addResources(File root, String prefix, List<EntrySpec> entries) throws MalformedURLException {
		boolean webXmlAdded = false;
		for (File f : root.listFiles()) {
			if (f.isDirectory()) {
				webXmlAdded = webXmlAdded || addResources(f, prefix + f.getName() + "/", entries);
			} else {
				addSpec(new URLEntrySpec(f.toURI().toURL(), prefix + f.getName()));
				
				if (f.getName().equals("web.xml") && 
						prefix.equals("WEB-INF/")) {
					debug("found.webxml");
					webXmlAdded = true;
				}
			}
		}
		
		return webXmlAdded;
	}
	
	protected void addLibEntries() throws MalformedURLException { 
		final List<String> libs = new ArrayList<>();

		for (Map.Entry<String, ArtifactResult> entry : this.loadedModules.entrySet()) {
		        ArtifactResult module = entry.getValue();
			if (module == null) {
				// it's an optional, missing module (likely java.*) 
				continue;
			}
			
			final File artifact = module.artifact();
			final String moduleName = entry.getKey();

			// use "-" for the version separator
			// use ".jar" so they'll get loaded by the container classloader
			final String name = ModuleUtil.moduleName(moduleName)
									+ "-" + ModuleUtil.moduleVersion(moduleName) + ".jar";
			if (name.contains("/") || name.contains("\\") || name.length() == 0) {
				throw new ToolUsageError(CeylonWarMessages.msg("module.name.illegal", name));
			}

			addSpec(new URLEntrySpec(artifact.toURI().toURL(),
							"WEB-INF/lib/" + name));
			libs.add(name);
		}

		// store the list of added libs so the WarInitializer knows what to copy out
		// to a repo if one has to be created
		final StringBuffer libList = new StringBuffer();
		for (String lib : libs) {
			libList.append(lib).append("\n");
		}
		addSpec(new StringEntrySpec(libList.toString(), "META-INF/libs.txt"));
	}
	
	protected void writeJarFile(File jarFile) throws IOException {
		try (JarOutputStream out = 
				new JarOutputStream(new 
						BufferedOutputStream(new 
								FileOutputStream(jarFile)))) {
			for (EntrySpec entry : entrySpecs) {
				entry.write(out);
			}
		}
	}
	
	abstract class EntrySpec {
		EntrySpec(final String name) {
			this.name = name;
		}
		
		void write(final JarOutputStream out) throws IOException {
			out.putNextEntry(new ZipEntry(this.name));
			IOUtils.copyStream(openStream(), out, true, false);
		}
		
		abstract InputStream openStream() throws IOException;
		
		final protected String name; 
	}
	
	class URLEntrySpec extends EntrySpec {
		URLEntrySpec(final URL url, final String name) {
			super(name);
			this.url = url;
		}
		
		InputStream openStream() throws IOException {
			return this.url.openStream();
		}
		
		final private URL url;
	}
	
	class StringEntrySpec extends EntrySpec {
		StringEntrySpec(final String content, final String name) {
			super(name);
			this.content = content;
		}
		
		InputStream openStream() throws IOException {
			return new ByteArrayInputStream(this.content.getBytes());
		}
	
		final private String content;
	}

	class PropertiesEntrySpec extends EntrySpec {

		PropertiesEntrySpec(final Properties properties, final String name) {
			super(name);
			this.properties = properties;
		}
		
		void write(final JarOutputStream out) throws IOException {
			out.putNextEntry(new ZipEntry(this.name));
			this.properties.store(out, "");
		}
		
		InputStream openStream() throws IOException {
			//unused
			return null;
		}
		
		final private Properties properties;
	}
	
	private String moduleNameOptVersion;
	private final List<EntrySpec> entrySpecs = new ArrayList<>();
	private final List<String> excludedModules = new ArrayList<>();
	private String out = null;
	private String name = null;
	private String resourceRoot = null;
}
