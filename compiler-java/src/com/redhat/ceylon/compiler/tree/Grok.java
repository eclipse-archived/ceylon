package com.redhat.ceylon.compiler.tree;

import java.math.BigInteger;

import com.redhat.ceylon.compiler.tree.CeylonTree.ImportDeclaration;
import com.sun.tools.javac.util.*;

public class Grok extends CeylonTree.Visitor {
    Context current;
    int depth;

    class Context implements Cloneable {
        CeylonTree compilationUnit;
        CeylonTree methodDeclaration;
        CeylonTree block;
        Context prev;
        List<CeylonTree> accum;
        List<CeylonTree.Annotation> annotations;
        List<CeylonTree.ImportDeclaration> imports = List.<ImportDeclaration>nil();
        CeylonTree context;

 /*       int getDepth(Context c) {
            int i = 0;
            while (c != null) {
                c = c.prev;
                i++;
            }
            return i;   
        }
        */
        public void push() {
 /*           System.err.println();
            indent();
            System.err.print("+ " + current.context.getClass());
            depth++; */
            try {
                Context c = (Context) clone();
                c.prev = this;
                current = c;
            } catch (CloneNotSupportedException e) {
                throw new Error(e);
            }
        }

 /*       void indent(){
            for (int i=0; i<getDepth(current);i++)
                System.err.print(' ');
        } */
        
        public void push(CeylonTree context) {
            push();
            current.context = context;
        }
        
        public void pop() {
            current = prev;
/*            depth--;
            System.err.println();
            indent();
            System.err.print("- " + current.context.getClass()); */
        }
    }

    public void visit(CeylonTree.CompilationUnit t) {
        current = new Context();
        current.compilationUnit = t;
        current.context = t;
        inner(t);
    }

    public void visit(CeylonTree.ClassDeclaration decl) {
        current.push(decl);
        inner(decl);
        current.pop();
        current.context.add(decl);
    }
    public void visit(CeylonTree.InterfaceDeclaration decl) {
        current.push(decl);
        inner(decl);
        current.pop();
        current.context.add(decl);
    }

    public void visit(CeylonTree.ImportDeclaration decl) {
        current.push(decl);
        inner(decl);
        current.pop();
        CeylonTree.CompilationUnit toplev = (CeylonTree.CompilationUnit) current.context;
        toplev.importDeclarations = toplev.importDeclarations.append(decl);
    }
    
    public void visit(CeylonTree.TypeDeclaration decl) {
        current.push(decl);
        inner(decl);
        current.pop();
        CeylonTree.Declaration def = decl.decl;
        // FIXME: Is this the right test?
        if (def.annotations == null)
            def.setAnnotations(decl.annotations);
        current.context.add(def);
    }
    
    public void visit(CeylonTree.TypeName name) {
        current.push(name);
        inner(name);
        current.pop();
        if (current.context instanceof CeylonTree.Operator) {
            current.context.append(name);
        }
        else {
            current.context.setName(name);
        }
    }
    
    public void visit(CeylonTree.Identifier id) {
        current.context.setName(id.token.getText());
        inner(id);
    }
    
    public void visit(CeylonTree.Block stmts) {
        current.push(stmts);
        inner(stmts);
        current.pop();
        current.context.append(stmts);
    }
    
    public void visit(CeylonTree.AnnotationList list) {
        inner(list);
    }
    
    public void visit(CeylonTree.UserAnnotation ann) {
        current.push(ann);
        inner(ann);
        current.pop();
        current.context.add(ann);
    }
    
    public void visit(CeylonTree.LanguageAnnotation ann) {
        current.push(ann);
        inner(ann);
        current.pop();
        current.context.add(ann);
     }
    
    public void visit(CeylonTree.AnnotationName name)
    {
        current.push(name);
        inner(name);
        current.pop();  
        current.context.setName(name.name);
    }
    
    public void visit(CeylonTree.MemberDeclaration member) {
        visit((CeylonTree.BaseMemberDeclaration)member);
    }
    public void visit(CeylonTree.AbstractMemberDeclaration member) {
        visit((CeylonTree.BaseMemberDeclaration)member);
    }
 
    public void visit(CeylonTree.BaseMemberDeclaration member)
    {
        // We don't know if this is going to be a method or a field.
        // We resolve this by saying if it has an arg list, it's a method.
        current.push(member);
        inner(member);
        current.pop();
        // member.setAnnotations(current.annotations);
        // current.annotations = null;

        for (CeylonTree.Annotation ann: member.annotations) {
        	if (ann instanceof CeylonTree.LanguageAnnotation) {
        		CeylonTree.LanguageAnnotation la = 
        			(CeylonTree.LanguageAnnotation)ann;
        		if (la.kind instanceof CeylonTree.Optional)
        			member.optional = true;
        	}
        } 
        
        if (member.attributeSetter != null) {
            CeylonTree.AttributeSetter tmp = member.attributeSetter;
            member.attributeSetter = null;
            tmp.decl = member;
            current.context.append(tmp);
            
        } else if (member.params != null) {
            // I'm not at all sure that it's worth having both MethodDeclaration and
            // AbstractMethodDeclaration, since it's trivial to distinguish because
            // one has statements; the other has none.
            CeylonTree.BaseMethodDeclaration decl = 
                member.stmts != null ? new CeylonTree.MethodDeclaration(member)
                                     : new CeylonTree.AbstractMethodDeclaration(member);
                
            current.context.add(decl);  
        } else {
            current.context.add(member);
        }
    }
    
    public void visit(CeylonTree.MemberName member)
    {
        current.push(member);
        inner(member);
        current.pop();
        
        if ((current.context) instanceof CeylonTree.BaseMemberDeclaration) {
            // FIXME: Another kludge
            CeylonTree.BaseMemberDeclaration decl = (CeylonTree.BaseMemberDeclaration)current.context;
            decl.setName(member);
        } else {
            current.context.append(member);
        }
    }
    
    public void visit(CeylonTree.MemberType type)
    {
        current.push(type);
        inner(type);
        current.pop();
        CeylonTree.BaseMemberDeclaration decl = (CeylonTree.BaseMemberDeclaration)current.context;
        decl.type = type.type;
    }
    
    public void visit(CeylonTree.Void v)
    {
        current.context.pushType(v);
    }

    public void visit(CeylonTree.Public v)
    {
        CeylonTree.LanguageAnnotation ann = (CeylonTree.LanguageAnnotation)current.context;
        ann.kind = v;
   }
    
    
    public void visit(CeylonTree.Default v)
    {
        CeylonTree.LanguageAnnotation ann = (CeylonTree.LanguageAnnotation)current.context;
        ann.kind = v;
   }
    
    public void visit(CeylonTree.Package v)
    {
        CeylonTree.LanguageAnnotation ann = (CeylonTree.LanguageAnnotation)current.context;
        ann.kind = v;
   }
    
    public void visit(CeylonTree.Abstract v)
    {
        CeylonTree.LanguageAnnotation ann = (CeylonTree.LanguageAnnotation)current.context;
        ann.kind = v;
   }
    
    public void visit(CeylonTree.Module v)
    {
        CeylonTree.LanguageAnnotation ann = (CeylonTree.LanguageAnnotation)current.context;
        ann.kind = v;
   }
    
    public void visit(CeylonTree.Optional v)
    {
        CeylonTree.LanguageAnnotation ann = (CeylonTree.LanguageAnnotation)current.context;
        ann.kind = v;
   }
    
    public void visit(CeylonTree.Mutable v)
    {
        CeylonTree.LanguageAnnotation ann = (CeylonTree.LanguageAnnotation)current.context;
        ann.kind = v;
   }
    
    public void visit(CeylonTree.Type type) {
        current.push(type);
        inner(type);
        current.pop();
        current.context.pushType(type);
    }
    
    public void visit(CeylonTree.FormalParameterList list) {
        current.push(list);
        inner(list);
        current.pop();
        current.context.setParameterList(list.theList);
    }
    
    public void visit(CeylonTree.FormalParameter p) {
        current.push(p);
        inner(p);
        current.pop();
        CeylonTree.FormalParameterList l = (CeylonTree.FormalParameterList)current.context;
        l.addFormalParameter(p);
    }
    
    public void visit(CeylonTree.ArgumentName name) {
        current.push(name);
        inner(name);
        current.pop();
        current.context.setName(name.name);
    }
    
    public void visit(CeylonTree.ReturnStatement stmt) {
        current.push(stmt);
        inner(stmt);
        current.pop();
        current.context.append(stmt);
    }
    
    public void visit(CeylonTree.ThrowStatement stmt) {
        current.push(stmt);
        inner(stmt);
        current.pop();
        current.context.append(stmt);
    }
    
    public void visit(CeylonTree.Expression expr) {
        current.push(expr);
        inner(expr);
        current.pop();
        current.context.append(expr.thing);
    }
    
    public void visit(CeylonTree.IntConstant expr) {
        inner(expr);
    }
    
    public void visit(CeylonTree.FloatConstant expr) {
        inner(expr);
    }
    
    public void visit(CeylonTree.StringConstant expr) {
        inner(expr);
    }
    
    public void visit(CeylonTree.NaturalLiteral lit) {
        lit.value = new BigInteger(lit.token.getText());
        current.context.append(lit);
    }
    
    public void visit(CeylonTree.FloatLiteral lit) {
        lit.value = Float.valueOf(lit.token.getText());
        current.context.append(lit);
    }
    
    public void visit(CeylonTree.SimpleStringLiteral lit) {
        String s = lit.token.getText();
        // Strip off quote markers from each end of the token
        lit.value = s.substring(1, s.length()-1);
        current.context.append(lit);
    }
    
    public void visit(CeylonTree.InitializerExpression expr) {
        current.push(expr);
        inner(expr);
        current.pop();
        current.context.setInitialValue(expr);
    }
    
    public void visit(CeylonTree.NamedArgument expr) {
        current.push(expr);
        inner(expr);
        current.pop();
    }
    
    public void visit(CeylonTree.ArgumentList expr) {
        current.push(expr);
        inner(expr);
        current.pop();
        
        // FIXME: is this the right way to remove the arg list?
        for (CeylonTree t: expr.args()) {
            current.context.append(t);
        }
    }
    
    public void visit(CeylonTree.Varargs expr) {
        inner(expr);
    }
    
    public void visit(CeylonTree.ExpressionList expr) {
        inner(expr);
    }
    
    public void visit(CeylonTree.ReflectedLiteral expr) {
        current.push(expr);
        inner(expr);
        current.pop();
        current.context.append(expr);
    }
    
    public void visit(CeylonTree.QuoteConstant expr) {
        inner(expr);
    }
    
    public void visit(CeylonTree.CharConstant expr) {
        inner(expr);
    }
    
    public void visit(CeylonTree.CharLiteral lit) {
        inner(lit);
        lit.value = lit.token.getText();
        current.context.append(lit);
        
    }
    
    public void visit(CeylonTree.CallExpression expr) {
        current.push(expr);
        inner(expr);
        current.pop();
        current.context.append(expr);
    }
    
    public void visit(CeylonTree.QuotedLiteral expr) {
        inner(expr);
        expr.value = expr.token.getText();
    }
    
    public void visit(CeylonTree.Operator op) {
        op.operatorKind = op.token.getType();
        current.push(op);
        inner(op);
        current.pop();        
        current.context.append(op);
    }
   
    public void visit(CeylonTree.TypeArgumentList list) {
        current.push(list);
        inner(list);
        current.pop();
        current.context.setTypeArgumentList(list);
    }
      
    public void visit(CeylonTree.Null theNull) {
        current.context.append(theNull);
    }
    
    public void visit(CeylonTree.None theNone) {
        current.context.append(theNone);
    }
    
    public void visit(CeylonTree.PrefixExpression expr) {
        current.push(expr);
        inner(expr);
        current.pop();
        current.context.append(expr);
    }
    
    public void visit(CeylonTree.PostfixExpression expr) {
        current.push(expr);
        inner(expr);
        current.pop();
        current.context.append(expr);
    }
    
    public void visit(CeylonTree.ExistsExpression expr) {
        current.push(expr);
        inner(expr);
        current.pop();
        current.context.append(expr);
    }
    
    public void visit(CeylonTree.NonemptyExpression expr) {
        current.push(expr);
        inner(expr);
        current.pop();
        current.context.append(expr);
    }
    
    public void visit(CeylonTree.IsExpression expr) {
        current.push(expr);
        inner(expr);
        current.pop();
        current.context.append(expr);
    }
 
    public void visit(CeylonTree.EnumList list) {
        current.push(list);
        inner(list);
        current.pop();       
        current.context.append(list);
    }
    
    public void visit(CeylonTree.SubscriptExpression expr) {
        current.push(expr);
        inner(expr);
        current.pop();
        CeylonTree.Expression e = (CeylonTree.Expression)current.context;
        e.addSubscript(expr);
   }
    
    public void visit(CeylonTree.OperatorDot expr) {
        expr.operatorKind = expr.token.getType();
        current.push(expr);
        inner(expr);
        current.pop();
        CeylonTree.Expression e = (CeylonTree.Expression)current.context;
        e.pushDot(expr);
   }
    
     public void visit(CeylonTree.LowerBound tree) {
        current.push(tree);
        inner(tree);
        current.pop();
        CeylonTree.SubscriptExpression expr = (CeylonTree.SubscriptExpression)current.context;
        expr.lowerBound = tree;
    }
    
    public void visit(CeylonTree.UpperBound tree) {
        current.push(tree);
        inner(tree);
        current.pop();
        CeylonTree.SubscriptExpression expr = (CeylonTree.SubscriptExpression)current.context;
        expr.upperBound = tree;
    }
    
    public void visit(CeylonTree.TypeParameterList tree) {
        current.push(tree);
        inner(tree);
        current.pop();
        CeylonTree.Declaration d = (CeylonTree.Declaration) current.context;
        d.setTypeParameterList(tree.params);
    }
  
    public void visit(CeylonTree.TypeParameter tree) {
        current.push(tree);
        inner(tree);
        current.pop();
        current.context.append(tree);
    }
  
    public void visit(CeylonTree.AliasDeclaration tree) {
        current.push(tree);
        inner(tree);
        current.pop();
        current.context.add(tree);
    }
  
    public void visit(CeylonTree.SatisfiesList tree) {
        current.push(tree);
        inner(tree);
        current.pop();
        current.context.addTypeConstraint(tree);
   }
  
    public void visit(CeylonTree.AbstractsList tree) {
        current.push(tree);
        inner(tree);
        current.pop();
        current.context.addTypeConstraint(tree);
   }
  
    public void visit(CeylonTree.TypeConstraintList tree) {
        inner(tree);
    }
 
    public void visit(CeylonTree.TypeConstraint tree) {
        current.push(tree);
        inner(tree);
        current.pop();
        current.context.addTypeConstraint(tree);
    }
 
    public void visit(CeylonTree.TypeVariance tree) {
        current.push(tree);
        inner(tree);
        current.pop();
        current.context.append(tree);
    }
 
    public void visit(CeylonTree.Out tree) {
        current.context.append(tree);
     }
    
    public void visit(CeylonTree.ImportList tree) {
        inner(tree);
      }
   
    public void visit(CeylonTree.ImportPath tree) {
        current.push(tree);
        inner(tree);
        current.pop();
        current.context.append(tree);
    }

    public void visit(CeylonTree.ImportWildcard tree) {
        current.push(tree);
        inner(tree);
        current.pop();
        current.context.append(tree);
    }
    
    public void visit(CeylonTree.IfStatement tree) {
        current.push(tree);
        inner(tree);
        current.pop();
        current.context.append(tree);
    }
    
    public void visit(CeylonTree.Condition tree) {
        current.push(tree);
        inner(tree);
        current.pop();
        current.context.setCondition(tree);
    }
    
    public void visit(CeylonTree.IfTrue tree) {
        current.push(tree);
        inner(tree);
        current.pop();
        current.context.setIfTrue(tree.block);
    }
    
    public void visit(CeylonTree.IfFalse tree) {
        current.push(tree);
        inner(tree);
        current.pop();
        current.context.setIfFalse(tree.block);
    }
    
   public void visit(CeylonTree.AnonymousMethod tree) {
        current.push(tree);
        inner(tree);
        current.pop();
        current.context.append(tree);
    }
    
    public void visit(CeylonTree.WhileStatement tree) {
        current.push(tree);
        inner(tree);
        current.pop();
        current.context.append(tree);
    }
    
    public void visit(CeylonTree.WhileBlock tree) {
        current.push(tree);
        inner(tree);
        current.pop();
        current.context.setIfTrue(tree.block);
    }
    
    public void visit(CeylonTree.AttributeSetter tree) {
        current.push(tree);
        inner(tree);
        current.pop();
        current.context.append(tree);
    }
    
    public void visit(CeylonTree.This tree) {
        current.context.append(tree);
    }
    
    public void visit(CeylonTree.Superclass tree) {
        current.push(tree);
        inner(tree);
        current.pop();
        current.context.setSuperclass(tree.theSuperclass);
    }
    public void visit(CeylonTree.InstanceDeclaration tree) {
        current.push(tree);
        inner(tree);
        current.pop();
        current.context.append(tree);
    }
    /*
    public void visit(CeylonTree.InstanceDeclaration tree) {
        current.push(tree);
        inner(tree);
        current.pop();
        current.context.append(tree);
    }
    */
    
    public void visit(CeylonTree.Subtype tree) {
        inner(tree);
        ((CeylonTree.Type) current.context).setSubtype(tree);
    }
 
    public void visit(CeylonTree.Super tree) {
        inner(tree);
        current.context.append(tree);
    }
    
    public void visit(CeylonTree.GetExpression tree) {
        current.push(tree);
        inner(tree);
        current.pop();
        current.context.append(tree);
    }
    
    public void visit(CeylonTree.SetExpression tree) {
        current.push(tree);
        inner(tree);
        current.pop();
        current.context.append(tree);
    }
      
    void inner(CeylonTree t) {
        for (CeylonTree child : t.children)
            child.accept(this);     
    }   
}
