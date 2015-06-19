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
package com.redhat.ceylon.compiler.java.test.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.Assert;

import com.redhat.ceylon.compiler.java.metadata.Variance;

import static com.redhat.ceylon.compiler.java.metadata.Variance.*;

import com.redhat.ceylon.model.loader.ModelLoader;
import com.redhat.ceylon.model.loader.ModelResolutionException;
import com.redhat.ceylon.model.loader.TypeParser;
import com.redhat.ceylon.model.loader.TypeParserException;
import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.model.typechecker.model.Interface;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.IntersectionType;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.Package;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.Scope;
import com.redhat.ceylon.model.typechecker.model.SiteVariance;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.model.typechecker.model.TypeParameter;
import com.redhat.ceylon.model.typechecker.model.UnionType;
import com.redhat.ceylon.model.typechecker.model.Unit;
import com.redhat.ceylon.model.typechecker.util.TypePrinter;

public class TypeParserTests {
    private static final Unit mockLangUnit = new Unit();
    private static final Unit mockPkgUnit = new Unit();
    private static final Unit mockDefaultUnit = new Unit();
    
    private static final class MockPackage extends Package {
        ArrayList<Declaration> members = new ArrayList<>();
        private final Module module;
        public MockPackage(Module module) {
            this.module = module;
            super.setModule(module);
        }

        public Module getModule() {
            return module;
        }

        @Override
        public void addMember(Declaration m) {
            members.add(m);
        }

        @Override
        public List<Declaration> getMembers() {
            return members;
        }
    }
    
    private static final Module mockLang = new Module(){
        public Package getPackage(String name) {
            return mockLangPackage;
        }
    };
    private static final Package mockLangPackage = new MockPackage(mockLang);
    
    static {
        mockLang.setUnit(mockLangUnit);
        mockLang.setLanguageModule(mockLang);
        mockLang.setName(Arrays.asList("ceylon", "language"));
        mockLang.setAvailable(true);
    
        mockLangPackage.setUnit(mockLangUnit);
        mockLangPackage.setName(Arrays.asList("ceylon", "language"));
        
        mockLang.getPackages().add(mockLangPackage);
        mockLangUnit.setPackage(mockLangPackage);
    }
    
    
    private static final Module mockDefaultModule = new Module(){
        
        public Package getPackage(String name) {
            if (name.isEmpty()) {
                return mockDefaultPackage;
            } else if ("pkg".equals(name)) {
                return mockDefaultPackage;
            } else if ("ceylon.language".equals(name)) {
                return mockLangPackage;
            }
            throw new RuntimeException(name);
        }
    };
    private static final Package mockDefaultPackage = new MockPackage(mockDefaultModule);
    static {
        mockDefaultModule.setLanguageModule(mockLang);
        mockDefaultModule.setName(Arrays.asList(""));
        mockDefaultModule.setUnit(mockDefaultUnit);
    
        mockDefaultPackage.setUnit(mockDefaultUnit);
        mockDefaultPackage.setName(Arrays.asList(""));
        mockDefaultUnit.setPackage(mockDefaultPackage);
    }
    
    private static final Module mockPkgModule = new Module(){
        
        public Package getPackage(String name) {
            if ("pkg".equals(name)) {
                return mockPkgPackage;
            } else if (name.isEmpty()) {
                return mockDefaultPackage;
            } else if ("ceylon.language".equals(name)) {
                return mockLangPackage;
            }
            throw new RuntimeException();
        }
    };
    private static final Package mockPkgPackage = new MockPackage(mockPkgModule);
    static {
        mockPkgModule.setLanguageModule(mockLang);
        mockPkgModule.setName(Arrays.asList("pkg"));
        mockPkgModule.setUnit(mockPkgUnit);
    
        mockPkgPackage.setUnit(mockPkgUnit);
        mockPkgPackage.setName(Arrays.asList("pkg"));
        mockPkgUnit.setPackage(mockPkgPackage);
    }
    
    static class MockLoader implements ModelLoader {

        static final ModelLoader instance = new MockLoader();

        private Map<String, ClassOrInterface> classes = new HashMap<String,ClassOrInterface>();
        private Module defaultModule = mockDefaultModule;
        private Package defaultPkg = mockDefaultModule.getPackage("");
        private Unit unit = mockDefaultUnit;
        private Module lang = mockLang;
        private Package langPkg = mockLang.getPackage("ceylon.language");
        
        private MockLoader(){
            //defaultModule.setName(Arrays.asList(""));
            //defaultModule.setLanguageModule(lang);
            //defaultPkg.setName(Arrays.asList(""));
            //defaultPkg.setModule(defaultModule);
            //unit.setPackage(defaultPkg);
            Class a = makeClass("a", defaultPkg);
            makeClass("b", a);
            makeClass("b", defaultPkg);
            makeClass("c", defaultPkg);
            makeClass("d", defaultPkg);
            makeClass("e", defaultPkg);
            makeClass("f", defaultPkg);
            Class t2 = makeParameterisedClass(defaultPkg, "t2", null, null, makeTp("A", Variance.NONE), makeTp("B", Variance.NONE));
            makeParameterisedClass(t2, "t2", null, null, makeTp("A", Variance.NONE), makeTp("B", Variance.NONE));
            Package otherPkg = new Package();
            otherPkg.setUnit(mockPkgUnit);
            otherPkg.setName(Arrays.asList("pkg"));
            Module otherModeul = new Module();
            otherModeul.setUnit(mockPkgUnit);
            otherPkg.setModule(otherModeul);
            makeClass("u", otherPkg);
            Class b = makeClass("v", otherPkg);
            makeClass("w", b);
            makeClass("package", otherPkg);
            
            langPkg.addMember(makeInterface("Empty", langPkg));
            langPkg.addMember(makeClass("Null", langPkg));
            langPkg.addMember(makeClass("Boolean", langPkg));
            langPkg.addMember(makeInterface("Nothing", langPkg));// a lie, it's not an interface
            
            TypeParameter itElement = makeTp("Element", OUT);
            Interface iterable = makeParameterisedInterface(langPkg, "Iterable", null, 
                    itElement, 
                    makeTp("Absent", OUT));
            langPkg.addMember(iterable);
            TypeParameter seqElement = makeTp("Element", OUT);
            Interface sequential = makeParameterisedInterface(langPkg, 
                    "Sequential", new Type[]{iterable.appliedType(null, Collections.singletonList(seqElement.getType()))},seqElement);
            langPkg.addMember(sequential);
            TypeParameter seqlElement = makeTp("Element", OUT);
            Interface sequence = makeParameterisedInterface(langPkg, 
                    "Sequence", new Type[]{sequential.appliedType(null, Collections.singletonList(seqlElement.getType()))}, 
                    seqlElement);
            langPkg.addMember(sequence);
            
            TypeParameter tupElement = makeTp("Element", OUT);
            langPkg.addMember(makeParameterisedClass(langPkg, "Tuple", null, new Type[]{sequential.appliedType(null, Collections.singletonList(tupElement.getType()))},
                    tupElement, 
                    makeTp("First", OUT),
                    makeTp("Rest", OUT)));
            
            langPkg.addMember(makeParameterisedInterface(langPkg, "Callable", null,
                    makeTp("Result", OUT), makeTp("Arguments", IN)));
            langPkg.addMember(makeParameterisedClass(langPkg, "Entry", null, null,
                    makeTp("Key", OUT), 
                    makeTp("Item", OUT)));
        }

        
        @Override
        public Type getType(Module module, String pkg, String name, Scope scope) {
            Class klass = (Class)getDeclaration(module, pkg, name, scope);
            return klass.getType();
        }

        @Override
        public Declaration getDeclaration(Module module, String pkg, String name, Scope scope) {
            ClassOrInterface klass = classes.get(name);
            if(klass == null)
                throw new ModelResolutionException("Unknown type: "+name);
            return klass;
        }
        
        private Class makeParameterisedClass(String name) {
            return makeParameterisedClass(null, name, null, null);
        }
        
        private Class makeParameterisedClass(Scope container, String name, Type extended, Type[] satisfied, TypeParameter...  tps) {
            return makeParameterizedClassOrInterface(makeClass(name, container), tps, extended, satisfied);
        }
        private Interface makeParameterisedInterface(Scope container, String name, Type[] satisfied, TypeParameter...tps) {
            return makeParameterizedClassOrInterface(makeInterface(name, container), tps, null, satisfied);
        }
        private <T extends ClassOrInterface> T makeParameterizedClassOrInterface(T klass, TypeParameter[] tps, Type extended, Type[] satisfied) {
            if (extended != null) {
                klass.setExtendedType(extended);
            }
            if (satisfied != null) {
                klass.setSatisfiedTypes(Arrays.asList(satisfied));
            }
            List<TypeParameter> typeParameters = new ArrayList<TypeParameter>(tps.length);
            for (TypeParameter tp : tps) {
                tp.setUnit(klass.getUnit());
                tp.setContainer(klass);
                tp.setDeclaration(klass);
                typeParameters.add(tp);
            }
            klass.setTypeParameters(typeParameters );
            return klass;
        }

        TypeParameter makeTp(String name, Variance v) {
            TypeParameter typeParam = new TypeParameter();
            
            typeParam.setName(name);
            typeParam.setContravariant(v == Variance.IN);
            typeParam.setCovariant(v == Variance.OUT);
            return typeParam;
        }
        
        private Class makeClass(String name, Scope container) {
            return makeClassOrInterface(new Class(), name, container);
        }
        private Interface makeInterface(String name, Scope container) {
            return makeClassOrInterface(new Interface(), name, container);
        }
        private <T extends ClassOrInterface> T makeClassOrInterface(T klass, String name, Scope container) {
            klass.setName(name);
            klass.setShared(true);
            
            container.addMember(klass);
            klass.setContainer(container);
            if (container instanceof Package) {
                klass.setUnit(((Package)container).getUnit());
            } else if (container instanceof ClassOrInterface) {
                klass.setUnit(((ClassOrInterface)container).getUnit());
            } else {
                throw new RuntimeException(""+container);
            }
            if (container.getQualifiedNameString().isEmpty()) {
                classes.put(name, klass);
            } else {
                classes.put(container.getQualifiedNameString()+"."+name, klass);
            }
            
            return klass;
        }

        @Override
        public Declaration getDeclaration(Module module, String typeName, DeclarationType declarationType) {
            throw new RuntimeException("Not yet implemented");
        }


        @Override
        public Module getLoadedModule(String moduleName, String version) {
            throw new RuntimeException("Not yet implemented");
        }
        
    }
    
    @Test
    public void testUnion(){
        Type type = new TypeParser(MockLoader.instance).decodeType("a|b|c", null, mockDefaultModule, mockPkgUnit);
        Assert.assertNotNull(type);
        TypeDeclaration declaration = type.getDeclaration();
        Assert.assertNotNull(declaration);
        Assert.assertTrue(declaration instanceof UnionType);
        UnionType union = (UnionType) declaration;
        List<Type> types = union.getCaseTypes();
        Assert.assertEquals(3, types.size());
        Assert.assertEquals("a", types.get(0).getDeclaration().getName());
        Assert.assertTrue(types.get(0).getDeclaration() instanceof Class);
        Assert.assertEquals("b", types.get(1).getDeclaration().getName());
        Assert.assertTrue(types.get(1).getDeclaration() instanceof Class);
        Assert.assertEquals("c", types.get(2).getDeclaration().getName());
        Assert.assertTrue(types.get(2).getDeclaration() instanceof Class);
    }

    @Test
    public void testIntersection(){
        Type type = new TypeParser(MockLoader.instance).decodeType("a&b&c", null, mockDefaultModule, mockPkgUnit);
        Assert.assertNotNull(type);
        TypeDeclaration declaration = type.getDeclaration();
        Assert.assertNotNull(declaration);
        Assert.assertTrue(declaration instanceof IntersectionType);
        IntersectionType intersection = (IntersectionType) declaration;
        List<Type> types = intersection.getSatisfiedTypes();
        Assert.assertEquals(3, types.size());
        Assert.assertEquals("a", types.get(0).getDeclaration().getName());
        Assert.assertTrue(types.get(0).getDeclaration() instanceof Class);
        Assert.assertEquals("b", types.get(1).getDeclaration().getName());
        Assert.assertTrue(types.get(1).getDeclaration() instanceof Class);
        Assert.assertEquals("c", types.get(2).getDeclaration().getName());
        Assert.assertTrue(types.get(2).getDeclaration() instanceof Class);
    }

    @Test
    public void testIntersectionAndUnion(){
        Type type = new TypeParser(MockLoader.instance).decodeType("a&b|c", null, mockDefaultModule, mockPkgUnit);
        Assert.assertNotNull(type);
        TypeDeclaration declaration = type.getDeclaration();
        Assert.assertNotNull(declaration);
        Assert.assertTrue(declaration instanceof UnionType);
        UnionType union = (UnionType) declaration;
        List<Type> unionTypes = union.getCaseTypes();
        Assert.assertEquals(2, unionTypes.size());
        
        Assert.assertTrue(unionTypes.get(0).getDeclaration() instanceof IntersectionType);
        IntersectionType intersection = (IntersectionType) unionTypes.get(0).getDeclaration();

        List<Type> intersectionTypes = intersection.getSatisfiedTypes();
        Assert.assertEquals(2, intersectionTypes.size());
        Assert.assertEquals("a", intersectionTypes.get(0).getDeclaration().getName());
        Assert.assertTrue(intersectionTypes.get(0).getDeclaration() instanceof Class);
        Assert.assertEquals("b", intersectionTypes.get(1).getDeclaration().getName());
        Assert.assertTrue(intersectionTypes.get(1).getDeclaration() instanceof Class);

        Assert.assertEquals("c", unionTypes.get(1).getDeclaration().getName());
        Assert.assertTrue(unionTypes.get(1).getDeclaration() instanceof Class);
    }

    @Test
    public void testParams(){
        Type type = new TypeParser(MockLoader.instance).decodeType("t2<b,c>", null, mockDefaultModule, mockPkgUnit);
        assertTypeWithParameters(type);
        
        Assert.assertTrue(type.getVarianceOverrides().isEmpty());
    }

    @Test
    public void testParamsVariance1(){
        Type type = new TypeParser(MockLoader.instance).decodeType("t2<in b,out c>", null, mockDefaultModule, mockPkgUnit);
        assertTypeWithParameters(type);

        Map<TypeParameter, SiteVariance> varianceOverrides = type.getVarianceOverrides();
        Assert.assertNotNull(varianceOverrides);
        Assert.assertEquals(2, varianceOverrides.size());
        List<TypeParameter> tps = type.getDeclaration().getTypeParameters();
        Assert.assertEquals(SiteVariance.IN, varianceOverrides.get(tps.get(0)));
        Assert.assertEquals(SiteVariance.OUT, varianceOverrides.get(tps.get(1)));
    }

    @Test
    public void testParamsVariance2(){
        Type type = new TypeParser(MockLoader.instance).decodeType("t2<b,out c>", null, mockDefaultModule, mockPkgUnit);
        assertTypeWithParameters(type);

        Map<TypeParameter, SiteVariance> varianceOverrides = type.getVarianceOverrides();
        Assert.assertNotNull(varianceOverrides);
        Assert.assertEquals(1, varianceOverrides.size());
        List<TypeParameter> tps = type.getDeclaration().getTypeParameters();
        Assert.assertEquals(null, varianceOverrides.get(tps.get(0)));
        Assert.assertEquals(SiteVariance.OUT, varianceOverrides.get(tps.get(1)));
    }

    @Test
    public void testParamsVariance3(){
        Type type = new TypeParser(MockLoader.instance).decodeType("t2<in b,c>", null, mockDefaultModule, mockPkgUnit);
        assertTypeWithParameters(type);

        Map<TypeParameter, SiteVariance> varianceOverrides = type.getVarianceOverrides();
        Assert.assertNotNull(varianceOverrides);
        Assert.assertEquals(1, varianceOverrides.size());
        List<TypeParameter> tps = type.getDeclaration().getTypeParameters();
        Assert.assertEquals(SiteVariance.IN, varianceOverrides.get(tps.get(0)));
        Assert.assertEquals(null, varianceOverrides.get(tps.get(1)));
    }

    private void assertTypeWithParameters(Type type) {
        Assert.assertNotNull(type);
        TypeDeclaration declaration = type.getDeclaration();
        Assert.assertNotNull(declaration);
        Assert.assertTrue(declaration instanceof Class);
        Assert.assertEquals("t2", declaration.getName());
        List<Type> tal = type.getTypeArgumentList();
        Assert.assertEquals(2, tal.size());
        Assert.assertEquals("b", tal.get(0).getDeclaration().getName());
        Assert.assertTrue(tal.get(0).getDeclaration() instanceof Class);
        Assert.assertEquals("c", tal.get(1).getDeclaration().getName());
        Assert.assertTrue(tal.get(1).getDeclaration() instanceof Class);
    }

    @Test
    public void testUnionParams(){
        Type type = new TypeParser(MockLoader.instance).decodeType("a|t2<b|c,t2<d,e|f>>", null, mockDefaultModule, mockPkgUnit);
        Assert.assertNotNull(type);
        TypeDeclaration declaration = type.getDeclaration();
        Assert.assertNotNull(declaration);
        Assert.assertTrue(declaration instanceof UnionType);
        UnionType union = (UnionType) declaration;
        List<Type> caseTypes = union.getCaseTypes();
        Assert.assertEquals(2, caseTypes.size());
        
        // a
        Assert.assertEquals("a", caseTypes.get(0).getDeclaration().getName());
        Assert.assertTrue(caseTypes.get(0).getDeclaration() instanceof Class);
        
        // first t2
        Type firstT2 = caseTypes.get(1);
        TypeDeclaration firstT2Declaration = firstT2.getDeclaration();
        Assert.assertNotNull(firstT2Declaration);
        Assert.assertTrue(firstT2Declaration instanceof Class);
        Assert.assertEquals("t2", firstT2Declaration.getName());
        Assert.assertEquals(2, firstT2.getTypeArgumentList().size());
        
        // b|c
        Type bc = firstT2.getTypeArgumentList().get(0);
        Assert.assertTrue(bc.getDeclaration() instanceof UnionType);
        Assert.assertEquals(2, bc.getDeclaration().getCaseTypes().size());
        
        // b
        Type b = bc.getDeclaration().getCaseTypes().get(0);
        Assert.assertEquals("b", b.getDeclaration().getName());
        Assert.assertTrue(b.getDeclaration() instanceof Class);

        // c
        Type c = bc.getDeclaration().getCaseTypes().get(1);
        Assert.assertEquals("c", c.getDeclaration().getName());
        Assert.assertTrue(c.getDeclaration() instanceof Class);
        
        // second t2
        Type secondT2 = firstT2.getTypeArgumentList().get(1);
        TypeDeclaration secondT2Declaration = firstT2.getDeclaration();
        Assert.assertNotNull(secondT2Declaration);
        Assert.assertTrue(secondT2Declaration instanceof Class);
        Assert.assertEquals("t2", secondT2Declaration.getName());
        Assert.assertEquals(2, secondT2.getTypeArgumentList().size());
        
        // d
        Type d = secondT2.getTypeArgumentList().get(0);
        Assert.assertEquals("d", d.getDeclaration().getName());
        Assert.assertTrue(d.getDeclaration() instanceof Class);

        // e|f
        Type ef = secondT2.getTypeArgumentList().get(1);
        Assert.assertTrue(ef.getDeclaration() instanceof UnionType);
        Assert.assertEquals(2, ef.getDeclaration().getCaseTypes().size());
        
        // e
        Type e = ef.getDeclaration().getCaseTypes().get(0);
        Assert.assertEquals("e", e.getDeclaration().getName());
        Assert.assertTrue(e.getDeclaration() instanceof Class);

        // f
        Type f = ef.getDeclaration().getCaseTypes().get(1);
        Assert.assertEquals("f", f.getDeclaration().getName());
        Assert.assertTrue(f.getDeclaration() instanceof Class);

    }

    @Test
    public void testQualified(){
        Type type = new TypeParser(MockLoader.instance).decodeType("a.b", null, mockDefaultModule, mockPkgUnit);
        Assert.assertNotNull(type);
        TypeDeclaration declaration = type.getDeclaration();
        Assert.assertNotNull(declaration);
        Assert.assertTrue(declaration instanceof Class);
        Assert.assertEquals("a.b", declaration.getQualifiedNameString());

        Type qualifyingType = type.getQualifyingType();
        Assert.assertNotNull(qualifyingType);
        TypeDeclaration qualifyingDeclaration = qualifyingType.getDeclaration();
        Assert.assertNotNull(qualifyingDeclaration);
        Assert.assertTrue(qualifyingDeclaration instanceof Class);
        Assert.assertEquals("a", qualifyingDeclaration.getName());
    }

    @Test
    public void testQualifiedAndParameterised(){
        Type type = new TypeParser(MockLoader.instance).decodeType("t2<a,b>.t2<c,d>", null, mockDefaultModule, mockPkgUnit);
        Assert.assertNotNull(type);
        TypeDeclaration declaration = type.getDeclaration();
        Assert.assertNotNull(declaration);
        Assert.assertTrue(declaration instanceof Class);
        Assert.assertEquals("t2.t2", declaration.getQualifiedNameString());
        Assert.assertEquals(2, type.getTypeArgumentList().size());
        
        // c
        Type c = type.getTypeArgumentList().get(0);
        Assert.assertEquals("c", c.getDeclaration().getName());
        Assert.assertTrue(c.getDeclaration() instanceof Class);

        // d
        Type d = type.getTypeArgumentList().get(1);
        Assert.assertEquals("d", d.getDeclaration().getName());
        Assert.assertTrue(d.getDeclaration() instanceof Class);

        Type qualifyingType = type.getQualifyingType();
        Assert.assertNotNull(qualifyingType);
        TypeDeclaration qualifyingDeclaration = qualifyingType.getDeclaration();
        Assert.assertNotNull(qualifyingDeclaration);
        Assert.assertTrue(qualifyingDeclaration instanceof Class);
        Assert.assertEquals("t2", qualifyingDeclaration.getName());
        Assert.assertEquals(2, qualifyingType.getTypeArgumentList().size());
        
        // a
        Type a = qualifyingType.getTypeArgumentList().get(0);
        Assert.assertEquals("a", a.getDeclaration().getName());
        Assert.assertTrue(a.getDeclaration() instanceof Class);

        // b
        Type b = qualifyingType.getTypeArgumentList().get(1);
        Assert.assertEquals("b", b.getDeclaration().getName());
        Assert.assertTrue(b.getDeclaration() instanceof Class);
    }

    @Test
    public void testPackageQualified(){
        Type type = new TypeParser(MockLoader.instance).decodeType("pkg::v", null, mockDefaultModule, mockPkgUnit);
        Assert.assertNotNull(type);
        TypeDeclaration declaration = type.getDeclaration();
        Assert.assertNotNull(declaration);
        Assert.assertTrue(declaration instanceof Class);
        Assert.assertEquals("pkg::v", declaration.getQualifiedNameString());

        Assert.assertNull(type.getQualifyingType());
    }

    @Test
    public void testComplexQualified(){
        Type type = new TypeParser(MockLoader.instance).decodeType("<pkg::u&pkg::v>.w", null, mockDefaultModule, mockPkgUnit);
        Assert.assertNotNull(type);
        TypeDeclaration declaration = type.getDeclaration();
        Assert.assertNotNull(declaration);
        Assert.assertTrue(declaration instanceof Class);
        Assert.assertEquals("pkg::v.w", declaration.getQualifiedNameString());

        Type qualifyingType = type.getQualifyingType();
        Assert.assertNotNull(qualifyingType);
        TypeDeclaration qualifyingDeclaration = qualifyingType.getDeclaration();
        Assert.assertNotNull(qualifyingDeclaration);
        Assert.assertTrue(qualifyingDeclaration instanceof IntersectionType);
        Assert.assertEquals("u&v", qualifyingDeclaration.getName());
    }
    
    @Test
    public void testEmptyAbbrev(){
        Type type = new TypeParser(MockLoader.instance).decodeType("[]", null, mockDefaultModule, mockPkgUnit);
        Assert.assertNotNull(type);
        TypeDeclaration declaration = type.getDeclaration();
        Assert.assertNotNull(declaration);
        Assert.assertTrue(declaration instanceof Interface);
        Assert.assertEquals("ceylon.language::Empty", declaration.getQualifiedNameString());
        Assert.assertNull(type.getQualifyingType());
    }
    
    @Test
    public void testSequenceAbbrev(){
        Type type = new TypeParser(MockLoader.instance).decodeType("[a+]", null, mockDefaultModule, mockPkgUnit);
        Assert.assertNotNull(type);
        TypeDeclaration declaration = type.getDeclaration();
        Assert.assertNotNull(declaration);
        Assert.assertTrue(declaration instanceof Interface);
        Assert.assertEquals("ceylon.language::Sequence", declaration.getQualifiedNameString());
        Assert.assertEquals("[a+]", type.asString());
        Assert.assertNull(type.getQualifyingType());
    }

    @Test
    public void testSequentialAbbrev(){
        Type type = new TypeParser(MockLoader.instance).decodeType("[a*]", null, mockDefaultModule, mockPkgUnit);
        Assert.assertNotNull(type);
        TypeDeclaration declaration = type.getDeclaration();
        Assert.assertNotNull(declaration);
        Assert.assertTrue(declaration instanceof Interface);
        Assert.assertEquals("ceylon.language::Sequential", declaration.getQualifiedNameString());
        Assert.assertEquals("a[]", type.asString());
        Assert.assertNull(type.getQualifyingType());
    }
    
    @Test
    public void testTuple1Abbrev(){
        Type type = new TypeParser(MockLoader.instance).decodeType("[a]", null, mockDefaultModule, mockPkgUnit);
        Assert.assertNotNull(type);
        TypeDeclaration declaration = type.getDeclaration();
        Assert.assertNotNull(declaration);
        Assert.assertTrue(declaration instanceof Class);
        Assert.assertEquals("ceylon.language::Tuple", declaration.getQualifiedNameString());
        Assert.assertEquals("[a]", type.asString());
        Assert.assertNull(type.getQualifyingType());
    }
    
    @Test
    public void testTuple2Abbrev(){
        Type type = new TypeParser(MockLoader.instance).decodeType("[a,b]", null, mockDefaultModule, mockPkgUnit);
        Assert.assertNotNull(type);
        TypeDeclaration declaration = type.getDeclaration();
        Assert.assertNotNull(declaration);
        Assert.assertTrue(declaration instanceof Class);
        Assert.assertEquals("ceylon.language::Tuple", declaration.getQualifiedNameString());
        Assert.assertEquals("[a, b]", type.asString());
        Assert.assertNull(type.getQualifyingType());
    }
    
    @Test
    public void testTuple3Abbrev(){
        Type type = new TypeParser(MockLoader.instance).decodeType("[a,b,c]", null, mockDefaultModule, mockPkgUnit);
        Assert.assertNotNull(type);
        TypeDeclaration declaration = type.getDeclaration();
        Assert.assertNotNull(declaration);
        Assert.assertTrue(declaration instanceof Class);
        Assert.assertEquals("ceylon.language::Tuple", declaration.getQualifiedNameString());
        Assert.assertEquals("[a, b, c]", type.asString());
        Assert.assertNull(type.getQualifyingType());
    }
    
    @Test
    public void testTuple1OrMoreAbbrev(){
        Type type = new TypeParser(MockLoader.instance).decodeType("[a,b*]", null, mockDefaultModule, mockPkgUnit);
        Assert.assertNotNull(type);
        TypeDeclaration declaration = type.getDeclaration();
        Assert.assertNotNull(declaration);
        Assert.assertTrue(declaration instanceof Class);
        Assert.assertEquals("ceylon.language::Tuple", declaration.getQualifiedNameString());
        Assert.assertEquals("[a, b*]", type.asString());
        Assert.assertNull(type.getQualifyingType());
    }
    
    @Test
    public void testTuple2OrMoreAbbrev(){
        Type type = new TypeParser(MockLoader.instance).decodeType("[a,b+]", null, mockDefaultModule, mockPkgUnit);
        Assert.assertNotNull(type);
        TypeDeclaration declaration = type.getDeclaration();
        Assert.assertNotNull(declaration);
        Assert.assertTrue(declaration instanceof Class);
        Assert.assertEquals("ceylon.language::Tuple", declaration.getQualifiedNameString());
        Assert.assertEquals("[a, b+]", type.asString());
        Assert.assertNull(type.getQualifyingType());
    }
    
    @Test
    public void testTuple2OrMoreAbbrev2(){
        Type type = new TypeParser(MockLoader.instance).decodeType("[a,b,c*]", null, mockDefaultModule, mockPkgUnit);
        Assert.assertNotNull(type);
        TypeDeclaration declaration = type.getDeclaration();
        Assert.assertNotNull(declaration);
        Assert.assertTrue(declaration instanceof Class);
        Assert.assertEquals("ceylon.language::Tuple", declaration.getQualifiedNameString());
        Assert.assertEquals("[a, b, c*]", type.asString());
        Assert.assertNull(type.getQualifyingType());
    }
    
    @Test
    public void testTuple3OrMoreAbbrev(){
        Type type = new TypeParser(MockLoader.instance).decodeType("[a,b,c+]", null, mockDefaultModule, mockPkgUnit);
        Assert.assertNotNull(type);
        TypeDeclaration declaration = type.getDeclaration();
        Assert.assertNotNull(declaration);
        Assert.assertTrue(declaration instanceof Class);
        Assert.assertEquals("ceylon.language::Tuple", declaration.getQualifiedNameString());
        Assert.assertEquals("[a, b, c+]", type.asString());
        Assert.assertNull(type.getQualifyingType());
    }
    
    @Test
    public void testTuple1To3Abbrev(){
        Type type = new TypeParser(MockLoader.instance).decodeType("[a,b=,c=]", null, mockDefaultModule, mockPkgUnit);
        Assert.assertNotNull(type);
        TypeDeclaration declaration = type.getDeclaration();
        Assert.assertNotNull(declaration);
        Assert.assertTrue(declaration instanceof Class);
        Assert.assertEquals("ceylon.language::Tuple", declaration.getQualifiedNameString());
        Assert.assertEquals("[a, b=, c=]", type.asString());
        Assert.assertEquals("ceylon.language::Tuple<a|b|c,a,ceylon.language::Empty|ceylon.language::Tuple<b|c,b,ceylon.language::Empty|ceylon.language::Tuple<c,c,ceylon.language::Empty>>>", printType(type));
        Assert.assertNull(type.getQualifyingType());
    }
    
    @Test
    public void testHomoTuple1(){
        Type type = new TypeParser(MockLoader.instance).decodeType("a[1]", null, mockDefaultModule, mockPkgUnit);
        Assert.assertNotNull(type);
        TypeDeclaration declaration = type.getDeclaration();
        Assert.assertNotNull(declaration);
        Assert.assertTrue(declaration instanceof Class);
        Assert.assertEquals("ceylon.language::Tuple", declaration.getQualifiedNameString());
        Assert.assertEquals("[a]", type.asString());
        Assert.assertNull(type.getQualifyingType());
    }
    
    @Test
    public void testHomoTuple2(){
        Type type = new TypeParser(MockLoader.instance).decodeType("a[2]", null, mockDefaultModule, mockPkgUnit);
        Assert.assertNotNull(type);
        TypeDeclaration declaration = type.getDeclaration();
        Assert.assertNotNull(declaration);
        Assert.assertTrue(declaration instanceof Class);
        Assert.assertEquals("ceylon.language::Tuple", declaration.getQualifiedNameString());
        Assert.assertEquals("a[2]", type.asString());
        Assert.assertNull(type.getQualifyingType());
    }
    
    @Test
    public void testIterableAbbrev(){
        try {
            new TypeParser(MockLoader.instance).decodeType("{a}", null, mockDefaultModule, mockPkgUnit);
            Assert.fail();
        } catch ( TypeParserException e) {
            Assert.assertEquals("com.redhat.ceylon.model.loader.TypeParserException: Expected multiplicity in abbreviated Iterable type: 2", e.toString());
        }
    }
    
    @Test
    public void testPossiblyEmptyIterableAbbrev(){
        Type type = new TypeParser(MockLoader.instance).decodeType("{a*}", null, mockDefaultModule, mockPkgUnit);
        Assert.assertNotNull(type);
        TypeDeclaration declaration = type.getDeclaration();
        Assert.assertNotNull(declaration);
        Assert.assertTrue(declaration instanceof Interface);
        Assert.assertEquals("ceylon.language::Iterable", declaration.getQualifiedNameString());
        Assert.assertEquals("{a*}", type.asString());
        Assert.assertNull(type.getQualifyingType());
    }
    
    @Test
    public void testNonEmptyIterableAbbrev(){
        Type type = new TypeParser(MockLoader.instance).decodeType("{a+}", null, mockDefaultModule, mockPkgUnit);
        Assert.assertNotNull(type);
        TypeDeclaration declaration = type.getDeclaration();
        Assert.assertNotNull(declaration);
        Assert.assertTrue(declaration instanceof Interface);
        Assert.assertEquals("ceylon.language::Iterable", declaration.getQualifiedNameString());
        Assert.assertEquals("{a+}", type.asString());
        Assert.assertNull(type.getQualifyingType());
    }
    
    @Test
    public void testCallableAbbrev(){
        Type type = new TypeParser(MockLoader.instance).decodeType("a(b)", null, mockDefaultModule, mockPkgUnit);
        Assert.assertNotNull(type);
        TypeDeclaration declaration = type.getDeclaration();
        Assert.assertNotNull(declaration);
        Assert.assertTrue(declaration instanceof Interface);
        Assert.assertEquals("ceylon.language::Callable", declaration.getQualifiedNameString());
        Assert.assertEquals("a(b)", type.asString());
        Assert.assertNull(type.getQualifyingType());
        
        type = new TypeParser(MockLoader.instance).decodeType("a(b,pkg::u*)", null, mockPkgModule, mockPkgUnit);
        Assert.assertNotNull(type);
        declaration = type.getDeclaration();
        Assert.assertNotNull(declaration);
        Assert.assertTrue(declaration instanceof Interface);
        Assert.assertEquals("ceylon.language::Callable", declaration.getQualifiedNameString());
        Assert.assertEquals("ceylon.language::Callable<a,ceylon.language::Tuple<b|pkg::u,b,ceylon.language::Sequential<pkg::u>>>", printType(type));
        Assert.assertNull(type.getQualifyingType());
        
        type = new TypeParser(MockLoader.instance).decodeType("a(b=,pkg::u*)", null, mockPkgModule, mockPkgUnit);
        Assert.assertNotNull(type);
        declaration = type.getDeclaration();
        Assert.assertNotNull(declaration);
        Assert.assertTrue(declaration instanceof Interface);
        Assert.assertEquals("ceylon.language::Callable", declaration.getQualifiedNameString());
        Assert.assertEquals("ceylon.language::Callable<a,ceylon.language::Empty|ceylon.language::Tuple<b|pkg::u,b,ceylon.language::Sequential<pkg::u>>>", printType(type));
        Assert.assertNull(type.getQualifyingType());
    }
    
    @Test
    public void testSpreadCallableAbbrev(){
        Type type = new TypeParser(MockLoader.instance).decodeType("a(*[b,a])", null, mockDefaultModule, mockPkgUnit);
        Assert.assertNotNull(type);
        TypeDeclaration declaration = type.getDeclaration();
        Assert.assertNotNull(declaration);
        Assert.assertTrue(declaration instanceof Interface);
        Assert.assertEquals("ceylon.language::Callable", declaration.getQualifiedNameString());
        Assert.assertEquals("a(b, a)", type.asString());
        Assert.assertNull(type.getQualifyingType());
    }
    
    @Test
    public void testEntryAbbrev(){
        Type type = new TypeParser(MockLoader.instance).decodeType(" a -> b ", null, mockDefaultModule, mockDefaultUnit);
        Assert.assertNotNull(type);
        TypeDeclaration declaration = type.getDeclaration();
        Assert.assertNotNull(declaration);
        Assert.assertTrue(declaration instanceof Class);
        Assert.assertEquals("ceylon.language::Entry", declaration.getQualifiedNameString());
        Assert.assertEquals("ceylon.language::Entry<a,b>", printType(type));
        Assert.assertNull(type.getQualifyingType());
        
        type = new TypeParser(MockLoader.instance).decodeType("pkg::u|pkg::v->b", null, mockPkgModule, mockPkgUnit);
        Assert.assertNotNull(type);
        declaration = type.getDeclaration();
        Assert.assertNotNull(declaration);
        Assert.assertTrue(declaration instanceof Class);
        Assert.assertEquals("ceylon.language::Entry", declaration.getQualifiedNameString());
        Assert.assertEquals("ceylon.language::Entry<pkg::u|pkg::v,b>", printType(type));
        Assert.assertNull(type.getQualifyingType());
    }
    
    @Test
    public void testOptionalAbbrev(){
        Type type = new TypeParser(MockLoader.instance).decodeType("a?", null, mockDefaultModule, mockPkgUnit);
        Assert.assertNotNull(type);
        TypeDeclaration declaration = type.getDeclaration();
        Assert.assertNotNull(declaration);
        Assert.assertTrue(declaration instanceof UnionType);
        Assert.assertEquals("ceylon.language::Null|a", printType(type));
        Assert.assertNull(type.getQualifyingType());
        
        type = new TypeParser(MockLoader.instance).decodeType("pkg::u&pkg::v?", null, mockDefaultModule, mockPkgUnit);
        Assert.assertNotNull(type);
        declaration = type.getDeclaration();
        Assert.assertNotNull(declaration);
        Assert.assertTrue(declaration instanceof IntersectionType);
        Assert.assertEquals("pkg::u&<ceylon.language::Null|pkg::v>", printType(type));
        Assert.assertNull(type.getQualifyingType());
        
        type = new TypeParser(MockLoader.instance).decodeType("pkg::u|pkg::v[]?", null, mockDefaultModule, mockPkgUnit);
        Assert.assertNotNull(type);
        declaration = type.getDeclaration();
        Assert.assertNotNull(declaration);
        Assert.assertTrue(declaration instanceof UnionType);
        Assert.assertEquals("pkg::u|ceylon.language::Null|ceylon.language::Sequential<pkg::v>", printType(type));
        Assert.assertNull(type.getQualifyingType());
    }

    @Test
    public void testCallableOfEntry(){
        Type type = new TypeParser(MockLoader.instance).decodeType("ceylon.language::Boolean(ceylon.language::Nothing->ceylon.language::Nothing)", null, mockDefaultModule, mockPkgUnit);
        Assert.assertNotNull(type);
        TypeDeclaration declaration = type.getDeclaration();
        Assert.assertNotNull(declaration);
        Assert.assertTrue(declaration instanceof Interface);
        Assert.assertEquals("ceylon.language::Callable<ceylon.language::Boolean,ceylon.language::Tuple<ceylon.language::Entry<ceylon.language::Nothing,ceylon.language::Nothing>,ceylon.language::Entry<ceylon.language::Nothing,ceylon.language::Nothing>,ceylon.language::Empty>>", printType(type));
        Assert.assertNull(type.getQualifyingType());
    }
    
    @Test
    public void testOptionalEntry(){
        Type type = new TypeParser(MockLoader.instance).decodeType("<ceylon.language::Boolean->a>?", null, mockDefaultModule, mockPkgUnit);
        Assert.assertNotNull(type);
        TypeDeclaration declaration = type.getDeclaration();
        Assert.assertNotNull(declaration);
        Assert.assertTrue(declaration instanceof UnionType);
        Assert.assertEquals("ceylon.language::Null|ceylon.language::Entry<ceylon.language::Boolean,a>", printType(type));
        Assert.assertNull(type.getQualifyingType());
    }

    @Test
    public void testTypeCallPackage(){
        Type type = new TypeParser(MockLoader.instance).decodeType("pkg::package", null, mockDefaultModule, mockPkgUnit);
        Assert.assertNotNull(type);
        TypeDeclaration declaration = type.getDeclaration();
        Assert.assertNotNull(declaration);
        Assert.assertTrue(declaration instanceof Class);
        Assert.assertEquals("pkg::package", printType(type));
        Assert.assertNull(type.getQualifyingType());
    }
    
    
    @Test(expected = ModelResolutionException.class)
    public void testParameterisedPackage(){
        new TypeParser(MockLoader.instance).decodeType("unknown<a>.b", null, mockDefaultModule, mockPkgUnit);
    }

    @Test(expected = ModelResolutionException.class)
    public void testUnknownMember(){
        new TypeParser(MockLoader.instance).decodeType("a.unknown", null, mockDefaultModule, mockPkgUnit);
    }

    @Test(expected = TypeParserException.class)
    public void testInvalidType(){
        new TypeParser(MockLoader.instance).decodeType("t2<a,b", null, mockDefaultModule, mockPkgUnit);
    }
    
    TypePrinter typePrinter = new TypePrinter(
            false, 
            true, 
            false, 
            false, 
            false, 
            true, 
            true);
    String printType(Type t) {
        return typePrinter.print(t, t.getDeclaration().getUnit());
    }
}
