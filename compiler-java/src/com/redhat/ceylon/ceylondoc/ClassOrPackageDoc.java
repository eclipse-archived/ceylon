package com.redhat.ceylon.ceylondoc;

import static com.redhat.ceylon.ceylondoc.Util.getDoc;
import static com.redhat.ceylon.ceylondoc.Util.getModifiers;

import java.io.IOException;
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.Annotation;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ParameterList;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.Value;

public abstract class ClassOrPackageDoc extends CeylonDoc {

    public ClassOrPackageDoc(String destDir, boolean showPrivate) {
        super(destDir, showPrivate);
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
        tag("br");
        around("span class='doc'", getDoc(m));
        writeSee(m);
        close("td");
        close("tr");
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
        tag("br");
        around("span class='doc'", getDoc(f));
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

}
