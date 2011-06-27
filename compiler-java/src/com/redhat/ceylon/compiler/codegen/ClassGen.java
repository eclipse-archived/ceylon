package com.redhat.ceylon.compiler.codegen;

import static com.sun.tools.javac.code.Flags.FINAL;
import static com.sun.tools.javac.code.Flags.INTERFACE;
import static com.sun.tools.javac.code.Flags.PUBLIC;
import static com.sun.tools.javac.code.Flags.PRIVATE;
import static com.sun.tools.javac.code.Flags.STATIC;
import static com.sun.tools.javac.code.Flags.ABSTRACT;
import static com.sun.tools.javac.code.TypeTags.VOID;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.redhat.ceylon.compiler.codegen.Gen2.Singleton;
import com.redhat.ceylon.compiler.codegen.StatementGen.StatementVisitor;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AttributeDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AttributeGetterDefinition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AttributeSetterDefinition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ObjectDefinition;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.compiler.util.Util;
import com.sun.source.tree.Tree.Kind;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCTypeParameter;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;

public class ClassGen extends GenPart {

    public ClassGen(Gen2 gen) {
        super(gen);
    }

    // FIXME: figure out what insertOverloadedClassConstructors does and port it

    public JCClassDecl convert(final Tree.ClassOrInterface cdecl) {
        final ListBuffer<JCVariableDecl> params = new ListBuffer<JCVariableDecl>();
        final ListBuffer<JCTree> defs = new ListBuffer<JCTree>();
        final ListBuffer<JCAnnotation> langAnnotations = new ListBuffer<JCAnnotation>();
        final ListBuffer<JCStatement> stmts = new ListBuffer<JCStatement>();
        final ListBuffer<JCStatement> initStmts = new ListBuffer<JCStatement>();
        final ListBuffer<JCTypeParameter> typeParams = new ListBuffer<JCTypeParameter>();
        final ListBuffer<JCExpression> satisfies = new ListBuffer<JCExpression>();
        final ListBuffer<Tree.AttributeDeclaration> attributeDecls = new ListBuffer<Tree.AttributeDeclaration>();
        final Map<String, Tree.AttributeGetterDefinition> getters = new HashMap<String, Tree.AttributeGetterDefinition>();
        final Map<String, Tree.AttributeSetterDefinition> setters = new HashMap<String, Tree.AttributeSetterDefinition>();

        class ClassVisitor extends StatementVisitor {
            Tree.ExtendedType extendedType;

            ClassVisitor(Tree.ClassOrInterface cdecl, ListBuffer<JCStatement> stmts) {
                gen.statementGen.super(cdecl, stmts);
            }

            public void visit(Tree.Parameter param) {
                JCVariableDecl var = at(cdecl).VarDef(make().Modifiers(0), names().fromString(param.getIdentifier().getText()), gen.convert(param.getType()), null);
                params.append(var);
            	if (param.getDeclarationModel().isCaptured()) {
	                JCVariableDecl localVar = at(cdecl).VarDef(make().Modifiers(FINAL | PRIVATE), names().fromString(param.getIdentifier().getText()), gen.convert(param.getType()), null);
	                defs.append(localVar);
	                initStmts.append(at(param).Exec(at(param).Assign(makeSelect("this", localVar.getName().toString()), at(param).Ident(var.getName()))));
            	}
            }

            public void visit(Tree.Block b) {
                b.visitChildren(this);
            }

            public void visit(Tree.MethodDefinition meth) {
                defs.appendList(convert(cdecl, meth));
            }

            public void visit(Tree.MethodDeclaration meth) {
            	defs.appendList(convert(cdecl, meth));
            }
            
            public void visit(Tree.Annotation ann) {
                // Handled in processAnnotations
            }

            public void visit(Tree.SatisfiedTypes theList) {
                for (Tree.Type t : theList.getTypes()) {
                    satisfies.append(gen.convert(t));
                }
            }

            // FIXME: Here we've simplified CeylonTree.MemberDeclaration to
            // Tree.AttributeDeclaration
            public void visit(Tree.AttributeDeclaration decl) {
            	boolean useField = decl.getDeclarationModel().isCaptured() || isShared(decl);
            	
            	Name attrName = names().fromString(decl.getIdentifier().getText());
            	
            	// Only a non-formal attribute has a corresponding field
            	// and if a class parameter exists with the same name we skip this part as well
            	if (!isFormal(decl) && !existsParam(params, attrName)) {
            		JCExpression initialValue = null;
	                if (decl.getSpecifierOrInitializerExpression() != null) {
	                	initialValue = gen.expressionGen.convertExpression(decl.getSpecifierOrInitializerExpression().getExpression());
	                }
	
	                final ListBuffer<JCAnnotation> langAnnotations = new ListBuffer<JCAnnotation>();
	
	                JCExpression type = gen.convert(decl.getType());
	
	                if (gen.isOptional(decl.getType())) {
	                    type = gen.optionalType(type);
	                }
	                
                	if (useField) {
                		// A captured attribute gets turned into a field
		                int modifiers = convertAttributeFieldDeclFlags(decl);
		                defs.append(at(decl).VarDef(at(decl).Modifiers(modifiers, langAnnotations.toList()), attrName, type, null));
		                if (initialValue != null) {
		                	// The attribute's initializer gets moved to the constructor
		                	// because it might be using locals of the initializer
		                    stmts.append(at(decl).Exec(at(decl).Assign(makeSelect("this", decl.getIdentifier().getText()), initialValue)));
		                }
                	} else {
                		// Otherwise it's local to the constructor
		                int modifiers = convertLocalDeclFlags(decl);
		                stmts.append(at(decl).VarDef(at(decl).Modifiers(modifiers, langAnnotations.toList()), attrName, type, initialValue));
                	}
            	}
            	
            	if (useField) {
                	// Remember attribute to be able to generate
                	// missing getters and setters later on
                	attributeDecls.append(decl);
            	}
            }

            public void visit(final Tree.AttributeGetterDefinition getter) {
                JCTree.JCMethodDecl getterDef = convert(cdecl, getter);
                defs.append(getterDef);
                getters.put(getter.getIdentifier().getText(), getter);
            }

            public void visit(final Tree.AttributeSetterDefinition setter) {
                JCTree.JCMethodDecl setterDef = convert(cdecl, setter);
                defs.append(setterDef);
                setters.put(setter.getIdentifier().getText(), setter);
            }

            public void visit(final Tree.ClassDefinition cdecl) {
                defs.append(convert(cdecl));
            }

            public void visit(final Tree.InterfaceDefinition cdecl) {
                defs.append(convert(cdecl));
            }

            // FIXME: also support Tree.SequencedTypeParameter
            public void visit(Tree.TypeParameterDeclaration param) {
                typeParams.append(convert(param));
            }

            public void visit(Tree.ExtendedType extendedType) {
                this.extendedType = extendedType;
                if (extendedType.getInvocationExpression().getPositionalArgumentList() != null) {
                    List<JCExpression> args = List.<JCExpression> nil();

                    for (Tree.PositionalArgument arg : extendedType.getInvocationExpression().getPositionalArgumentList().getPositionalArguments())
                        args = args.append(gen.expressionGen.convertArg(arg));

                    stmts.append(at(extendedType).Exec(at(extendedType).Apply(List.<JCExpression> nil(), at(extendedType).Ident(names()._super), args)));
                }
            }

            // FIXME: implement
            public void visit(Tree.TypeConstraint l) {
            }
        }

        ClassVisitor visitor = new ClassVisitor(cdecl, stmts);
        cdecl.visitChildren(visitor);

        if (cdecl instanceof Tree.AnyClass) {
        	// Constructor
            JCMethodDecl meth = at(cdecl).MethodDef(make().Modifiers(convertConstructorDeclFlags(cdecl)), names().init, at(cdecl).TypeIdent(VOID), List.<JCTypeParameter> nil(), params.toList(), List.<JCExpression> nil(), at(cdecl).Block(0, initStmts.toList().appendList(stmts.toList())), null);
            defs.append(meth);

            // FIXME:
            // insertOverloadedClassConstructors(defs,
            // (CeylonTree.ClassDeclaration) cdecl);
        }

        JCTree superclass;
        if (cdecl instanceof Tree.AnyInterface) {
            // The VM insists that interfaces have java.lang.Object as their
            // superclass
            superclass = makeIdent(syms().objectType);
        } else {
            if (visitor.extendedType == null)
                superclass = makeIdent(syms().ceylonObjectType);
            else {
                // FIXME: is this typecast normal here?
                superclass = gen.convert((Tree.Type) visitor.extendedType.getType());
            }
        }

        long mods = convertClassDeclFlags(cdecl);
        if (cdecl instanceof Tree.AnyInterface)
            mods |= INTERFACE;

        addGettersAndSetters(defs, attributeDecls);
        
        JCClassDecl classDef = at(cdecl).ClassDef(at(cdecl).Modifiers(mods, langAnnotations.toList()), 
                names().fromString(cdecl.getIdentifier().getText()),
                processTypeConstraints(cdecl.getTypeConstraintList(), typeParams.toList()), 
                superclass, satisfies.toList(), defs.toList());

        return classDef;
    }

	public boolean existsParam(ListBuffer<? extends JCTree> params, Name attrName) {
		for (JCTree decl : params) {
			if (decl instanceof JCVariableDecl) {
				JCVariableDecl var = (JCVariableDecl)decl;
				if (var.name.equals(attrName)) {
					return true;
				}
			}
		}
		return false;
	}

	private JCTree.JCMethodDecl convert(ClassOrInterface classDecl, AttributeSetterDefinition cdecl) {
        JCBlock body = gen.statementGen.convert(classDecl, cdecl.getBlock());
        String name = cdecl.getIdentifier().getText();
        return make().MethodDef(make().Modifiers(0), names().fromString(Util.getSetterName(name)), 
                makeIdent("void"), 
                List.<JCTree.JCTypeParameter>nil(), 
                List.<JCTree.JCVariableDecl>of(make().VarDef(make().Modifiers(0), names().fromString(name), gen.convert(cdecl.getType()), null)), 
                List.<JCTree.JCExpression>nil(), 
                body, null);
    }

    public JCTree.JCMethodDecl convert(ClassOrInterface classDecl, AttributeGetterDefinition cdecl) {
        JCBlock body = gen.statementGen.convert(classDecl, cdecl.getBlock());
        return make().MethodDef(make().Modifiers(0), names().fromString(Util.getGetterName(cdecl.getIdentifier().getText())), 
                gen.convert(cdecl.getType()), 
                List.<JCTree.JCTypeParameter>nil(), 
                List.<JCTree.JCVariableDecl>nil(), 
                List.<JCTree.JCExpression>nil(), 
                body, null);
    }

    private int convertClassDeclFlags(Tree.ClassOrInterface cdecl) {
        int result = 0;

        result |= isShared(cdecl) ? PUBLIC : 0;
        result |= isAbstract(cdecl) ? ABSTRACT : 0;

        return result;
    }

    private int convertMethodDeclFlags(Tree.Declaration cdecl) {
        int result = 0;

        result |= isShared(cdecl) ? PUBLIC : PRIVATE;
        result |= isFormal(cdecl) ? ABSTRACT : 0;
        result |= !(isFormal(cdecl) || isDefault(cdecl)) ? FINAL : 0;

        return result;
    }

    private int convertConstructorDeclFlags(Tree.Declaration cdecl) {
        int result = 0;

        result |= isShared(cdecl) ? PUBLIC : 0;

        return result;
    }

    private int convertAttributeFieldDeclFlags(Tree.AttributeDeclaration cdecl) {
        int result = 0;

        result |= isMutable(cdecl) ? 0 : FINAL;
        result |= PRIVATE;

        return result;
    }

    private int convertLocalDeclFlags(Tree.AttributeDeclaration cdecl) {
        int result = 0;

        result |= isMutable(cdecl) ? 0 : FINAL;

        return result;
    }

    private int convertAttributeGetSetDeclFlags(Tree.AttributeDeclaration cdecl) {
        int result = 0;

        result |= isMutable(cdecl) ? 0 : FINAL;
        result |= isShared(cdecl) ? PUBLIC : PRIVATE;

        return result;
    }

    private int convertObjectDeclFlags(Tree.ObjectDefinition cdecl) {
        int result = 0;

        result |= FINAL;
        result |= isShared(cdecl) ? PUBLIC : 0;

        return result;
    }

    private void addGettersAndSetters(final ListBuffer<JCTree> defs, ListBuffer<Tree.AttributeDeclaration> attributeDecls) {
        class GetterVisitor extends Visitor {
            public void visit(Tree.AttributeDeclaration decl) {
                defs.add(makeGetter(decl));
                if(isMutable(decl))
                    defs.add(makeSetter(decl));
            }
        }
        GetterVisitor v = new GetterVisitor();
        for(Tree.AttributeDeclaration def : attributeDecls){
            def.visit(v);
        }
    }

    private JCTree makeGetter(Tree.AttributeDeclaration decl) {
        // FIXME: add at() calls?
    	Name atrrName = names().fromString(decl.getIdentifier().getText());
        JCBlock body = make().Block(0, List.<JCTree.JCStatement>of(make().Return(make().Select(makeIdent("this"), atrrName))));
        
        int mods = convertAttributeGetSetDeclFlags(decl);
        final ListBuffer<JCAnnotation> langAnnotations = new ListBuffer<JCAnnotation>();
        if (isActual(decl)) {
        	langAnnotations.append(makeOverride());
        }
        
        return make().MethodDef(make().Modifiers(mods, langAnnotations.toList()),
        		names().fromString(Util.getGetterName(atrrName.toString())), 
        		gen.convert(decl.getType()), List.<JCTree.JCTypeParameter>nil(), 
                List.<JCTree.JCVariableDecl>nil(), 
                List.<JCTree.JCExpression>nil(), 
                body, null);
    }

    private JCTree makeSetter(Tree.AttributeDeclaration decl) {
        // FIXME: add at() calls?
    	Name atrrName = names().fromString(decl.getIdentifier().getText());
        JCBlock body = make().Block(0, List.<JCTree.JCStatement>of(
                make().Exec(
                        make().Assign(make().Select(makeIdent("this"), atrrName),
                                makeIdent(atrrName.toString())))));
        
        int mods = convertAttributeGetSetDeclFlags(decl);
        final ListBuffer<JCAnnotation> langAnnotations = new ListBuffer<JCAnnotation>();
        if (isActual(decl)) {
        	langAnnotations.append(makeOverride());
        }
        
        return make().MethodDef(make().Modifiers(mods, langAnnotations.toList()),
        		names().fromString(Util.getSetterName(atrrName.toString())), 
                makeIdent("void"), 
                List.<JCTree.JCTypeParameter>nil(), 
                List.<JCTree.JCVariableDecl>of(make().VarDef(make().Modifiers(0), atrrName, gen.convert(decl.getType()), null)), 
                List.<JCTree.JCExpression>nil(), 
                body, null);
    }

    // Rewrite a list of Ceylon-style type constraints into Java trees.
    // class TypeWithParameter<X, Y>()
    // given X satisfies List
    // given Y satisfies Comparable
    // becomes
    // class TypeWithParameter<X extends List, Y extends Comparable> extends
    // ceylon.Object {
    private List<JCTypeParameter> processTypeConstraints(Tree.TypeConstraintList typeConstraintList, List<JCTypeParameter> typeParams) {
        if (typeConstraintList == null)
            return typeParams;

        LinkedHashMap<String, JCTypeParameter> symtab = new LinkedHashMap<String, JCTypeParameter>();
        for (JCTypeParameter item : typeParams) {
            symtab.put(item.getName().toString(), item);
        }

        for (final Tree.TypeConstraint tc : typeConstraintList.getTypeConstraints()) {
            String name = tc.getIdentifier().getText();
            JCTypeParameter tp = symtab.get(name);
            if (tp == null)
                throw new RuntimeException("Class \"" + name + "\" in satisfies list not found");

            ListBuffer<JCExpression> bounds = new ListBuffer<JCExpression>();
            if (tc.getSatisfiedTypes() != null) {
                for (Tree.Type type : tc.getSatisfiedTypes().getTypes())
                    bounds.add(gen.convert(type));

                if (tp.getBounds() != null) {
                    tp.bounds = tp.getBounds().appendList(bounds.toList());
                } else {
                    JCTypeParameter newTp = at(tc).TypeParameter(names().fromString(name), bounds.toList());
                    symtab.put(name, newTp);
                }
            }

            if (tc.getAbstractedType() != null)
                throw new RuntimeException("\"abstracts\" not supported yet");
        }

        // FIXME: This just converts a map to a List. There ought to be a
        // better way to do it
        ListBuffer<JCTypeParameter> result = new ListBuffer<JCTypeParameter>();
        for (JCTypeParameter p : symtab.values()) {
            result.add(p);
        }
        return result.toList();
    }

    private List<JCTree> convert(Tree.ClassOrInterface cdecl, Tree.MethodDefinition decl) {
        final Singleton<JCBlock> body = new Singleton<JCBlock>();
        
        body.append(gen.statementGen.convert(cdecl, decl.getBlock()));
        
    	return convertMethod(cdecl, decl, body);
    }

    private List<JCTree> convert(Tree.ClassOrInterface cdecl, Tree.MethodDeclaration decl) {
    	return convertMethod(cdecl, decl, null);
    }
    
    private List<JCTree> convertMethod(Tree.ClassOrInterface cdecl, Tree.AnyMethod decl, Singleton<JCBlock> body) {
    	List<JCTree> result = List.<JCTree> nil();
        final ListBuffer<JCVariableDecl> params = new ListBuffer<JCVariableDecl>();
        final ListBuffer<JCAnnotation> langAnnotations = new ListBuffer<JCAnnotation>();
        Singleton<JCExpression> restypebuf = new Singleton<JCExpression>();
        ListBuffer<JCAnnotation> jcAnnotations = new ListBuffer<JCAnnotation>();
        final ListBuffer<JCTypeParameter> typeParams = new ListBuffer<JCTypeParameter>();

        processMethodDeclaration(cdecl, decl, params, restypebuf, typeParams, langAnnotations);
        
        JCExpression restype = restypebuf.thing();

        // FIXME: Handle lots more flags here

        if (isActual(decl)) {
            jcAnnotations.append(makeOverride());
        }

        if (gen.isOptional(decl.getType())) {
            restype = gen.optionalType(restype);
        }

        int mods = convertMethodDeclFlags(decl);
        JCMethodDecl meth = at(decl).MethodDef(make().Modifiers(mods, jcAnnotations.toList()), 
                names().fromString(decl.getIdentifier().getText()), 
                restype, processTypeConstraints(decl.getTypeConstraintList(), typeParams.toList()), 
                params.toList(), List.<JCExpression> nil(), (body != null) ? body.thing() : null, null);
        result = result.append(meth);
        
        return result;
    }
    
    void methodClass(Tree.ClassOrInterface classDecl, Tree.MethodDefinition decl, final ListBuffer<JCTree> defs, boolean topLevel) {
        // Generate a class with the
        // name of the method and a corresponding run() method.

        final ListBuffer<JCVariableDecl> params = new ListBuffer<JCVariableDecl>();
        final Singleton<JCBlock> body = new Singleton<JCBlock>();
        Singleton<JCExpression> restype = new Singleton<JCExpression>();
        final ListBuffer<JCAnnotation> langAnnotations = new ListBuffer<JCAnnotation>();
        final ListBuffer<JCTypeParameter> typeParams = new ListBuffer<JCTypeParameter>();

        processMethodDeclaration(classDecl, decl, params, restype, typeParams, langAnnotations);

        body.append(gen.statementGen.convert(classDecl, decl.getBlock()));

        JCMethodDecl meth = at(decl).MethodDef(make().Modifiers((topLevel ? PUBLIC | STATIC : 0)), names().fromString("run"), restype.thing(), processTypeConstraints(decl.getTypeConstraintList(), typeParams.toList()), params.toList(), List.<JCExpression> nil(), body.thing(), null);

        List<JCTree> innerDefs = List.<JCTree> of(meth);

        // Try and find a class to insert this method into
        JCClassDecl classDef = null;
        for (JCTree def : defs) {
            if (def.getKind() == Kind.CLASS) {
                classDef = (JCClassDecl) def;
                break;
            }
        }

        String name;
        if (topLevel)
            name = decl.getIdentifier().getText();
        else
            name = tempName();

        // No class has been made yet so make one
        if (classDef == null) {
            classDef = at(decl).ClassDef(at(decl).Modifiers((topLevel ? PUBLIC : 0), List.<JCAnnotation> nil()), names().fromString(name), List.<JCTypeParameter> nil(), makeIdent(syms().ceylonObjectType), List.<JCExpression> nil(), List.<JCTree> nil());

            defs.append(classDef);
        }

        classDef.defs = classDef.defs.appendList(innerDefs);
    }

    // FIXME: There must be a better way to do this.
    private void processMethodDeclaration(final Tree.ClassOrInterface classDecl, final Tree.AnyMethod decl, final ListBuffer<JCVariableDecl> params, final Singleton<JCExpression> restype, final ListBuffer<JCTypeParameter> typeParams, final ListBuffer<JCAnnotation> langAnnotations) {

        for (Tree.Parameter param : decl.getParameterLists().get(0).getParameters()) {
            params.append(convert(param));
        }

        if (decl.getTypeParameterList() != null)
            for (Tree.TypeParameterDeclaration t : decl.getTypeParameterList().getTypeParameterDeclarations()) {
                typeParams.append(convert(t));
            }

        restype.append(gen.convert(decl.getType()));
    }

    // this is due to the above commented code
    @SuppressWarnings("unused")
    private JCExpression convert(final Tree.Annotation userAnn, Tree.ClassOrInterface classDecl, String methodName) {
        List<JCExpression> values = List.<JCExpression> nil();
        // FIXME: handle named arguments
        for (Tree.PositionalArgument arg : userAnn.getPositionalArgumentList().getPositionalArguments()) {
            values = values.append(gen.expressionGen.convertExpression(arg.getExpression()));
        }

        JCExpression classLiteral;
        if (classDecl != null) {
            classLiteral = makeSelect(classDecl.getIdentifier().getText(), "class");
        } else {
            classLiteral = makeSelect(methodName, "class");
        }

        // FIXME: can we have something else?
        Tree.BaseMemberExpression primary = (Tree.BaseMemberExpression) userAnn.getPrimary();
        JCExpression result = at(userAnn).Apply(null, makeSelect(primary.getIdentifier().getText(), "run"), values);
        JCIdent addAnnotation = at(userAnn).Ident(names().fromString("addAnnotation"));
        List<JCExpression> args;
        if (methodName != null)
            args = List.<JCExpression> of(classLiteral, gen.expressionGen.ceylonLiteral(methodName), result);
        else
            args = List.<JCExpression> of(classLiteral, result);

        result = at(userAnn).Apply(null, addAnnotation, args);

        return result;
    }

    private boolean isShared(Tree.Declaration decl) {
        return decl.getDeclarationModel().isShared();
    }

    private boolean isAbstract(Tree.ClassOrInterface decl) {
        return decl.getDeclarationModel().isAbstract();
    }

    private boolean isDefault(Tree.Declaration decl) {
        return decl.getDeclarationModel().isDefault();
    }

    private boolean isFormal(Tree.Declaration decl) {
        return decl.getDeclarationModel().isFormal();
    }

    private boolean isActual(Tree.Declaration decl) {
        return decl.getDeclarationModel().isActual();
    }

    private boolean isMutable(Tree.AttributeDeclaration decl) {
        return decl.getDeclarationModel().isVariable();
    }

    public JCAnnotation makeOverride() {
    	return make().Annotation(makeIdent(syms().overrideType), List.<JCExpression> nil());
	}

    private JCTypeParameter convert(Tree.TypeParameterDeclaration param) {
        // FIXME: implement this
        if (param.getTypeVariance() != null)
            throw new RuntimeException("Variance not implemented");
        Tree.Identifier name = param.getIdentifier();
        return at(param).TypeParameter(names().fromString(name.getText()), List.<JCExpression> nil());
    }

    private JCVariableDecl convert(Tree.Parameter param) {
        at(param);
        Name name = names().fromString(param.getIdentifier().getText());
        JCExpression type = gen.variableType(param.getType(), param.getAnnotationList());

        if (gen.isOptional(param.getType())) {
            type = gen.optionalType(type);
        }

        JCVariableDecl v = at(param).VarDef(make().Modifiers(FINAL), name, type, null);

        return v;
    }

    public JCTree convert(AttributeDeclaration decl) {
        // we make a class for it
        String name = decl.getIdentifier().getText();
        String className = "$"+name;
        String getterName = Util.getGetterName(name);
        String fieldName = "value";
        boolean shared = isShared(decl);
        boolean variable = isMutable(decl);
        JCExpression type = gen.convert(decl.getType());
        
        // its value
        JCExpression initialValue = null;
        if (decl.getSpecifierOrInitializerExpression() != null)
            initialValue = gen.expressionGen.convertExpression(decl.getSpecifierOrInitializerExpression().getExpression());

        // make a static var for it
        int varMods = PRIVATE | (variable ? 0 : FINAL) | STATIC; 
        JCVariableDecl varDef = at(decl).VarDef(make().Modifiers(varMods), names().fromString(fieldName), 
                type, initialValue);
        
        // now make a getter in any case
        int methodMods = (shared ? PUBLIC : 0) | STATIC;
        JCBlock getterBody = make().Block(0, List.<JCTree.JCStatement>of(make().Return(makeIdent(fieldName))));
        JCMethodDecl getter = make().MethodDef(make().Modifiers(methodMods), names().fromString(getterName), 
                type, List.<JCTree.JCTypeParameter>nil(), List.<JCTree.JCVariableDecl>nil(), 
                List.<JCTree.JCExpression>nil(), getterBody, null);

        JCMethodDecl setter = null;
        if(variable){
            String setterName = Util.getSetterName(name);
            JCBlock setterBody = make().Block(0, 
                    List.<JCTree.JCStatement>of(
                            make().Exec(
                                    make().Assign(makeIdent(className, fieldName), 
                                            makeIdent(fieldName)))));
            setter = make().MethodDef(make().Modifiers(methodMods), 
                    names().fromString(setterName), 
                    makeIdent("void"), 
                    List.<JCTree.JCTypeParameter>nil(), 
                    List.<JCTree.JCVariableDecl>of(make().VarDef(make().Modifiers(0), 
                            names().fromString(fieldName), type, null)), 
                    List.<JCTree.JCExpression>nil(), 
                    setterBody, null);
        }
        
        // and the class
        List<JCTree> defs = (setter != null) ?
                List.<JCTree>of(varDef, getter, setter)
                : List.<JCTree>of(varDef, getter);
        int classMods = (shared ? PUBLIC : 0) | FINAL;
        return make().ClassDef(make().Modifiers(classMods), names().fromString(className), 
                List.<JCTree.JCTypeParameter>nil(), null, List.<JCTree.JCExpression>nil(), defs);
    }

	public JCTree convert(ObjectDefinition decl) {
        // we make a class for it
        String name = decl.getIdentifier().getText();
        
        int classMods = convertObjectDeclFlags(decl);
        return make().ClassDef(make().Modifiers(classMods), names().fromString(name), 
                List.<JCTree.JCTypeParameter>nil(), null, List.<JCTree.JCExpression>nil(), null);
	}
}
