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
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.Annotation;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Package;

public class ModuleDoc extends CeylonDoc {

    private Module module;
    private PackageDoc rootPackageDoc;

    public ModuleDoc(CeylonDocTool tool, Writer writer, Module module) throws IOException {
        super(module, tool, writer);
        this.module = module;
    }

    public void generate() throws IOException {
        writeHeader("Overview");
        writeNavBar();

        open("div class='container-fluid'");
        writeDescription();
        writePackagesTable();
        close("div");

        for (Package pkg : module.getPackages()) {
            if (tool.isRootPackage(module, pkg) && !pkg.getMembers().isEmpty()) {
                rootPackageDoc = new PackageDoc(tool, writer, pkg);
                rootPackageDoc.generate();
            }
        }

        writeFooter();
    }

    @Override
    protected void registerAdditionalKeyboardShortcuts() throws IOException {
        if( rootPackageDoc != null ) {
            rootPackageDoc.registerAdditionalKeyboardShortcuts();
            keyboardShortcuts.putAll(rootPackageDoc.keyboardShortcuts);
        }
    }

    private void writeDescription() throws IOException {
        open("div class='module-description'");

        writeLinkSourceCode(module);
        around("div class='doc section'", getDoc(module, linkRenderer()));

        writeBy(module);
        writeLicense(module);

        close("div");
    }

    private void writeLicense(Module module) throws IOException {
        Annotation annotation = Util.getAnnotation(module.getAnnotations(),"license");
        if (annotation == null)
            return;

        String license = annotation.getPositionalArguments().get(0);
        if (license == null || license.isEmpty())
            return;

        open("div class='license section'");
        around("span class='title'", "License: ");
        around("span class='value'", license);
        close("div");
    }

    private void writePackagesTable() throws IOException {
        List<Package> packages = tool.getPackages(module);
        if (!packages.isEmpty()) {
            openTable("section-packages", "Packages", 2, true);
            for (Package pkg : tool.getPackages(module)) {
                writePackagesTableRow(pkg);
            }
            closeTable();
        }
    }

    private void writePackagesTableRow(Package pkg) throws IOException {
        open("tr");

        open("td");
        writeIcon(pkg);
        if (pkg.getNameAsString().isEmpty()) {
            around("a class='link' href='index.html'", "default package");
        } else {
            around("a class='link' href='" + tool.getObjectUrl(module, pkg) + "'", pkg.getNameAsString());
        }
        close("td");

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