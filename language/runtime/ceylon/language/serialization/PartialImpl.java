package ceylon.language.serialization;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashSet;

import ceylon.language.AssertionError;
import ceylon.language.Collection;
import ceylon.language.Entry;
import ceylon.language.String;
import ceylon.language.impl.rethrow_;
import ceylon.language.meta.model.ClassModel;
import ceylon.language.meta.model.Type;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.runtime.metamodel.AppliedClass;
import com.redhat.ceylon.compiler.java.runtime.metamodel.AppliedMemberClass;
import com.redhat.ceylon.compiler.java.runtime.metamodel.Metamodel;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.java.runtime.serialization.$Serialization$;
import com.redhat.ceylon.compiler.java.runtime.serialization.Serializable;
import com.redhat.ceylon.model.typechecker.model.MethodOrValue;
import com.redhat.ceylon.model.typechecker.model.ProducedType;
import com.redhat.ceylon.model.typechecker.model.ProducedTypedReference;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;

@Ceylon(major = 8, minor=0)
@com.redhat.ceylon.compiler.java.metadata.Class
class PartialImpl extends Partial {
    PartialImpl(java.lang.Object id) {
        super(id);
    }
    
    private TypeDescriptor.Class getClassTypeDescriptor() {
        ClassModel<?, ?> classModel = getClazz();
        if (classModel == null) {
            throw new DeserializationException("no class specified for instance with id " + getId());
        } 
        TypeDescriptor[] typeArguments = ((TypeDescriptor.Class)((ReifiedType)classModel).$getType$()).getTypeArguments();
        if (classModel instanceof AppliedClass) {
            return ((TypeDescriptor.Class)typeArguments[0]);
        } else if (classModel instanceof AppliedMemberClass) {
            return ((TypeDescriptor.Class)((TypeDescriptor.Member)typeArguments[1]).getMember());
        } else {
            throw new AssertionError("unexpected class model for instance with id " + getId() + ": " 
                    + (classModel != null ? classModel.getClass().getName() : "null")); 
        }
    }
    
    private TypeDescriptor.Class getOuterClassTypeDescriptor() {
        ClassModel<?, ?> classModel = getClazz();
        if (classModel instanceof AppliedMemberClass) {
            TypeDescriptor[] typeArguments = ((TypeDescriptor.Class)((ReifiedType)classModel).$getType$()).getTypeArguments();
            // MemberClass<Container, Type, Arguments>
            return (TypeDescriptor.Class)typeArguments[0];
        } else {
            return null; 
        }
    }
    
    @Override
    public java.lang.Object instantiate() {
        final ClassModel<?, ?> classModel = getClazz();
        if (classModel == null) {
            throw new DeserializationException("no class specified for instance with id " + getId());
        }
        final java.lang.Class<?> clazz = getClassTypeDescriptor().getKlass();
        final Class<?> outerClass;
        Object outer;
        if (classModel instanceof AppliedClass) {
            // Class<Type, Arguments>
            outerClass = null;
            outer = null;
        } else if (classModel instanceof AppliedMemberClass) {
            // MemberClass<Container, Type, Arguments>
            // the algorithm in DeserializationContext
            // should ensure the container exists by the point we're called.
            outerClass = getOuterClassTypeDescriptor().getKlass();
            outer = super.getContainer();
            if (outer instanceof Partial) {
                outer = ((Partial) outer).getInstance_();
            }
            if (outer == null) {
                throw new DeserializationException("no containing instance specified for member instance with id" + getId());
            }
        } else {
            throw new AssertionError("unexpected class model: " 
                    + (classModel != null ? classModel.getClass().getName() : "null"));
        }
        // Construct arrays for types and arguments for reflective instantiation
        // of the serialization constructor
        Collection<?> typeArgs = classModel.getTypeArguments().getItems();
        Class<?>[] types = new Class[(outerClass != null ? 2 : 1) + Util.toInt(typeArgs.getSize())];
        Object[] args = new Object[(outer != null ? 2 : 1) + Util.toInt(typeArgs.getSize())];
        int ii = 0;
        if (outerClass != null) {
            types[ii] = outerClass;
            args[ii] = outer;
            ii++;
        }
        types[ii] = $Serialization$.class;
        args[ii] = null;
        ii++;
        for (int jj = 0 ; jj < typeArgs.getSize(); ii++, jj++) {
            types[ii] = TypeDescriptor.class;
            args[ii] = Metamodel.getTypeDescriptor((Type)typeArgs.getFromFirst(jj));
        }

        try {
            Constructor<?> ctor = clazz.getDeclaredConstructor(types);
            ctor.setAccessible(true);
            // Actually we need to pass something equivalent to the type descriptors here
            // because the companion instances can require those. But we don't have the deconstructed yet!
            // This means we have to obtain the type descriptors from the class model
            java.lang.Object newInstance = ctor.newInstance(args);// Pass a null $Serialization$
            if (newInstance instanceof Serializable) {
                super.setInstance_(newInstance);
            } else {
                // we should never get here (a NoSuchMethodException should've been thrown and caught below) 
                throw new AssertionError("instance class " + classModel + " is not serializable for instance with id " + getId());
            }
        } catch (NoSuchMethodException e) {
            throw new DeserializationException("instance class " + classModel + " is not serializable for instance with id " + getId());
        } catch (InvocationTargetException e) {
            // Should never happen: it's a compiler-generate constructor
            rethrow_.rethrow(e);
        } catch (SecurityException e) {
            // Should never happen
            rethrow_.rethrow(e);
        } catch (InstantiationException|IllegalAccessException|IllegalArgumentException e) {
            // Should never happen: it's a compiler-generate constructor
            rethrow_.rethrow(e);
        }
        return null; 
    }
    
    @Override
    public <Id> java.lang.Object initialize(TypeDescriptor reifiedId, DeserializationContextImpl<Id> context) {
        Object instance_ = getInstance_();
        if (!(instance_ instanceof Serializable)) {
            // we should never get here
            throw new AssertionError("Cannot initialize instance that is not serializable");
        }
        Serializable instance = (Serializable)instance_;
        //NativeMap<java.lang.Object, Id> state = (NativeMap<java.lang.Object, Id>)getState();
        if (instance_ instanceof ceylon.language.Array) {
            initializeArray(context, (ceylon.language.Array<?>)instance);
        } else {
            initializeObject(context, instance);
        }
        return null;
    }

    protected <Id> void initializeArray(DeserializationContextImpl<Id> context,
            ceylon.language.Array instance) {
        NativeMap<java.lang.Object, Id> state = (NativeMap<java.lang.Object, Id>)getState();
        // In this case we statically know the $references$, and they are:
        // the array's size and each of its elements (as integers)
        String sizeAttr = ceylon.language.String.instance("ceylon.language::Array.size");
        ceylon.language.Integer size = (ceylon.language.Integer)getReferredInstance(context, 
                (NativeMap<ceylon.language.String, Id>)(NativeMap)state, 
                sizeAttr);
        if (size == null) {
            throw new DeserializationException("lacking sufficient state for instance with id " + getId() + ": ceylon.language::Array.size");
        }
        instance.$set$(sizeAttr, size);
        int sz = Util.toInt(size.longValue());
        for (int ii = 0; ii < sz; ii++) {
            ceylon.language.Integer index = ceylon.language.Integer.instance(ii);
            Id id = (Id)state.get(index);
            if (id == null) {
                throw new DeserializationException("lacking sufficient state for instance with id " + getId() + ": index " + ii);
            }
            TypeDescriptor.Class arrayType = (TypeDescriptor.Class)Metamodel.getTypeDescriptor(instance);
            ProducedType arrayElementType = Metamodel.getModuleManager().getCachedProducedType(arrayType.getTypeArguments()[0]);
            Object element = getReferredInstance(context, id);
            ProducedType elementType = Metamodel.getModuleManager().getCachedProducedType(Metamodel.getTypeDescriptor(element));
            if (elementType.isSubtypeOf(arrayElementType)) {
                instance.$set$(ceylon.language.Integer.instance(ii), element);
            } else {
                throw notAssignable(ii, 
                        arrayElementType,
                        elementType);
            }
        }
        if (state.getSize() != sz + 1) {
            throw new DeserializationException("lacking sufficient state for instance with id " + getId());
        }
    }

    protected <Id> void initializeObject(
            DeserializationContextImpl<Id> context,
            Serializable instance) {
        NativeMap<ceylon.language.String, Id> state = (NativeMap<ceylon.language.String, Id>)getState();
        // TODO If it were a map of java.lang.String we'd avoid pointless extra boxing
        java.util.Collection<java.lang.String> attributeNames = (java.util.Collection)instance.$references$();
        if (state.getSize() != attributeNames.size()) {
            HashSet<ceylon.language.String> missingNames = new HashSet<ceylon.language.String>();
            java.util.Iterator<java.lang.String> it = attributeNames.iterator();
            while (it.hasNext()) {
                missingNames.add(ceylon.language.String.instance(it.next()));
            }
            ceylon.language.Iterator<? extends ceylon.language.String> it2 = state.getKeys().iterator();
            Object next;
            while (((next = it2.next()) instanceof ceylon.language.String)) {
                missingNames.remove(next);
            }
            throw insufficiantState(missingNames);
        }
        for (java.lang.String attributeName : attributeNames) {
            Object referredInstance = getReferredInstance(context, state, 
                    ceylon.language.String.instance(attributeName));
            java.lang.String unqualifiedName = attributeName.substring(attributeName.lastIndexOf('.')+1, attributeName.length());
            
            TypeDescriptor.Class classTypeDescriptor = getClassTypeDescriptor();
            Entry<TypeDescriptor.Class,String> cacheKey = new Entry<TypeDescriptor.Class,String>(TypeDescriptor.klass(TypeDescriptor.Class.class), String.$TypeDescriptor$, classTypeDescriptor, String.instance(unqualifiedName));
            ProducedType attributeOrIndexType = context.getMemberTypeCache().get(cacheKey);
            if (attributeOrIndexType == null) {
                ProducedType pt = Metamodel.getModuleManager().getCachedProducedType(classTypeDescriptor);
                MethodOrValue attributeDeclaration = (MethodOrValue)((TypeDeclaration)pt.getDeclaration()).getMember(
                        unqualifiedName, null, false);
                ProducedTypedReference attributeType = pt.getTypedMember(
                        attributeDeclaration, Collections.<ProducedType>emptyList(), true);
                attributeOrIndexType = attributeType.getType();
                context.getMemberTypeCache().put(cacheKey, attributeOrIndexType);
            }
            
            ProducedType instanceType = Metamodel.getModuleManager().getCachedProducedType(
                    Metamodel.getTypeDescriptor(referredInstance));
            
            if (instanceType.isSubtypeOf(attributeOrIndexType)) {
                // XXX the JVM will check the assignability, but we need to 
                // check assignability at the ceylon level, so we need to know 
                /// type of the attribute an the type that we're assigning.
                // XXX this check is really expensive!
                // we should cache the attribute type on the context
                // when can we avoid this check.
                // XXX we can cache MethodHandle setters on the context!
                instance.$set$(attributeName, referredInstance);
            } else {
                throw notAssignable(attributeName, attributeOrIndexType, instanceType);
            }
        }
    }
    
    java.lang.String descriptor(java.lang.Object referent) {
        if (referent instanceof java.lang.String 
                || referent instanceof ceylon.language.String) {
            return "attribute " + referent;
        } else if (referent instanceof java.lang.Integer
                || referent instanceof java.lang.Long
                || referent instanceof ceylon.language.Integer) {
            return "index " + referent;
        } else {
            return java.lang.String.valueOf(referent);
        }
    }
    
    DeserializationException notAssignable(java.lang.Object attributeOrIndex, ProducedType attributeOrIndexType, ProducedType instanceType) {
        return new DeserializationException("instance not assignable to " + descriptor(attributeOrIndex) + " of id " + getId() + ": "
                + instanceType.getProducedTypeName() + " is not assignable to " + attributeOrIndexType.getProducedTypeName());
    }
    
    DeserializationException insufficiantState(java.util.Collection<ceylon.language.String> missingNames) {
        return new DeserializationException("lacking sufficient state for instance with id " + getId() + ": " + missingNames);
    }

    /** Get the (leaked, partially constructed) instance with the given name. */
    protected <Id> java.lang.Object getReferredInstance(
            DeserializationContextImpl<Id> context,
            NativeMap<ceylon.language.String, Id> state,
            ceylon.language.String attributeName) {
        Id referredId = state.get(attributeName);
        return getReferredInstance(context, referredId);
    }
    
    /** Get the (leaked, partially constructed) instance with the given id. */
    protected <Id> java.lang.Object getReferredInstance(
            DeserializationContextImpl<Id> context, Id referredId) {
        java.lang.Object referred = context.leakInstance(referredId);
        if (referred instanceof Partial) {
            referred = ((Partial)referred).getInstance_();
        }
        return referred;
    }
}