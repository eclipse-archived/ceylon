package com.redhat.ceylon.ceylondoc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Package;

public class SummaryDoc extends CeylonDoc {

    private Module module;

    public SummaryDoc(CeylonDocTool tool, Module module) throws IOException {
        super(module, tool, tool.getObjectFile(module));
        this.module = module;
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
        open("div");
        write(module.getNameAsString() + "/" + module.getVersion());
        close("div");
        close("div");
    }

    private void packages() throws IOException {
        openTable("Packages", "Package", "Description");
        for (Package pkg : getPackages()) {
            doc(pkg);
        }
        close("table");
    }

    private List<Package> getPackages() {
        List<Package> packages = new ArrayList<Package>();
        for (Package pkg : module.getPackages()) {
            if (pkg.getMembers().size() > 0)
                packages.add(pkg);
        }
        Collections.sort(packages, new Comparator<Package>() {
            @Override
            public int compare(Package a, Package b) {
                return a.getNameAsString().compareTo(b.getNameAsString());
            }

        });
        return packages;
    }

    private void doc(Package c) throws IOException {
        open("tr class='TableRowColor'");
        open("td", "code");
        if (c.getNameAsString().isEmpty())
            around("a href='index.html'", "default package");
        else
            around("a href='" + join("/", c.getName()) + "/index.html'", c.getNameAsString());
        close("code", "td");
        open("td");
        write(c.getNameAsString());
        close("td");
        close("tr");
    }


    @Override
    protected String getObjectUrl(Object to) {
        return getObjectUrl(module, to);
    }
    
    @Override
    protected String getResourceUrl(String to) {
        return getResourceUrl(module, to);
    }
}
