package com.redhat.ceylon.compiler.test.model;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.redhat.ceylon.compiler.codegen.ModelLoader;
import com.redhat.ceylon.compiler.codegen.TypeParser;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.UnionType;

public class TypeParserTest {
    static class MockLoader implements ModelLoader {

        static final ModelLoader instance = new MockLoader();
        
        @Override
        public ProducedType getType(String name, Scope scope) {
            Class klass = new Class();
            klass.setName(name);
            if(name.equals("t2")){
                List<TypeParameter> typeParameters = new ArrayList<TypeParameter>(2);
                TypeParameter typeParam = new TypeParameter();
                typeParam.setName("A");
                typeParameters.add(typeParam);
                typeParam = new TypeParameter();
                typeParam.setName("B");
                typeParameters.add(typeParam);
                klass.setTypeParameters(typeParameters );
            }
            return klass.getType();
        }

        @Override
        public Declaration getDeclaration(String typeName) {
            throw new RuntimeException("Not yet implemented");
        }
        
    }
    
    @Test
    public void testUnion(){
        ProducedType type = new TypeParser().decodeType("a|b|c", null, MockLoader.instance);
        Assert.assertNotNull(type);
        TypeDeclaration declaration = type.getDeclaration();
        Assert.assertNotNull(declaration);
        Assert.assertTrue(declaration instanceof UnionType);
        UnionType union = (UnionType) declaration;
        Assert.assertEquals(3, union.getCaseTypes().size());
        Assert.assertEquals("a", union.getCaseTypes().get(0).getDeclaration().getName());
        Assert.assertTrue(union.getCaseTypes().get(0).getDeclaration() instanceof Class);
        Assert.assertEquals("b", union.getCaseTypes().get(1).getDeclaration().getName());
        Assert.assertTrue(union.getCaseTypes().get(1).getDeclaration() instanceof Class);
        Assert.assertEquals("c", union.getCaseTypes().get(2).getDeclaration().getName());
        Assert.assertTrue(union.getCaseTypes().get(2).getDeclaration() instanceof Class);
    }

    @Test
    public void testParams(){
        ProducedType type = new TypeParser().decodeType("t2<b,c>", null, MockLoader.instance);
        Assert.assertNotNull(type);
        TypeDeclaration declaration = type.getDeclaration();
        Assert.assertNotNull(declaration);
        Assert.assertTrue(declaration instanceof Class);
        Assert.assertEquals("t2", declaration.getName());
        Assert.assertEquals(2, type.getTypeArgumentList().size());
        Assert.assertEquals("b", type.getTypeArgumentList().get(0).getDeclaration().getName());
        Assert.assertTrue(type.getTypeArgumentList().get(0).getDeclaration() instanceof Class);
        Assert.assertEquals("c", type.getTypeArgumentList().get(1).getDeclaration().getName());
        Assert.assertTrue(type.getTypeArgumentList().get(1).getDeclaration() instanceof Class);
    }

    @Test
    public void testUnionParams(){
        ProducedType type = new TypeParser().decodeType("a|t2<b|c,t2<d,e|f>>", null, MockLoader.instance);
        Assert.assertNotNull(type);
        TypeDeclaration declaration = type.getDeclaration();
        Assert.assertNotNull(declaration);
        Assert.assertTrue(declaration instanceof UnionType);
        UnionType union = (UnionType) declaration;
        List<ProducedType> caseTypes = union.getCaseTypes();
        Assert.assertEquals(2, caseTypes.size());
        
        // a
        Assert.assertEquals("a", caseTypes.get(0).getDeclaration().getName());
        Assert.assertTrue(caseTypes.get(0).getDeclaration() instanceof Class);
        
        // first t2
        ProducedType firstT2 = caseTypes.get(1);
        TypeDeclaration firstT2Declaration = firstT2.getDeclaration();
        Assert.assertNotNull(firstT2Declaration);
        Assert.assertTrue(firstT2Declaration instanceof Class);
        Assert.assertEquals("t2", firstT2Declaration.getName());
        Assert.assertEquals(2, firstT2.getTypeArgumentList().size());
        
        // b|c
        ProducedType bc = firstT2.getTypeArgumentList().get(0);
        Assert.assertTrue(bc.getDeclaration() instanceof UnionType);
        Assert.assertEquals(2, bc.getDeclaration().getCaseTypes().size());
        
        // b
        ProducedType b = bc.getDeclaration().getCaseTypes().get(0);
        Assert.assertEquals("b", b.getDeclaration().getName());
        Assert.assertTrue(b.getDeclaration() instanceof Class);

        // c
        ProducedType c = bc.getDeclaration().getCaseTypes().get(1);
        Assert.assertEquals("c", c.getDeclaration().getName());
        Assert.assertTrue(c.getDeclaration() instanceof Class);
        
        // second t2
        ProducedType secondT2 = firstT2.getTypeArgumentList().get(1);
        TypeDeclaration secondT2Declaration = firstT2.getDeclaration();
        Assert.assertNotNull(secondT2Declaration);
        Assert.assertTrue(secondT2Declaration instanceof Class);
        Assert.assertEquals("t2", secondT2Declaration.getName());
        Assert.assertEquals(2, secondT2.getTypeArgumentList().size());
        
        // d
        ProducedType d = secondT2.getTypeArgumentList().get(0);
        Assert.assertEquals("d", d.getDeclaration().getName());
        Assert.assertTrue(d.getDeclaration() instanceof Class);

        // e|f
        ProducedType ef = secondT2.getTypeArgumentList().get(1);
        Assert.assertTrue(ef.getDeclaration() instanceof UnionType);
        Assert.assertEquals(2, ef.getDeclaration().getCaseTypes().size());
        
        // e
        ProducedType e = ef.getDeclaration().getCaseTypes().get(0);
        Assert.assertEquals("e", e.getDeclaration().getName());
        Assert.assertTrue(e.getDeclaration() instanceof Class);

        // f
        ProducedType f = ef.getDeclaration().getCaseTypes().get(1);
        Assert.assertEquals("f", f.getDeclaration().getName());
        Assert.assertTrue(f.getDeclaration() instanceof Class);

    }
}
