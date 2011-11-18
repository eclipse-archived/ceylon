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

import static com.redhat.ceylon.ceylondoc.Util.getModifiers;

import java.io.IOException;
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
    private List<Class> classes;
    private List<Interface> interfaces;
    private List<MethodOrValue> attributes;
    private List<Method> methods;

	public PackageDoc(CeylonDocTool tool, Package pkg) throws IOException {
		super(pkg.getModule(), tool, tool.getObjectFile(pkg));
		this.pkg = pkg;
		loadMembers();
	}

    private void loadMembers() {
        classes = new ArrayList<Class>();
        interfaces = new ArrayList<Interface>();
        attributes = new ArrayList<MethodOrValue>();
        methods = new ArrayList<Method>();
        for (Declaration m : pkg.getMembers()) {
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
        setupWriter();
        htmlHead();
        summary();
        attributes();
        methods();
        interfaces();
        classes();
        close("body");
        close("html");
        writer.flush();
        writer.close();
    }

    private void htmlHead() throws IOException {
        htmlHead("Package " + pkg.getName());
    }

    private void summary() throws IOException {
        open("div class='nav menu'");
        open("div");
        around("a href='"+getObjectUrl(module)+"'", "Overview");
        close("div");
        open("div class='selected'");
        write("Package");
        close("div");
        open("div");
        write("Class");
        close("div");
        open("div");
        write(pkg.getModule().getNameAsString() + "/" + pkg.getModule().getVersion());
        close("div");
        String srcUrl = getSrcUrl(pkg);
        if (!tool.isOmitSource()
                && srcUrl != null) {
            open("div class='source-code package'");
            around("a href='" + srcUrl + "'", "Source Code");
            close("div");
        }
        close("div");

        open("div class='head summary'");
        around("h1", "Package ", "<code>", pkg.getNameAsString(), "</code>");
        close("div");
    }

    private void methods() throws IOException {
        if (methods.isEmpty())
            return;
        openTable("Methods", "Modifier and Type", "Method and Description");
        for (Method m : methods) {
            doc(m);
        }
        close("table");
    }

    private void attributes() throws IOException {
        openTable("Attributes", "Modifier and Type", "Name and Description");
        for (MethodOrValue v : attributes) {
            doc(v);
        }
        close("table");
    }

    private void interfaces() throws IOException {
        openTable("Interfaces", "Modifier and Type", "Description");
        for (Interface i : interfaces) {
            doc(i);
        }
        close("table");
    }

    private void classes() throws IOException {
        openTable("Classes", "Modifier and Type", "Description");
        for (Class c : classes) {
            doc(c);
        }
        close("table");
    }

    private void doc(ClassOrInterface d) throws IOException {
        open("tr class='TableRowColor category'");
        open("td", "code");
        around("span class='modifiers'", getModifiers(d));
        write(" ");
        link(d.getType());
        close("code", "td");
        open("td");
        startPrintingLongDoc(d);
        endLongDocAndPrintShortDoc(d);
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
}
