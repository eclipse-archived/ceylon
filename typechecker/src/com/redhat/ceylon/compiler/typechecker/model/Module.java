package com.redhat.ceylon.compiler.typechecker.model;

import java.util.ArrayList;
import java.util.List;

public class Module {

    private List<String> name;
	private String version;
    private List<Package> packages = new ArrayList<Package>();
    private List<Module> dependencies = new ArrayList<Module>();
    private Module languageModule;
    private boolean available;
    private String license;
    private String doc;
    
    /**
     * Whether or not the module is available in the
     * source path or the repository
     */
    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public List<String> getName() {
        return name;
    }

    public void setName(List<String> name) {
        this.name = name;
    }

    public List<Package> getPackages() {
        return packages;
    }

    public List<Module> getDependencies() {
        return dependencies;
    }
    
    public Module getLanguageModule() {
        return languageModule;
    }
    
    public void setLanguageModule(Module languageModule) {
        this.languageModule = languageModule;
    }

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public List<Package> getAllPackages() {
        List<Package> list = new ArrayList<Package>();
        list.addAll(packages);
        for (Module m : dependencies) {
            list.addAll(m.getPackages());
        }
        return list;
    }

	public Package getPackage(String name) {
        for (Package pkg: getAllPackages()) {
            if ( pkg.getQualifiedNameString().equals(name) ) {
                return pkg;
            }
        }
        return null;
	}
	
	public String getNameAsString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < name.size(); i++) {
            sb.append(name.get(i));
            if (i < name.size() - 1) {
                sb.append('.');
            }
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Module[" + getNameAsString() + "]";
    }
    
    public String getDoc() {
		return doc;
	}
    
    public void setDoc(String doc) {
		this.doc = doc;
	}
    
    public String getLicense() {
		return license;
	}
    
    public void setLicense(String license) {
		this.license = license;
	}
    
}
