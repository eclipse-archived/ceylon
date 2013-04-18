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

package com.redhat.ceylon.compiler.java.codegen;

import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.ProducedTypedReference;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
import com.sun.tools.javac.tree.JCTree.JCReturn;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;

/**
 * Builds a class for global variables. See {@link GlobalTransformer} for an overview.
 *
 * The generated class can be customized by calling methods of this class.
 */
public class AttributeDefinitionBuilder {
    private boolean hasField = true;
    private final String fieldName;

    private final JCTree.JCExpression attrType;
    private final JCTree.JCExpression attrTypeRaw;
    private final String attrName;
    private String javaClassName;

    private long modifiers;

    private boolean readable = true;
    private final MethodDefinitionBuilder getterBuilder;

    private JCTree.JCExpression variableInit;

    private boolean writable = true;
    private final MethodDefinitionBuilder setterBuilder;
    
    private AbstractTransformer owner;

    private boolean toplevel = false;
    
    private boolean noAnnotations = false;
    
    // do we need a constructor that takes the initial value? 
    private boolean valueConstructor;
    
    private boolean late;
    private boolean variable;
    private ListBuffer<JCAnnotation> annotations;

    private AttributeDefinitionBuilder(AbstractTransformer owner, TypedDeclaration attrType, 
            String javaClassName, String attrName, String fieldName, boolean toplevel) {
        int typeFlags = 0;
        ProducedTypedReference typedRef = owner.getTypedReference(attrType);
        ProducedTypedReference nonWideningTypedRef = owner.nonWideningTypeDecl(typedRef);
        ProducedType nonWideningType = owner.nonWideningType(typedRef, nonWideningTypedRef);
        if (!CodegenUtil.isUnBoxed(nonWideningTypedRef.getDeclaration())) {
            typeFlags |= AbstractTransformer.JT_NO_PRIMITIVES;
        }
        
        this.attrType = owner.makeJavaType(nonWideningType, typeFlags);
        this.attrTypeRaw = owner.makeJavaType(nonWideningType, AbstractTransformer.JT_RAW);
        this.owner = owner;
        this.javaClassName = javaClassName;
        this.attrName = attrName;
        this.fieldName = fieldName;
        this.toplevel = toplevel;
        this.late = attrType.isLate();
        this.variable = attrType.isVariable();
        
        // Make sure we use the declaration for building the getter/setter names, as we might be trying to
        // override a JavaBean property with an "isFoo" getter, or non-Ceylon casing, and we have to respect that.
        getterBuilder = MethodDefinitionBuilder
            .method2(owner, Naming.getGetterName(attrType))
            .block(generateDefaultGetterBlock())
            .isOverride(attrType.isActual())
            .modelAnnotations(attrType.getAnnotations())
            .resultType(this.attrType, attrType);
        setterBuilder = MethodDefinitionBuilder
            .method2(owner, Naming.getSetterName(attrType))
            .block(generateDefaultSetterBlock())
            // only actual if the superclass is also variable
            .isOverride(attrType.isActual() && ((TypedDeclaration)attrType.getRefinedDeclaration()).isVariable())
            .parameter(Flags.FINAL, null, attrName, attrType, nonWideningTypedRef.getDeclaration(), nonWideningType, 0, true);
    }
    
    public static AttributeDefinitionBuilder wrapped(AbstractTransformer owner, 
            String javaClassName, String attrName, TypedDeclaration attrType, 
            boolean toplevel) {
        return new AttributeDefinitionBuilder(owner, attrType, javaClassName, attrName, "value", toplevel);
    }
    
    public static AttributeDefinitionBuilder getter(AbstractTransformer owner, 
            String attrAndFieldName, TypedDeclaration attrType) {
        return new AttributeDefinitionBuilder(owner, attrType, null, 
                attrAndFieldName, attrAndFieldName, false)
            .skipField()
            .immutable();
    }
    
    public static AttributeDefinitionBuilder setter(AbstractTransformer owner, 
            String attrAndFieldName, TypedDeclaration attrType) {
        return new AttributeDefinitionBuilder(owner, attrType, null, 
                attrAndFieldName, attrAndFieldName, false)
            .skipField()
            .skipGetter();
    }
    
    public AttributeDefinitionBuilder annotations(List<JCAnnotation> annotations) {
        if (annotations != null) {
            if (this.annotations == null) {
                this.annotations = ListBuffer.lb();
            }
            this.annotations.appendList(annotations);
        }
        return this;
    }
    
    /**
     * Generates the class and returns the generated tree.
     * @return the generated class tree, to be added to the appropriate {@link JCTree.JCCompilationUnit}.
     */
    public List<JCTree> build() {
        ListBuffer<JCTree> defs = ListBuffer.lb();
        appendDefinitionsTo(defs);
        if (javaClassName != null) {
            ClassDefinitionBuilder classBuilder = 
                    ClassDefinitionBuilder
                    .klass(owner, javaClassName, null)
                    .modifiers(Flags.FINAL | (modifiers & (Flags.PUBLIC | Flags.PRIVATE)))
                    .constructorModifiers(Flags.PRIVATE)
                    .annotations(owner.makeAtAttribute())
                    .defs(defs.toList());
            if(valueConstructor && hasField)
                generateValueConstructor(classBuilder.addConstructor());
            return classBuilder.build();
        } else {
            return defs.toList();
        }
    }

    /**
     * Appends to <tt>defs</tt> the definitions that would go into the class generated by {@link #build()}
     * @param defs a {@link ListBuffer} to which the definitions will be appended.
     */
    public void appendDefinitionsTo(ListBuffer<JCTree> defs) {
        if (hasField) {
            defs.append(generateField());
            if(variableInit != null)
                defs.append(generateFieldInit());
        }

        if (readable) {
            getterBuilder.modifiers(getGetSetModifiers());
            getterBuilder.noAnnotations(noAnnotations);
            if (this.annotations != null) {
                getterBuilder.annotations(this.annotations.toList());
            }
            defs.append(getterBuilder.build());
        }

        if (writable) {
            setterBuilder.modifiers(getGetSetModifiers());
            setterBuilder.noAnnotations(noAnnotations);
            if (this.annotations != null && !readable) {
                setterBuilder.annotations(this.annotations.toList());
            }
            defs.append(setterBuilder.build());
        }
    }

    private void generateValueConstructor(MethodDefinitionBuilder methodDefinitionBuilder) {
        ParameterDefinitionBuilder paramBuilder = ParameterDefinitionBuilder.instance(owner, fieldName).type(attrType, null);
        JCTree.JCAssign init = owner.make().Assign(owner.makeQualIdent(owner.makeUnquotedIdent("this"), fieldName), owner.makeUnquotedIdent(fieldName));
        methodDefinitionBuilder.parameter(paramBuilder).body(owner.make().Exec(init));
    }

    private long getGetSetModifiers() {
        return modifiers & (Flags.PUBLIC | Flags.PRIVATE | Flags.ABSTRACT | Flags.FINAL | Flags.STATIC);
    }

    private JCTree generateField() {
        long flags = Flags.PRIVATE | (modifiers & Flags.STATIC);
        // only make it final if we have an init, otherwise we still have to initialise it
        if (!writable && (variableInit != null || valueConstructor)) {
            flags |= Flags.FINAL;
        }

        return owner.make().VarDef(
                owner.make().Modifiers(flags),
                owner.names().fromString(Naming.quoteIfJavaKeyword(fieldName)),
                (toplevel || late) ? owner.make().TypeArray(attrType) : attrType,
                null
        );
    }

    private JCTree generateFieldInit() {
        long flags = (modifiers & Flags.STATIC);

        JCTree.JCExpression varInit = variableInit;
        if (toplevel || late) {
            varInit = owner.make().NewArray(
                    attrTypeRaw,
                    List.<JCTree.JCExpression>nil(),
                    List.<JCTree.JCExpression>of(varInit)
            );
        }
        JCTree.JCAssign init = owner.make().Assign(owner.makeUnquotedIdent(fieldName), varInit);
        return owner.make().Block(flags, 
                List.<JCTree.JCStatement>of(owner.make().Exec(init)));
    }

    private JCTree.JCBlock generateDefaultGetterBlock() {
        JCTree.JCExpression returnExpr = owner.makeUnquotedIdent(fieldName);
        if (toplevel || late) {
            returnExpr = owner.make().Indexed(returnExpr, owner.make().Literal(0));
        }
        JCReturn returnValue = owner.make().Return(returnExpr);
        List<JCStatement> stmts;
        
        stmts = List.<JCTree.JCStatement>of(returnValue);   
        
        JCTree.JCBlock block = owner.make().Block(0L, stmts);
        if (toplevel || late) {            
            JCExpression msg = owner.makeCeylonString(late ? "Accessing uninitialized 'late' attribute" : "Cyclic initialization");
            JCTree.JCThrow throwStmt = owner.make().Throw(owner.makeNewClass(owner.makeIdent(owner.syms().ceylonInitializationExceptionType), 
                    List.<JCExpression>of(msg)));
            JCTree.JCBlock catchBlock = owner.make().Block(0, List.<JCTree.JCStatement>of(throwStmt));
            JCVariableDecl excepType = owner.makeVar("ex", owner.make().Type(owner.syms().nullPointerExceptionType), null);
            JCTree.JCCatch catcher = owner.make().Catch(excepType , catchBlock);
            JCTree.JCTry tryExpr = owner.make().Try(block, List.<JCTree.JCCatch>of(catcher), null);
            block = owner.make().Block(0L, List.<JCTree.JCStatement>of(tryExpr));
        }
        return block;
    }

    public JCTree.JCBlock generateDefaultSetterBlock() {
        JCExpression fld = fld();
        if (toplevel || late) {
            fld = owner.make().Indexed(fld, owner.make().Literal(0));
        }
        List<JCStatement> stmts = List.<JCTree.JCStatement>of(
                owner.make().Exec(
                        owner.make().Assign(
                                fld,
                                owner.makeUnquotedIdent(attrName))));
        if (late) {
            JCStatement init = owner.make().Exec(
                    owner.make().Assign(fld(), 
                            owner.make().NewArray(this.attrType, 
                                    List.<JCExpression>of(owner.make().Literal(1)), 
                                    null)));
            if (variable) {  
                stmts = stmts.prepend(owner.make().If(generateLateInitializedPred(JCTree.EQ),
                        init, null));
            } else {
                stmts = stmts.prepend(init);
                stmts = stmts.prepend(owner.make().If(generateLateInitializedPred(JCTree.NE),
                        owner.make().Throw(owner.makeNewClass( 
                                owner.make().Type(owner.syms().ceylonInitializationExceptionType), 
                                List.<JCExpression>of(owner.makeCeylonString("Re-initialization of 'late' attribute")))),
                                null));
            }
        }
        return owner.make().Block(0L, stmts);
    }

    private JCExpression fld() {
        JCExpression fld;
        if (fieldName.equals(attrName)) {
            fld = owner.makeSelect("this", fieldName);
        } else {
            fld = owner.makeUnquotedIdent(fieldName);
        }
        return fld;
    }
    
    private JCExpression generateLateInitializedPred(int cmp) {
        return owner.make().Binary(cmp, 
                fld(), 
                owner.makeNull());
    }

    public AttributeDefinitionBuilder modifiers(long... modifiers) {
        long mods = 0;
        for (long mod : modifiers) {
            mods |= mod;
        }
        this.modifiers = mods;
        return this;
    }

    public AttributeDefinitionBuilder is(long flag, boolean value) {
        if (value) {
            this.modifiers |= flag;
        } else {
            this.modifiers &= ~flag;
        }
        return this;
    }

    public AttributeDefinitionBuilder noAnnotations() {
        this.noAnnotations = true;
        return this;
    }

    public AttributeDefinitionBuilder isFormal(boolean isFormal) {
        getterBuilder.isAbstract(isFormal);
        setterBuilder.isAbstract(isFormal);
        return this;
    }

    /**
     * Causes no field to be generated.
     * @return this instance for method chaining
     */
    public AttributeDefinitionBuilder skipField() {
        this.hasField = false;
        return this;
    }

    /**
     * Sets the code block to use for the generated getter. If no getter is generated the code block will be
     * silently ignored.
     * @param getterBlock a code block
     * @return this instance for method chaining
     */
    public AttributeDefinitionBuilder getterBlock(JCTree.JCBlock getterBlock) {
        skipField();
        getterBuilder.block(getterBlock);
        return this;
    }

    /**
     * Causes no getter to be generated.
     * @return this instance for method chaining
     */
    public AttributeDefinitionBuilder skipGetter() {
        this.readable = false;
        return this;
    }

    /**
     * Sets the code block to use for the generated setter. If no setter is generated the code block will be
     * silently ignored.
     * @param setterBlock a code block
     * @return this instance for method chaining
     */
    public AttributeDefinitionBuilder setterBlock(JCTree.JCBlock setterBlock) {
        setterBuilder.block(setterBlock);
        return this;
    }

    /**
     * Causes the generated attribute to be immutable. The <tt>value</tt> field is declared <tt>final</tt> and no
     * setter is generated.
     * @return this instance for method chaining
     */
    public AttributeDefinitionBuilder immutable() {
        this.writable = false;
        return this;
    }

    /**
     * The <tt>value</tt> field will be declared with the initial value given by the parameter
     * @param initialValue the initial value of the global.
     * @return this instance for method chaining
     */
    public AttributeDefinitionBuilder initialValue(JCTree.JCExpression initialValue) {
        this.variableInit = initialValue;
        return this;
    }

    /**
     * Marks the getter/setter methods as not actual. In general <tt>actual</tt> is derived from
     * the model while creating this builder so it will be correct. You can only disable this
     * computation. Enabling <tt>actual</tt> would otherwise depend on the question of whether the
     * getter is or not actual which may be different for the setter if the refined decl is not variable
     * so we'd need two parameters.
     */
    public AttributeDefinitionBuilder notActual() {
        getterBuilder.isOverride(false);
        setterBuilder.isOverride(false);
        return this;
    }
 
    /**
     * Produces a constructor that receives the initial value for this attribute.
     */
    public AttributeDefinitionBuilder valueConstructor(){
        valueConstructor = true;
        return this;
    }
}