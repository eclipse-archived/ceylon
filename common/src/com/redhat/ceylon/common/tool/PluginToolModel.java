package com.redhat.ceylon.common.tool;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.redhat.ceylon.common.tools.ModuleSpec;

import static com.redhat.ceylon.common.tools.ModuleSpec.Option.*;

public class PluginToolModel<T extends Tool> extends AnnotatedToolModel<T> {
    private final String pluginPath;

    private final Properties pluginProperties;
    private final String pluginSummary;
    private final ModuleSpec pluginModule;
    private final String pluginClassName;
    private final boolean pluginHidden;
    
    public PluginToolModel(String name, String pluginPath) {
        super(name);
        this.pluginPath = pluginPath;

        pluginProperties = new Properties();
        try (InputStream is = new FileInputStream(pluginPath)) {
            pluginProperties.load(is);
        } catch (IOException e) {
            throw new ModelException("Could not load tool plugin file for '" + name + "'", e);
        }
        pluginSummary = pluginProperties.getProperty("summary", "");
        String module = pluginProperties.getProperty("module");
        if (module == null || module.isEmpty()) {
            throw new ModelException("Missing 'module' property in tool plugin file for '" + name + "'");
        }
        pluginModule = ModuleSpec.parse(module, VERSION_REQUIRED);
        pluginClassName = pluginProperties.getProperty("class", getDefaultToolClassName(pluginModule.getName(), name));
        pluginHidden = "true".equals(pluginProperties.getProperty("hidden", ""));
    }

    private static String getDefaultToolClassName(String module, String name) {
        String[] parts = name.split("-");
        StringBuilder newName = new StringBuilder();
        for (String part : parts) {
            if (!part.isEmpty()) {
                newName.append(Character.toUpperCase(part.charAt(0)));
                if (part.length() > 1) {
                    newName.append(part.substring(1).toLowerCase());
                }
            }
        }
        String className = "Ceylon" + newName.toString() + "Tool";
        if (!"default".equals(module)) {
            className = module + "." + className;
        }
        return className;
    }
    
    public String getToolPath() {
        return pluginPath;
    }
    
    @Override
    public boolean isHidden() {
        return pluginHidden || super.isHidden();
    }

    @Override
    public boolean isPlumbing() {
        return false;
    }

    public String getToolSummary() {
        return pluginSummary;
    }

    public ModuleSpec getToolModule() {
        return pluginModule;
    }

    public String getToolClassName() {
        return pluginClassName;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<T> getToolClass() {
        Class<T> tc = super.getToolClass();
        if (tc == null) {
            ClassLoader mcl = getToolLoader().loadModule(pluginModule.getName(), pluginModule.getVersion());
            try {
                tc = (Class<T>) mcl.loadClass(pluginClassName);
            } catch (ClassNotFoundException e) {
                throw new ModelException("Could not load plugin class '" + pluginClassName + "'", e);
            }
            getToolLoader().checkClass(tc);
            setToolClass(tc);
        }
        return tc;
    }
    
    
}
