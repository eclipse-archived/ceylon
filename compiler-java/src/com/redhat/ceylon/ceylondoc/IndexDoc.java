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
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.redhat.ceylon.compiler.java.codegen.Decl;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.Setter;
import com.redhat.ceylon.compiler.typechecker.model.TypeAlias;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AttributeDeclaration;

public class IndexDoc extends CeylonDoc {

    private Module module;
    private Set<String> tagIndex = new TreeSet<String>();

    public IndexDoc(CeylonDocTool tool, Writer writer, Module module) throws IOException {
        super(module, tool, writer);
        this.module = module;
    }
    
    public void generate() throws IOException {
        write("var index = [\n");
        indexPackages();
        write("];\n");
        writeTagIndex();
    }

    private void writeTagIndex() throws IOException {
        write("var tagIndex = [\n");
        Iterator<String> tagIterator = tagIndex.iterator();
        while (tagIterator.hasNext()) {
            write("'");
            write(escapeJSONString(tagIterator.next()));
            write("'");
            if (tagIterator.hasNext()) {
                write(",\n");
            }
        }
        write("];\n");
    }

    private void indexPackages() throws IOException {
        for (Package pkg : tool.getPackages(module)) {
            indexPackage(pkg);
        }
        // get rid of the eventual final dangling JSON list comma but adding a module entry 
        writeIndexElement(
                module.getNameAsString(), 
                getType(module), 
                linkRenderer().to(module).getUrl(), 
                Util.getDocFirstLine(module, linkRenderer()), 
                null, null);
    }

    private void indexPackage(Package pkg) throws IOException {
        writeIndexElement(
                pkg.getNameAsString(), 
                getType(pkg), 
                linkRenderer().to(pkg).getUrl(), 
                Util.getDocFirstLine(pkg, linkRenderer()), 
                null, 
                getIcons(pkg));
        write(",\n");
        indexMembers(pkg, pkg.getMembers());
    }

    private void indexMembers(Scope container, List<Declaration> members) throws IOException {
        for (Declaration decl : members) {
            if (!tool.shouldInclude(decl)) {
                continue;
            }
            if (decl instanceof ClassOrInterface) {
                ClassOrInterface classOrInterface = (ClassOrInterface) decl;
                indexMembers(classOrInterface, classOrInterface.getMembers());
            }
            if (indexDecl(container, decl)) {
                write(",\n");
            }
        }
    }

    private boolean indexDecl(Scope container, Declaration decl) throws IOException {
        String url;
        String name = decl.getName();
        
        if (decl instanceof ClassOrInterface) {
            url = linkRenderer().to(decl).getUrl();
        } else if ((decl instanceof MethodOrValue
                    && decl instanceof Setter == false
                    && !((MethodOrValue)decl).isParameter()) 
                || decl instanceof TypeAlias) {
            url = tool.getObjectUrl(module, container, false) + "#" + name;
            if (decl.isMember()) {
                name = ((ClassOrInterface) container).getName() + "." + name;
            }
        } else if (decl instanceof Setter
                || (decl instanceof MethodOrValue && ((MethodOrValue)decl).isParameter())
                || decl instanceof TypeParameter) {
            return false; // ignore
        } else {
            throw new RuntimeException("Unknown type of object: " + decl);
        }
        
        String type = getType(decl);
        String doc = Util.getDocFirstLine(decl, linkRenderer());
        List<String> tags = Util.getTags(decl);
        tagIndex.addAll(tags);
        
        writeIndexElement(name, type, url, doc, tags, getIcons(decl));
        
        return true;
    }

    private void writeIndexElement(String name, String type, String url, String doc, List<String> tags, List<String> icons) throws IOException {
        write("{'name': '");
        write(name);
        write("', 'type': '");
        write(type);
        write("', 'url': '");
        write(url);
        write("', 'doc': '");
        write(escapeJSONString(doc).trim());
        write("', 'tags': [");
        if( tags != null ) {
            Iterator<String> tagIterator = tags.iterator();
            while (tagIterator.hasNext()) {
                write("'");
                write(escapeJSONString(tagIterator.next()).trim());
                write("'");
                if (tagIterator.hasNext()) {
                    write(", ");
                }
            }        
        }
        write("],");
        write("'icons': [");
        if( icons != null ) {
            Iterator<String> iconIterator = icons.iterator();
            while (iconIterator.hasNext()) {
                write("'");
                write(escapeJSONString(iconIterator.next()).trim());
                write("'");
                if (iconIterator.hasNext()) {
                    write(", ");
                }
            }        
        }
        write("]}");
    }

    private String escapeJSONString(String doc) {
        if(doc == null)
            return "";
        char[] chars = doc.toCharArray();
        // assume worst case size
        StringBuffer escaped = new StringBuffer(chars.length * 2);
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if(c == '\n')
                escaped.append("\\n");
            else if(c == '\'')
                escaped.append("\\'");
            else if(c == '\\')
                escaped.append("\\\\");
            else
                escaped.append(c);
        }
        return escaped.toString();
    }
    
    private String getType(Object obj) {
        if (obj instanceof Class) {
            return Character.isUpperCase(((Class)obj).getName().charAt(0)) ? "class" : "object";
        } else if (obj instanceof Interface) {
            return "interface";
        } else if (obj instanceof TypeAlias) {
            return "alias";
        } else if (obj instanceof AttributeDeclaration || (obj instanceof Declaration && Decl.isGetter((Declaration)obj))) {
            return "attribute";
        } else if (obj instanceof Method) {
            return "function";
        } else if (obj instanceof Value) {
            return "value";
        } else if (obj instanceof Package) {
            return "package";
        } else if (obj instanceof Module) {
            return "module";
        }
        throw new RuntimeException(CeylondMessages.msg("error.unexpected", obj));
    }
    
    @Override
    protected Object getFromObject() {
        return module;
    }

}