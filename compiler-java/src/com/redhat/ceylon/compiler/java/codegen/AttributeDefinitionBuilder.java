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

import com.redhat.ceylon.compiler.java.util.Util;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCAssign;
import com.sun.tools.javac.tree.JCTree.JCExpression;
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
    private final String attrName;
    private String className;

    private long modifiers;

    private final ListBuffer<JCAnnotation> annotations = ListBuffer.lb();

    private boolean readable = true;
    private final MethodDefinitionBuilder getterBuilder;

    private JCTree.JCExpression variableInit;

    private boolean writable = true;
    private final MethodDefinitionBuilder setterBuilder;
    
    private boolean isHashCode = false;
    
    private AbstractTransformer owner;

    private AttributeDefinitionBuilder(AbstractTransformer owner, TypedDeclaration attrType, String className, String attrName, String fieldName) {
        boolean isGenericsType = owner.isGenericsImplementation(attrType);
        int typeFlags = isGenericsType ? AbstractTransformer.TYPE_ARGUMENT : 0;
        // Special erasure for the "hash" attribute which gets translated to hashCode()
        if ("hash".equals(attrName) && owner.isCeylonInteger(attrType.getType())) {
            typeFlags = AbstractTransformer.SMALL_TYPE;
            isHashCode = true;
        }
        
        JCExpression type = owner.makeJavaType(attrType.getType(), typeFlags);
        this.attrType = type;
        
        this.owner = owner;
        this.className = className;
        this.attrName = attrName;
        this.fieldName = fieldName;
        
        getterBuilder = MethodDefinitionBuilder
            .systemMethod(owner, Util.getGetterName(attrName))
            .block(generateDefaultGetterBlock())
            .isActual(attrType.isActual())
            .resultType(type, attrType);
        setterBuilder = MethodDefinitionBuilder
            .systemMethod(owner, Util.getSetterName(attrName))
            .block(generateDefaultSetterBlock())
            .isActual(attrType.isActual())
            .parameter(0, attrName, attrType);
    }

    public static AttributeDefinitionBuilder wrapped(AbstractTransformer owner, String name, TypedDeclaration attrType) {
        return new AttributeDefinitionBuilder(owner, attrType, name, name, "value");
    }
    
    public static AttributeDefinitionBuilder getter(AbstractTransformer owner, String name, TypedDeclaration attrType) {
        return new AttributeDefinitionBuilder(owner, attrType, null, name, name)
            .skipField()
            .immutable();
    }
    
    public static AttributeDefinitionBuilder setter(AbstractTransformer owner, String name, TypedDeclaration attrType) {
        return new AttributeDefinitionBuilder(owner, attrType, null, name, name)
            .skipField()
            .skipGetter();
    }
    
    /**
     * Generates the class and returns the generated tree.
     * @return the generated class tree, to be added to the appropriate {@link JCTree.JCCompilationUnit}.
     */
    public List<JCTree> build() {
        ListBuffer<JCTree> defs = ListBuffer.lb();
        appendDefinitionsTo(defs);
        if (className != null) {
            return ClassDefinitionBuilder
                .klass(owner, className)
                .modifiers(Flags.FINAL | (modifiers & (Flags.PUBLIC | Flags.PRIVATE)))
                .constructorModifiers(Flags.PRIVATE)
                .annotations(owner.makeAtAttribute())
                .annotations(annotations.toList())
                .defs(defs.toList())
                .build();
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
            defs.append(getterBuilder.build());
        }

        if (writable) {
            setterBuilder.modifiers(getGetSetModifiers());
            defs.append(setterBuilder.build());
        }
    }

    private long getGetSetModifiers() {
        return modifiers & (Flags.PUBLIC | Flags.PRIVATE | Flags.ABSTRACT | Flags.FINAL | Flags.STATIC);
    }

    private JCTree generateField() {
        long flags = Flags.PRIVATE | (modifiers & Flags.STATIC);
        // only make it final if we have an init, otherwise we still have to initialise it
        if (!writable && variableInit != null) {
            flags |= Flags.FINAL;
        }

        return owner.make().VarDef(
                owner.make().Modifiers(flags),
                owner.names().fromString(Util.quoteIfJavaKeyword(fieldName)),
                attrType,
                null
        );
    }

    private JCTree generateFieldInit() {
        long flags = (modifiers & Flags.STATIC);

        JCAssign init = owner.make().Assign(owner.makeUnquotedIdent(fieldName), variableInit);
        return owner.make().Block(flags, 
                List.<JCTree.JCStatement>of(owner.make().Exec(init)));
    }

    public JCTree.JCBlock generateDefaultGetterBlock() {
        JCExpression returnExpr = owner.makeUnquotedIdent(fieldName);
        if (isHashCode) {
            returnExpr = owner.make().TypeCast(owner.syms().intType, returnExpr);
        }
        return owner.make().Block(0L, List.<JCTree.JCStatement>of(owner.make().Return(returnExpr)));
    }

    public JCTree.JCBlock generateDefaultSetterBlock() {
        JCExpression fld;
        if (fieldName.equals(attrName)) {
            fld = owner.makeSelect("this", fieldName);
        } else {
            fld = owner.makeUnquotedIdent(fieldName);
        }
        return owner.make().Block(0L, List.<JCTree.JCStatement>of(
                owner.make().Exec(
                        owner.make().Assign(
                                fld,
                                owner.makeUnquotedIdent(attrName)))));
    }

    /**
     * Sets the name for generated class.
     * If not used will use the same name as for the variable.
     * @param className the new class name
     * @return this instance for method chaining
     */
    public AttributeDefinitionBuilder className(String className) {
        this.className = className;
        return this;
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

    public AttributeDefinitionBuilder annotations(List<JCTree.JCAnnotation> annotations) {
        this.annotations.appendList(annotations);
        return this;
    }

    public AttributeDefinitionBuilder isActual(boolean isActual) {
        getterBuilder.isActual(isActual);
        setterBuilder.isActual(isActual);
        return this;
    }

    public AttributeDefinitionBuilder isFormal(boolean isFormal) {
        getterBuilder.isFormal(isFormal);
        setterBuilder.isFormal(isFormal);
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
     * Causes the generated global to be immutable. The <tt>value</tt> field is declared <tt>final</tt> and no
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
}