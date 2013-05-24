package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.Arrays;

import ceylon.language.Iterator;
import ceylon.language.Sequential;
import ceylon.language.metamodel.AppliedType;
import ceylon.language.metamodel.nothingType_;

import com.redhat.ceylon.compiler.java.language.EnumeratedTypeError;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;

/**
 * Factory methods for constructing predicates on Declarations. 
 */
class DeclarationPredicate {
    
    private DeclarationPredicate() {}

    /**
     * A predicate about a Declaration
     */
    static interface Predicate {
        /** Returns true if the predicate matches/accepts the given model */
        boolean accept(Declaration declaration);
    }
    
    /**
     * A predicate that {@linkplain Predicate#accept(Declaration) accepts} 
     * nothing, alway returning {@code false}.
     */
    static final Predicate FALSE = new Predicate() {
        /** Returns {@code false}, always. */
        @Override
        public boolean accept(Declaration declaration) {
            return false;
        }
        @Override
        public String toString() {
            return "false";
        }
    };
    
    /**
     * A predicate that {@linkplain Predicate#accept(Declaration) accepts} 
     * everything, alway returning {@code true}.
     */
    static final Predicate TRUE = new Predicate() {
        /** Returns {@code true}, always. */
        @Override
        public boolean accept(Declaration declaration) {
            return true;
        }
        @Override
        public String toString() {
            return "true";
        }
    };
    
    /**
     * The logical conjunction of a number of other predicates.
     */
    private static final class And implements Predicate {
        private final Predicate[] others;
        private And(Predicate[] others) {
            this.others = others;
        }
        /** 
         * Returns {@code false} iff all the other predicates 
         * {@linkplain Predicate#accept(Declaration) accept()} 
         * the given declaration.
         */
        @Override
        public boolean accept(Declaration declaration) {
            for (Predicate other : others) {
                if (!other.accept(declaration)) {
                    return false;
                }
            }
            return true;
        }
        @Override
        public String toString() {
            return "(and " + Arrays.toString(others) +")";
        }
    }
    
    /**
     * Returns a predicate that is the logical conjunction of a number of 
     * other predicates.
     */
    public static Predicate and(Predicate... others) {
        if (others.length == 1) {
            return others[0];
        }
        for (Predicate pred : others) {
            if (pred == FALSE) {
                return FALSE;
            }
        }
        return new And(others);
    }
    
    /**
     * The logical disjunction of a number of other predicates.
     */
    private static final class Or implements Predicate {
        private final Predicate[] others;
        public Or(Predicate[] others) {
            this.others = others;
        }
        /** 
         * Returns {@code false} iff at least one of the other predicates 
         * {@linkplain Predicate#accept(Declaration) accept()}s 
         * the given declaration.
         */
        @Override
        public boolean accept(Declaration declaration) {
            for (Predicate other : others) {
                if (other.accept(declaration)) {
                    return true;
                }
            }
            return false;
        }
        @Override
        public String toString() {
            return "(or " + Arrays.toString(others) +")";
        }
    }
    
    /**
     * Returns a predicate that is the logical disjunction of a number of 
     * other predicates.
     */
    public static Predicate or(Predicate... others) {
        if (others.length == 1) {
            return others[0];
        }
        for (Predicate pred : others) {
            if (pred == TRUE) {
                return TRUE;
            }
        }
        return new Or(others);
    } 
    
    private static final Predicate VALUE = new Predicate() {  
        @Override
        public boolean accept(Declaration declaration) {
            return declaration instanceof com.redhat.ceylon.compiler.typechecker.model.Value;
        }
        @Override
        public String toString() {
            return "(kind = Value)";
        }
    };
    
    private static final Predicate VARIABLE = new Predicate() {  
        @Override
        public boolean accept(Declaration declaration) {
            return declaration instanceof com.redhat.ceylon.compiler.typechecker.model.Value
                    && ((com.redhat.ceylon.compiler.typechecker.model.Value)declaration).isVariable();
        }
        @Override
        public String toString() {
            return "(kind = Variable)";
        }
    };
    
    private static final Predicate FUNCTION = new Predicate() {  
        @Override
        public boolean accept(Declaration declaration) {
            return declaration instanceof com.redhat.ceylon.compiler.typechecker.model.Method;
        }
        @Override
        public String toString() {
            return "(kind = Function)";
        }
    };
    
    private static final Predicate CLASS = new Predicate() {  
        @Override
        public boolean accept(Declaration declaration) {
            return declaration instanceof com.redhat.ceylon.compiler.typechecker.model.Class;
        }
        @Override
        public String toString() {
            return "(kind = Class)";
        }
    };

    private static final Predicate INTERFACE = new Predicate() {  
        @Override
        public boolean accept(Declaration declaration) {
            return declaration instanceof com.redhat.ceylon.compiler.typechecker.model.Interface;
        }
        @Override
        public String toString() {
            return "(kind = Interface)";
        }
    };
    
    private static final Predicate CLASS_OR_INTERFACE = new Predicate() {  
        @Override
        public boolean accept(Declaration declaration) {
            return declaration instanceof com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
        }
        @Override
        public String toString() {
            return "(kind = ClassOrInterface)";
        }
    };
    
    /**
     * Returns a predicate for declarations being of the given kind 
     * (Class, Interface, Function, Value etc)
     * @param kind A TypeDescriptor for the sought declaration kind.   
     */
    public static Predicate fromDeclarationKind(TypeDescriptor kind) {
        if (kind instanceof TypeDescriptor.Class) {
            Class<? extends ceylon.language.metamodel.untyped.Declaration> declarationClass = (Class)((TypeDescriptor.Class) kind).getKlass();
            if (declarationClass == ceylon.language.metamodel.untyped.Variable.class) {
                return VARIABLE;
            } else if (declarationClass == ceylon.language.metamodel.untyped.Value.class) {
                return VALUE;
            } else if (declarationClass == ceylon.language.metamodel.untyped.Function.class) {
                return FUNCTION;
            } else if (declarationClass == ceylon.language.metamodel.untyped.Class.class) {
                return CLASS;
            } else if (declarationClass == ceylon.language.metamodel.untyped.Interface.class) {
                return INTERFACE;
            } else if (declarationClass == ceylon.language.metamodel.untyped.ClassOrInterface.class) {
                return CLASS_OR_INTERFACE;
            } else if (declarationClass == ceylon.language.metamodel.untyped.Declaration.class) {
                return TRUE;
            }
            throw new EnumeratedTypeError("Supposedly exhaustive switch was not exhaustive");
        } else if (kind instanceof TypeDescriptor.Union) {
            TypeDescriptor[] members = ((TypeDescriptor.Union)kind).getMembers();
            Predicate[] preds = new Predicate[members.length];
            int ii = 0;
            for (TypeDescriptor member : members) {
                preds[ii++] = fromDeclarationKind(member);
            }
            return or(preds);
        } else if (kind instanceof TypeDescriptor.Intersection) {
            TypeDescriptor[] members = ((TypeDescriptor.Intersection)kind).getMembers();
            Predicate[] preds = new Predicate[members.length];
            int ii = 0;
            for (TypeDescriptor member : members) {
                preds[ii++] = fromDeclarationKind(member);
            }
            return and(preds);
        } else if (kind == TypeDescriptor.NothingType) {
            return FALSE;
        }
        throw new EnumeratedTypeError("Supposedly exhaustive switch was not exhaustive");
    }
    
    /**
     * A Predicate on the declarations having a given annotation 
     */
    private static class HavingAnnotation<Annotation> 
            implements Predicate {

        private final TypeDescriptor annotation;
        private final AppliedClassOrInterfaceType<Annotation> at;

        public HavingAnnotation(TypeDescriptor annotation,
                AppliedClassOrInterfaceType<Annotation> at) {
            this.annotation = annotation;
            this.at = at;
        }

        /** Acceps the declaration if the required annotation is present */
        @Override
        public boolean accept(Declaration memberModel) {
            FreeDeclaration member = Metamodel.getOrCreateMetamodel(memberModel);
            Sequential<Annotation> annotations = Metamodel.annotations(annotation, at, member);
            return !annotations.getEmpty();
        }
        @Override
        public String toString() {
            return "(having-annotation " + at +")";
        }
    }
    
    /**
     * Returns a predicate for the declarations having the given annotation 
     */
    public static <Kind extends ceylon.language.metamodel.untyped.Declaration, Annotation>  
            Predicate hasAnnotation(TypeDescriptor annotation) {
        AppliedType at = Metamodel.getAppliedMetamodel(annotation);
        return hasAnnotation(annotation, at);
    }
    
    private static <Annotation> Predicate hasAnnotation(TypeDescriptor annotation, AppliedType at) {
        if (at instanceof nothingType_) {
            return FALSE;
        } else if (at instanceof AppliedClassOrInterfaceType) {
            return new HavingAnnotation<AnnotationBearing>(annotation, (AppliedClassOrInterfaceType)at);
        } else if (at instanceof AppliedUnionType) {
            Sequential<? extends AppliedType> caseTypes = ((AppliedUnionType)at).getCaseTypes();
            return or(hasAnnotationPredicates(annotation, caseTypes));
        } else if (at instanceof AppliedIntersectionType) {
            Sequential<? extends AppliedType> satisfiedTypes = ((AppliedIntersectionType)at).getSatisfiedTypes();
            return and(hasAnnotationPredicates(annotation, satisfiedTypes));
        } else {
            throw new EnumeratedTypeError("Supposedly exhaustive switch was not exhaustive");
        }
    }
    
    private static Predicate[] hasAnnotationPredicates(TypeDescriptor annotation,
            Sequential<? extends AppliedType> caseTypes) {
        Predicate[] preds = new Predicate[(int)caseTypes.getSize()];
        int ii = 0;
        Iterator<? extends AppliedType> iterator = caseTypes.iterator();
        Object element = iterator.next();
        while (element instanceof AppliedType) {
            preds[ii++] = hasAnnotation(annotation, (AppliedType)element);
            element = iterator.next();
        }
        return preds;
    }
}
