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
     * A predicate
     */
    static interface Predicate<T> {
        /** Returns true if the predicate matches/accepts the given thing */
        boolean accept(T candidate);
    }
    
    /**
     * A predicate that {@linkplain Predicate#accept(Declaration) accepts} 
     * nothing, alway returning {@code false}.
     */
    private static final Predicate<?> FALSE = new Predicate<Object>() {
        /** Returns {@code false}, always. */
        @Override
        public boolean accept(Object candidate) {
            return false;
        }
        @Override
        public String toString() {
            return "false";
        }
    };
    
    @SuppressWarnings("unchecked")
    static <T> Predicate<T> false_() {
        return (Predicate<T>)FALSE;
    }
    
    /**
     * A predicate that {@linkplain Predicate#accept(Declaration) accepts} 
     * everything, alway returning {@code true}.
     */
    static final Predicate<?> TRUE = new Predicate<Object>() {
        /** Returns {@code true}, always. */
        @Override
        public boolean accept(Object candidate) {
            return true;
        }
        @Override
        public String toString() {
            return "true";
        }
    };
    
    @SuppressWarnings("unchecked")
    static <T> Predicate<T> true_() {
        return (Predicate<T>)TRUE;
    }
    
    /**
     * The logical conjunction of a number of other predicates.
     */
    private static final class And<T> implements Predicate<T> {
        private final Predicate<T>[] others;
        private And(Predicate<T>[] others) {
            this.others = others;
        }
        /** 
         * Returns {@code false} iff all the other predicates 
         * {@linkplain Predicate#accept(Object) accept()} 
         * the given declaration.
         */
        @Override
        public boolean accept(T candidate) {
            for (Predicate<T> other : others) {
                if (!other.accept(candidate)) {
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
    @SafeVarargs
    public static <T> Predicate<T> and(Predicate<T>... others) {
        if (others.length == 1) {
            return others[0];
        }
        for (Predicate<T> pred : others) {
            if (pred == FALSE) {
                return false_();
            }
        }
        return new And<T>(others);
    }
    
    /**
     * The logical disjunction of a number of other predicates.
     */
    private static final class Or<T> implements Predicate<T> {
        private final Predicate<T>[] others;
        public Or(Predicate<T>[] others) {
            this.others = others;
        }
        /** 
         * Returns {@code false} iff at least one of the other predicates 
         * {@linkplain Predicate#accept(Object) accept()}s 
         * the given declaration.
         */
        @Override
        public boolean accept(T candidate) {
            for (Predicate<T> other : others) {
                if (other.accept(candidate)) {
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
    @SafeVarargs
    public static <T> Predicate<T> or(Predicate<T>... others) {
        if (others.length == 1) {
            return others[0];
        }
        for (Predicate<T> pred : others) {
            if (pred == TRUE) {
                return true_();
            }
        }
        return new Or<T>(others);
    } 
    
    private static final Predicate<Declaration> VALUE = new Predicate<Declaration>() {  
        @Override
        public boolean accept(Declaration declaration) {
            return declaration instanceof com.redhat.ceylon.compiler.typechecker.model.Value;
        }
        @Override
        public String toString() {
            return "(kind = Value)";
        }
    };
    
    private static final Predicate<Declaration> VARIABLE = new Predicate<Declaration>() {  
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
    
    private static final Predicate<Declaration> FUNCTION = new Predicate<Declaration>() {  
        @Override
        public boolean accept(Declaration declaration) {
            return declaration instanceof com.redhat.ceylon.compiler.typechecker.model.Method;
        }
        @Override
        public String toString() {
            return "(kind = Function)";
        }
    };
    
    private static final Predicate<Declaration> CLASS = new Predicate<Declaration>() {  
        @Override
        public boolean accept(Declaration declaration) {
            return declaration instanceof com.redhat.ceylon.compiler.typechecker.model.Class;
        }
        @Override
        public String toString() {
            return "(kind = Class)";
        }
    };

    private static final Predicate<Declaration> INTERFACE = new Predicate<Declaration>() {  
        @Override
        public boolean accept(Declaration declaration) {
            return declaration instanceof com.redhat.ceylon.compiler.typechecker.model.Interface;
        }
        @Override
        public String toString() {
            return "(kind = Interface)";
        }
    };
    
    private static final Predicate<Declaration> CLASS_OR_INTERFACE = new Predicate<Declaration>() {  
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
    public static Predicate<Declaration> fromDeclarationKind(TypeDescriptor kind) {
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
                return true_();
            }
            throw new EnumeratedTypeError("Supposedly exhaustive switch was not exhaustive");
        } else if (kind instanceof TypeDescriptor.Union) {
            TypeDescriptor[] members = ((TypeDescriptor.Union)kind).getMembers();
            @SuppressWarnings("unchecked")
            Predicate<Declaration>[] preds = new Predicate[members.length];
            int ii = 0;
            for (TypeDescriptor member : members) {
                preds[ii++] = fromDeclarationKind(member);
            }
            return or(preds);
        } else if (kind instanceof TypeDescriptor.Intersection) {
            TypeDescriptor[] members = ((TypeDescriptor.Intersection)kind).getMembers();
            @SuppressWarnings("unchecked")
            Predicate<Declaration>[] preds = new Predicate[members.length];
            int ii = 0;
            for (TypeDescriptor member : members) {
                preds[ii++] = fromDeclarationKind(member);
            }
            return and(preds);
        } else if (kind == TypeDescriptor.NothingType) {
            return false_();
        }
        throw new EnumeratedTypeError("Supposedly exhaustive switch was not exhaustive");
    }
    
    /**
     * A Predicate on the declarations having a given annotation 
     */
    private static class HavingAnnotation<Annotation> 
            implements Predicate<Declaration> {

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
            Predicate<Declaration> hasAnnotation(TypeDescriptor annotation) {
        AppliedType at = Metamodel.getAppliedMetamodel(annotation);
        return hasAnnotation(annotation, at);
    }
    
    private static <Annotation> Predicate<Declaration> hasAnnotation(TypeDescriptor annotation, AppliedType at) {
        if (at instanceof nothingType_) {
            return false_();
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
    
    private static Predicate<Declaration>[] hasAnnotationPredicates(TypeDescriptor annotation,
            Sequential<? extends AppliedType> caseTypes) {
        @SuppressWarnings("unchecked")
        Predicate<Declaration>[] preds = new Predicate[(int)caseTypes.getSize()];
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
