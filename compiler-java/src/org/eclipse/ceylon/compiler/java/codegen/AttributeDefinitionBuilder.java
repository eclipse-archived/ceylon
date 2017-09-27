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

package org.eclipse.ceylon.compiler.java.codegen;

import static org.eclipse.ceylon.model.loader.NamingBase.getJavaAttributeName;

import org.eclipse.ceylon.compiler.java.codegen.AbstractTransformer.BoxingStrategy;
import org.eclipse.ceylon.compiler.java.codegen.Naming.SyntheticName;
import org.eclipse.ceylon.compiler.java.codegen.recovery.HasErrorException;
import org.eclipse.ceylon.compiler.typechecker.tree.Node;
import org.eclipse.ceylon.langtools.tools.javac.code.Flags;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCAnnotation;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCBlock;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCCatch;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCExpression;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCIf;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCStatement;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCVariableDecl;
import org.eclipse.ceylon.langtools.tools.javac.util.List;
import org.eclipse.ceylon.langtools.tools.javac.util.ListBuffer;
import org.eclipse.ceylon.model.loader.NamingBase;
import org.eclipse.ceylon.model.typechecker.model.Class;
import org.eclipse.ceylon.model.typechecker.model.ClassOrInterface;
import org.eclipse.ceylon.model.typechecker.model.FunctionOrValue;
import org.eclipse.ceylon.model.typechecker.model.Type;
import org.eclipse.ceylon.model.typechecker.model.TypeParameter;
import org.eclipse.ceylon.model.typechecker.model.TypedDeclaration;
import org.eclipse.ceylon.model.typechecker.model.TypedReference;

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
     * If this is a wrapped attribute, this is the class builder for the 
     * wrapper class
     */
    private final int typeFlags;
    private final Type attrType;
    
    private final boolean toplevel;
    private final boolean late;
    private final boolean variable;
    
    private long modifiers;
    private long fieldModifiers = Flags.PRIVATE;

    private boolean readable = true;
    private MethodDefinitionBuilder getterBuilder;

    private JCTree.JCExpression initialValue;
    private JCTree.JCExpression memoizedInitialValue;
    private HasErrorException initialException;

    private boolean writable = true;
    private MethodDefinitionBuilder setterBuilder;
    
    private AbstractTransformer owner;

    private int annotationFlags = Annotations.MODEL_AND_USER;
    
    // do we need a constructor that takes the initial value? 
    private boolean valueConstructor;
    
    private ListBuffer<JCAnnotation> classAnnotations;
    private ListBuffer<JCAnnotation> modelAnnotations;
    private ListBuffer<JCAnnotation> userAnnotations;
    private ListBuffer<JCAnnotation> userAnnotationsSetter;
    private ListBuffer<JCAnnotation> fieldAnnotations;
    private JCAnnotation fieldNullability;
    private boolean isHash;
    
    private JCExpression setterClass;
    private JCExpression getterClass;
    private boolean deferredInitError;
    private final InitTest initTest;
    private final boolean useJavaBox;
    private BoxingStrategy initialValueBoxing;

    private AttributeDefinitionBuilder(AbstractTransformer owner, Node node, TypedDeclaration attrType, 
            String attrName, String fieldName, boolean toplevel, boolean indirect, 
            boolean field, boolean isIndirect, JCExpression memoizedInitialValue) {
        int typeFlags = 0;
        TypedReference typedRef = owner.getTypedReference(attrType);
        TypedReference nonWideningTypedRef = owner.nonWideningTypeDecl(typedRef);
        Type nonWideningType = owner.nonWideningType(typedRef, nonWideningTypedRef);
        if (isIndirect) {
            //attrName = Naming.getAttrClassName(model, 0);
            nonWideningType = owner.getGetterInterfaceType(attrType);
        }
        if(!field && attrType.isActual()
                && CodegenUtil.hasTypeErased(attrType))
            typeFlags |= AbstractTransformer.JT_RAW;
        if (!CodegenUtil.isUnBoxed(nonWideningTypedRef.getDeclaration())) {
            typeFlags |= AbstractTransformer.JT_NO_PRIMITIVES;
        }
        this.isHash = CodegenUtil.isHashAttribute((FunctionOrValue) attrType);
        this.attrTypedDecl = attrType;
        this.owner = owner;
        this.attrType = nonWideningType;
        this.useJavaBox = owner.useJavaBox(attrTypedDecl, this.attrType) 
                || 
                (owner.isJavaPrimitiveBoxableType(((TypedDeclaration)attrTypedDecl.getRefinedDeclaration()).getType(), true)
                && owner.useJavaBox(attrTypedDecl, ((TypedDeclaration)attrTypedDecl.getRefinedDeclaration()).getType())
                && !CodegenUtil.isUnBoxed((TypedDeclaration)attrTypedDecl.getRefinedDeclaration()));
        this.typeFlags = typeFlags;
        this.attrName = attrName;
        this.fieldName = fieldName;
        this.toplevel = toplevel;
        this.late = attrTypedDecl.isLate();
        final boolean lateWithInit = this.late && CodegenUtil.needsLateInitField(attrType, owner.typeFact());
        final boolean hasInitFlag = (toplevel && !late) || lateWithInit;
        this.variable = attrType.isVariable();
        this.deferredInitError = toplevel && !late;
        this.memoizedInitialValue = memoizedInitialValue;
        
        // Make sure we use the declaration for building the getter/setter names, as we might be trying to
        // override a JavaBean property with an "isFoo" getter, or non-Ceylon casing, and we have to respect that.
        this.initTest = owner.isEe(attrTypedDecl) && hasInitFlag ? new NoInitTest() : 
                        hasInitFlag ? new FieldInitTest() : 
                            new NullnessInitTest();
        if (!field) {
            getterBuilder = MethodDefinitionBuilder
                .getter(owner, attrType, indirect)
                .block(owner.make().Block(0L, makeGetter()))
                .isOverride(attrType.isActual())
                .isTransient(Decl.isTransient(attrType))
                .modelAnnotations(attrType.getAnnotations())
                .resultType(attrType(), attrType);
            ParameterDefinitionBuilder pdb = ParameterDefinitionBuilder.systemParameter(owner, setterParameterName());
//            pdb.at(node);
            pdb.modifiers(Flags.FINAL);
            pdb.aliasName(setterParameterName());
            int seterParamFlags = 0;
            if (owner.rawParameters(attrType)) {
                seterParamFlags |= AbstractTransformer.JT_RAW;
            }
            pdb.type(new TransformedType(MethodDefinitionBuilder.paramType(owner, nonWideningTypedRef.getDeclaration(), nonWideningType, seterParamFlags), 
                    owner.makeJavaTypeAnnotations(attrType),
                    owner.makeNullabilityAnnotations(attrType)));
            
            
            setterBuilder = MethodDefinitionBuilder
                .setter(owner, attrType)
                .block(owner.make().Block(0L, makeSetter()))
                // only actual if the superclass is also variable
                .isOverride(attrType.isActual() && ((TypedDeclaration)attrType.getRefinedDeclaration()).isVariable());
            if (attrType.isStatic()) {
                for (TypeParameter tp : Strategy.getEffectiveTypeParameters(attrType)) {
                    getterBuilder.typeParameter(owner.makeTypeParameter(tp, null), null);
                    getterBuilder.reifiedTypeParameter(tp);
                    setterBuilder.typeParameter(owner.makeTypeParameter(tp, null), null);
                    setterBuilder.reifiedTypeParameter(tp);
                }
            }
            setterBuilder.parameter(pdb);
        } else {
            setterBuilder = null;
            getterBuilder = null;
        }
    }
    
    public String setterParameterName() {
        return attrName;
    }
    
    public static AttributeDefinitionBuilder wrapped(AbstractTransformer owner, 
            String attrName, TypedDeclaration attrType, 
            boolean toplevel, JCExpression memoizedInitialValue) {
        return new AttributeDefinitionBuilder(owner, null, attrType, attrName, NamingBase.Unfix.$object$.toString(), toplevel, false, false, false, memoizedInitialValue);
    }
    
    public static AttributeDefinitionBuilder singleton(AbstractTransformer owner, 
            String attrName, TypedDeclaration attrType, 
            boolean toplevel) {
        AttributeDefinitionBuilder adb = new AttributeDefinitionBuilder(owner, null, attrType, attrName, attrName, toplevel, false, false, false, null);
        adb.getterBuilder.realName(attrType.getName());
        return adb;
    }
    
    public static AttributeDefinitionBuilder indirect(AbstractTransformer owner, 
            String attrName, TypedDeclaration attrType, 
            boolean toplevel) {
        return new AttributeDefinitionBuilder(owner, null, attrType, attrName, "value", toplevel, true, false, false, null);
    }
    
    public static AttributeDefinitionBuilder getter(AbstractTransformer owner, 
            String attrAndFieldName, TypedDeclaration attrType) {
        return getter(owner, attrAndFieldName, attrType, null);
    }
    
    public static AttributeDefinitionBuilder getter(AbstractTransformer owner, 
            String attrAndFieldName, TypedDeclaration attrType, JCExpression memoizedInitialiValue) {
        String getterName = Naming.getGetterName(attrType, false);
        AttributeDefinitionBuilder adb = new AttributeDefinitionBuilder(owner, null, attrType, 
                        attrAndFieldName, attrAndFieldName, false, false, false, false, memoizedInitialiValue);
        if (!"ref".equals(getterName)
                && !"get_".equals(getterName)
                && !attrType.getName().equals(getJavaAttributeName(getterName))
                && attrType.isShared()
                && !Decl.isValueConstructor(attrType)) {
            adb.getterBuilder.realName(attrType.getName());
        }
        return adb
            .skipField()
            .immutable();
    }
    
    public static AttributeDefinitionBuilder setter(AbstractTransformer owner, 
            Node node, 
            String attrAndFieldName, TypedDeclaration attrType) {
        return setter(owner, node, attrAndFieldName, attrType, null);
    }
    
    public static AttributeDefinitionBuilder setter(AbstractTransformer owner, 
            Node node, 
            String attrAndFieldName, TypedDeclaration attrType, JCExpression memoizedInitialValue) {
        AttributeDefinitionBuilder adb = new AttributeDefinitionBuilder(owner, node, attrType, 
                attrAndFieldName, attrAndFieldName, false, false, false, false, memoizedInitialValue)
            .skipField()
            .skipGetter();
        return adb;
    }
    
    public static AttributeDefinitionBuilder field(AbstractTransformer owner, 
            Node node, 
            String attrAndFieldName, TypedDeclaration attrType, boolean isIndirect) {
        AttributeDefinitionBuilder adb = new AttributeDefinitionBuilder(owner, node, attrType, 
                attrAndFieldName, attrAndFieldName, false, false, true, isIndirect, null).skipField();
        return adb;
    }
    
    public AttributeDefinitionBuilder modelAnnotations(List<JCAnnotation> annotations) {
        if (annotations != null) {
            if (this.modelAnnotations == null) {
                this.modelAnnotations = new ListBuffer<JCAnnotation>();
            }
            this.modelAnnotations.appendList(annotations);
        }
        return this;
    }

    public AttributeDefinitionBuilder classAnnotations(List<JCAnnotation> annotations) {
        if (annotations != null) {
            if (this.classAnnotations == null) {
                this.classAnnotations = new ListBuffer<JCAnnotation>();
            }
            this.classAnnotations.appendList(annotations);
        }
        return this;
    }
    
    public AttributeDefinitionBuilder fieldAnnotations(List<JCAnnotation> annotations) {
        if (annotations != null) {
            if (this.fieldAnnotations == null) {
                this.fieldAnnotations = new ListBuffer<JCAnnotation>();
            }
            this.fieldAnnotations.appendList(annotations);
        }
        return this;
    }
    
    public AttributeDefinitionBuilder fieldNullability(JCAnnotation annotation) {
        this.fieldNullability = annotation;
        return this;
    }

    public AttributeDefinitionBuilder userAnnotations(List<JCAnnotation> annotations) {
        if (annotations != null) {
            if (this.userAnnotations == null) {
                this.userAnnotations = new ListBuffer<JCAnnotation>();
            }
            this.userAnnotations.appendList(annotations);
        }
        return this;
    }
    
    public AttributeDefinitionBuilder userAnnotationsSetter(List<JCAnnotation> annotations) {
        if (annotations != null) {
            if (this.userAnnotationsSetter == null) {
                this.userAnnotationsSetter = new ListBuffer<JCAnnotation>();
            }
            this.userAnnotationsSetter.appendList(annotations);
        }
        return this;
    }
    
    
    public List<JCTree> build() {
        return buildWithWrapperClass(null);
    }
    /**
     * Generates the class and returns the generated tree.
     * @return the generated class tree, to be added to the appropriate {@link JCTree.JCCompilationUnit}.
     */
    public List<JCTree> buildWithWrapperClass(ClassDefinitionBuilder classBuilder) {
        ListBuffer<JCTree> defs = new ListBuffer<JCTree>();
        appendDefinitionsTo(defs, classBuilder != null);
        if (classBuilder != null) {
            classBuilder.getInitBuilder().modifiers(Flags.PRIVATE);
            classBuilder
                    .modifiers(Flags.FINAL | (modifiers & (Flags.PUBLIC | Flags.PRIVATE | Flags.STRICTFP)))
                    .defs(defs.toList());
            if (owner.isJavaStrictfp(attrTypedDecl)) {
                //classBuilder.modifiers(Flags.STRICTFP);
            }
            if(getterClass == null){
                classBuilder.annotations(owner.makeAtAttribute(setterClass))
                    .annotations(owner.makeAtName(attrName))
                    .satisfies(getSatisfies(classBuilder != null));
            }else{
                classBuilder.annotations(owner.makeAtIgnore());
                classBuilder.annotations(owner.makeAtSetter(getterClass));
            }

            if(classAnnotations != null)
                classBuilder.annotations(classAnnotations.toList());
            if(valueConstructor && hasField)
                generateValueConstructor(classBuilder.addConstructor(this.attrTypedDecl.isDeprecated()));
            return classBuilder.build();
        } else {
            return defs.toList();
        }
    }
    /**
     * Appends to <tt>defs</tt> the definitions that would go into the class generated by {@link #build()}
     * @param defs a {@link ListBuffer} to which the definitions will be appended.
     */
    private void appendDefinitionsTo(ListBuffer<JCTree> defs, boolean withWrapperClass) {
        if (hasField) {
            if (initialException == null) {
                // the value, init flag and exception fields
                defs.appendList(AbstractTransformer.upcastList(makeFields()));
                if(initialValue != null) {
                    long flags = (modifiers & Flags.STATIC);
                    defs.append(owner.make().Block(flags, makeInit(true)));
                }
            }
        }
        
        if (readable) {
            if (initialException != null) {
                getterBuilder.block(owner.make().Block(0, List.<JCStatement>of(owner.makeThrowUnresolvedCompilationError(initialException))));
            }
            getterBuilder.modifiers(getGetSetModifiers(withWrapperClass));
            getterBuilder.annotationFlags(annotationFlags);
            if (this.modelAnnotations != null) {
                getterBuilder.modelAnnotations(this.modelAnnotations.toList());
            }
            if (owner.isEe(attrTypedDecl) && !attrTypedDecl.isDefault()) {
                getterBuilder.modelAnnotations(owner.makeAtFinal());
            }
            if (this.userAnnotations != null) {
                getterBuilder.userAnnotations(this.userAnnotations.toList());
            }
            defs.append(getterBuilder.build());
        }

        if (writable) {
            if (initialException != null) {
                setterBuilder.block(owner.make().Block(0, List.<JCStatement>of(owner.makeThrowUnresolvedCompilationError(initialException))));
            }
            setterBuilder.modifiers(getGetSetModifiers(withWrapperClass));
            // mark it with @Ignore if it's late but not variable
            setterBuilder.annotationFlags(annotationFlags | (late && !variable ? Annotations.IGNORE : 0));
            if (this.userAnnotationsSetter != null) {
                setterBuilder.userAnnotations(this.userAnnotationsSetter.toList());
            }
            defs.append(setterBuilder.build());
        }
    }

    private List<Type> getSatisfies(boolean withClassBuilder) {
        List<Type> types = List.<Type>nil();
        if (withClassBuilder && readable && !toplevel) {
            types = types.append(owner.getGetterInterfaceType(attrTypedDecl));
        }
        return types;
    }
    
    private void generateValueConstructor(MethodDefinitionBuilder methodDefinitionBuilder) {
        ParameterDefinitionBuilder paramBuilder = ParameterDefinitionBuilder.systemParameter(owner, fieldName).type(new TransformedType(valueFieldType()));
        JCTree.JCAssign init = owner.make().Assign(makeValueFieldAccess(), owner.makeUnquotedIdent(fieldName));
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

    /** modifiers for the getter & setter */
    private long getGetSetModifiers(boolean withWrapperClass) {
        long mods = modifiers;
        if (withWrapperClass) {
            mods |= Flags.PUBLIC;
        }
        return mods & (Flags.PUBLIC | Flags.PRIVATE | Flags.ABSTRACT | Flags.FINAL | Flags.STATIC | Flags.SYNCHRONIZED | Flags.NATIVE | Flags.STRICTFP);
    }
    private JCExpression makeFieldAccess(long mods, String fieldName) {
        // javac won't let us access a static final field from within a static block
        // using a class-qualified name (defeats it's DA analysis?)
        if ((mods & (Flags.FINAL | Flags.STATIC)) == (Flags.FINAL | Flags.STATIC)) {
            return owner.makeUnquotedIdent(Naming.quoteFieldName(fieldName));
        } else {
            final JCExpression initFlagFieldOwner;
            if (toplevel) {
                initFlagFieldOwner = owner.naming.makeName(attrTypedDecl, Naming.NA_FQ | Naming.NA_WRAPPER);
            } else if (attrTypedDecl.isStatic() || Decl.isValueConstructor(attrTypedDecl)) {
                initFlagFieldOwner = owner.makeJavaType((((ClassOrInterface)attrTypedDecl.getContainer())).getType(), AbstractTransformer.JT_RAW);
            } else {
                initFlagFieldOwner = owner.naming.makeThis();
            }
            return owner.makeQualIdent(initFlagFieldOwner, Naming.quoteFieldName(fieldName));
        }
    }
    /** Abstraction for initialization checking */
    abstract class InitTest {
        /** Make the declaration of the initialization check field */
        public abstract List<JCStatement> makeInitCheckField(List<JCStatement> stmts);
        /** Make an initialization test expression, or return null if there is no initialization test */
        public abstract JCExpression makeInitTest(boolean positiveIfTest);
        /** Mark the value as initialized or uninitialized */
        public abstract JCStatement makeInitInitialized(boolean initialized);
    }
    /** The nullness of the value field is used to track initialization */
    class NullnessInitTest extends InitTest {
        @Override
        public List<JCStatement> makeInitCheckField(List<JCStatement> stmts) {
            return stmts;
        }
        @Override
        public JCExpression makeInitTest(boolean positiveIfTest) {
            // TODO this should be encapsulated so the s11n stuff and this
            // code can just call something common
            return owner.make().Binary(positiveIfTest ? JCTree.Tag.NE : JCTree.Tag.EQ, 
                    makeValueFieldAccess(), owner.makeNull());
        }

        @Override
        public JCStatement makeInitInitialized(boolean initialized) {
            return null;
        }
    }
    /** A separate boolean flag field is used to track initialization */
    class FieldInitTest extends InitTest {
        /** A boolean field is used to track initialization of a value */
        @Override
        public List<JCStatement> makeInitCheckField(List<JCStatement> stmts) {
            long flags = initFieldMods();
            
            return stmts.append(owner.make().VarDef(
                    owner.make().Modifiers(flags, owner.makeAtIgnore()),
                    owner.names().fromString(Naming.getInitializationFieldName(fieldName)),
                    owner.make().Type(owner.syms().booleanType),
                    owner.make().Literal(false)));
        }

        protected long initFieldMods() {
            long flags = Flags.PRIVATE | (modifiers & Flags.STATIC) | Flags.VOLATILE;
            if((flags & Flags.STATIC) == 0)
                flags |= Flags.TRANSIENT;
            return flags;
        }
        
        @Override
        public JCExpression makeInitTest(boolean positiveIfTest) {
            // TODO this should be encapsulated so the s11n stuff and this
            // code can just call something common
            JCExpression ret = makeInitFieldAccess();
            if(!positiveIfTest)
                return owner.make().Unary(JCTree.Tag.NOT, ret);
            return ret;
        }

        protected JCExpression makeInitFieldAccess() {
            return makeFieldAccess(initFieldMods(), Naming.getInitializationFieldName(fieldName));
        }

        @Override
        public JCStatement makeInitInitialized(boolean initialized) {
            return owner.make().Exec(owner.make().Assign(makeInitFieldAccess(),
                    owner.make().Literal(initialized)));
        }
    }
    /** Initialization checking is disabled */
    class NoInitTest extends InitTest {
        @Override
        public JCExpression makeInitTest(boolean positiveIfTest) {
            return null;
        }

        @Override
        public JCStatement makeInitInitialized(boolean initialized) {
            return null;
        }

        @Override
        public List<JCStatement> makeInitCheckField(List<JCStatement> stmts) {
            return stmts;
        }
    }
    
    /** Make the declaration of the value field */
    private JCStatement makeValueField() {
        List<JCAnnotation> annos = fieldAnnotations != null ? fieldAnnotations.toList() : List.<JCAnnotation>nil();
        JCExpression type = valueFieldType();
        if (fieldNullability!=null && !TransformedType.isPrimitive(type)) {
            annos = annos.append(fieldNullability);
        }
        return owner.make().VarDef(
                owner.make().Modifiers(valueFieldModifiers(), annos),
                owner.names().fromString(Naming.quoteFieldName(fieldName)),
                type, null
        );
    }
    
    JCExpression javaBoxType(Type t) {
        if (owner.typeFact().getListDeclaration().equals(t.getDeclaration())) {
            Type elementType = t.getTypeArgumentList().get(0);
            JCExpression expr;
            if (owner.useJavaBoxInternal(attrTypedDecl, elementType, true)) {
                expr = javaBoxType(elementType);
            } else {
                expr = owner.makeJavaType(elementType, AbstractTransformer.JT_TYPE_ARGUMENT);
            }
            return owner.make().TypeApply(owner.make().QualIdent(owner.syms().listType.tsym), List.of(expr));
        }
        if (owner.typeFact().getSetDeclaration().equals(t.getDeclaration())) {
            Type elementType = t.getTypeArgumentList().get(0);
            JCExpression expr;
            if (owner.useJavaBoxInternal(attrTypedDecl, elementType, true)) {
                expr = javaBoxType(elementType);
            } else {
                expr = owner.makeJavaType(elementType, AbstractTransformer.JT_TYPE_ARGUMENT);
            }
            return owner.make().TypeApply(owner.make().QualIdent(owner.syms().setType.tsym), List.of(expr));
        }
        if (owner.typeFact().getMapDeclaration().equals(t.getDeclaration())) {
            Type keyType = t.getTypeArgumentList().get(0);
            Type itemType = t.getTypeArgumentList().get(1);
            JCExpression keyexpr;
            if (owner.useJavaBoxInternal(attrTypedDecl, keyType, true)) {
                keyexpr = javaBoxType(keyType);
            } else {
                keyexpr = owner.makeJavaType(keyType, AbstractTransformer.JT_TYPE_ARGUMENT);
            }
            JCExpression itemexpr;
            if (owner.useJavaBoxInternal(attrTypedDecl, itemType, true)) {
                itemexpr = javaBoxType(itemType);
            } else {
                itemexpr = owner.makeJavaType(itemType, AbstractTransformer.JT_TYPE_ARGUMENT);
            }
            return owner.make().TypeApply(owner.make().QualIdent(owner.syms().mapType.tsym), List.of(keyexpr, itemexpr));
        }
        return owner.javaBoxType(t);
    }
    
    JCExpression valueFieldType() {
        //owner.classGen().transformClassParameterType(attrTypedDecl);
        if (attrTypedDecl.getContainer() instanceof Class
                && attrTypedDecl.isParameter()
                && Decl.isTransient(attrTypedDecl)) {
                Type paramType = attrTypedDecl.getType();
                return owner.makeJavaType(attrTypedDecl, paramType, 0);
        }
        if ((valueFieldModifiers() & Flags.STATIC) != 0
                && attrType.isTypeParameter()) {
            return owner.make().Type(owner.syms().objectType);
        }
        if (useJavaBox) {
            Type t = owner.simplifyType(attrType);
            return javaBoxType(t);
        } else {
            return owner.makeJavaType(attrType, typeFlags);
        }

    }

    /** The modifiers for the value field */
    protected long valueFieldModifiers() {
        long flags = fieldModifiers | (modifiers & (Flags.STATIC | Flags.FINAL | Flags.TRANSIENT | Flags.VOLATILE));
        // only make it final if we have an init, otherwise we still have to initialise it
        if (!writable && memoizedInitialValue == null && (initialValue != null || valueConstructor)) {
            flags |= Flags.FINAL;
        }
        return flags;
    }
    
    /** Make the declaration of the saved exception field */
    private List<JCStatement> makeExceptionField(List<JCStatement> stmts) {
        List<JCStatement> result = List.<JCStatement>of(owner.make().VarDef(
                owner.make().Modifiers(exceptionFieldMods()),
                owner.names().fromString(Naming.quoteIfJavaKeyword(Naming.getToplevelAttributeSavedExceptionName())),
                owner.makeJavaType(owner.syms().throwableType.tsym),
                null));
        return result.prependList(stmts);
    }

    /** The modifiers for the saved exception field */
    protected int exceptionFieldMods() {
        return Flags.PRIVATE | Flags.STATIC | Flags.FINAL;
    }
    
    /** Make the declaration of the value, initialization check and saved exception fields */
    private List<JCStatement> makeFields() {
        List<JCStatement> stmts = List.<JCStatement>of(makeValueField());
        stmts = initTest.makeInitCheckField(stmts);
        if (deferredInitError) {
            stmts = makeExceptionField(stmts);
        }
        return stmts;
    }
    
    /** Generate the initializer statements where the field gets set to its initial value */
    private List<JCStatement> initValueField() {
        List<JCStatement> stmts = List.<JCStatement>nil();
        JCExpression initValue = this.initialValue;
        if (initValue == null) {
            initValue = this.memoizedInitialValue;
        }
        if (initValue != null) {
            if (useJavaBox && initialValueBoxing != BoxingStrategy.JAVA) {
                SyntheticName temp =  owner.naming.temp();
                JCExpression type = owner.makeJavaType(attrType, initialValueBoxing == BoxingStrategy.BOXED ? AbstractTransformer.JT_NO_PRIMITIVES : 0);
                initValue = 
                        owner.make().LetExpr(
                                owner.makeVar(temp, 
                                        type,
                                        initValue),
                        owner.make().Conditional(
                        owner.make().Binary(JCTree.Tag.EQ, 
                                temp.makeIdent(), 
                                owner.makeNull()),
                        owner.makeNull(),
                        owner.boxJavaType(
                        owner.unboxType(temp.makeIdent(), owner.simplifyType(attrType)),
                        owner.simplifyType(attrType))));
            }
            stmts = stmts.prepend(owner.make().Exec(owner.make().Assign(
                    makeValueFieldAccess(),
                    initValue)));
        }
        return stmts;
    }

    protected JCExpression makeValueFieldAccess() {
        return makeFieldAccess(valueFieldModifiers(), Naming.quoteFieldName(fieldName));
    }
    
    /** surrounda the init expression with a try/catch that saves the exception */
    private List<JCStatement> initWithExceptionHandling(List<JCStatement> stmts) {
        
        String exceptionName = "x"; // doesn't matter

        // $initException$ = null
        stmts = stmts.append(owner.make().Exec(owner.make().Assign(makeExceptionFieldAccess(), 
                owner.makeNull())));
        
        // $initException$ = x
        JCStatement saveException = owner.make().Exec(owner.make().Assign(
                makeExceptionFieldAccess(), 
                owner.makeUnquotedIdent(exceptionName)));
        // value = null
        JCStatement nullValue = owner.make().Exec(owner.make().Assign(
                makeValueFieldAccess(), owner.makeDefaultExprForType(attrType)));
        // the catch statements
        List<JCStatement> handlerStmts = List.<JCStatement>nil();
        JCStatement initFlagFalse = initTest.makeInitInitialized(false);
        if (initFlagFalse != null) {
            handlerStmts = handlerStmts.prepend(initFlagFalse);
        }
        handlerStmts = handlerStmts.prepend(nullValue);
        handlerStmts = handlerStmts.prepend(saveException);
        JCBlock handlerBlock = owner.make().Block(0, handlerStmts);
        
        // the catch block
        JCExpression throwableType = owner.makeJavaType(owner.syms().throwableType.tsym);
        JCVariableDecl exceptionParam = owner.make().VarDef(owner.make().Modifiers(0), 
                owner.naming.makeUnquotedName(exceptionName), 
                throwableType , null);
        JCCatch catchers = owner.make().Catch(exceptionParam, handlerBlock);
        
        // the try/catch
        JCTree.JCTry try_ = owner.make().Try(owner.make().Block(0, stmts), List.<JCTree.JCCatch>of(catchers), null);
        return List.<JCTree.JCStatement>of(try_);
    }
    
    public List<JCStatement> makeInit(boolean isInitialized) {
        List<JCStatement> result = initValueField();
        // $init$value = true;
        result = makeInitialized(isInitialized, result);
        if (deferredInitError) {
            result = initWithExceptionHandling(result);
        }
        return result;
    }

    private List<JCStatement> makeInitialized(boolean isInitialized, List<JCStatement> result) {
        JCStatement makeInitInitialized = initTest.makeInitInitialized(isInitialized);
        if (makeInitInitialized != null) {
            result = result.append(makeInitInitialized);
        }
        return result;
    }
    
    private List<JCTree.JCStatement> makeStandardGetter() {
        JCTree.JCExpression returnExpr = makeValueFieldAccess();
        // make sure we turn hash long to int properly
        if(isHash)
            returnExpr = owner.convertToIntForHashAttribute(returnExpr);
        if (useJavaBox && !owner.isJavaString(attrType)) {
            returnExpr = owner.make().Conditional(
                    owner.make().Binary(JCTree.Tag.EQ, makeValueFieldAccess(), owner.makeNull()), 
                    owner.makeNull(), 
                    owner.boxType(owner.unboxJavaType(returnExpr, attrType),
                            owner.simplifyType(attrType)));
        }
        if (attrTypedDecl.isStatic()) {
            returnExpr = owner.make().TypeCast(owner.makeJavaType(attrType), returnExpr);
        }
        return List.<JCTree.JCStatement>of(owner.make().Return(returnExpr));
    }
    
    public List<JCStatement> getterExceptionThrowing(List<JCStatement> stmts) {
        JCExpression init = initTest.makeInitTest(true);
        if (init != null) {
            List<JCStatement> catchStmts;
            JCExpression msg = owner.make().Literal(attrTypedDecl.isLate() ? "Accessing uninitialized 'late' attribute '"+attrName+"'" : "Cyclic initialization trying to read the value of '"+attrName+"' before it was set");
            JCTree.JCThrow throwStmt = owner.make().Throw(owner.makeNewClass(owner.makeIdent(owner.syms().ceylonInitializationErrorType), 
                    List.<JCExpression>of(msg)));
            if(deferredInitError){
                JCStatement rethrow = owner.make().Exec(owner.utilInvocation().rethrow( 
                        makeExceptionFieldAccess()));
                // rethrow the init exception if we have one
                JCIf ifThrow = owner.make().If(owner.make().Binary(JCTree.Tag.NE, makeExceptionFieldAccess(), 
                        owner.makeNull()), rethrow, null);
                catchStmts = List.<JCTree.JCStatement>of(ifThrow, throwStmt);
            }else if (memoizedInitialValue != null) {
                JCExpression f = makeValueFieldAccess();
                if (isHash) {
                    f = owner.convertToIntForHashAttribute(f);
                }
                catchStmts = List.<JCStatement>of(owner.make().Return(f))
                        .prependList(makeInitialized(true, initValueField()));
                
            }else {
                catchStmts = List.<JCTree.JCStatement>of(throwStmt);
            }
            stmts = List.<JCTree.JCStatement>of(
                    owner.make().If(init, 
                        owner.make().Block(0, stmts),
                        owner.make().Block(0, catchStmts)));
        } 
        return stmts;
    }
    
    private List<JCStatement> makeGetter() {
        List<JCStatement> stmts = makeStandardGetter();
        if ((toplevel || late)) {
            stmts = getterExceptionThrowing(stmts);
        }
        return stmts;
    }
    
    private List<JCStatement> setter() {
        JCExpression fld = makeValueFieldAccess();
        JCExpression setValue = owner.makeQuotedIdent(setterParameterName());
        if (useJavaBox && !owner.isJavaString(attrType)) {
            setValue = owner.make().Conditional(
                    owner.make().Binary(JCTree.Tag.EQ, 
                            owner.makeQuotedIdent(setterParameterName()), 
                            owner.makeNull()),
                    owner.makeNull(),
                    owner.boxJavaType(
                    owner.unboxType(setValue, owner.simplifyType(attrType)), attrType));
        }
        List<JCStatement> stmts = List.<JCStatement>of(owner.make().Exec(
                owner.make().Assign(
                        fld,
                        setValue)));
        return stmts;
    }

    private List<JCStatement> makeMarkInitialized(List<JCStatement> stmts) {
        JCStatement markInitialized = initTest.makeInitInitialized(true);
        if (markInitialized != null) {
            stmts = stmts.append(markInitialized);
        }           
        return stmts;
    }
    
    /** Decorator of other InitCheck which save exceptions during init until the value is accessed */
    private List<JCTree.JCStatement> rethrowInitialError(List<JCTree.JCStatement> stmts) {
        JCStatement rethrow = owner.make().Exec(owner.utilInvocation().rethrow( 
                makeExceptionFieldAccess()));
        // rethrow the init exception if we have one
        JCIf ifThrow = owner.make().If(owner.make().Binary(JCTree.Tag.NE, makeExceptionFieldAccess(), 
                owner.makeNull()), rethrow, null);
        stmts = stmts.prepend(ifThrow);
        return stmts;
    }

    protected JCExpression makeExceptionFieldAccess() {
        return makeFieldAccess(exceptionFieldMods(), Naming.getToplevelAttributeSavedExceptionName());
    }
    
    private List<JCStatement> makeSetter() {
        List<JCStatement> stmts = setter();
        if (late) {
            stmts = makeMarkInitialized(stmts);
        }
        if (!variable) {
            stmts = makeMarkReinitialized(stmts);
        }
        if (deferredInitError) {
            stmts = rethrowInitialError(stmts);
        }
        return stmts;
    }

    protected List<JCStatement> makeMarkReinitialized(List<JCStatement> stmts) {
        JCExpression init = initTest.makeInitTest(false);
        if (init != null) {
            stmts = List.<JCStatement>of(
                    owner.make().If(
                        init,
                        owner.make().Block(0, stmts), 
                        owner.make().Block(0, List.<JCStatement>of(owner.make().Throw(owner.makeNewClass( 
                                owner.make().Type(owner.syms().ceylonInitializationErrorType), 
                                List.<JCExpression>of(owner.make().Literal("Re-initialization of 'late' attribute"))))))));
        }
        return stmts;
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
    
    public AttributeDefinitionBuilder fieldVisibilityModifiers(long modifiers) {
        this.fieldModifiers = modifiers & (Flags.PUBLIC | Flags.PRIVATE | Flags.PROTECTED);
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
    
    public AttributeDefinitionBuilder isJavaNative(boolean isNative) {
        if (isNative) {
            skipField();
            getterBuilder.noBody();
            setterBuilder.noBody();
        }
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
    public AttributeDefinitionBuilder initialValue(JCTree.JCExpression initialValue, BoxingStrategy initialValueBoxing) {
        this.initialValue = initialValue;
        this.initialValueBoxing = initialValueBoxing;
        return this;
    }
    

    public AttributeDefinitionBuilder initialValueError(HasErrorException initialException) {
        this.initialException = initialException;
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
    
    public JCExpression buildUninitTest() {
        return initTest.makeInitTest(false);
    }
    
    public List<JCStatement> buildFields() {
        return makeFields();
    }
    
    public List<JCStatement> buildInit(boolean isInitialized) {
        return makeInit(isInitialized);
    }
}
