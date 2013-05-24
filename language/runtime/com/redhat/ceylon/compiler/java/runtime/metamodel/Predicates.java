package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.Arrays;

import ceylon.language.Iterator;
import ceylon.language.Sequential;
import ceylon.language.metamodel.Annotated;
import ceylon.language.metamodel.AppliedType;
import ceylon.language.metamodel.ClassOrInterface;
import ceylon.language.metamodel.ConstrainedAnnotation;
import ceylon.language.metamodel.nothingType_;

import com.redhat.ceylon.compiler.java.language.EnumeratedTypeError;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;

/**
 * Factory methods for constructing predicates on things. 
 */
class Predicates {
    
    private Predicates() {}

    /**
     * A predicate
     */
    static interface Predicate<T> {
        /** Returns true if the predicate matches/accepts the given thing */
        boolean accept(T candidate);
    }
    
    /**
     * A predicate that {@linkplain Predicate#accept(Object) accepts} 
     * nothing, always returning {@code false}.
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
     * A predicate that {@linkplain Predicate#accept(Object) accepts} 
     * everything, always returning {@code true}.
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
    
    /** Predicate on Declarations that accepts Value */
    private static final Predicate<Declaration> DECLARATION_IS_VALUE = new Predicate<Declaration>() {  
        @Override
        public boolean accept(Declaration declaration) {
            return declaration instanceof com.redhat.ceylon.compiler.typechecker.model.Value;
        }
        @Override
        public String toString() {
            return "(kind = Value)";
        }
    };
    
    /** Predicate on Declarations that accepts variable Value */
    private static final Predicate<Declaration> DECLARATION_IS_VARIABLE = new Predicate<Declaration>() {  
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
    
    /** Predicate on Declarations that accepts Method */
    private static final Predicate<Declaration> DECLARATION_IS_FUNCTION = new Predicate<Declaration>() {  
        @Override
        public boolean accept(Declaration declaration) {
            return declaration instanceof com.redhat.ceylon.compiler.typechecker.model.Method;
        }
        @Override
        public String toString() {
            return "(kind = Function)";
        }
    };
    
    /** Predicate on Declarations that accepts Class */
    private static final Predicate<Declaration> DECLARATION_IS_CLASS = new Predicate<Declaration>() {  
        @Override
        public boolean accept(Declaration declaration) {
            return declaration instanceof com.redhat.ceylon.compiler.typechecker.model.Class;
        }
        @Override
        public String toString() {
            return "(kind = Class)";
        }
    };

    /** Predicate on Declarations that accepts Interface */
    private static final Predicate<Declaration> DECLARATION_IS_INTERFACE = new Predicate<Declaration>() {  
        @Override
        public boolean accept(Declaration declaration) {
            return declaration instanceof com.redhat.ceylon.compiler.typechecker.model.Interface;
        }
        @Override
        public String toString() {
            return "(kind = Interface)";
        }
    };
    
    /** Predicate on Declarations that accepts ClassOrInterface */
    private static final Predicate<Declaration> DECLARATION_IS_CLASS_OR_INTERFACE = new Predicate<Declaration>() {  
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
     * Returns a Predicate for Declarations being of the given kind 
     * (Class, Interface, Function, Value etc)
     * @param kind A TypeDescriptor for the sought declaration kind.   
     */
    public static Predicate<Declaration> isDeclarationOfKind(TypeDescriptor kind) {
        if (kind instanceof TypeDescriptor.Class) {
            Class<? extends ceylon.language.metamodel.untyped.Declaration> declarationClass = (Class)((TypeDescriptor.Class) kind).getKlass();
            if (declarationClass == ceylon.language.metamodel.untyped.Variable.class) {
                return DECLARATION_IS_VARIABLE;
            } else if (declarationClass == ceylon.language.metamodel.untyped.Value.class) {
                return DECLARATION_IS_VALUE;
            } else if (declarationClass == ceylon.language.metamodel.untyped.Function.class) {
                return DECLARATION_IS_FUNCTION;
            } else if (declarationClass == ceylon.language.metamodel.untyped.Class.class) {
                return DECLARATION_IS_CLASS;
            } else if (declarationClass == ceylon.language.metamodel.untyped.Interface.class) {
                return DECLARATION_IS_INTERFACE;
            } else if (declarationClass == ceylon.language.metamodel.untyped.ClassOrInterface.class) {
                return DECLARATION_IS_CLASS_OR_INTERFACE;
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
                preds[ii++] = isDeclarationOfKind(member);
            }
            return or(preds);
        } else if (kind instanceof TypeDescriptor.Intersection) {
            TypeDescriptor[] members = ((TypeDescriptor.Intersection)kind).getMembers();
            @SuppressWarnings("unchecked")
            Predicate<Declaration>[] preds = new Predicate[members.length];
            int ii = 0;
            for (TypeDescriptor member : members) {
                preds[ii++] = isDeclarationOfKind(member);
            }
            return and(preds);
        } else if (kind == TypeDescriptor.NothingType) {
            return false_();
        }
        throw new EnumeratedTypeError("Supposedly exhaustive switch was not exhaustive");
    }
    
    /**
     * A Predicate on the Declarations having a given annotation 
     */
    private static class AnnotatedWith<A extends ceylon.language.metamodel.Annotation<A>> 
            implements Predicate<Declaration> {

        private final TypeDescriptor annotation;
        private final AppliedClassOrInterfaceType<A> at;

        public AnnotatedWith(TypeDescriptor annotation,
                AppliedClassOrInterfaceType<A> at) {
            this.annotation = annotation;
            this.at = at;
        }

        /** Acceps the Declaration if the required annotation is present */
        @Override
        public boolean accept(Declaration memberModel) {
            FreeDeclaration member = Metamodel.getOrCreateMetamodel(memberModel);
            Sequential<? extends A> annotations = Metamodel.<A>annotations(annotation, member);
            return !annotations.getEmpty();
        }
        @Override
        public String toString() {
            return "(having-annotation " + at +")";
        }
    }
    
    /**
     * Returns a predicate for Declarations having the given annotation 
     */
    public static <Kind extends ceylon.language.metamodel.untyped.Declaration, A extends ceylon.language.metamodel.Annotation<A>>  
            Predicate<Declaration> isDeclarationAnnotatedWith(TypeDescriptor annotation) {
        AppliedType at = Metamodel.getAppliedMetamodel(annotation);
        return Predicates.<A>isDeclarationAnnotatedWith(annotation, at);
    }
    
    private static <A extends ceylon.language.metamodel.Annotation<A>> Predicate<Declaration> 
    isDeclarationAnnotatedWith(TypeDescriptor annotation, AppliedType at) {
        if (at instanceof nothingType_) {
            return false_();
        } else if (at instanceof AppliedClassOrInterfaceType) {
            return new AnnotatedWith<A>(annotation, (AppliedClassOrInterfaceType)at);
        } else if (at instanceof AppliedUnionType) {
            Sequential<? extends AppliedType> caseTypes = ((AppliedUnionType)at).getCaseTypes();
            return or(mapTypesToDeclarationAnnotatedWith(annotation, caseTypes));
        } else if (at instanceof AppliedIntersectionType) {
            Sequential<? extends AppliedType> satisfiedTypes = ((AppliedIntersectionType)at).getSatisfiedTypes();
            return and(mapTypesToDeclarationAnnotatedWith(annotation, satisfiedTypes));
        } else {
            throw new EnumeratedTypeError("Supposedly exhaustive switch was not exhaustive");
        }
    }
    
    private static Predicate<Declaration>[] mapTypesToDeclarationAnnotatedWith(TypeDescriptor annotation,
            Sequential<? extends AppliedType> caseTypes) {
        @SuppressWarnings("unchecked")
        Predicate<Declaration>[] preds = new Predicate[(int)caseTypes.getSize()];
        int ii = 0;
        Iterator<? extends AppliedType> iterator = caseTypes.iterator();
        Object element = iterator.next();
        while (element instanceof AppliedType) {
            preds[ii++] = isDeclarationAnnotatedWith(annotation, (AppliedType)element);
            element = iterator.next();
        }
        return preds;
    }
    
    /**
     * Returns a Predicate on Annotations that are of the given type
     * @param $reifiedAnnotation
     * @return
     */
    public static <A extends ceylon.language.metamodel.Annotation<A>> Predicate<A> isAnnotationOfType(TypeDescriptor $reifiedAnnotation) {
        AppliedType at = Metamodel.getAppliedMetamodel($reifiedAnnotation);
        return isAnnotationOfType($reifiedAnnotation, at);
    }

    private static <A extends ceylon.language.metamodel.Annotation<A>> Predicate<A> isAnnotationOfType(
            TypeDescriptor $reifiedAnnotation, AppliedType at)
            throws EnumeratedTypeError {
        if (at instanceof nothingType_) {
            return false_();
        } else if (at instanceof AppliedUnionType) {
            Sequential<? extends AppliedType> caseTypes = ((AppliedUnionType)at).getCaseTypes();
            return or(Predicates.<A>mapTypesToIsAnnotationOfType($reifiedAnnotation, caseTypes));
        } else if (at instanceof AppliedIntersectionType) {
            Sequential<? extends AppliedType> satisfiedTypes = ((AppliedIntersectionType)at).getSatisfiedTypes();
            return and(Predicates.<A>mapTypesToIsAnnotationOfType($reifiedAnnotation, satisfiedTypes));
        } else if (at instanceof AppliedClassOrInterfaceType) {
            // TODO What if reified is Annotation, or ContrainedAnnotation, or OptionalAnnotation, or SequencedAnnotation?
            AnnotationPredicate<A> predicate = annotationPredicate($reifiedAnnotation, (AppliedClassOrInterfaceType)at);
            return predicate;
        } else {
            throw new EnumeratedTypeError("Supposedly exhaustive switch was not exhaustive");            
        }
    }
    
    private static <A extends ceylon.language.metamodel.Annotation<A>> Predicate<A>[] mapTypesToIsAnnotationOfType(TypeDescriptor $reifiedAnnotation, 
            Sequential<? extends AppliedType> caseTypes) {
        @SuppressWarnings("unchecked")
        Predicate<A>[] preds = new Predicate[(int)caseTypes.getSize()];
        int ii = 0;
        Iterator<? extends AppliedType> iterator = caseTypes.iterator();
        Object element = iterator.next();
        while (element instanceof AppliedType) {
            preds[ii++] = isAnnotationOfType($reifiedAnnotation, (AppliedType)element);
            element = iterator.next();
        }
        return preds;
    }

    static interface AnnotationPredicate<A extends ceylon.language.metamodel.Annotation<A>> extends Predicates.Predicate<A> {
        // TODO Is this a worthwhile optimization to make?
        /** 
         * Whether we should instantiate the given Java annotation into a 
         * Ceylon annotation. If this returns true 
         * {@link #accept(ceylon.language.metamodel.Annotation)} 
         * will still be called to determine whether the Ceylon annotation 
         * meets the acceptance criteria.
         */
        public boolean shouldInstantiate(Class<? extends java.lang.annotation.Annotation> jAnnotationType);
        
        /** 
         * Whether the given Ceylon annotation should be 
         * added to the results 
         */
        public boolean accept(A cAnnotation);
    }
    
    private static <A extends ceylon.language.metamodel.Annotation<A>,
    Value extends ConstrainedAnnotation<? extends Value, ? extends Values, ? super ProgramElement>, Values, ProgramElement extends Annotated>
    AnnotationPredicate<A> annotationPredicate(
            final TypeDescriptor $reifiedValues,
            ClassOrInterface<? extends ConstrainedAnnotation<? extends Value, Values, ? super ProgramElement>> annotationType) {
        final Class<?> refAnnotationClass = Metamodel.getReflectedAnnotationClass(annotationType);
        final Class<?> refAnnotationType;
        final Class<?> refAnnotationWrapperType;
        if (ceylon.language.Annotation.class == refAnnotationClass
                || ceylon.language.metamodel.ConstrainedAnnotation.class == refAnnotationClass
                || ceylon.language.metamodel.OptionalAnnotation.class == refAnnotationClass
                || ceylon.language.metamodel.SequencedAnnotation.class == refAnnotationClass) {
            return new AnnotationPredicate<A>() {

                @Override
                public boolean shouldInstantiate(
                        Class<? extends java.lang.annotation.Annotation> jAnnotationType) {
                    return true;
                }

                @Override
                public boolean accept(A cAnnotation) {
                    return Metamodel.isReified(cAnnotation, $reifiedValues);    
                }
            };
        } else {
            try {
                refAnnotationType = Class.forName(refAnnotationClass.getName()+"$annotation", 
                        false, refAnnotationClass.getClassLoader());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            Class<?> c;
            try {
                c = Class.forName(refAnnotationClass.getName()+"$annotations", 
                        false, refAnnotationClass.getClassLoader());
            } catch (ClassNotFoundException e) {
                c = null;
            }
            refAnnotationWrapperType = c;
        }
        return new AnnotationPredicate<A>() {

            @Override
            public boolean shouldInstantiate(
                    Class<? extends java.lang.annotation.Annotation> jAnnotationType) {
                return refAnnotationType == null || refAnnotationType.isAssignableFrom(jAnnotationType)
                        || (refAnnotationWrapperType != null 
                            && refAnnotationWrapperType.isAssignableFrom(jAnnotationType));
            }

            @Override
            public boolean accept(A cAnnotation) {
                return refAnnotationClass.isInstance(cAnnotation);
            }
        };
    }
}
