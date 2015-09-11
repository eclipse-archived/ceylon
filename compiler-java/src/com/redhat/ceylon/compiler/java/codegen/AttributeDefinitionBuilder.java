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

import com.redhat.ceylon.compiler.java.codegen.recovery.HasErrorException;
import com.redhat.ceylon.model.typechecker.model.FunctionOrValue;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.model.typechecker.model.TypedReference;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCCatch;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
import com.sun.tools.javac.tree.JCTree.JCIf;
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

    private final TypedDeclaration attrTypedDecl;
    private final String attrName;
    /** 
     * If this is a wrapped attribute, and this builder is responsible for 
     * generating the wrapper class, this is the name of the wrapper class 
     */
    private final String javaClassName;
    /**
     * If this is a wrapped attribute, this is the class builder for the 
     * wrapper class
     */
    private final ClassDefinitionBuilder classBuilder;
    private final int typeFlags;
    private final Type attrType;
    
    private final boolean toplevel;
    private final boolean late;
    private final boolean lateWithInit;
    private final boolean variable;
    
    private long modifiers;

    private boolean readable = true;
    private final MethodDefinitionBuilder getterBuilder;

    private JCTree.JCExpression variableInit;
    private HasErrorException variableInitThrow;

    private boolean writable = true;
    private final MethodDefinitionBuilder setterBuilder;
    
    private AbstractTransformer owner;

    private int annotationFlags = Annotations.MODEL_AND_USER;
    
    // do we need a constructor that takes the initial value? 
    private boolean valueConstructor;
    
    private ListBuffer<JCAnnotation> classAnnotations;
    private ListBuffer<JCAnnotation> modelAnnotations;
    private ListBuffer<JCAnnotation> userAnnotations;
    private ListBuffer<JCAnnotation> userAnnotationsSetter;
    private ListBuffer<JCAnnotation> fieldAnnotations;
    private boolean isHash;
    
    private JCExpression setterClass;
    private JCExpression getterClass;

    private AttributeDefinitionBuilder(AbstractTransformer owner, TypedDeclaration attrType, 
            String javaClassName, ClassDefinitionBuilder classBuilder, String attrName, String fieldName, boolean toplevel, boolean indirect) {
        int typeFlags = 0;
        TypedReference typedRef = owner.getTypedReference(attrType);
        TypedReference nonWideningTypedRef = owner.nonWideningTypeDecl(typedRef);
        Type nonWideningType = owner.nonWideningType(typedRef, nonWideningTypedRef);
        if(attrType.isActual()
                && CodegenUtil.hasTypeErased(attrType))
            typeFlags |= AbstractTransformer.JT_RAW;
        if (!CodegenUtil.isUnBoxed(nonWideningTypedRef.getDeclaration())) {
            typeFlags |= AbstractTransformer.JT_NO_PRIMITIVES;
        }
        this.isHash = CodegenUtil.isHashAttribute((FunctionOrValue) attrType);
        this.attrTypedDecl = attrType;
        this.owner = owner;
        this.javaClassName = javaClassName;
        if (javaClassName != null) {
            this.classBuilder = 
                    ClassDefinitionBuilder
                    .klass(owner, javaClassName, null, false);
        } else {
            this.classBuilder = classBuilder;
        }
        this.attrType = nonWideningType;
        this.typeFlags = typeFlags;
        this.attrName = attrName;
        this.fieldName = fieldName;
        this.toplevel = toplevel;
        this.late = attrType.isLate();
        this.lateWithInit = this.late && CodegenUtil.needsLateInitField(attrType, owner.typeFact());
        this.variable = attrType.isVariable();
        
        // Make sure we use the declaration for building the getter/setter names, as we might be trying to
        // override a JavaBean property with an "isFoo" getter, or non-Ceylon casing, and we have to respect that.
        getterBuilder = MethodDefinitionBuilder
            .getter(owner, attrType, indirect)
            .block(generateDefaultGetterBlock())
            .isOverride(attrType.isActual())
            .isTransient(Decl.isTransient(attrType))
            .modelAnnotations(attrType.getAnnotations())
            .resultType(attrType(), attrType);
        
        ParameterDefinitionBuilder pdb = ParameterDefinitionBuilder.systemParameter(owner, attrName);
        pdb.modifiers(Flags.FINAL);
        
        pdb.aliasName(attrName);
        int seterParamFlags = 0;
        if (owner.rawParameters(attrType)) {
            seterParamFlags |= AbstractTransformer.JT_RAW;
        }
        pdb.type(MethodDefinitionBuilder.paramType(owner, nonWideningTypedRef.getDeclaration(), nonWideningType, seterParamFlags), 
                owner.makeJavaTypeAnnotations(attrType));
        
        
        setterBuilder = MethodDefinitionBuilder
            .setter(owner, attrType)
            .block(generateDefaultSetterBlock())
            // only actual if the superclass is also variable
            .isOverride(attrType.isActual() && ((TypedDeclaration)attrType.getRefinedDeclaration()).isVariable())
            .parameter(pdb);
    }
    
    public static AttributeDefinitionBuilder wrapped(AbstractTransformer owner, 
            String javaClassName, ClassDefinitionBuilder classBuilder, String attrName, TypedDeclaration attrType, 
            boolean toplevel) {
        return new AttributeDefinitionBuilder(owner, attrType, javaClassName, classBuilder, attrName, "value", toplevel, false);
    }
    
    public static AttributeDefinitionBuilder singleton(AbstractTransformer owner, 
            String javaClassName, ClassDefinitionBuilder classBuilder, String attrName, TypedDeclaration attrType, 
            boolean toplevel) {
        AttributeDefinitionBuilder adb = new AttributeDefinitionBuilder(owner, attrType, javaClassName, classBuilder, attrName, attrName, toplevel, false);
        adb.getterBuilder.realName(attrType.getName());
        return adb;
    }
    
    public static AttributeDefinitionBuilder indirect(AbstractTransformer owner, 
            String javaClassName, String attrName, TypedDeclaration attrType, 
            boolean toplevel) {
        return new AttributeDefinitionBuilder(owner, attrType, javaClassName, null, attrName, "value", toplevel, true);
    }
    
    public static AttributeDefinitionBuilder getter(AbstractTransformer owner, 
            String attrAndFieldName, TypedDeclaration attrType) {
        return new AttributeDefinitionBuilder(owner, attrType, null, null,
                attrAndFieldName, attrAndFieldName, false, false)
            .skipField()
            .immutable();
    }
    
    public static AttributeDefinitionBuilder setter(AbstractTransformer owner, 
            String attrAndFieldName, TypedDeclaration attrType) {
        return new AttributeDefinitionBuilder(owner, attrType, null, null,
                attrAndFieldName, attrAndFieldName, false, false)
            .skipField()
            .skipGetter();
    }
    
    public AttributeDefinitionBuilder modelAnnotations(List<JCAnnotation> annotations) {
        if (annotations != null) {
            if (this.modelAnnotations == null) {
                this.modelAnnotations = ListBuffer.lb();
            }
            this.modelAnnotations.appendList(annotations);
        }
        return this;
    }

    public AttributeDefinitionBuilder classAnnotations(List<JCAnnotation> annotations) {
        if (annotations != null) {
            if (this.classAnnotations == null) {
                this.classAnnotations = ListBuffer.lb();
            }
            this.classAnnotations.appendList(annotations);
        }
        return this;
    }
    
    public AttributeDefinitionBuilder fieldAnnotations(List<JCAnnotation> annotations) {
        if (annotations != null) {
            if (this.fieldAnnotations == null) {
                this.fieldAnnotations = ListBuffer.lb();
            }
            this.fieldAnnotations.appendList(annotations);
        }
        return this;
    }

    public AttributeDefinitionBuilder userAnnotations(List<JCAnnotation> annotations) {
        if (annotations != null) {
            if (this.userAnnotations == null) {
                this.userAnnotations = ListBuffer.lb();
            }
            this.userAnnotations.appendList(annotations);
        }
        return this;
    }
    
    public AttributeDefinitionBuilder userAnnotationsSetter(List<JCAnnotation> annotations) {
        if (annotations != null) {
            if (this.userAnnotationsSetter == null) {
                this.userAnnotationsSetter = ListBuffer.lb();
            }
            this.userAnnotationsSetter.appendList(annotations);
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
            classBuilder.getInitBuilder().modifiers(Flags.PRIVATE);
            classBuilder
                    .modifiers(Flags.FINAL | (modifiers & (Flags.PUBLIC | Flags.PRIVATE)))
                    .defs(defs.toList());
            if(getterClass == null){
                classBuilder.annotations(owner.makeAtAttribute(setterClass))
                    .annotations(owner.makeAtName(attrName))
                    .satisfies(getSatisfies());
            }else{
                classBuilder.annotations(owner.makeAtIgnore());
                classBuilder.annotations(owner.makeAtSetter(getterClass));
            }

            if(classAnnotations != null)
                classBuilder.annotations(classAnnotations.toList());
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
            if (variableInitThrow == null) {
                defs.append(generateField());
                if (hasInitFlag()) {
                    defs.append(generateInitFlagField());
                }
                if(isDeferredInitError())
                    defs.append(generateInitExceptionField());
                if(variableInit != null) {
                    defs.append(generateFieldInit());
                }
            }
        }
        
        if (readable) {
            if (variableInitThrow != null) {
                getterBuilder.block(owner.make().Block(0, List.<JCStatement>of(owner.makeThrowUnresolvedCompilationError(variableInitThrow))));
            }
            getterBuilder.modifiers(getGetSetModifiers());
            getterBuilder.annotationFlags(annotationFlags);
            if (this.modelAnnotations != null) {
                getterBuilder.modelAnnotations(this.modelAnnotations.toList());
            }
            if (this.userAnnotations != null) {
                getterBuilder.userAnnotations(this.userAnnotations.toList());
            }
            defs.append(getterBuilder.build());
        }

        if (writable) {
            if (variableInitThrow != null) {
                setterBuilder.block(owner.make().Block(0, List.<JCStatement>of(owner.makeThrowUnresolvedCompilationError(variableInitThrow))));
            }
            setterBuilder.modifiers(getGetSetModifiers());
            // mark it with @Ignore if it's late but not variable
            setterBuilder.annotationFlags(annotationFlags | (late && !variable ? Annotations.IGNORE : 0));
            if (this.userAnnotationsSetter != null) {
                setterBuilder.userAnnotations(this.userAnnotationsSetter.toList());
            }
            defs.append(setterBuilder.build());
        }
    }

    private boolean isDeferredInitError() {
        return toplevel && !late;
    }

    private List<Type> getSatisfies() {
        List<Type> types = List.<Type>nil();
        if (javaClassName != null && readable && !toplevel) {
            types = types.append(owner.getGetterInterfaceType(attrTypedDecl));
        }
        return types;
    }
    
    private void generateValueConstructor(MethodDefinitionBuilder methodDefinitionBuilder) {
        ParameterDefinitionBuilder paramBuilder = ParameterDefinitionBuilder.systemParameter(owner, fieldName).type(attrType(), null);
        JCTree.JCAssign init = owner.make().Assign(owner.makeQualIdent(owner.naming.makeThis(), fieldName), owner.makeUnquotedIdent(fieldName));
        methodDefinitionBuilder.parameter(paramBuilder).body(owner.make().Exec(init));
    }

    private JCExpression attrType(){
        // make sure we generate int getters for hash
        if(this.isHash){
            return owner.make().Type(owner.syms().intType);
        }else{
            return owner.makeJavaType(attrType, typeFlags);
        }
    }

    private JCExpression attrTypeRaw(){
        // make sure we generate int getters for hash
        if(this.isHash){
            return owner.make().Type(owner.syms().intType);
        }else{
            return owner.makeJavaType(attrType, AbstractTransformer.JT_RAW);
        }
    }

    private long getGetSetModifiers() {
        long mods = modifiers;
        if (javaClassName != null) {
            mods |= Flags.PUBLIC;
        }
        return mods & (Flags.PUBLIC | Flags.PRIVATE | Flags.ABSTRACT | Flags.FINAL | Flags.STATIC);
    }

    private JCTree generateField() {
        long flags = Flags.PRIVATE | (modifiers & Flags.STATIC);
        // only make it final if we have an init, otherwise we still have to initialise it
        if (!writable && (variableInit != null || valueConstructor)) {
            flags |= Flags.FINAL;
        }

        return owner.make().VarDef(
                owner.make().Modifiers(flags, fieldAnnotations != null ? fieldAnnotations.toList() : List.<JCAnnotation>nil()),
                owner.names().fromString(Naming.quoteFieldName(fieldName)),
                attrType(),
                null
        );
    }
    
    private JCTree generateInitFlagField() {
        long flags = Flags.PRIVATE | (modifiers & Flags.STATIC) | Flags.VOLATILE;
        if((flags & Flags.STATIC) == 0)
            flags |= Flags.TRANSIENT;
        
        return owner.make().VarDef(
                owner.make().Modifiers(flags),
                owner.names().fromString(Naming.getInitializationFieldName(fieldName)),
                owner.make().Type(owner.syms().booleanType),
                owner.make().Literal(false)
        );
    }

    private JCTree generateInitExceptionField() {
        long flags = Flags.PRIVATE | Flags.STATIC | Flags.FINAL;

        return owner.make().VarDef(
                owner.make().Modifiers(flags),
                owner.names().fromString(Naming.quoteIfJavaKeyword(Naming.getToplevelAttributeSavedExceptionName())),
                owner.makeJavaType(owner.syms().throwableType.tsym),
                null
        );
    }

    private JCTree generateFieldInit() {
        long flags = (modifiers & Flags.STATIC);
        
        JCTree.JCExpression varInit = variableInit;
        if (hasInitFlag()) {
            varInit = variableInit;
        }
        JCTree.JCAssign init = owner.make().Assign(owner.makeUnquotedIdent(Naming.quoteFieldName(fieldName)), varInit);
        List<JCStatement> stmts;
        if(isDeferredInitError()){
            // surround the init expression with a try/catch that saves the exception
            
            String exceptionName = "x"; // doesn't matter
            
            // $initException$ = x
            JCStatement saveException = owner.make().Exec(owner.make().Assign(
                    owner.makeUnquotedIdent(Naming.getToplevelAttributeSavedExceptionName()), 
                    owner.makeUnquotedIdent(exceptionName)));
            // value = null
            JCStatement nullValue = owner.make().Exec(owner.make().Assign(owner.makeUnquotedIdent(fieldName), owner.makeDefaultExprForType(this.attrType)));
            // the catch statements
            JCStatement initFlagFalse = owner.make().Exec(owner.make().Assign(
                    owner.naming.makeUnquotedIdent(Naming.getInitializationFieldName(fieldName)), 
                    owner.make().Literal(false)));
            JCBlock handlerBlock = owner.make().Block(0, List.<JCTree.JCStatement>of(saveException, nullValue, initFlagFalse));
            
            // the catch block
            JCExpression throwableType = owner.makeJavaType(owner.syms().throwableType.tsym);
            JCVariableDecl exceptionParam = owner.make().VarDef(owner.make().Modifiers(0), 
                    owner.naming.makeUnquotedName(exceptionName), 
                    throwableType , null);
            JCCatch catchers = owner.make().Catch(exceptionParam, handlerBlock);
            
            // $initException$ = null
            JCTree.JCAssign nullException = owner.make().Assign(owner.makeUnquotedIdent(Naming.getToplevelAttributeSavedExceptionName()), 
                    owner.makeNull());
            // $init$value = true;
            JCTree.JCAssign initFlagTrue = owner.make().Assign(
                    owner.naming.makeUnquotedIdent(Naming.getInitializationFieldName(fieldName)), 
                    owner.make().Literal(true));
            // save the value, mark the exception as null
            List<JCStatement> body = List.<JCTree.JCStatement>of(
                    owner.make().Exec(init), 
                    owner.make().Exec(nullException),
                    owner.make().Exec(initFlagTrue));
            
            // the try/catch
            JCTree.JCTry try_ = owner.make().Try(owner.make().Block(0, body), List.<JCTree.JCCatch>of(catchers), null);
            stmts = List.<JCTree.JCStatement>of(try_);
        }else{
            stmts = List.<JCTree.JCStatement>of(owner.make().Exec(init));
        }
        return owner.make().Block(flags, stmts);
    }

    private JCTree.JCBlock generateDefaultGetterBlock() {
        JCTree.JCExpression returnExpr = owner.makeQuotedIdent(fieldName);
        // make sure we turn hash long to int properly
        if(isHash)
            returnExpr = owner.convertToIntForHashAttribute(returnExpr);
        JCReturn returnValue = owner.make().Return(returnExpr);
        List<JCStatement> stmts;
        
        stmts = List.<JCTree.JCStatement>of(returnValue);
        
        JCTree.JCBlock block;
        if (toplevel || late) {
            JCExpression msg = owner.make().Literal(late ? "Accessing uninitialized 'late' attribute '"+attrName+"'" : "Cyclic initialization trying to read the value of '"+attrName+"' before it was set");
            JCTree.JCThrow throwStmt = owner.make().Throw(owner.makeNewClass(owner.makeIdent(owner.syms().ceylonInitializationErrorType), 
                    List.<JCExpression>of(msg)));
            List<JCStatement> catchStmts;
            if(isDeferredInitError()){
                JCStatement rethrow = owner.make().Exec(owner.utilInvocation().rethrow( 
                        owner.makeUnquotedIdent(Naming.getToplevelAttributeSavedExceptionName())));
                // rethrow the init exception if we have one
                JCIf ifThrow = owner.make().If(owner.make().Binary(JCTree.NE, owner.makeUnquotedIdent(Naming.getToplevelAttributeSavedExceptionName()), 
                        owner.makeNull()), rethrow, null);
                catchStmts = List.<JCTree.JCStatement>of(ifThrow, throwStmt);
            }else{
                catchStmts = List.<JCTree.JCStatement>of(throwStmt);
            }
            block = owner.make().Block(0L, List.<JCTree.JCStatement>of(
                    owner.make().If(makeInitFlagExpr(true), 
                    owner.make().Block(0, stmts),
                    owner.make().Block(0, catchStmts))));
        } else {
            block = owner.make().Block(0L, stmts);
        }
        return block;
    }
    
    
    private boolean hasInitFlag() {
        return toplevel || lateWithInit;
    }

    public JCTree.JCBlock generateDefaultSetterBlock() {
        JCExpression fld = fld();
        JCExpressionStatement assign = owner.make().Exec(
                owner.make().Assign(
                        fld,
                        owner.makeQuotedIdent(attrName)));
        List<JCStatement> stmts = List.<JCTree.JCStatement>of(assign);
        if (late) {
            
            JCExpressionStatement makeInit = null;
            if(lateWithInit)
                makeInit = owner.make().Exec(owner.make().Assign(makeInitFlagExpr(true),
                                                                 owner.make().Literal(true)));
            if (variable) {
                if(makeInit != null)
                    stmts = List.<JCStatement>of(assign, makeInit);
                //else stmts is already assign
            } else {
                List<JCStatement> then = List.<JCStatement>of(owner.make().Return(null));
                if(makeInit != null)
                    then = then.prepend(makeInit);
                then = then.prepend(assign);
                stmts = List.of(
                    owner.make().If(
                        makeInitFlagExpr(false),
                        owner.make().Block(0, then), 
                        null),
                    owner.make().Throw(owner.makeNewClass( 
                                       owner.make().Type(owner.syms().ceylonInitializationErrorType), 
                                       List.<JCExpression>of(owner.make().Literal("Re-initialization of 'late' attribute")))
                ));
            }
        }
        if (hasInitFlag() && isDeferredInitError()){
            JCStatement rethrow = owner.make().Exec(owner.utilInvocation().rethrow( 
                    owner.makeUnquotedIdent(Naming.getToplevelAttributeSavedExceptionName())));
            // rethrow the init exception if we have one
            JCIf ifThrow = owner.make().If(owner.make().Binary(JCTree.NE, owner.makeUnquotedIdent(Naming.getToplevelAttributeSavedExceptionName()), 
                    owner.makeNull()), rethrow, null);
            stmts = stmts.prepend(ifThrow);
        }
        return owner.make().Block(0L, stmts);
    }

    private JCExpression makeInitFlagExpr(boolean positiveIfTest) {
        final JCExpression initFlagFieldOwner;
        if (toplevel) {
            if (classBuilder != null) {
                // TODO Needs to be qualified name
                initFlagFieldOwner = owner.naming.makeUnquotedIdent(classBuilder.getClassName());
            } else {
                initFlagFieldOwner = owner.naming.makeUnquotedIdent(javaClassName);
            }
        } else {
            initFlagFieldOwner = owner.naming.makeThis();
        }
        // TODO this should be encapsulated so the s11n stuff and this
        // code can just call something common
        if(toplevel || lateWithInit){
            JCExpression ret = owner.naming.makeQualIdent(initFlagFieldOwner, Naming.getInitializationFieldName(fieldName));
            if(!positiveIfTest)
                return owner.make().Unary(JCTree.NOT, ret);
            return ret;
        }else
            return owner.make().Binary(positiveIfTest ? JCTree.NE : JCTree.EQ, 
                                       owner.naming.makeQualIdent(initFlagFieldOwner, fieldName), owner.makeNull());
    }

    private JCExpression fld() {
        JCExpression fld;
        if (fieldName.equals(attrName)) {
            fld = owner.makeSelect("this", Naming.quoteFieldName(fieldName));
        } else {
            fld = owner.makeQuotedIdent(fieldName);
        }
        return fld;
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

    public AttributeDefinitionBuilder noModelAnnotations() {
        this.annotationFlags = Annotations.noModel(annotationFlags);
        return this;
    }
    
    public AttributeDefinitionBuilder ignoreAnnotations() {
        this.annotationFlags = Annotations.noUserOrModel(Annotations.ignore(annotationFlags));
        return this;
    }

    public AttributeDefinitionBuilder noAnnotations() {
        this.annotationFlags = 0;
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
    

    public AttributeDefinitionBuilder initialValueError(HasErrorException variableInitThrow) {
        this.variableInitThrow = variableInitThrow;
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

    /**
     * Makes a reference to the setter class for this setter
     */
    public void setterClass(JCExpression setterClass) {
        this.setterClass = setterClass;
    }

    /**
     * Marks this attribute as a setter and skip generating an @Attribute annotation for it,
     * use a @Setter(getterClass) instead
     */
    public void isSetter(JCExpression getterClass) {
        this.getterClass = getterClass;
    }
}
