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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.redhat.ceylon.ceylondoc.Util.DeclarationComparatorByName;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Getter;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.Value;

public class PackageDoc extends ClassOrPackageDoc {

    private final Package pkg;
    private final boolean sharingPageWithModule;
    private final List<Class> classes = new ArrayList<Class>();
    private final List<Interface> interfaces = new ArrayList<Interface>();
    private final List<MethodOrValue> attributes = new ArrayList<MethodOrValue>();
    private final List<Method> methods = new ArrayList<Method>();
    private final List<Class> exceptions = new ArrayList<Class>();

	public PackageDoc(CeylonDocTool tool, Writer writer, Package pkg) throws IOException {
		super(pkg.getModule(), tool, writer);
		this.pkg = pkg;
		this.sharingPageWithModule = tool.isRootPackage(module, pkg);
		loadMembers();
	}

    private void loadMembers() {
        for (Declaration m : pkg.getMembers()) {
            if (!tool.shouldInclude(m)) {
                continue;
            }
            if (m instanceof Interface) {
                interfaces.add((Interface) m);
            } else if (m instanceof Class) {
                Class c = (Class) m;
                if (Util.isException(c)) {
                    exceptions.add(c);
                } else {
                    classes.add(c);
                }
            } else if (m instanceof Value) {
                attributes.add((MethodOrValue) m);
            } else if (m instanceof Method) {
                methods.add((Method) m);
            }
        }
        
        Collections.sort(classes, DeclarationComparatorByName.INSTANCE);
        Collections.sort(interfaces, DeclarationComparatorByName.INSTANCE);
        Collections.sort(attributes, DeclarationComparatorByName.INSTANCE);
        Collections.sort(methods, DeclarationComparatorByName.INSTANCE);
        Collections.sort(exceptions, DeclarationComparatorByName.INSTANCE);
    }

    public void generate() throws IOException {
        if (!sharingPageWithModule) {
            writeHeader("Package " + pkg.getName());
            writeNavBar();
        }
        
        writeSubNav();
        
        open("div class='container-fluid'");
        
        writeDescription();
        writeAttributes();
        writeMethods();
        writeInterfaces();
        writeClasses();
        writeExceptions();
        
        close("div");
        
        if (!sharingPageWithModule) {
            writeFooter();
        }
    }

    private void writeSubNav() throws IOException {
        open("div class='sub-navbar'");
        
        writeLinkSourceCode(pkg);
        
        if (sharingPageWithModule) {
            open("div id='section-package' class='sub-navbar-inner'");
        } else {
            open("div class='sub-navbar-inner'");
        }
        
        around("span class='sub-navbar-label'", "package");
        writeIcon(pkg);
        around("span class='sub-navbar-name'", pkg.getNameAsString());
        close("div"); // sub-navbar-inner
        
        open("div class='sub-navbar-menu'");
        if (!sharingPageWithModule) {
            writeSubNavBarLink(linkRenderer().to(module).getUrl(), "Overview", 'O', "Jump to module documentation");
        }
        if (!attributes.isEmpty()) {
            writeSubNavBarLink("#section-attributes", "Attributes", 'A', "Jump to attributes");
        }
        if (!methods.isEmpty()) {
            writeSubNavBarLink("#section-methods", "Methods", 'M', "Jump to methods");
        }
        if (!interfaces.isEmpty()) {
            writeSubNavBarLink("#section-interfaces", "Interfaces", 'I', "Jump to interfaces");
        }
        if (!classes.isEmpty()) {
            writeSubNavBarLink("#section-classes", "Classes", 'C', "Jump to classes");
        }
        if (!exceptions.isEmpty()) {
            writeSubNavBarLink("#section-exceptions", "Exceptions", 'E', "Jump to exceptions");
        }
        close("div"); // sub-navbar-menu
        
        close("div"); // sub-navbar
    }

    private void writeDescription() throws IOException {
        open("div class='package-description'");
        around("div class='doc'", getDoc(pkg, linkRenderer()));
        writeBy(pkg);
        close("div");
    }

    private void writeMethods() throws IOException {
        if (methods.isEmpty()) {
            return;
        }
        openTable("section-methods", "Methods", 2, true);
        for (Method m : methods) {
            doc(m);
        }
        close("table");
    }

    private void writeAttributes() throws IOException {
        if (attributes.isEmpty()) {
            return;
        }
        openTable("section-attributes", "Attributes", 2, true);
        for (MethodOrValue v : attributes) {
            doc(v);
        }
        closeTable();
    }

    private void writeInterfaces() throws IOException {
        if (interfaces.isEmpty()) {
            return;
        }
        openTable("section-interfaces", "Interfaces", 2, true);
        for (Interface i : interfaces) {
            doc(i);
        }
        closeTable();
    }

    private void writeClasses() throws IOException {
        if (classes.isEmpty()) {
            return;
        }
        openTable("section-classes", "Classes", 2, true);
        for (Class c : classes) {
            doc(c);
        }
        closeTable();
    }
    
    private void writeExceptions() throws IOException {
        if (exceptions.isEmpty()) {
            return;
        }
        openTable("section-exceptions", "Exceptions", 2, true);
        for (Class e : exceptions) {
            doc(e);
        }
        closeTable();
    }
    
    @Override
    protected void registerAdditionalKeyboardShortcuts() throws IOException {
        registerKeyboardShortcut('p', "index.html");
        if (!attributes.isEmpty()) {
            registerKeyboardShortcut('a', "#section-attributes");
        } 
        if (!methods.isEmpty()) {
            registerKeyboardShortcut('m', "#section-methods");
        }
        if (!interfaces.isEmpty()) {
            registerKeyboardShortcut('i', "#section-interfaces");
        }
        if (!classes.isEmpty()) {
            registerKeyboardShortcut('c', "#section-classes");
        }
        if (!exceptions.isEmpty()) {
            registerKeyboardShortcut('e', "#section-exceptions");
        }
    }

    @Override
    protected Object getFromObject() {
        return pkg;
    }
}
