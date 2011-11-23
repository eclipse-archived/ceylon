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
import java.util.Comparator;
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Getter;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.Setter;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.model.ValueParameter;

public class IndexDoc extends CeylonDoc {

    private Module module;

    public IndexDoc(CeylonDocTool tool, Writer writer, Module module) throws IOException {
        super(module, tool, writer);
        this.module = module;
    }
    
    public void generate() throws IOException {
        write("var index = [\n");
        indexPackages();
        write("];\n");
    }

    private void indexPackages() throws IOException {
        for (Package pkg : getPackages()) {
            indexPackage(pkg);
        }
        // get rid of the eventual final dangling JSON list comma but adding a module entry 
        writeIndexElement(module.getNameAsString(), tool.kind(module), 
                getObjectUrl(module), Util.getDocFirstLine(module));
    }

    private void indexPackage(Package pkg) throws IOException {
        writeIndexElement(pkg.getNameAsString(), tool.kind(pkg), 
                getObjectUrl(pkg), Util.getDocFirstLine(pkg));
        write(",\n");
        indexMembers(pkg);
    }

    private void indexMembers(Scope pkg) throws IOException {
        for(Declaration decl : pkg.getMembers()){
            if(decl instanceof ClassOrInterface)
                indexMembers((Scope) decl);
            if(indexDecl(pkg, decl))
                write(",\n");
        }
    }

    private boolean indexDecl(Scope container, Declaration decl) throws IOException {
        String name = decl.getName();
        String url;
        if(decl instanceof ClassOrInterface){
            url = getObjectUrl(decl);
        }else if(decl instanceof Method
                || decl instanceof Value
                || decl instanceof Getter){
            url = getObjectUrl(container) + "#" + name;
            if(decl.isMember())
                name = ((ClassOrInterface)container).getName() + "." + name;
        }else if(decl instanceof Setter
                || decl instanceof ValueParameter
                || decl instanceof TypeParameter){
            // ignore
            return false;
        }else
            throw new RuntimeException("Unknown type of object: "+decl);
        String type = tool.kind(decl);
        String doc = Util.getDocFirstLine(decl);
        writeIndexElement(name, type, url, doc);
        return true;
    }

    private void writeIndexElement(String name, String type, String url, String doc) throws IOException {
        write("{name: '");
        write(name);
        write("', type: '");
        write(type);
        write("', url: '");
        write(url);
        write("', doc: '");
        write(escapeJSONString(doc).trim());
        write("'}");
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

    private List<Package> getPackages() {
        List<Package> packages = new ArrayList<Package>();
        for (Package pkg : module.getPackages()) {
            if (pkg.getMembers().size() > 0)
                packages.add(pkg);
        }
        Collections.sort(packages, new Comparator<Package>() {
            @Override
            public int compare(Package a, Package b) {
                return a.getNameAsString().compareTo(b.getNameAsString());
            }

        });
        return packages;
    }

    @Override
    protected String getObjectUrl(Object to) throws IOException {
        return tool.getObjectUrl(module, to);
    }
    
    @Override
    protected String getResourceUrl(String to) throws IOException {
        return tool.getResourceUrl(module, to);
    }
    
    @Override
    protected String getSrcUrl(Object to) throws IOException {
        return tool.getSrcUrl(module, to);
    }
}
