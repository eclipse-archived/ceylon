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
import static com.redhat.ceylon.ceylondoc.Util.getModifiers;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Getter;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.Value;

public class PackageDoc extends ClassOrPackageDoc {

    private Package pkg;
    /**
     * The {@linkplain #shouldInclude(Declaration) visible} 
     * classes in the package
     */
    private List<Class> classes;
    /**
     * The {@linkplain #shouldInclude(Declaration) visible} 
     * interfaces in the package
     */
    private List<Interface> interfaces;
    /**
     * The {@linkplain #shouldInclude(Declaration) visible} 
     * attributes in the package
     */
    private List<MethodOrValue> attributes;
    /**
     * The {@linkplain #shouldInclude(Declaration) visible} 
     * methods in the package
     */
    private List<Method> methods;
    private final boolean sharingPageWithModule;

	public PackageDoc(CeylonDocTool tool, Writer writer, Package pkg) throws IOException {
		super(pkg.getModule(), tool, writer);
		this.sharingPageWithModule = tool.isRootPackage(module, pkg);
		this.pkg = pkg;
		loadMembers();
	}

    private void loadMembers() {
        classes = new ArrayList<Class>();
        interfaces = new ArrayList<Interface>();
        attributes = new ArrayList<MethodOrValue>();
        methods = new ArrayList<Method>();
        for (Declaration m : pkg.getMembers()) {
            if (!shouldInclude(m)) {
                continue;
            }
            if (m instanceof Interface)
                interfaces.add((Interface) m);
            else if (m instanceof Class)
                classes.add((Class) m);
            else if (m instanceof Value || m instanceof Getter)
                attributes.add((MethodOrValue) m);
            else if (m instanceof Method)
                methods.add((Method) m);
        }
        Comparator<Declaration> comparator = new Comparator<Declaration>() {
            @Override
            public int compare(Declaration a, Declaration b) {
                return a.getName().compareTo(b.getName());
            }
        };
        Collections.sort(classes, comparator);
        Collections.sort(interfaces, comparator);
        Collections.sort(attributes, comparator);
        Collections.sort(methods, comparator);
    }

    public void generate() throws IOException {
        if (!sharingPageWithModule) {
            htmlHead();
            writeNav(module, pkg, DocType.PACKAGE);
        } else {
            writeKeyboardShortcuts();
        }
        subMenu();
        summary();
        attributes();
        methods();
        interfaces();
        classes();
        if (!sharingPageWithModule) {
            close("body");
            close("html");
        }
    }

    private void htmlHead() throws IOException {
        htmlHead("Package " + pkg.getName());
    }

    private void summary() throws IOException {
        open("div class='head summary'");
        String id = "";
        if (tool.isRootPackage(module, pkg)) {
            id = " id='section-package'";
        }
        open("h1" + id);
        write("Package ");
        around("code", pkg.getNameAsString());
        close("h1");
        writeSourceLink(pkg);
        close("div");
        
        around("div class='doc'", getDoc(pkg));
        
        writeBy(pkg.getAuthors(), false);
    }

    protected void subMenu() throws IOException {
        if (attributes.isEmpty()
                && methods.isEmpty()
                && classes.isEmpty()
                && interfaces.isEmpty()) {
            return;
        }
        open("div class='submenu'");
        if (!attributes.isEmpty()) {
            printSubMenuItem("section-attributes", getAccessKeyed("Attributes", 'A', "Jump to attributes"));
        }
        if (!methods.isEmpty()) {
            printSubMenuItem("section-methods", getAccessKeyed("Methods", 'M', "Jump to methods"));
        }
        if (!classes.isEmpty()) {
            printSubMenuItem("section-classes", getAccessKeyed("Classes", 'C', "Jump to classes"));
        }
        if (!interfaces.isEmpty()) {
            printSubMenuItem("section-interfaces", getAccessKeyed("Interfaces", 'I', "Jump to interfaces"));
        }
        close("div");
    }

    private void methods() throws IOException {
        if (methods.isEmpty()) {
            return;
        }
        openTable("section-methods", "Methods", "Modifier and Type", "Method and Description");
        for (Method m : methods) {
            doc(m);
        }
        close("table");
    }

    private void attributes() throws IOException {
        if (attributes.isEmpty()) {
            return;
        }
        openTable("section-attributes", "Attributes", "Modifier and Type", "Name and Description");
        for (MethodOrValue v : attributes) {
            doc(v);
        }
        close("table");
    }

    private void interfaces() throws IOException {
        if (interfaces.isEmpty()) {
            return;
        }
        openTable("section-interfaces", "Interfaces", "Modifier and Type", "Description");
        for (Interface i : interfaces) {
            doc(i);
        }
        close("table");
    }

    private void classes() throws IOException {
        if (classes.isEmpty()) {
            return;
        }
        openTable("section-classes", "Classes", "Modifier and Type", "Description");
        for (Class c : classes) {
            doc(c);
        }
        close("table");
    }

    private void doc(ClassOrInterface d) throws IOException {
        open("tr class='TableRowColor category'");
        open("td", "code");
        writeIcon(d);
        around("span class='modifiers'", getModifiers(d));
        write(" ");
        link(d.getType());
        close("code", "td");
        open("td");
        writeTagged(d);
        writeDescription(d);
        close("td");
        close("tr");
    }

    @Override
    protected String getObjectUrl(Object to) throws IOException {
        return tool.getObjectUrl(pkg, to);
    }

    @Override
    protected String getResourceUrl(String to) throws IOException {
        return tool.getResourceUrl(pkg, to);
    }
    
    @Override
    protected String getSrcUrl(Object to) throws IOException {
        return tool.getSrcUrl(pkg, to);
    }

    @Override
    protected void writeAdditionalKeyboardShortcuts() throws IOException {
        writeKeyboardShortcut('p', "index.html");
        if (!attributes.isEmpty()) {
            writeKeyboardShortcut('a', "#section-attributes");
        } 
        if (!methods.isEmpty()) {
            writeKeyboardShortcut('m', "#section-methods");
        }
        if (!classes.isEmpty()) {
            writeKeyboardShortcut('c', "#section-classes");
        }
        if (!interfaces.isEmpty()) {
            writeKeyboardShortcut('i', "#section-interfaces");
        }
    }
}
