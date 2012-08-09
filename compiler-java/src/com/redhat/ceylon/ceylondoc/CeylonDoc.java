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
import java.util.Iterator;
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.Annotation;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.Scope;

public abstract class CeylonDoc extends Markup {

    protected final DocTool tool;
    protected final Module module;

    public CeylonDoc(Module module, DocTool tool, Writer writer) {
        super(writer);
        this.module = module;
        this.tool = tool;
    }
    
    protected LinkRenderer linkRenderer() {
        return new LinkRenderer(tool, writer, getFromObject());
    }
    
    protected void linkToDeclaration(Declaration declaration) throws IOException {
        linkRenderer().to(declaration).write();
    }
    
    protected abstract Object getFromObject();

    protected static Package getPackage(Scope decl) {
        while (!(decl instanceof Package)) {
            decl = decl.getContainer();
        }
        return (Package) decl;
    }

    protected boolean shouldInclude(Declaration decl){
        return tool.shouldInclude(decl);
    }
    
    protected void writeNav(Module module, Object decl, DocType docType) throws IOException {
        open("div class='nav menu'");
        open("div");
        linkRenderer().to(module).useCustomText(getAccessKeyed("Overview", 'O', "Module documentation")).write();
        close("div");
        if(docType == DocType.PACKAGE)
            open("div class='selected'");
        else
            open("div");
        if(docType != DocType.MODULE
                && docType != DocType.SEARCH) {
            String accessKeyed = getAccessKeyed("Package", 'P', "Package documentation");
            if (decl instanceof Declaration) {
                linkRenderer().to(getPackage(((Declaration)decl).getContainer())).useCustomText(accessKeyed).write();
            } else if (decl instanceof Package) {
                linkRenderer().to((Package)decl).useCustomText(accessKeyed).write();
            } else {
                throw new RuntimeException("" + decl);
            }
        } else
            write("Package");
        close("div");
        if(docType == DocType.TYPE){
            open("div class='selected'");
            linkRenderer().to((ClassOrInterface)decl).useCustomText(getAccessKeyed("Type", 'T', "Type documentation")).write();
        }else{
            open("div");
            write("Type");
        }
        close("div");

        open("div");
        around("a href='"+linkRenderer().getResourceUrl("../search.html")+"'", getAccessKeyed("Search", 'S', "Search this module"));
        close("div");
        
        writeFilterDropdownMenu(docType);
        
        open("div");
        write(module.getNameAsString() + "/" + module.getVersion());
        close("div");

        close("div");
    }
    
    private void writeFilterDropdownMenu(DocType docType) throws IOException {
        if( docType != DocType.SEARCH ) {
            open("div id='filterMenu'");
            open("div id='filterDropdownLink'");
            write("<span title='Filter this page [Shortcut: F]'>");
            write("<span class='accesskey'>F</span>ilter</span>");
            write("<span id='filterDropdownLinkInfo'></span>");
            write("<span id='filterDropdownCaret'></span>");
            close("div"); // filterDropdownLink
            open("div id='filterDropdownPanel'");
            write("<div id='filterDropdownPanelInfo'>Filter declarations by tags.</div>");
            write("<div id='filterDropdownPanelTags'></div>");
            open("div id='filterActions'");
            write("<a id='filterActionAll'>All</a>");
            write("<a id='filterActionNone'>None</a>");
            write("<a id='filterActionMore'>Show more</a>");
            close("div"); // filterActions
            close("div"); // filterDropdownPanel
            close("div"); // filterMenu
        }        
    }

    protected String getAccessKeyed(String string, char key, String tooltip) {
        int index = string.indexOf(key);
        if(index == -1)
            return string;
        String before = string.substring(0, index);
        String after = string.substring(index+1);
        return "<span title='" + tooltip + " [Shortcut: " + key + "]'>" +
                before + "<span class='accesskey'>" + key + "</span>" + after +
                "</span>";
    }

    protected void writeKeyboardShortcuts() throws IOException{
        // shortcuts
        open("script type='text/javascript'");
        write("jQuery('html').keypress(function(evt){\n");
        write(" evt = evt || window.event;\n");
        write(" var keyCode = evt.keyCode || evt.which;\n");
        writeKeyboardShortcut('s', linkRenderer().getResourceUrl("../search.html"));
        writeKeyboardShortcut('o', linkRenderer().getResourceUrl("../index.html"));
        writeAdditionalKeyboardShortcuts();
        write("});\n");
        close("script");
    }

    protected void writeAdditionalKeyboardShortcuts() throws IOException {
        // for subclasses
    }

    protected void writeKeyboardShortcut(char c, String url) throws IOException {
        write(" if(keyCode == "+(int)c+"){\n");
        write("  document.location = '"+url+"';\n"); 
        write(" }\n");
    }

    protected void htmlHead(String title, String... additional) throws IOException {
        write("<!DOCTYPE html>");
        open("html xmlns='http://www.w3.org/1999/xhtml'");
        open("head");
        tag("meta charset='UTF-8'");
        around("title", title);
        tag("link href='" + linkRenderer().getResourceUrl("shCore.css") + "' rel='stylesheet' type='text/css'");
        tag("link href='" + linkRenderer().getResourceUrl("shThemeDefault.css") + "' rel='stylesheet' type='text/css'");
        tag("link href='" + linkRenderer().getResourceUrl("style.css") + "' rel='stylesheet' type='text/css'");
        for (String add : additional) {
            if (add.endsWith(".css")) {
                tag("link href='" + linkRenderer().getResourceUrl(add) + "' rel='stylesheet' type='text/css'");
            } else if (!add.endsWith(".js")) {
                throw new RuntimeException(CeylondMessages.msg("error.unexpectedAdditionalResource", add));
            }
        }
        around("script type='text/javascript' src='" + linkRenderer().getResourceUrl("jquery-1.7.min.js") + "'");
        around("script type='text/javascript' src='" + linkRenderer().getResourceUrl("shCore.js") + "'");
        around("script type='text/javascript' src='" + linkRenderer().getResourceUrl("shBrushCeylon.js") + "'");
        around("script type='text/javascript' src='" + linkRenderer().getResourceUrl("index.js") + "'");
        around("script type='text/javascript' src='" + linkRenderer().getResourceUrl("ceylond.js") + "'");
        for (String add : additional) {
            if (add.endsWith(".js")) {
                around("script type='text/javascript' src='" + linkRenderer().getResourceUrl(add) + "'");
            } else if (!add.endsWith(".css")) {
                throw new RuntimeException(CeylondMessages.msg("error.unexpectedAdditionalResource", add));
            }
        }
        close("head");
        open("body");
        writeKeyboardShortcuts();
    }
    
    protected void writeSourceLink(Object modPkgOrDecl) throws IOException {
        String srcUrl = linkRenderer().getSrcUrl(modPkgOrDecl);
        if (tool.isIncludeSourceCode() && srcUrl != null) {
            open("a class='link-source-code "+DocType.typeOf(modPkgOrDecl).name().toLowerCase()+"' href='" + srcUrl + "'");
            write("<i class='icon-source-code'></i>");
            write("Source Code");
            close("a");
        }
    }
    
    protected void writeBy(Declaration decl) throws IOException {
        Annotation by = Util.getAnnotation(decl, "by");
        if (by != null) {
            writeBy(by.getPositionalArguments(), true);
        }
    }

    protected void writeBy(List<String> authors, boolean unquote) throws IOException {
        if (!authors.isEmpty()) {
            StringBuilder authorBuilder = new StringBuilder();
            Iterator<String> authorIterator = authors.iterator();
            while (authorIterator.hasNext()) {
                if (unquote) {
                    authorBuilder.append(Util.unquote(authorIterator.next()));
                } else {
                    authorBuilder.append(authorIterator.next());
                }
                if (authorIterator.hasNext()) {
                    authorBuilder.append(", ");
                }
            }
            around("div class='by'", "By: ", authorBuilder.toString());
        }
    }
    
    protected void writeIcon(Declaration d) throws IOException {
        List<String> icons = new ArrayList<String>();
        
        Annotation deprecated = Util.findAnnotation(d, "deprecated");
        if (deprecated != null) {
            icons.add("icon-decoration-deprecated");
        }
        
        if( d instanceof ClassOrInterface ) {
            if (d instanceof Interface) {
                icons.add("icon-interface");
            }
            if (d instanceof Class) {
                icons.add("icon-class");
                if (((Class) d).isAbstract()) {
                    icons.add("icon-decoration-abstract");
                }
            }
            if (!d.isShared() ) {
                icons.add("icon-decoration-local");
            }
        }
        
        if (d instanceof MethodOrValue) {
            if( d.isShared() ) {
                icons.add("icon-shared-member");
            }
            else {
                icons.add("icon-local-member");
            }
            if( d.isFormal() ) {
                icons.add("icon-decoration-formal");
            }
            if (d.isActual()) {
                Declaration refinedDeclaration = d.getRefinedDeclaration();
                if (refinedDeclaration != null) {
                    if (refinedDeclaration.isFormal()) {
                        icons.add("icon-decoration-impl");
                    }
                    if (refinedDeclaration.isDefault()) {
                        icons.add("icon-decoration-over");
                    }
                }
            }            
        }
        
        int i = 0;
        for (String icon : icons) {
            open("i class='" + icon + "'");
            i++;
        }
        while (i-- > 0) {
            close("i");
        }
    }
    
}