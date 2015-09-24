package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.Arrays;

import ceylon.language.Annotated;
import ceylon.language.Iterator;
import ceylon.language.Sequential;
import ceylon.language.meta.declaration.AnnotatedDeclaration;
import ceylon.language.meta.model.ClassOrInterface;
import ceylon.language.meta.model.Type;
import ceylon.language.meta.model.nothingType_;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.language.EnumeratedTypeError;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.model.typechecker.model.Constructor;
import com.redhat.ceylon.model.typechecker.model.Declaration;

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
     * A predicate to look things up by name
     */
    private static final class NamePredicate implements Predicate<Declaration> {

        private String name;

        NamePredicate(String name){
            this.name = name;
        }
        
        @Override
        public boolean accept(Declaration candidate) {
            return candidate.getName().equals(name);
        }
        
    }

    /**
     * Returns a new name predicate
     */
    static NamePredicate isDeclarationNamed(String name){
        return new NamePredicate(name);
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
    static <T> Predicate<T> and(Predicate<T>... others) {
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
    static <T> Predicate<T> or(Predicate<T>... others) {
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
    private static final Predicate<Declaration> DECLARATION_IS_REFERENCE = new Predicate<Declaration>() {  
        @Override
        public boolean accept(Declaration declaration) {
            return declaration instanceof com.redhat.ceylon.model.typechecker.model.Value
                    && !((com.redhat.ceylon.model.typechecker.model.Value)declaration).isTransient();
        }
        @Override
        public String toString() {
            return "(kind = Value)";
        }
    };
    
    /** Predicate on Declarations that accepts Value */
    private static final Predicate<Declaration> DECLARATION_IS_VALUE = new Predicate<Declaration>() {  
        @Override
        public boolean accept(Declaration declaration) {
            return declaration instanceof com.redhat.ceylon.model.typechecker.model.Value
                    && !(((com.redhat.ceylon.model.typechecker.model.Value)declaration).getTypeDeclaration() instanceof Constructor);
        }
        @Override
        public String toString() {
            return "(kind = Value)";
        }
    };
    
    /** Predicate on Declarations that accepts Function */
    private static final Predicate<Declaration> DECLARATION_IS_FUNCTION = new Predicate<Declaration>() {  
        @Override
        public boolean accept(Declaration declaration) {
            return declaration instanceof com.redhat.ceylon.model.typechecker.model.Function
                    && !(((com.redhat.ceylon.model.typechecker.model.Function)declaration).getTypeDeclaration() instanceof Constructor);
        }
        @Override
        public String toString() {
            return "(kind = Function)";
        }
    };

    /** Predicate on Declarations that accepts Function and Value */
    private static final Predicate<Declaration> DECLARATION_IS_FUNCTION_OR_VALUE = new Predicate<Declaration>() {
        @Override
        public boolean accept(Declaration declaration) {
            return (declaration instanceof com.redhat.ceylon.model.typechecker.model.Value
                || declaration instanceof com.redhat.ceylon.model.typechecker.model.Function)
                && !(((com.redhat.ceylon.model.typechecker.model.FunctionOrValue)declaration).getTypeDeclaration() instanceof Constructor)
            ;
        }
        @Override
        public String toString() {
            return "(kind = Value || kind = Function)";
        }
    };
    
    /** Predicate on Declarations that accepts Class */
    private static final Predicate<Declaration> DECLARATION_IS_CLASS = new Predicate<Declaration>() {  
        @Override
        public boolean accept(Declaration declaration) {
            return declaration instanceof com.redhat.ceylon.model.typechecker.model.Class;
        }
        @Override
        public String toString() {
            return "(kind = Class)";
        }
    };
    
    private static final Predicate<Declaration> DECLARATION_IS_CLASS_WITH_INIT = new Predicate<Declaration>() {  
        @Override
        public boolean accept(Declaration declaration) {
            return declaration instanceof com.redhat.ceylon.model.typechecker.model.Class
                    && !((com.redhat.ceylon.model.typechecker.model.Class)declaration).hasConstructors();
        }
        @Override
        public String toString() {
            return "(kind = ClassWithInitializer)";
        }
    };
    
    private static final Predicate<Declaration> DECLARATION_IS_CLASS_WITH_CTORS = new Predicate<Declaration>() {  
        @Override
        public boolean accept(Declaration declaration) {
            return declaration instanceof com.redhat.ceylon.model.typechecker.model.Class
                    && ((com.redhat.ceylon.model.typechecker.model.Class)declaration).hasConstructors();
        }
        @Override
        public String toString() {
            return "(kind = ClassWithConstructors)";
        }
    };
    
    /** Predicate on Declarations that accepts Constructor */
    private static final Predicate<Declaration> DECLARATION_IS_CONSTRUCTOR = new Predicate<Declaration>() {  
        @Override
        public boolean accept(Declaration declaration) {
            return declaration instanceof com.redhat.ceylon.model.typechecker.model.FunctionOrValue
                    && ((com.redhat.ceylon.model.typechecker.model.FunctionOrValue)declaration).getTypeDeclaration() instanceof Constructor;
        }
        @Override
        public String toString() {
            return "(kind = Constructor)";
        }
    };
    
    private static final Predicate<Declaration> DECLARATION_IS_CALLABLE_CONSTRUCTOR = new Predicate<Declaration>() {  
        @Override
        public boolean accept(Declaration declaration) {
            return declaration instanceof com.redhat.ceylon.model.typechecker.model.Function
                    && ((com.redhat.ceylon.model.typechecker.model.Function)declaration).getTypeDeclaration() instanceof Constructor;
        }
        @Override
        public String toString() {
            return "(kind = CallableConstructor)";
        }
    };
    
    private static final Predicate<Declaration> DECLARATION_IS_VALUE_CONSTRUCTOR = new Predicate<Declaration>() {  
        @Override
        public boolean accept(Declaration declaration) {
            return declaration instanceof com.redhat.ceylon.model.typechecker.model.Value
                    && ((com.redhat.ceylon.model.typechecker.model.Value)declaration).getTypeDeclaration() instanceof Constructor;
        }
        @Override
        public String toString() {
            return "(kind = ValueConstructor)";
        }
    };

    /** Predicate on Declarations that accepts Interface */
    private static final Predicate<Declaration> DECLARATION_IS_INTERFACE = new Predicate<Declaration>() {  
        @Override
        public boolean accept(Declaration declaration) {
            return declaration instanceof com.redhat.ceylon.model.typechecker.model.Interface;
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
            return declaration instanceof com.redhat.ceylon.model.typechecker.model.ClassOrInterface;
        }
        @Override
        public String toString() {
            return "(kind = ClassOrInterface)";
        }
    };
    
    /** Predicate on Declarations that accepts Alias */
    private static final Predicate<Declaration> DECLARATION_IS_ALIAS = new Predicate<Declaration>() {  
        @Override
        public boolean accept(Declaration declaration) {
            return declaration instanceof com.redhat.ceylon.model.typechecker.model.TypeAlias;
        }
        @Override
        public String toString() {
            return "(kind = Alias)";
        }
    };

    /**
     * Returns a Predicate for Declarations being of the given kind 
     * (Class, Interface, Function, Value etc)
     * @param kind A TypeDescriptor for the sought declaration kind.   
     */
    static Predicate<Declaration> isDeclarationOfKind(TypeDescriptor kind) {
        if (kind instanceof TypeDescriptor.Class) {
            Class<?> declarationClass = (Class<?>)((TypeDescriptor.Class) kind).getKlass();
            if (declarationClass == ceylon.language.meta.declaration.ReferenceDeclaration.class) {
                return DECLARATION_IS_REFERENCE;
            } else if (declarationClass == ceylon.language.meta.declaration.ValueDeclaration.class) {
                return DECLARATION_IS_VALUE;
            } else if (declarationClass == ceylon.language.meta.declaration.FunctionDeclaration.class) {
                return DECLARATION_IS_FUNCTION;
            } else if (declarationClass == ceylon.language.meta.declaration.FunctionOrValueDeclaration.class) {
                return DECLARATION_IS_FUNCTION_OR_VALUE;
            } else if (declarationClass == ceylon.language.meta.declaration.ClassDeclaration.class) {
                return DECLARATION_IS_CLASS;
            } else if (declarationClass == ceylon.language.meta.declaration.ClassWithInitializerDeclaration.class) {
                return DECLARATION_IS_CLASS_WITH_INIT;
            } else if (declarationClass == ceylon.language.meta.declaration.ClassWithConstructorsDeclaration.class) {
                return DECLARATION_IS_CLASS_WITH_CTORS;
            } else if (declarationClass == ceylon.language.meta.declaration.InterfaceDeclaration.class) {
                return DECLARATION_IS_INTERFACE;
            } else if (declarationClass == ceylon.language.meta.declaration.ClassOrInterfaceDeclaration.class) {
                return DECLARATION_IS_CLASS_OR_INTERFACE;
            } else if (declarationClass == ceylon.language.meta.declaration.AliasDeclaration.class) {
                return DECLARATION_IS_ALIAS;
            } else if (declarationClass == ceylon.language.meta.declaration.ConstructorDeclaration.class) {
                return DECLARATION_IS_CONSTRUCTOR;
            } else if (declarationClass == ceylon.language.meta.declaration.CallableConstructorDeclaration.class) {
                return DECLARATION_IS_CALLABLE_CONSTRUCTOR;
            } else if (declarationClass == ceylon.language.meta.declaration.ValueConstructorDeclaration.class) {
                return DECLARATION_IS_VALUE_CONSTRUCTOR;
            } else if (declarationClass == ceylon.language.meta.declaration.NestableDeclaration.class) {
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
    private static class AnnotatedWith<A extends java.lang.annotation.Annotation> 
            implements Predicate<Declaration> {

        private final TypeDescriptor annotation;
        private final ClassOrInterface<A> at;

        public AnnotatedWith(TypeDescriptor annotation,
                             ClassOrInterface<A> at) {
            this.annotation = annotation;
            this.at = at;
        }

        /** Acceps the Declaration if the required annotation is present */
        @Override
        public boolean accept(Declaration memberModel) {
            AnnotatedDeclaration member = Metamodel.getOrCreateMetamodel(memberModel);
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
    static <Kind extends ceylon.language.meta.declaration.Declaration, A extends java.lang.annotation.Annotation>  
            Predicate<Declaration> isDeclarationAnnotatedWith(TypeDescriptor annotation) {
        Type<?> at = Metamodel.getAppliedMetamodel(annotation);
        return Predicates.<A>isDeclarationAnnotatedWith(annotation, at);
    }
    
    @SuppressWarnings("unchecked")
    private static <A extends java.lang.annotation.Annotation> Predicate<Declaration> 
    isDeclarationAnnotatedWith(TypeDescriptor annotation, Type<?> at) {
        if (at instanceof nothingType_) {
            return false_();
        } else if (at instanceof ClassOrInterface) {
            return new AnnotatedWith<A>(annotation, (ClassOrInterface<A>)at);
        } else if (at instanceof AppliedUnionType) {
            Sequential<? extends Type<?>> caseTypes = ((AppliedUnionType<?>)at).getCaseTypes();
            return or(mapTypesToDeclarationAnnotatedWith(annotation, caseTypes));
        } else if (at instanceof AppliedIntersectionType) {
            Sequential<? extends Type<?>> satisfiedTypes = ((AppliedIntersectionType<?>)at).getSatisfiedTypes();
            return and(mapTypesToDeclarationAnnotatedWith(annotation, satisfiedTypes));
        } else {
            throw new EnumeratedTypeError("Supposedly exhaustive switch was not exhaustive: "+at);
        }
    }
    
    private static Predicate<Declaration>[] mapTypesToDeclarationAnnotatedWith(TypeDescriptor annotation,
            Sequential<? extends Type<?>> caseTypes) {
        @SuppressWarnings("unchecked")
        Predicate<Declaration>[] preds = new Predicate[Util.toInt(caseTypes.getSize())];
        int ii = 0;
        Iterator<? extends Type<?>> iterator = caseTypes.iterator();
        Object element = iterator.next();
        while (element instanceof Type) {
            preds[ii++] = isDeclarationAnnotatedWith(annotation, (Type<?>)element);
            element = iterator.next();
        }
        return preds;
    }
    
    /**
     * Returns a Predicate on Annotations that are of the given type
     * @param $reifiedAnnotation
     * @return
     */
    static <A extends java.lang.annotation.Annotation> Predicate<A> isAnnotationOfType(TypeDescriptor $reifiedAnnotation) {
        Type<?> at = Metamodel.getAppliedMetamodel($reifiedAnnotation);
        return isAnnotationOfType($reifiedAnnotation, at);
    }

    private static <A extends java.lang.annotation.Annotation> Predicate<A> isAnnotationOfType(
            TypeDescriptor $reifiedAnnotation, Type<?> at)
            throws EnumeratedTypeError {
        if (at instanceof nothingType_) {
            return false_();
        } else if (at instanceof AppliedUnionType) {
            Sequential<? extends Type<?>> caseTypes = ((AppliedUnionType<?>)at).getCaseTypes();
            return or(Predicates.<A>mapTypesToIsAnnotationOfType($reifiedAnnotation, caseTypes));
        } else if (at instanceof AppliedIntersectionType) {
            Sequential<? extends Type<?>> satisfiedTypes = ((AppliedIntersectionType<?>)at).getSatisfiedTypes();
            return and(Predicates.<A>mapTypesToIsAnnotationOfType($reifiedAnnotation, satisfiedTypes));
        } else if (at instanceof ClassOrInterface) {
            // TODO What if reified is Annotation, or ContrainedAnnotation, or OptionalAnnotation, or SequencedAnnotation?
            @SuppressWarnings({ "rawtypes", "unchecked" })
            AnnotationPredicate<A> predicate = annotationPredicate($reifiedAnnotation, (ClassOrInterface)at);
            return predicate;
        } else {
            throw new EnumeratedTypeError("Supposedly exhaustive switch was not exhaustive: "+at);
        }
    }
    
    private static <A extends java.lang.annotation.Annotation> Predicate<A>[] mapTypesToIsAnnotationOfType(TypeDescriptor $reifiedAnnotation, 
            Sequential<? extends Type<?>> caseTypes) {
        @SuppressWarnings("unchecked")
        Predicate<A>[] preds = new Predicate[Util.toInt(caseTypes.getSize())];
        int ii = 0;
        Iterator<? extends Type<?>> iterator = caseTypes.iterator();
        Object element = iterator.next();
        while (element instanceof Type) {
            preds[ii++] = isAnnotationOfType($reifiedAnnotation, (Type<?>)element);
            element = iterator.next();
        }
        return preds;
    }

    static interface AnnotationPredicate<A extends java.lang.annotation.Annotation> extends Predicates.Predicate<A> {
        // TODO Is this a worthwhile optimization to make?
        /** 
         * Whether we should instantiate the given Java annotation into a 
         * Ceylon annotation. If this returns true 
         * {@link #accept(ceylon.language.meta.model.Annotation)} 
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
    
    private static <A extends java.lang.annotation.Annotation,
    Value extends java.lang.annotation.Annotation, Values, ProgramElement extends Annotated>
    AnnotationPredicate<A> annotationPredicate(
            final TypeDescriptor $reifiedValues,
            ClassOrInterface<? extends java.lang.annotation.Annotation> annotationType) {
        final Class<?> refAnnotationClass = Metamodel.getReflectedAnnotationClass(annotationType);
        final Class<?> refAnnotationType;
        final Class<?> refAnnotationWrapperType;
        if (java.lang.annotation.Annotation.class == refAnnotationClass
                || ceylon.language.Annotation.class == refAnnotationClass
                || ceylon.language.ConstrainedAnnotation.class == refAnnotationClass
                || ceylon.language.OptionalAnnotation.class == refAnnotationClass
                || ceylon.language.SequencedAnnotation.class == refAnnotationClass) {
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
            if (refAnnotationClass.getAnnotation(Ceylon.class) == null) {
                // Java annotation
                refAnnotationType = refAnnotationClass;
                refAnnotationWrapperType = null;
            }else{
                try {
                    refAnnotationType = Class.forName(refAnnotationClass.getName()+"$annotation$", 
                            false, refAnnotationClass.getClassLoader());
                } catch (ClassNotFoundException e) {
                    throw Metamodel.newModelError("Class not found: " + refAnnotationClass.getName()+"$annotation$");
                }
                Class<?> c;
                try {
                    c = Class.forName(refAnnotationClass.getName()+"$annotations$", 
                            false, refAnnotationClass.getClassLoader());
                } catch (ClassNotFoundException e) {
                    c = null;
                }
                refAnnotationWrapperType = c;
            }
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
