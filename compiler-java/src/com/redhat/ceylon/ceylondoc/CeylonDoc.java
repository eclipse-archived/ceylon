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
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.Annotation;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Package;

public abstract class CeylonDoc extends Markup {

    protected final CeylonDocTool tool;
    protected final Module module;

    public CeylonDoc(Module module, CeylonDocTool tool, Writer writer) {
        super(writer);
        this.module = module;
        this.tool = tool;
    }
    
    protected final LinkRenderer linkRenderer() {
        return new LinkRenderer(tool, writer, getFromObject());
    }

    protected final void linkToDeclaration(Declaration declaration) throws IOException {
        linkRenderer().to(declaration).write();
    }
    
    protected final void writeHeader(String title, String... additionalCss) throws IOException {
        write("<!DOCTYPE html>");
        open("html");
        open("head");
        tag("meta charset='UTF-8'");
        around("title", title);

        tag("link href='" + linkRenderer().getResourceUrl("shCore.css") + "' rel='stylesheet' type='text/css'");
        tag("link href='" + linkRenderer().getResourceUrl("shThemeDefault.css") + "' rel='stylesheet' type='text/css'");
        tag("link href='" + linkRenderer().getResourceUrl("bootstrap.min.css") + "' rel='stylesheet' type='text/css'");
        tag("link href='" + linkRenderer().getResourceUrl("ceylondoc.css") + "' rel='stylesheet' type='text/css'");

        for (String css : additionalCss) {
            if (!css.endsWith(".css")) {
                throw new RuntimeException(CeylondMessages.msg("error.unexpectedAdditionalResource", css));
            }
            tag("link href='" + linkRenderer().getResourceUrl(css) + "' rel='stylesheet' type='text/css'");
        }

        close("head");
        open("body");
    }

    protected final void writeFooter(String... additionalJs) throws IOException {
        around("script type='text/javascript' src='" + linkRenderer().getResourceUrl("jquery-1.8.2.min.js") + "'");
        around("script type='text/javascript' src='" + linkRenderer().getResourceUrl("bootstrap.min.js") + "'");
        around("script type='text/javascript' src='" + linkRenderer().getResourceUrl("shCore.js") + "'");
        around("script type='text/javascript' src='" + linkRenderer().getResourceUrl("shBrushCeylon.js") + "'");
        around("script type='text/javascript' src='" + linkRenderer().getResourceUrl("index.js") + "'");
        around("script type='text/javascript' src='" + linkRenderer().getResourceUrl("ceylondoc.js") + "'");
        
        for (String js : additionalJs) {
            if (!js.endsWith(".js")) {
                throw new RuntimeException(CeylondMessages.msg("error.unexpectedAdditionalResource", js));
            }
            around("script type='text/javascript' src='" + linkRenderer().getResourceUrl(js) + "'");
        }
        
        writeKeyboardShortcuts();
        
        close("body");
        close("html");
    }

    protected final void writeNavBar() throws IOException {
        open("div class='navbar navbar-inverse navbar-static-top'");
        open("div class='navbar-inner'");

        open("a class='module-header' href='" + linkRenderer().to(module).getUrl() + "'");
        around("i class='module-logo'");
        around("span class='module-label'", "module");
        around("span class='module-name'", module.getNameAsString());
        around("span class='module-version'", module.getVersion());
        close("a");

        writeNavBarSearchMenu();
        writeNavBarFilterMenu();

        close("div"); // navbar-inner
        close("div"); // navbar
    }

    protected void writeNavBarSearchMenu() throws IOException {
        open("ul class='nav pull-right'");
        write("<li class='divider-vertical' />");
        write("<li><a href='" + linkRenderer().getResourceUrl("../search.html") + "' title='Search this module [Shortcut: S]'><i class='icon-search'></i>Search</a></li>");
        close("ul");
    }
    
    protected void writeNavBarFilterMenu() throws IOException {
        open("ul class='nav pull-right'");
        write("<li class='divider-vertical' />");
        open("li id='filterDropdown' class='dropdown'");
        write("<a href='#' title='Filter declarations by tags [Shortcut: F]' role='button' class='dropdown-toggle' data-toggle='dropdown'><i class='icon-filter'></i>Filter <span id='filterDropdownLinkInfo'></span> <b class='caret'></b></a>");
        open("ul id='filterDropdownPanel' class='dropdown-menu'");
        write("<div id='filterDropdownPanelInfo'>Filter declarations by tags</div>");
        write("<div id='filterDropdownPanelTags'></div>");
        write("<li class='divider'></li>");
        open("div id='filterActions'");
        write("<a id='filterActionAll'>All</a>");
        write("<a id='filterActionNone'>None</a>");
        write("<a id='filterActionMore'>Show more</a>");
        close("div"); // filterActions
        close("ul"); // filterDropdownPanel
        close("li"); // filterDropdown
        close("ul"); // nav        
    }

    protected final void writeSubNavBarLink(String href, String text, char key, String tooltip) throws IOException {
        open("a href='" + href + "'");
        
        int index = text.indexOf(key);
        String before = text.substring(0, index);
        String after = text.substring(index+1);
        
        write("<span title='", tooltip, " [Shortcut: ", String.valueOf(key), "]'>");
        write(before, "<span class='accesskey'>", String.valueOf(key), "</span>", after, "</span>");
        
        close("a");
    }

    protected void writeKeyboardShortcuts() throws IOException{
        open("script type='text/javascript'");
        write("jQuery('html').keypress(function(evt){\n");
        write("  evt = evt || window.event;\n");
        write("  var keyCode = evt.keyCode || evt.which;\n");
        writeKeyboardShortcut('s', linkRenderer().getResourceUrl("../search.html"));
        writeKeyboardShortcut('o', linkRenderer().getResourceUrl("../index.html"));
        writeAdditionalKeyboardShortcuts();
        write("});\n");
        close("script");
    }

    protected void writeAdditionalKeyboardShortcuts() throws IOException {
        // for subclasses
    }

    protected final void writeKeyboardShortcut(char c, String url) throws IOException {
        write(" if(keyCode == "+(int)c+"){\n");
        write("   document.location = '"+url+"';\n"); 
        write(" }\n");
    }

    protected final void writeLinkSourceCode(Object obj) throws IOException {
        String srcUrl = linkRenderer().getSrcUrl(obj);
        if (tool.isIncludeSourceCode() && srcUrl != null) {
            open("a class='link-source-code' href='" + srcUrl + "'");
            write("<i class='icon-source-code'></i>");
            write("Source Code");
            close("a");
        }
    }
    
    protected final void writeBy(Object obj) throws IOException {
        List<Annotation> annotations;
        
        if( obj instanceof Declaration ) {
            annotations = ((Declaration) obj).getAnnotations();
        } else if ( obj instanceof Module ) {
            annotations = ((Module) obj).getAnnotations();
        } else if( obj instanceof Package ) {
            annotations = ((Package) obj).getAnnotations();
        } else {
            throw new IllegalArgumentException();
        }
        
        List<String> authors = new ArrayList<>();
        for (Annotation annotation : annotations) {
            if (annotation.getName().equals("by")) {
                for (String author : annotation.getPositionalArguments()) {
                    authors.add(Util.unquote(author));
                }
            }
        }
        
        if (!authors.isEmpty()) {
            open("div class='by section'");
            around("span class='title'", "By: ");
            around("span class='value'", Util.join(", ", authors));
            close("div");
        }        
    }
    
    protected final void writeIcon(Object obj) throws IOException {
        List<String> icons = getIcons(obj);
    
        int i = 0;
        for (String icon : icons) {
            open("i class='" + icon + "'");
            i++;
        }
        while (i-- > 0) {
            close("i");
        }
    }

    protected final List<String> getIcons(Object obj) {
        List<String> icons = new ArrayList<String>();

        if( obj instanceof Declaration ) {
            Declaration decl = (Declaration) obj;

            Annotation deprecated = Util.findAnnotation(decl, "deprecated");
            if (deprecated != null) {
                icons.add("icon-decoration-deprecated");
            }

            if( decl instanceof ClassOrInterface ) {
                if (decl instanceof Interface) {
                    icons.add("icon-interface");
                }
                if (decl instanceof Class) {
                    icons.add("icon-class");
                    if (((Class) decl).isAbstract()) {
                        icons.add("icon-decoration-abstract");
                    }
                }
                if (!decl.isShared() ) {
                    icons.add("icon-decoration-local");
                }
            }

            if (decl instanceof MethodOrValue) {
                if( decl.isShared() ) {
                    icons.add("icon-shared-member");
                }
                else {
                    icons.add("icon-local-member");
                }
                if( decl.isFormal() ) {
                    icons.add("icon-decoration-formal");
                }
                if (decl.isActual()) {
                    Declaration refinedDeclaration = decl.getRefinedDeclaration();
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
        }

        if( obj instanceof Package ) {
            Package pkg = (Package) obj;

            icons.add("icon-package");
            if (!pkg.isShared()) {
                icons.add("icon-decoration-local");
            }
        }

        return icons;
    }

    protected abstract Object getFromObject();    
    
}