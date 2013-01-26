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

import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Element;
import com.redhat.ceylon.compiler.typechecker.model.IntersectionType;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.UnionType;
import com.redhat.ceylon.compiler.typechecker.model.Unit;
import com.redhat.ceylon.compiler.typechecker.model.Value;

public class LinkRenderer {
    
    private static final Map<String, Boolean> checkModuleUrlCache = new HashMap<String, Boolean>();
    private StringBuffer buffer = new StringBuffer();
    private Object to;
    private Object from;
    private CeylonDocTool ceylonDocTool;
    private Writer writer;
    private String customText;
    private Scope scope;
    private Declaration anchor;
    private boolean skipTypeArguments;
    private boolean forDeclaration = false;
    
    public LinkRenderer(CeylonDocTool ceylonDocTool, Writer writer, Object from) {
        this.ceylonDocTool = ceylonDocTool;
        this.writer = writer;
        this.from = from;
    }
    
    public LinkRenderer(LinkRenderer linkRenderer) {
        this.buffer = linkRenderer.buffer;
        this.to = linkRenderer.to;
        this.from = linkRenderer.from;
        this.ceylonDocTool = linkRenderer.ceylonDocTool;
        this.writer = linkRenderer.writer;
        this.customText = linkRenderer.customText;
        this.scope = linkRenderer.scope;
        this.anchor = linkRenderer.anchor;
        this.skipTypeArguments = linkRenderer.skipTypeArguments;
        this.forDeclaration = linkRenderer.forDeclaration;
    }
    
    public LinkRenderer to(Object to) {
        this.to = to;
        return this;
    }

    /**
     * Include things like in/out/default value of Type Parameters
     */
    public LinkRenderer forDeclaration(boolean forDeclaration) {
        this.forDeclaration = forDeclaration;
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

    public LinkRenderer skipTypeArguments() {
        this.skipTypeArguments = true;
        return this;
    }
    
    public String getLink() {
        try {
            if (to instanceof String) {
                processDeclarationLink((String) to);
            } else if (to instanceof ProducedType) {
                processProducedType((ProducedType) to);
            } else if (to instanceof IntersectionType) {
                processIntersectionType((IntersectionType) to);
            } else if (to instanceof UnionType) {
                processUnionType((UnionType) to);
            } else if (to instanceof ClassOrInterface) {
                processClassOrInterface((ClassOrInterface) to, null);
            } else if (to instanceof Declaration) {
                processDeclaration((Declaration) to);
            } else if (to instanceof Module) {
                processModule((Module) to);
            } else if (to instanceof Package) {
                processPackage((Package) to);
            }
            return buffer.toString();
        } finally {
            buffer.setLength(0);
        }
    }
    
    public String getUrl() {
        return getUrl(to);
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

    private void processModule(Module module) {
        String moduleUrl = getUrl(module);
        buffer.append(buildLinkElement(moduleUrl, module.getNameAsString()));
    }
    
    private void processPackage(Package pkg) {
        String pkgUrl = getUrl(pkg);
        buffer.append(buildLinkElement(pkgUrl, pkg.getNameAsString()));
    }

    private void processProducedType(ProducedType producedType) {
        if (producedType != null) {
            TypeDeclaration typeDeclaration = producedType.getDeclaration();
            Unit unit = typeDeclaration.getUnit();
            if (typeDeclaration instanceof IntersectionType) {
                processIntersectionType((IntersectionType) typeDeclaration);
            } else if (typeDeclaration instanceof UnionType) {
                processUnionType((UnionType) typeDeclaration);
            } else if (typeDeclaration instanceof ClassOrInterface) {
                // special sugar for Sequential<Foo>
                if(typeDeclaration == unit.getSequentialDeclaration()){
                    ProducedType iteratedType = unit.getIteratedType(producedType);
                    // process the iterated type rather than this
                    processProducedType(iteratedType);
                    buffer.append("[]");
                }else {
                    processClassOrInterface((ClassOrInterface) typeDeclaration, producedType.getTypeArgumentList());
                }
            } else if (typeDeclaration instanceof TypeParameter) {
                buffer.append("<span class='type-parameter'>").append(typeDeclaration.getName()).append("</span>");
            } else {
                buffer.append(producedType.getProducedTypeName());
            }
        }
    }

    private void processIntersectionType(IntersectionType intersectionType) {
        boolean first = true;
        for (ProducedType st : intersectionType.getSatisfiedTypes()) {
            if (first) {
                first = false;
            } else {
                buffer.append("&amp;");
            }
            processProducedType(st);
        }
    }

    private void processUnionType(UnionType unionType) {
        if( isOptionalTypeAbbreviation(unionType) ) {
            ProducedType nonOptionalType = getNonOptionalTypeForDisplay(unionType);
            processProducedType(nonOptionalType);
            buffer.append("?");
            return;
        }
        
        boolean first = true;
        for (ProducedType producedType : unionType.getCaseTypes()) {
            if (first) {
                first = false;
            } else {
                buffer.append("|");
            }
            processProducedType(producedType);
        }        
    }

    private void processClassOrInterface(ClassOrInterface clazz, List<ProducedType> typeArguments) {
        String clazzName = clazz.getName();
        
        String clazzUrl = getUrl(clazz);
        if (clazzUrl != null) {
            buffer.append(buildLinkElement(clazzUrl, clazzName));
        } else {
            buffer.append(clazzName);
        }

        if (!skipTypeArguments) {
            if (typeArguments != null) {
                processTypeParameterList(clazz.getTypeParameters(), typeArguments);
            } else {
                processTypeParameterList(clazz.getTypeParameters(), null);
            }
        }
    }

    private void processTypeParameterList(List<TypeParameter> typeParameters, List<ProducedType> typeArguments) {
        if (typeParameters != null && !typeParameters.isEmpty()){
            // find the last non-defaulted arg
            int firstDefaultedTypeArgument = 0;
            if(!forDeclaration && typeArguments != null){
                firstDefaultedTypeArgument = typeArguments.size();
                for(int i=typeArguments.size()-1;i>=0;i--){
                    TypeParameter typeParameter = typeParameters.get(i);
                    if(!typeParameter.isDefaulted())
                        break; // not defaulted, stop
                    if(typeArguments.get(i).isExactly(typeParameter.getDefaultTypeArgument()))
                        firstDefaultedTypeArgument = i;
                    else
                        break; // found first non-default one so stop
                }
            }
            if(forDeclaration || typeArguments == null || firstDefaultedTypeArgument > 0) {
                buffer.append("<span class='type-parameter'>");
                buffer.append("&lt;");
                boolean first = true;
                int i = 0;
                for (TypeParameter typeParam : typeParameters) {
                    if(!forDeclaration && typeArguments != null && i == firstDefaultedTypeArgument)
                        break;
                    ProducedType typeParamType;
                    if(typeArguments != null)
                        typeParamType = typeArguments.get(i);
                    else
                        typeParamType = typeParam.getType();

                    if (first) {
                        first = false;
                    } else {
                        buffer.append(", ");
                    }

                    if (forDeclaration) {
                        if (typeParam.isContravariant()) {
                            buffer.append("<span class='type-parameter-keyword'>in </span>");
                        }
                        if (typeParam.isCovariant()) {
                            buffer.append("<span class='type-parameter-keyword'>out </span>");
                        }
                    }

                    boolean oldForDeclaration = forDeclaration;
                    forDeclaration = false;
                    processProducedType(typeParamType);
                    forDeclaration = oldForDeclaration;
                    
                    if (forDeclaration) {
                        if (typeParam.isDefaulted()) {
                            if (typeParam.getDefaultTypeArgument() != null) {
                                buffer.append("<span class='type-parameter-keyword'> = </span>");
                                oldForDeclaration = forDeclaration;
                                forDeclaration = false;
                                processProducedType(typeParam.getDefaultTypeArgument());
                                forDeclaration = oldForDeclaration;
                            } else {
                                buffer.append("<span class='type-parameter-keyword'>=</span>");
                            }
                        }
                    }
                    i++;
                }
                buffer.append("&gt;");
                buffer.append("</span>");
            }
        }
    }
    
    private void processDeclaration(Declaration decl) {
        String declName = decl.getName();
        Scope declContainer = decl.getContainer();
        
        if (anchor != null) {
            throw new IllegalArgumentException();
        }
        anchor = decl;
        
        String url = getUrl(declContainer);
        if( url != null ) {
            buffer.append(buildLinkElement(url, declName));
        } else {
            buffer.append(declName);
        }
    }

    private void processDeclarationLink(String declLink) {
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
        if (currentDecl != null && 
                !isParameter(currentDecl)) {
            if (currentDecl instanceof ClassOrInterface) {
                processClassOrInterface((ClassOrInterface) currentDecl, null);
            } else {
                processDeclaration(currentDecl);
            }
        } else {
            buffer.append(declLink);
        }
    }

    private boolean isValueWithTypeObject(Declaration decl) {
        if (decl instanceof Value) {
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
        if(decl instanceof Value == false)
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

    private boolean isOptionalTypeAbbreviation(UnionType unionType) {
        return unionType.getCaseTypes().size() == 2 &&
                com.redhat.ceylon.compiler.typechecker.model.Util.isElementOfUnion(
                        unionType, unionType.getUnit().getNullDeclaration());
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

    /**
     * When parameter is <code>UnionType[Element?]</code>, we can not use method <code>Unit.getDefiniteType()</code>, 
     * because its result is <code>IntersectionType[Element&Object]</code> and to html is rendered <code>Element&Object?</code>.
     */
    private ProducedType getNonOptionalTypeForDisplay(UnionType unionType) {
        ProducedType nonOptionalType = null;
        Class nothingDeclaration = unionType.getUnit().getNullDeclaration();
        for (ProducedType ct : unionType.getCaseTypes()) {
            TypeDeclaration ctd = ct.getDeclaration();
            if (ctd instanceof Class && ctd.equals(nothingDeclaration)) {
                continue;
            } else {
                nonOptionalType = ct;
                break;
            }
        }
        return nonOptionalType;
    }

    private String getUrl(Object to) {
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
            Package pkg = getPackage(((ClassOrInterface) to));
            url = getExternalModuleUrl(pkg.getModule());
            if (url != null) {
                url += buildPackageUrlPath(pkg);
                url += ceylonDocTool.kind(to) + "_" + ceylonDocTool.getFileName((Scope)to) + ".html";
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
}