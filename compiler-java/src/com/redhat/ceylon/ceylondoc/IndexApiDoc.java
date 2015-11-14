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

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.redhat.ceylon.ceylondoc.Util.ReferenceableComparatorByName;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.Package;
import com.redhat.ceylon.model.typechecker.model.Value;

public class IndexApiDoc extends CeylonDoc {

    private final Module module;

    public IndexApiDoc(CeylonDocTool tool, Writer writer, Module module) throws IOException {
        super(module, tool, writer);
        this.module = module;
    }

    public void generate() throws IOException {
        List<Declaration> declarations = collectDeclarations();

        writeHeader("Index");
        writeNavBar();

        open("div class='container-fluid'");
        write("<h1>Index</h1>");
        writeIndexTable(declarations);
        close("div");

        writeFooter();
    }

    private void writeIndexTable(List<Declaration> declarations) throws IOException {
        open("table id='api-index' class='table table-hover'");
        open("tbody");

        for (Declaration declaration : declarations) {
            writeIndexRow(declaration);
        }

        close("tbody");
        close("table");
    }

    private void writeIndexRow(Declaration declaration) throws IOException {
        open("tr");

        open("td id='" + declaration.getQualifiedNameString() + "'");
        writeIcon(declaration);
        open("code");
        linkRenderer().to(declaration).printAbbreviated(false).printTypeParameters(false).write();
        close("code");
        close("td");

        open("td");
        write(Util.getDocFirstLine(declaration, linkRenderer()));
        writeTagged(declaration);
        close("td");

        close("tr");
    }

    private List<Declaration> collectDeclarations() {
        List<Declaration> declarations = new ArrayList<Declaration>();

        for (Package pkg : tool.getPackages(module)) {
            if (tool.shouldInclude(pkg)) {
                List<Declaration> members = pkg.getMembers();
                for (Declaration member : members) {
                    if (tool.shouldInclude(member)) {
                        if (member instanceof Value && ((Value) member).getTypeDeclaration().isAnonymous()) {
                            continue;
                        }
                        declarations.add(member);
                    }
                }
            }
        }

        Collections.sort(declarations, ReferenceableComparatorByName.INSTANCE);

        return declarations;
    }

    @Override
    protected void writeNavBarExpandAllCollapseAll() throws IOException {
        // noop
    }

    @Override
    protected void writeNavBarIndexMenu() throws IOException {
        // noop
    }

    @Override
    protected Object getFromObject() {
        return module;
    }

}