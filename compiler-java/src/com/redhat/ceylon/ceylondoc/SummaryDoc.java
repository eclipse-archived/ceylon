/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

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
        htmlHead();
        overview();
        packages();
        close("body");
        close("html");
        writer.flush();
        writer.close();
    }

    private void htmlHead() throws IOException {
        open("html");
        open("head");
        around("title", "Overview");
        tag("link href='style.css' rel='stylesheet' type='text/css'");
        close("head");
        open("body");
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
        String srcUrl = getSrcUrl(module);
        if (!tool.isOmitSource()
                && srcUrl != null) {
            open("div class='source-code module'");
            around("a href='" + srcUrl + "'", "Source Code");
            close("div");
        }
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
    protected String getObjectUrl(Object to) throws IOException {
        return tool.getObjectUrl(module, to);
    }
    
    @Override
    protected String getResourceUrl(String to) throws IOException {
        return tool.getResourceUrl(module, to);
    }
    
    @Override
    protected String getSrcUrl(Object to) throws IOException {
        return tool.getSrcUrl(module, to);
    }
}
