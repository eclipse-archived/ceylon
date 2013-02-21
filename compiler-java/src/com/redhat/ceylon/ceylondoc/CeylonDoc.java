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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.compiler.typechecker.model.Annotation;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;

public abstract class CeylonDoc extends Markup {

    protected final CeylonDocTool tool;
    protected final Module module;
    protected final Map<Character, String> keyboardShortcuts = new HashMap<Character, String>();

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

        writeNavBarInfoMenu();
        writeNavBarSearchMenu();
        writeNavBarFilterMenu();

        close("div"); // navbar-inner
        close("div"); // navbar
    }

    private void writeNavBarInfoMenu() throws IOException {
        open("ul class='nav pull-right'");
        write("<li class='divider-vertical' />");
        open("li id='infoDropdown' class='dropdown'");
        write("<a href='#' title='Show keyboard shortcuts [Shortcut: ?]' role='button' class='dropdown-toggle' data-toggle='dropdown'><i class='icon-info'></i></a>");
        open("ul id='info-dropdown-panel' class='dropdown-menu'");
        around("h4", "Keyboard Shortcuts");
        write("<li class='divider'></li>");
        
        open("div id='info-common-shortcuts'");
        writeKeyboardShortcutInfo("f", "Open filter by tags");
        writeKeyboardShortcutInfo("s", "Open search page");
        writeKeyboardShortcutInfo("?", "Open this information panel");
        close("div"); // info-common-shortcuts
        write("<li class='divider'></li>");
        
        open("div class='row-fluid'");
        
        open("div id='info-doc-shortcuts' class='span6'");
        around("h5","Documentation:");
        writeKeyboardShortcutInfo("o", "Jump to module documentation");
        writeKeyboardShortcutInfo("p", "Jump to package documentation");
        writeKeyboardShortcutInfo("a", "Jump to attributes");
        writeKeyboardShortcutInfo("c", "Jump to constructor");
        writeKeyboardShortcutInfo("m", "Jump to methods");
        writeKeyboardShortcutInfo("i", "Jump to interfaces");
        writeKeyboardShortcutInfo("c", "Jump to classes");
        writeKeyboardShortcutInfo("e", "Jump to exceptions");
        close("div"); // info-doc-shortcuts
        
        open("div id='info-search-shortcuts' class='span6'");
        around("h5", "Search page:");
        writeKeyboardShortcutInfo("enter", "Jump to selected declaration");
        writeKeyboardShortcutInfo("esc", "Clear search query/Go to overview");
        writeKeyboardShortcutInfo("up", "Move selection up");
        writeKeyboardShortcutInfo("down", "Move selection down");
        close("div"); // info-search-shortcuts
        
        close("div"); // row-fluid
        close("ul"); // dropdown-menu
        close("li"); // dropdown
        close("ul"); // nav        
    }

    private void writeKeyboardShortcutInfo(String key, String info) throws IOException {
        open("div id='"+key+"'");
        around("span class='key badge'", key);
        around("span class='info muted'", info);
        close("div");
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
        around("h4 id='filterDropdownPanelInfo'", "Filter declarations by tags");
        write("<li class='divider'></li>");
        
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
        if( index == -1 ) {
            write("<span title='", tooltip, "'>", text, "</span>");
        } else {
            String before = text.substring(0, index);
            String after = text.substring(index+1);

            write("<span title='", tooltip, " [Shortcut: ", String.valueOf(key), "]'>");
            write(before, "<span class='accesskey'>", String.valueOf(key), "</span>", after, "</span>");
        }
        
        close("a");
    }

    protected void writeKeyboardShortcuts() throws IOException{
        registerKeyboardShortcut('s', linkRenderer().getResourceUrl("../search.html"));
        registerKeyboardShortcut('o', linkRenderer().getResourceUrl("../index.html"));
        registerAdditionalKeyboardShortcuts();
        
        if( !keyboardShortcuts.isEmpty() ) {
            open("script type='text/javascript'");
            write("jQuery('html').keypress(function(evt){\n");
            write("  evt = evt || window.event;\n");
            write("  var keyCode = evt.keyCode || evt.which;\n");
            
            write("  if (keyCode == " + (int)'?' + ") {\n");
            write("    $('#infoDropdown > .dropdown-toggle').click();\n");
            write("  }\n");

            for(Map.Entry<Character, String> keyboardShortcut : keyboardShortcuts.entrySet()) {
                write(" if(keyCode == "+(int)keyboardShortcut.getKey().charValue()+"){\n");
                write("   document.location = '"+keyboardShortcut.getValue()+"';\n"); 
                write(" }\n");
            }

            write("});\n");
            
            write("enableInfoKeybordShortcut('\\\\?');\n");
            for (Map.Entry<Character, String> keyboardShortcut : keyboardShortcuts.entrySet()) {
                write("enableInfoKeybordShortcut('"+ keyboardShortcut.getKey() + "');\n");
            }
            
            close("script");
        }
    }

    protected void registerAdditionalKeyboardShortcuts() throws IOException {
        // for subclasses
    }

    protected final void registerKeyboardShortcut(char c, String url) throws IOException {
        keyboardShortcuts.put(c, url);
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
                    authors.add(author);
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
                    Class klass = (Class) decl;
                    if (klass.isAnonymous()) {
                        icons.add("icon-object");
                    } else {
                        icons.add("icon-class");
                    }
                    if (klass.isAbstract()) {
                        icons.add("icon-decoration-abstract");
                    }
                }
                if (!decl.isShared() ) {
                    icons.add("icon-decoration-local");
                }
            }

            if (decl instanceof TypedDeclaration) {
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