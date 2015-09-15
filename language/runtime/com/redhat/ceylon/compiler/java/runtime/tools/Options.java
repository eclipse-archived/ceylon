package com.redhat.ceylon.compiler.java.runtime.tools;

import java.util.LinkedList;
import java.util.List;

public class Options {
    private String systemRepository;
    private List<String> userRepositories = new LinkedList<String>();
    private boolean offline;
    private boolean verbose;
    private String verboseCategory;
    private boolean noDefaultRepositories;
    private String run;
    private String overrides;
    
    public String getSystemRepository() {
        return systemRepository;
    }
    public void setSystemRepository(String systemRepository) {
        this.systemRepository = systemRepository;
    }
    
    public List<String> getUserRepositories() {
        return userRepositories;
    }
    public void setUserRepositories(List<String> userRepositories) {
        this.userRepositories = userRepositories;
    }
    public void addUserRepository(String userRepository){
        userRepositories.add(userRepository);
    }
    
    public boolean isOffline() {
        return offline;
    }
    public void setOffline(boolean offline) {
        this.offline = offline;
    }
    
    public boolean isVerbose() {
        return verbose;
    }
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
        if(verbose){
            if(verboseCategory == null)
                verboseCategory = "all";
        }else{
            verboseCategory = null;
        }
    }

    public String getVerboseCategory() {
        return verboseCategory;
    }
    public void setVerboseCategory(String verboseCategory) {
        if(verboseCategory == null)
            verbose = false;
        else{
            verbose = true;
            if(verboseCategory.isEmpty())
                verboseCategory = "all";
        }
        this.verboseCategory = verboseCategory;
    }
    
    public boolean isVerbose(String category){
        String categories = getVerboseCategory();
        if(categories == null)
            return false;
        for(String cat : categories.split(",")){
            if(cat.equals(category))
                return true;
            if(cat.equals("all"))
                return true;
        }
        return false;
    }
    
    public boolean isNoDefaultRepositories() {
        return noDefaultRepositories;
    }
    public void setNoDefaultRepositories(boolean noDefaultRepositories) {
        this.noDefaultRepositories = noDefaultRepositories;
    }
    
    public String getRun() {
        return run;
    }
    public void setRun(String run) {
        this.run = run;
    }

    public String getOverrides() {
        return overrides;
    }
    public void setOverrides(String overrides) {
        this.overrides = overrides;
    }
}
