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

import static com.redhat.ceylon.ceylondoc.CeylondMessages.msg;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.compiler.java.codegen.Decl;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Element;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Unit;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.util.ProducedTypeNamePrinter;

public class LinkRenderer {
    
    private static final Map<String, Boolean> checkModuleUrlCache = new HashMap<String, Boolean>();
    
    private Object to;
    private Object from;
    private CeylonDocTool ceylonDocTool;
    private Writer writer;
    private String customText;
    private Scope scope;
    private Declaration anchor;
    private boolean printAbbreviated = true;
    private boolean printTypeParameters = true;
    private boolean printTypeParameterDetail = false;
    
    private final ProducedTypeNamePrinter producedTypeNamePrinter = new ProducedTypeNamePrinter() {
        
        @Override
        public String getSimpleDeclarationName(Declaration declaration, Unit unit) {
            String result = null;
            
            if (declaration instanceof ClassOrInterface) {
                ClassOrInterface clazz = (ClassOrInterface) declaration;
                String clazzUrl = getUrl(clazz, null);
                result = clazzUrl != null ? buildLinkElement(clazzUrl, clazz.getName()) : clazz.getName();
            } else if (declaration instanceof TypeParameter) {
                result = "<span class='type-parameter'>" + declaration.getName(unit) + "</span>";
            } else if (declaration instanceof TypedDeclaration) {
                result = processTypedDeclaration((TypedDeclaration) declaration);
            } else {
                result = declaration.getName();
            }
            
            return encodeResult(result);
        }

        @Override
        public boolean printAbbreviated() {
            return printAbbreviated;
        }

        @Override
        public boolean printTypeParameters() {
            return printTypeParameters;
        }

        @Override
        public boolean printTypeParameterDetail() {
            return printTypeParameterDetail;
        }

        @Override
        public boolean printQualifyingType() {
            return false;
        }
        
    };
    
    public LinkRenderer(CeylonDocTool ceylonDocTool, Writer writer, Object from) {
        this.ceylonDocTool = ceylonDocTool;
        this.writer = writer;
        this.from = from;
    }
    
    public LinkRenderer(LinkRenderer linkRenderer) {
        this.to = linkRenderer.to;
        this.from = linkRenderer.from;
        this.ceylonDocTool = linkRenderer.ceylonDocTool;
        this.writer = linkRenderer.writer;
        this.customText = linkRenderer.customText;
        this.scope = linkRenderer.scope;
        this.anchor = linkRenderer.anchor;
        this.printAbbreviated = linkRenderer.printAbbreviated;
        this.printTypeParameters = linkRenderer.printTypeParameters;
        this.printTypeParameterDetail = linkRenderer.printTypeParameterDetail;
    }
    
    public LinkRenderer to(Object to) {
        this.to = to;
        return this;
    }

    public LinkRenderer useCustomText(String customText) {
        this.customText = customText;
        return this;
    }
    
    public LinkRenderer useScope(Module module) {
        scope = module.getPackage(module.getNameAsString());
        return this;
    }

    public LinkRenderer useScope(Package pkg) {
        scope = pkg;
        return this;
    }

    public LinkRenderer useScope(Declaration decl) {
        scope = resolveScope(decl);
        return this;
    }
    
    public LinkRenderer useAnchor(Declaration anchor) {
        this.anchor = anchor;
        return this;
    }
    
    public LinkRenderer printAbbreviated(boolean printAbbreviated) {
        this.printAbbreviated = printAbbreviated;
        return this;
    }

    public LinkRenderer printTypeParameters(boolean printTypeParameters) {
        this.printTypeParameters = printTypeParameters;
        return this;
    }
    
    public LinkRenderer printTypeParameterDetail(boolean printTypeParameterDetail) {
        this.printTypeParameterDetail = printTypeParameterDetail;
        return this;
    }

    public String getLink() {
        String link = null;
        if (to instanceof String) {
            link = processWikiLink((String) to);
        } else if (to instanceof ProducedType) {
            link = processProducedType((ProducedType) to);
        } else if (to instanceof TypeDeclaration) {
            link = processProducedType(((TypeDeclaration) to).getType());
        } else if (to instanceof TypedDeclaration) {
            link = processTypedDeclaration((TypedDeclaration) to);
        } else if (to instanceof Module) {
            link = processModule((Module) to);
        } else if (to instanceof Package) {
            link = processPackage((Package) to);
        }
        return link;
    }
    
    public String getUrl() {
        return getUrl(to, anchor);
    }
    
    public String getResourceUrl(String to) throws IOException {
        return ceylonDocTool.getResourceUrl(from, to);
    }

    public String getSrcUrl(Object to) throws IOException {
        return ceylonDocTool.getSrcUrl(from, to);
    }

    public void write() throws IOException {
        String link = getLink();
        writer.write(link);
    }

    private String processModule(Module module) {
        String moduleUrl = getUrl(module, anchor);
        return buildLinkElement(moduleUrl, module.getNameAsString());
    }
    
    private String processPackage(Package pkg) {
        String pkgUrl = getUrl(pkg, anchor);
        return buildLinkElement(pkgUrl, pkg.getNameAsString());
    }

    private String processProducedType(ProducedType producedType) {
        String result = producedTypeNamePrinter.getProducedTypeName(producedType, null);
        return decodeResult(result);
    }
    
    private String processTypedDeclaration(TypedDeclaration decl) {
        String declName = decl.getName();
        Scope declContainer = decl.getContainer();
        
        String url = getUrl(declContainer, decl);
        if( url != null ) {
            return buildLinkElement(url, declName);
        } else {
            return declName;
        }
    }

    private String processWikiLink(String declLink) {
        String declName;
        Scope currentScope;
        
        int pkgSeparatorIndex = declLink.indexOf("::");
        if( pkgSeparatorIndex == -1 ) {
            declName = declLink;
            currentScope = scope;
        } else {
            String pkgName = declLink.substring(0, pkgSeparatorIndex);
            declName = declLink.substring(pkgSeparatorIndex+2, declLink.length());
            currentScope = ceylonDocTool.getCurrentModule().getPackage(pkgName);
        }
        
        String[] declNames = declName.split("\\.");
        Declaration currentDecl = null;
        boolean isNested = false;
        for (String currentDeclName : declNames) {
            currentDecl = resolveDeclaration(currentScope, currentDeclName, isNested);
            if (currentDecl != null) {
                if( isValueWithTypeObject(currentDecl) ) {
                    TypeDeclaration objectType = ((Value)currentDecl).getTypeDeclaration();
                    currentScope = objectType;
                    currentDecl = objectType;
                } else {
                    currentScope = resolveScope(currentDecl);
                }
                isNested = true;
            } else {
                break;
            }
        }
        
        // we can't link to parameters yet, unless they're toplevel
        if (currentDecl != null && !isParameter(currentDecl)) {
            if (currentDecl instanceof TypeDeclaration) {
                return processProducedType(((TypeDeclaration) currentDecl).getType());
            } else {
                return processTypedDeclaration((TypedDeclaration) currentDecl);
            }
        } else {
            return declLink;
        }
    }

    private boolean isValueWithTypeObject(Declaration decl) {
        if (Decl.isValue(decl)) {
            TypeDeclaration typeDeclaration = ((Value) decl).getTypeDeclaration();
            if (typeDeclaration instanceof Class && typeDeclaration.isAnonymous()) {
                return true;
            }
        }
        return false;
    }

    private boolean isParameter(Declaration decl) {
        if(decl instanceof Parameter)
            return true;
        if(Decl.isValue(decl) == false)
            return false;
        Value value = (Value)decl;
        return !value.isToplevel() && !value.isClassOrInterfaceMember();
    }
    
    private Declaration resolveDeclaration(Scope scope, String declName, boolean isNested) {
        Declaration decl = null;

        if (scope != null) {
            decl = scope.getMember(declName, null, false);

            if (decl == null && !isNested && scope instanceof Element) {
                decl = ((Element) scope).getUnit().getImportedDeclaration(declName, null, false);
            }

            if (decl == null && !isNested) {
                decl = resolveDeclaration(scope.getContainer(), declName, isNested);
            }
        }

        return decl;
    }

    private Scope resolveScope(Declaration decl) {
        if (decl == null) {
            return null;
        } else if (decl instanceof Scope) {
            return (Scope) decl;
        } else {
            return decl.getContainer();
        }
    }

    private boolean isInCurrentModule(Object obj) {
        Module objModule = null;
        if (obj instanceof Module) {
            objModule = (Module) obj;
        } else if (obj instanceof Scope) {
            objModule = getPackage((Scope) obj).getModule();
        } else if (obj instanceof Element) {
            objModule = getPackage(((Element) obj).getScope()).getModule();
        }
        
        Module currentModule = ceylonDocTool.getCurrentModule();
        if (currentModule != null && objModule != null) {
            return currentModule.equals(objModule);
        }
        
        return false;
    }
    
    private Package getPackage(Scope scope) {
        while (!(scope instanceof Package)) {
            scope = scope.getContainer();
        }
        return (Package) scope;
    }   

    private String getUrl(Object to, Declaration anchor) {
        String url;
        
        if (isInCurrentModule(to)) {
            url = getLocalUrl(to);
        } else {
            url = getExternalUrl(to);
        }        
            
        if (url != null && anchor != null) {
            String sectionPackageAnchor = "#section-package";
            if (url.endsWith(sectionPackageAnchor)) {
                url = url.substring(0, url.length() - sectionPackageAnchor.length());
            }
            url = url + "#" + anchor.getName();
        }            
            
        return url;
    }
    
    private String getLocalUrl(Object to) {
        try {
            return ceylonDocTool.getObjectUrl(from, to);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getExternalUrl(Object to) {
        String url = null;
        if (to instanceof Module) {
            url = getExternalModuleUrl((Module)to);
            if (url != null) {
                url += "index.html";
            }
        } else if (to instanceof Package) {
            Package pkg = (Package)to;
            url = getExternalModuleUrl(pkg.getModule());
            if (url != null) {
                url += buildPackageUrlPath(pkg);
                url += "index.html";
            }
        } else if (to instanceof ClassOrInterface) {
            ClassOrInterface klass = (ClassOrInterface) to;
            Package pkg = getPackage(klass);
            url = getExternalModuleUrl(pkg.getModule());
            if (url != null) {
                url += buildPackageUrlPath(pkg);
                url += ceylonDocTool.getFileName(klass);
            }
        }
        return url;
    }
    
    private String getExternalModuleUrl(Module module) {
        if( ceylonDocTool.getLinks() != null ) {
            String moduleName = module.getNameAsString();
            
            for (String link : ceylonDocTool.getLinks()) {
                String[] linkParts = divideToPatternAndUrl(link);
                String moduleNamePattern = linkParts[0];
                String moduleRepoUrl = linkParts[1];
                
                if (moduleNamePattern == null) {
                    String moduleDocUrl = buildModuleUrl(moduleRepoUrl, module);
                    if (isHttpProtocol(moduleDocUrl) && checkHttpUrlExist(moduleDocUrl)) {
                        return moduleDocUrl;
                    }
                    if (isFileProtocol(moduleDocUrl) && checkFileUrlExist(moduleDocUrl)) {
                        return moduleDocUrl;
                    }
                } else if (moduleName.startsWith(moduleNamePattern)) {
                    return buildModuleUrl(moduleRepoUrl, module);
                }
            }
        }
        return null;
    }
    
    private String buildLinkElement(String url, String text) {
        StringBuilder linkBuilder = new StringBuilder();
        linkBuilder.append("<a class='link' href='").append(url).append("'>");
        if( customText != null ) {
            linkBuilder.append(customText);
        } else {
            linkBuilder.append(text);
        }
        linkBuilder.append("</a>");
        return linkBuilder.toString();
    }    

    private String buildModuleUrl(String moduleRepoUrl, Module module) {
        StringBuilder moduleUrlBuilder = new StringBuilder();
        moduleUrlBuilder.append(moduleRepoUrl);
        if (!moduleRepoUrl.endsWith("/")) {
            moduleUrlBuilder.append("/");
        }
        moduleUrlBuilder.append(Util.join("/", module.getName()));
        moduleUrlBuilder.append("/");
        moduleUrlBuilder.append(module.getVersion());
        moduleUrlBuilder.append("/module-doc/");
        return moduleUrlBuilder.toString();
    }
    
    private String buildPackageUrlPath(Package pkg) {
        List<String> packagePath = pkg.getName().subList(pkg.getModule().getName().size(), pkg.getName().size());
        if (!packagePath.isEmpty()) {
            return Util.join("/", packagePath) + "/";
        }
        return "";
    }
    
    public static String[] divideToPatternAndUrl(String link) {
        String moduleRepoUrl = null;
        String moduleNamePattern = null;

        int indexOfSeparator = link.indexOf("=");
        if (indexOfSeparator != -1) {
            moduleNamePattern = link.substring(0, indexOfSeparator);
            moduleRepoUrl = link.substring(indexOfSeparator + 1);
        } else {
            moduleRepoUrl = link;
        }

        return new String[] { moduleNamePattern, moduleRepoUrl };
    }
    
    public static boolean isHttpProtocol(String url) {
        return url.startsWith("http://") || url.startsWith("https://");
    }

    public static boolean isFileProtocol(String url) {
        return url.startsWith("file://");
    }

    private boolean checkHttpUrlExist(String moduleUrl) {
        Boolean result = checkModuleUrlCache.get(moduleUrl);
        if( result == null ) {
            try {
                HttpURLConnection con = (HttpURLConnection) new URL(moduleUrl + "index.html").openConnection();
                con.setRequestMethod("HEAD");
                int responseCode = con.getResponseCode();
    
                if( responseCode == HttpURLConnection.HTTP_OK ) {
                    result = Boolean.TRUE;                
                } else {
                    ceylonDocTool.getLogger().info(msg("info.urlDoesNotExist", moduleUrl));
                    result = Boolean.FALSE;
                }
            }
            catch (IOException e) {
                ceylonDocTool.getLogger().info(msg("info.urlDoesNotExist", moduleUrl));
                result = Boolean.FALSE;
            }
            checkModuleUrlCache.put(moduleUrl, result);
        }
        return result.booleanValue();
    }
    
    private boolean checkFileUrlExist(String moduleUrl) {
        Boolean result = checkModuleUrlCache.get(moduleUrl);
        if( result == null ) {
            File moduleDocDir = new File(moduleUrl.substring("file://".length()));
            if (moduleDocDir.isDirectory() && moduleDocDir.exists()) {
                result = Boolean.TRUE;
            } else {
                ceylonDocTool.getLogger().info(msg("info.urlDoesNotExist", moduleUrl));
                result = Boolean.FALSE;
            }
        }
        return result.booleanValue();
    }
    
    private static String encodeResult(String text) {
        if (text != null) {
            text = text.replaceAll("<", "#LT;");
            text = text.replaceAll(">", "#GT;");
        }
        return text;
    }
    
    private static String decodeResult(String text) {
        if (text != null) {
            text = text.replaceAll("&", "&amp;");
            text = text.replaceAll("<", "&lt;");
            text = text.replaceAll(">", "&gt;");
            text = text.replaceAll("#LT;", "<");
            text = text.replaceAll("#GT;", ">");

            text = text.replaceAll("&lt;in ", "&lt;<span class='type-parameter-keyword'>in </span>");
            text = text.replaceAll(",in ", ",<span class='type-parameter-keyword'>in </span>");

            text = text.replaceAll("&lt;out ", "&lt;<span class='type-parameter-keyword'>out </span>");
            text = text.replaceAll(",out ", ",<span class='type-parameter-keyword'>out </span>");
        }
        return text;
    }    
    
}