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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.model.loader.AbstractModelLoader;
import com.redhat.ceylon.model.typechecker.model.Annotated;
import com.redhat.ceylon.model.typechecker.model.Annotation;
import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.model.typechecker.model.Constructor;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Interface;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.ModuleImport;
import com.redhat.ceylon.model.typechecker.model.NothingType;
import com.redhat.ceylon.model.typechecker.model.Package;
import com.redhat.ceylon.model.typechecker.model.Referenceable;
import com.redhat.ceylon.model.typechecker.model.TypeAlias;
import com.redhat.ceylon.model.typechecker.model.TypedDeclaration;

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

    protected final void writeHeader(String title, String... additionalCss) throws IOException {
        write("<!DOCTYPE html>");
        open("html");
        open("head");
        tag("meta charset='UTF-8'");
        around("title", title);

        tag("link href='" + linkRenderer().getResourceUrl("favicon.ico") + "' rel='shortcut icon'");
        tag("link href='" + linkRenderer().getResourceUrl("ceylon.css") + "' rel='stylesheet' type='text/css'");
        tag("link href='" + linkRenderer().getResourceUrl("bootstrap.min.css") + "' rel='stylesheet' type='text/css'");
        tag("link href='" + linkRenderer().getResourceUrl("ceylondoc.css") + "' rel='stylesheet' type='text/css'");
        tag("link href='http://fonts.googleapis.com/css?family=Source+Sans+Pro|Inconsolata|Inconsolata:700|PT+Sans|PT+Sans:700' rel='stylesheet' type='text/css'");
        
        for (String css : additionalCss) {
            if (!css.endsWith(".css")) {
                throw new RuntimeException(CeylondMessages.msg("error.unexpectedAdditionalResource", css));
            }
            tag("link href='" + linkRenderer().getResourceUrl(css) + "' rel='stylesheet' type='text/css'");
        }
        
        close("head");
        open("body");
        
        if (!Util.isEmpty(tool.getHeader())) {
            around("header", tool.getHeader());
        }
    }

    protected final void writeFooter(String... additionalJs) throws IOException {
        open("script type='text/javascript'");
        write("var resourceBaseUrl = '" + tool.getResourceUrl(getFromObject(), "") + "'");
        close("script");
        
        around("script type='text/javascript' src='" + linkRenderer().getResourceUrl("jquery-1.8.2.min.js") + "'");
        around("script type='text/javascript' src='" + linkRenderer().getResourceUrl("bootstrap.min.js") + "'");
        around("script type='text/javascript' src='" + linkRenderer().getResourceUrl("rainbow.min.js") + "'");
        around("script type='text/javascript' src='" + linkRenderer().getResourceUrl("ceylon.js") + "'");
        around("script type='text/javascript' src='" + linkRenderer().getResourceUrl("index.js") + "'");
        around("script type='text/javascript' src='" + linkRenderer().getResourceUrl("ceylondoc.js") + "'");
        
        for (String js : additionalJs) {
            if (!js.endsWith(".js")) {
                throw new RuntimeException(CeylondMessages.msg("error.unexpectedAdditionalResource", js));
            }
            around("script type='text/javascript' src='" + linkRenderer().getResourceUrl(js) + "'");
        }
        
        writeKeyboardShortcuts();
        
        if (!Util.isEmpty(tool.getFooter())) {
            around("footer", tool.getFooter());
        }
        
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

        open("ul class='nav pull-right'");
        write("<li class='divider-vertical' />");
        writeNavBarExpandAllCollapseAll();
        writeNavBarInfoMenu();
        close("ul"); // nav
        
        open("ul class='nav pull-right'");
        write("<li class='divider-vertical' />");
        writeNavBarIndexMenu();
        writeNavBarSearchMenu();
        writeNavBarFilterMenu();
        close("ul"); // nav

        close("div"); // navbar-inner
        close("div"); // navbar
    }
    
    protected void writeNavBarExpandAllCollapseAll() throws IOException {
        write("<li><a class='expand-all' href='#' title='Expand All [Shortcut: + plus]'><i class='icon-expand-all'></i></a></li>");
        write("<li><a class='collapse-all' href='#' title='Collapse All [Shortcut: - minus]'><i class='icon-collapse-all'></i></a></li>");
    }
    
    protected void writeNavBarInfoMenu() throws IOException {
        open("li id='infoDropdown' class='dropdown'");
        write("<a href='#' title='Show keyboard shortcuts [Shortcut: ?]' role='button' class='dropdown-toggle' data-toggle='dropdown'><i class='icon-info'></i></a>");
        open("ul id='info-dropdown-panel' class='dropdown-menu'");
        around("h4", "Keyboard Shortcuts");
        write("<li class='divider'></li>");
        
        open("div class='row-fluid'");
        
        open("div id='info-common-shortcuts' class='span6'");
        writeKeyboardShortcutInfo("f", "Open filter by tags");
        writeKeyboardShortcutInfo("s", "Open search page");
        writeKeyboardShortcutInfo("?", "Open this information panel");
        close("div"); // info-common-shortcuts
        
        open("div id='info-expand-collapse-shortcuts' class='span6'");
        writeKeyboardShortcutInfo("+", "Expand all");
        writeKeyboardShortcutInfo("-", "Collapse all");
        close("div"); // info-expand-collapse-shortcuts
        
        close("div"); // row-fluid
        write("<li class='divider'></li>");
        open("div class='row-fluid'");
        
        open("div id='info-doc-shortcuts' class='span6'");
        around("h5","Documentation:");
        writeKeyboardShortcutInfo("o", "Jump to module documentation");
        writeKeyboardShortcutInfo("p", "Jump to package documentation");
        writeKeyboardShortcutInfo("l", "Jump to aliases");
        writeKeyboardShortcutInfo("n", "Jump to annotations");
        writeKeyboardShortcutInfo("z", "Jump to initializer");
        writeKeyboardShortcutInfo("t", "Jump to constructors");
        if( getFromObject() instanceof Module || getFromObject() instanceof Package ) {
            writeKeyboardShortcutInfo("v", "Jump to values");
            writeKeyboardShortcutInfo("f", "Jump to functions");
        } else {
            writeKeyboardShortcutInfo("a", "Jump to attributes");
            writeKeyboardShortcutInfo("m", "Jump to methods");
        }
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
    }

    private void writeKeyboardShortcutInfo(String key, String info) throws IOException {
        open("div id='"+key+"'");
        around("span class='key badge'", key);
        around("span class='info muted'", info);
        close("div");
    }
    
    protected void writeNavBarIndexMenu() throws IOException {
        write("<li><a href='" + linkRenderer().getResourceUrl("../api-index.html") + "' title='Index'></i>Index</a></li>");
    }

    protected void writeNavBarSearchMenu() throws IOException {
        write("<li><a href='" + linkRenderer().getResourceUrl("../search.html") + "' title='Search this module [Shortcut: S]'><i class='icon-search'></i>Search</a></li>");
    }
    
    protected void writeNavBarFilterMenu() throws IOException {
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
            
            write("  if( !evt.ctrlKey && !evt.altKey ) {\n");
            write("    if (keyCode == " + (int)'?' + ") {\n");
            write("      $('#infoDropdown > .dropdown-toggle').click();\n");
            write("    }\n");
            for(Map.Entry<Character, String> keyboardShortcut : keyboardShortcuts.entrySet()) {
                write("    if(keyCode == "+(int)keyboardShortcut.getKey().charValue()+"){\n");
                write("      document.location = '"+keyboardShortcut.getValue()+"';\n"); 
                write("    }\n");
            }
            write("  }\n"); 
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

            if( decl instanceof ClassOrInterface || decl instanceof Constructor ) {
                if (decl instanceof Interface) {
                    icons.add("icon-interface");
                    if (Util.isEnumerated((ClassOrInterface) decl)) {
                        icons.add("icon-decoration-enumerated");
                    }
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
                    if (klass.isFinal() && !klass.isAnonymous() && !klass.isAnnotation()) {
                        icons.add("icon-decoration-final");
                    }
                    if (Util.isEnumerated(klass)) {
                        icons.add("icon-decoration-enumerated");
                    }
                }
                if (decl instanceof Constructor) {
                    icons.add("icon-class");
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
                if( ((TypedDeclaration) decl).isVariable() ) {
                    icons.add("icon-decoration-variable");
                }
            }
            
            if (decl instanceof TypeAlias || decl instanceof NothingType) {
                icons.add("icon-type-alias");
            }
            
            if (decl.isAnnotation()) {
                icons.add("icon-decoration-annotation");
            }
        }

        if (obj instanceof Package) {
            Package pkg = (Package) obj;

            icons.add("icon-package");
            if (!pkg.isShared()) {
                icons.add("icon-decoration-local");
            }
        }
        
        if (obj instanceof ModuleImport) {
            ModuleImport moduleImport = (ModuleImport) obj;

            icons.add("icon-module");
            if (moduleImport.isExport()) {
                icons.add("icon-module-exported-decoration");
            }
            if (moduleImport.isOptional()) {
                icons.add("icon-module-optional-decoration");
            }
        }

        if (obj instanceof Module) {
            icons.add("icon-module");
        }

        return icons;
    }
    
    protected final void writePackageNavigation(Package pkg) throws IOException {
        open("span class='package-identifier'");
        if (!module.isDefault()) {
            List<String> moduleNames = module.getName();
            List<String> pkgNames = pkg.getName();
            List<String> subpkgNames = pkgNames.subList(moduleNames.size(), pkgNames.size());
            linkRenderer().to(module.getRootPackage()).write();
            if (!subpkgNames.isEmpty()) {
                StringBuilder subpkgNameBuilder = new StringBuilder(module.getNameAsString());
                for (String subpkgName : subpkgNames) {
                    subpkgNameBuilder.append(".").append(subpkgName);
                    Package subpkg = module.getDirectPackage(subpkgNameBuilder.toString());
                    write(".");
                    if (subpkg != null) {
                        linkRenderer().to(subpkg).useCustomText(subpkgName).write();
                    } else {
                        write(subpkgName);
                    }
                }
            }
        } else {
            linkRenderer().to(pkg).write();
        }
        close("span");
    }
    
    protected final void writePackagesTable(String title, List<Package> packages) throws IOException {
        if (!packages.isEmpty()) {
            openTable("section-packages", title, 2, true);
            for (Package pkg : packages) {
                writePackagesTableRow(pkg);
            }
            closeTable();
        }
    }

    protected final void writePackagesTableRow(Package pkg) throws IOException {
        open("tr");

        open("td");
        writeIcon(pkg);
        if (pkg.getNameAsString().isEmpty()) {
            around("a class='link' href='index.html'", "default package");
        } else {
            around("a class='link' href='" + tool.getObjectUrl(getFromObject(), pkg) + "'", pkg.getNameAsString());
        }
        close("td");

        open("td");
        writeTagged(pkg);
        write(Util.getDocFirstLine(pkg, linkRenderer()));
        close("td");

        close("tr");
    }
    
    protected final void writeAnnotations(Referenceable referenceable) throws IOException {
        Tree.AnnotationList annotationList = null;

        Node node = tool.getNode(referenceable);
        if (node instanceof Tree.Declaration) {
            annotationList = ((Tree.Declaration) node).getAnnotationList();
        } else if (node instanceof Tree.ImportModule) {
            annotationList = ((Tree.ImportModule) node).getAnnotationList();
        } else if (node instanceof Tree.ModuleDescriptor) {
            annotationList = ((Tree.ModuleDescriptor) node).getAnnotationList();
        } else if (node instanceof Tree.PackageDescriptor) {
            annotationList = ((Tree.PackageDescriptor) node).getAnnotationList();
        }

        if (annotationList != null) {
            annotationList.visit(new WriteAnnotationsVisitor(referenceable));
        }
    }

    private class WriteAnnotationsVisitor extends Visitor {
    
        private final Referenceable referenceable;
        private Tree.Annotation annotation;
        private Tree.InvocationExpression invocationExpression;
        private Tree.PositionalArgument positionalArgument;
    
        public WriteAnnotationsVisitor(Referenceable referenceable) {
            this.referenceable = referenceable;
        }

        @Override
        public void visit(Tree.AnnotationList that) {
            try {
                boolean containsAnnotations = false;
                for (Tree.Annotation annotation : that.getAnnotations()) {
                    if (!isCeylonLanguageAnnotation(annotation)) {
                        containsAnnotations = true;
                        break;
                    }
                }
                
                if( containsAnnotations ) {
                    open("div class='annotations section'");
                    around("span class='title'", "Annotations: ");
                    open("ul");
                    super.visit(that);
                    close("ul");
                    close("div");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        
        @Override
        public void visit(Tree.AnonymousAnnotation that) {
            // noop
        }
    
        @Override
        public void visit(Tree.Annotation that) {
            try {
                if (isCeylonLanguageAnnotation(that)) {
                    return;
                }
                open("li");
                Tree.Annotation old = annotation;
                annotation = that;
                super.visit(that);
                annotation = old;
                close("li");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    
        @Override
        public void visit(Tree.MemberOrTypeExpression that) {
            try {
                linkRenderer().to(that.getDeclaration()).useScope(referenceable).printParenthesisAfterMethodName(false).write();
                super.visit(that);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    
        @Override
        public void visit(Tree.InvocationExpression that) {
            Tree.InvocationExpression old = invocationExpression;
            invocationExpression = that;
            super.visit(that);
            invocationExpression = old;
        }
    
        @Override
        public void visit(Tree.PositionalArgumentList that) {
            try {
                if (!that.getPositionalArguments().isEmpty() || annotation != invocationExpression) {
                    write("(");
                }
                positionalArgument = null;
                super.visit(that);
                positionalArgument = null;
                if (!that.getPositionalArguments().isEmpty() || annotation != invocationExpression) {
                    write(")");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        
        @Override
        public void visit(Tree.PositionalArgument that) {
            try {
                if (positionalArgument != null) {
                    write(", ");
                }
                super.visit(that);
                positionalArgument = that;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    
        @Override
        public void visit(Tree.NamedArgumentList that) {
            try {
                write("{");
                super.visit(that);
                write("}");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        
        @Override
        public void visit(Tree.NamedArgument that) {
            try {
                write(that.getParameter().getName());
                write("=");
                super.visit(that);
                write(";");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        
        @Override
        public void visit(Tree.SequenceEnumeration that) {
            try {
                Tree.PositionalArgument old = positionalArgument;
                positionalArgument = null;
                write("{");
                super.visit(that);
                write("}");
                positionalArgument = old;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    
        @Override
        public void visit(Tree.Tuple that) {
            try {
                Tree.PositionalArgument old = positionalArgument;
                positionalArgument = null;
                write("[");
                super.visit(that);
                write("]");
                positionalArgument = old;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    
        @Override
        public void visit(Tree.Literal that) {
            try {
                open("span class='literal'");
                if (that instanceof Tree.StringLiteral) {
                    write("&quot;");
                }
                write(that.getText());
                if (that instanceof Tree.StringLiteral) {
                    write("&quot;");
                }
                close("span");
                super.visit(that);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    
        @Override
        public void visit(Tree.MemberLiteral that) {
            try {
                if (that instanceof Tree.ValueLiteral) {
                    write("`");
                    around("span class='keyword'", "value ");
                } else if (that instanceof Tree.FunctionLiteral) {
                    write("`");
                    around("span class='keyword'", "function ");
                }
                linkRenderer().to(that.getDeclaration()).useScope(referenceable).write();
                write("`");
                super.visit(that);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    
        @Override
        public void visit(Tree.TypeLiteral that) {
            try {
                if (that instanceof Tree.AliasLiteral) {
                    write("`");
                    around("span class='keyword'", "alias ");
                } else if (that instanceof Tree.ClassLiteral) {
                    write("`");
                    around("span class='keyword'", "class ");
                } else if (that instanceof Tree.InterfaceLiteral) {
                    write("`");
                    around("span class='keyword'", "interface ");
                } else if (that instanceof Tree.TypeParameterLiteral) {
                    write("`");
                    around("span class='keyword'", "given ");
                }
                linkRenderer().to(that.getDeclaration()).useScope(referenceable).write();
                write("`");
                super.visit(that);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    
        @Override
        public void visit(Tree.ModuleLiteral that) {
            try {
                write("`");
                around("span class='keyword'", "module ");
                linkRenderer().to(that.getImportPath().getModel()).useScope(referenceable).write();
                write("`");
                super.visit(that);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    
        @Override
        public void visit(Tree.PackageLiteral that) {
            try {
                write("`");
                around("span class='keyword'", "package");
                write(" ");
                linkRenderer().to(that.getImportPath().getModel()).useScope(referenceable).write();
                write("`");
                super.visit(that);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private boolean isCeylonLanguageAnnotation(Tree.Annotation annotation) {
            if (annotation.getPrimary() instanceof Tree.MemberOrTypeExpression) {
                Declaration declaration = ((Tree.MemberOrTypeExpression) annotation.getPrimary()).getDeclaration();
                if (declaration.getQualifiedNameString().startsWith(AbstractModelLoader.CEYLON_LANGUAGE)) {
                    return true;
                }
            }
            return false;
        }
    
    }    

    protected final <T extends Referenceable & Annotated> void writeTagged(T decl) throws IOException {
        List<String> tags = Util.getTags(decl);
        if (!tags.isEmpty()) {
            open("div class='tags section'");
            Iterator<String> tagIterator = tags.iterator();
            while (tagIterator.hasNext()) {
                String tag = tagIterator.next();
                write("<a class='tag label' name='" + tag + "' href='javascript:;' title='Enable/disable tag filter'>" + tag + "</a>");
            }
            close("div");
        }
    }

    protected abstract Object getFromObject();    
    
}