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

import static com.redhat.ceylon.ceylondoc.Util.getDoc;
import static com.redhat.ceylon.ceylondoc.Util.getDocFirstLine;
import static com.redhat.ceylon.ceylondoc.Util.getModifiers;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.Annotation;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ParameterList;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.Value;

public abstract class ClassOrPackageDoc extends CeylonDoc {

	public ClassOrPackageDoc(Module module, CeylonDocTool tool, File file) {
		super(module, tool, file);		
	}

	protected void htmlHead(String title) throws IOException {
	    open("html");
        open("head");
        around("title", title);
        tag("link href='" + getResourceUrl("style.css") + "' rel='stylesheet' type='text/css'");
        around("script type='text/javascript' src='" + getResourceUrl("jquery-1.7.min.js") + "'");
        around("script type='text/javascript' src='" + getResourceUrl("ceylond.js") + "'");
        close("head");
        open("body");
	}
	
    protected void writeSee(Declaration decl) throws IOException {
        Annotation see = Util.getAnnotation(decl, "see");
        if(see == null)
            return;
        boolean first = true;
        open("div class='see'");
        write("See also: ");
        for(String target : see.getPositionalArguments()){
            // try to resolve in containing scopes
            TypeDeclaration targetDecl = resolveDeclaration((Scope) decl, target);
            if(targetDecl != null){
                if (!first) {
                    write(", ");
                } else {
                    first = false;
                }
                if(targetDecl.isMember())
                    linkToMember(targetDecl);
                else
                    link(targetDecl.getType());
            }
        }
        close("div");
    }

    private TypeDeclaration resolveDeclaration(Scope decl, String target) {
        if(decl == null)
            return null;
        TypeDeclaration member = (TypeDeclaration) decl.getMember(target);
        if (member != null)
            return member;
        return resolveDeclaration(decl.getContainer(), target);
    }

    protected void doc(Method m) throws IOException {
        open("tr class='TableRowColor' id='" + m.getName() + "'");
        open("td", "code");
        around("span class='modifiers'", getModifiers(m));
        write(" ");
        link(m.getType());
        close("code", "td");
        open("td", "code");
        write(m.getName());
        List<TypeParameter> typeParameters = m.getTypeParameters();
        if (!typeParameters.isEmpty()) {
            write("&lt;");
            boolean first = true;
            for (TypeParameter type : typeParameters) {
                if (first)
                    first = false;
                else
                    write(", ");
                write(type.getName());
            }
            write("&gt;");
        }
        writeParameterList(m.getParameterLists());
        close("code");
        linkSource(m);
        tag("br");
        startPrintingLongDoc(m);
        writeSee(m);
        endLongDocAndPrintShortDoc(m);
        close("td");
        close("tr");
    }

    private void linkSource(MethodOrValue m) throws IOException {
        if (tool.isOmitSource()) {
            return;
        }
        String srcUrl;
        if (m.isToplevel()) {
            srcUrl = getSrcUrl(m);
        } else {
            srcUrl = getSrcUrl(m.getContainer());
        }
        int[] lines = tool.getDeclarationSrcLocation(m);
        open("div class='source-code member'");
        around("a href='" + srcUrl + "#" + lines[0] + "," + lines[1] + "'", "Source Code");
        close("div");
    }

    protected void doc(MethodOrValue f) throws IOException {
        if (f instanceof Value) {
            f = (Value) f;
        }
        open("tr class='TableRowColor' id='" + f.getName() + "'");
        open("td", "code");
        around("span class='modifiers'", getModifiers(f));
        write(" ");
        link(f.getType());
        close("code", "td");
        open("td", "code");
        write(f.getName());
        close("code");
        linkSource(f);
        tag("br");
        startPrintingLongDoc(f);
        endLongDocAndPrintShortDoc(f);
        close("td");
        close("tr");
    }

    protected void writeParameterList(List<ParameterList> parameterLists) throws IOException {
        for (ParameterList lists : parameterLists) {
            write("(");
            boolean first = true;
            for (Parameter param : lists.getParameters()) {
                if (!first) {
                    write(", ");
                } else {
                    first = false;
                }
                link(param.getType());
                write(" ", param.getName());
            }
            write(")");
        }
    }

    protected void endLongDocAndPrintShortDoc(Declaration d) throws IOException {
        close("div");
        open("div class='short'");
        around("div class='doc'", getDocFirstLine(d));
        close("div");
    }

    protected void startPrintingLongDoc(Declaration d) throws IOException {
        open("div class='long'");
        around("div class='doc'", getDoc(d));
    }

}
