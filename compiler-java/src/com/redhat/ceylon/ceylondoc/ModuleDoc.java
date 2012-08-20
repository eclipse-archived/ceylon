/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

package com.redhat.ceylon.ceylondoc;

import static com.redhat.ceylon.ceylondoc.Util.getDoc;

import java.io.IOException;
import java.io.Writer;

import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Package;

public class ModuleDoc extends CeylonDoc {

    private Module module;

    public ModuleDoc(DocTool tool, Writer writer, Module module) throws IOException {
        super(module, tool, writer);
        this.module = module;
    }
    
    public void generate() throws IOException {
        htmlHead();
        overview();
        summary();
        packages();
    }
    
    public void complete() throws IOException {
        close("body");
        close("html");
    }

    private void htmlHead() throws IOException {
        htmlHead("Overview");
    }

    private void overview() throws IOException {
        writeNav(module, module, DocType.MODULE);
    }

    private void summary() throws IOException {
        open("div class='head summary'");
        open("h1 id='section-module'");
        write("Module ");
        around("code", module.getNameAsString());
        close("h1");
        writeSourceLink(module);
        close("div");
        
        around("div class='doc'", getDoc(module, linkRenderer()));
        
        writeBy(Util.getAuthors(module), false);
    }

    private void packages() throws IOException {
        openTable("section-packages", "Packages", "Package", "Description");
        for (Package pkg : tool.getPackages(module)) {
            doc(pkg);
        }
        close("table");
    }

    private void doc(Package pkg) throws IOException {
        open("tr class='TableRowColor'");
        open("td", "code");
        if (pkg.getNameAsString().isEmpty()) {
            around("a href='index.html'", "default package");
        } else {
            around("a href='" + tool.getObjectUrl(module, pkg) + "'", pkg.getNameAsString());
        }
        close("code", "td");
        open("td");
        write(Util.getDocFirstLine(pkg, linkRenderer()));
        close("td");
        close("tr");
    }

    @Override
    protected Object getFromObject() {
        return module;
    }

}
