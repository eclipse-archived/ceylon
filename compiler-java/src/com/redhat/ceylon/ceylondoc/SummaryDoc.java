package com.redhat.ceylon.ceylondoc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Modules;
import com.redhat.ceylon.compiler.typechecker.model.Package;

public class SummaryDoc extends CeylonDoc {

    private Modules modules;

    public SummaryDoc(String destDir, Modules modules) throws IOException {
        super(destDir);
        this.modules = modules;
    }

    public void generate() throws IOException {
        setupWriter();
        open("html");
        open("head");
        around("title", "Overview");
        tag("link href='style.css' rel='stylesheet' type='text/css'");
        close("head");
        open("body");
        overview();
        packages();
        close("body");
        close("html");
        writer.flush();
        writer.close();
    }

    private void overview() throws IOException {
        open("div class='nav'");
        open("div class='selected'");
        write("Overview");
        close("div");
        open("div");
        write("Package");
        close("div");
        open("div");
        write("Class");
        close("div");
        close("div");
    }

    private void packages() throws IOException {
        openTable("Packages", "Package", "Description");
        for(Package pkg : getPackages()){
            doc(pkg);
        }
        close("table");
    }

    private List<Package> getPackages(){
        List<Package> packages = new ArrayList<Package>();
        for(Module m : modules.getListOfModules()){
            for(Package pkg : m.getPackages()){
                packages.add(pkg);
            }
        }
        Collections.sort(packages, new Comparator<Package>(){
            @Override
            public int compare(Package a, Package b) {
                return a.getNameAsString().compareTo(b.getNameAsString());
            }
            
        });
        return packages;
    }

    private void doc(Package c) throws IOException {
        open("tr class='TableRowColor'");
        open("td");
        if(c.getNameAsString().isEmpty())
            around("a href='index.html'", "default package");
        else
            around("a href='"+join("/",c.getQualifiedName())+"/index.html'", c.getNameAsString());
        close("td");
        open("td");
        write(c.getNameAsString());
        close("td");
        close("tr");
    }

    @Override
    protected String getPathToBase() {
        return "";
    }

    @Override
    protected File getOutputFile() {
        return new File(destDir, "overview-summary.html");
    }
}
