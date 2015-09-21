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
import static com.redhat.ceylon.ceylondoc.Util.isEmpty;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.redhat.ceylon.ceylondoc.Util.ModuleImportComparatorByName;
import com.redhat.ceylon.model.loader.AbstractModelLoader;
import com.redhat.ceylon.model.typechecker.model.Annotation;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.ModuleImport;
import com.redhat.ceylon.model.typechecker.model.Package;

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
        writePackagesTable("Packages", tool.getPackages(module));
        writeDependencies();
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
        String doc = getDoc(module, linkRenderer());
        if (isEmpty(doc)) {
            tool.warningMissingDoc(module.getNameAsString(), module);
        }
        around("div class='doc section'", doc);

        writeAnnotations(module);
        writeBy(module);
        writeLicense(module);

        close("div");
    }

    private void writeLicense(Module module) throws IOException {
        Annotation annotation = Util.getAnnotation(module.getUnit(), module.getAnnotations(),"license");
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
    
    private void writeDependencies() throws IOException {
        List<ModuleImport> moduleImports = new ArrayList<ModuleImport>(module.getImports());

        Iterator<ModuleImport> moduleImportIterator = moduleImports.listIterator();
        while (moduleImportIterator.hasNext()) {
            ModuleImport moduleImport = moduleImportIterator.next();
            if (moduleImport.getModule().getNameAsString().equals(AbstractModelLoader.CEYLON_LANGUAGE)) {
                moduleImportIterator.remove();
            }
        }

        Collections.sort(moduleImports, ModuleImportComparatorByName.INSTANCE);

        if (!moduleImports.isEmpty()) {
            openTable("section-dependencies", "Dependencies", 3, false);
            for (ModuleImport moduleImport : moduleImports) {
                writeDependencyRow(moduleImport);
            }
            closeTable();
        }
    }

    private void writeDependencyRow(ModuleImport moduleImport) throws IOException {
        StringBuilder tooltip = new StringBuilder();
        if (moduleImport.isExport()) {
            tooltip.append("shared ");
        }
        if (moduleImport.isOptional()) {
            tooltip.append("optional ");
        }
        tooltip.append("import of module ");
        tooltip.append(moduleImport.getModule().getNameAsString());
        tooltip.append(" ");
        tooltip.append(moduleImport.getModule().getVersion());
        
        open("tr");
        
        open("td class='shrink'");
        open("span title='" + tooltip + "'");
        writeIcon(moduleImport);
        close("span");
        open("code class='decl-label'");
        linkRenderer().to(moduleImport.getModule()).write();
        String backend = moduleImport.getNativeBackend();
        if(backend != null){
            write(" (");
            write(backend);
            write(")");
        }
        close("code");
        close("td");
        
        open("td class='shrink'");
        open("code");
        write(moduleImport.getModule().getVersion());
        close("code");
        close("td");
        
        open("td");
        open("div class='description import-description'");
        write(getDoc(moduleImport, linkRenderer()));
        close("div");
        close("td");
        
        close("tr");
    }

    @Override
    protected Object getFromObject() {
        return module;
    }

}