package ceylon.language.serialization;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

import ceylon.language.Anything;
import ceylon.language.AssertionError;
import ceylon.language.Collection;
import ceylon.language.Entry;
import ceylon.language.String;
import ceylon.language.Tuple;
import ceylon.language.impl.rethrow_;
import ceylon.language.meta.declaration.ClassDeclaration;
import ceylon.language.meta.declaration.ValueDeclaration;
import ceylon.language.meta.model.ClassModel;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.runtime.metamodel.AppliedClass;
import com.redhat.ceylon.compiler.java.runtime.metamodel.AppliedMemberClass;
import com.redhat.ceylon.compiler.java.runtime.metamodel.Metamodel;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.java.runtime.serialization.$Serialization$;
import com.redhat.ceylon.compiler.java.runtime.serialization.ElementImpl;
import com.redhat.ceylon.compiler.java.runtime.serialization.MemberImpl;
import com.redhat.ceylon.compiler.java.runtime.serialization.Serializable;
import com.redhat.ceylon.model.typechecker.model.FunctionOrValue;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.model.typechecker.model.TypedReference;

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
            args[ii] = Metamodel.getTypeDescriptor((ceylon.language.meta.model.Type<?>)typeArgs.getFromFirst(jj));
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
    public <Id> java.lang.Object initialize(TypeDescriptor $reified$Id, DeserializationContextImpl<Id> context) {
        Object instance_ = getInstance_();
        if (!(instance_ instanceof Serializable)) {
            // we should never get here
            throw new AssertionError("Cannot initialize instance that is not serializable");
        }
        Serializable instance = (Serializable)instance_;
        if (instance_ instanceof ceylon.language.Array) {
            initializeArray(context, (ceylon.language.Array<?>)instance);
        } else if (instance_ instanceof ceylon.language.Tuple) {
            initializeTuple($reified$Id, context, (ceylon.language.Tuple<?,?,?>)instance);
        } else {
            initializeObject(context, instance);
        }
        setState(null);
        return null;
    }

    protected <Id> void initializeTuple(TypeDescriptor $reified$Id,DeserializationContextImpl<Id> context,
            ceylon.language.Tuple<?,?,?> instance) {
        NativeMap<ReachableReference, Id> state = (NativeMap<ReachableReference, Id>)getState();
        ValueDeclaration firstAttribute = (ValueDeclaration)
                ((ClassDeclaration) Metamodel.getOrCreateMetamodel(Tuple.class))
                .getMemberDeclaration(ValueDeclaration.$TypeDescriptor$, "first");
        MemberImpl firstMember = new MemberImpl(firstAttribute);
        java.lang.Object first = getReferredInstance(context, state, 
                firstMember);
        ValueDeclaration restAttribute = (ValueDeclaration)
                ((ClassDeclaration) Metamodel.getOrCreateMetamodel(Tuple.class))
                .getMemberDeclaration(ValueDeclaration.$TypeDescriptor$, "rest");
        MemberImpl restMember = new MemberImpl(restAttribute);
        Id restId = state.get(restMember);
        java.lang.Object referredRest = context.leakInstance(restId);
        if (referredRest instanceof Partial 
                && !((PartialImpl)referredRest).getInitialized()) {
            // Safe because tuples are immutable => no cycles 
            ((PartialImpl)referredRest).initialize($reified$Id, context);
        }
        java.lang.Object rest = getReferredInstance(context, state, restMember);
        ((Tuple<?,?,?>)instance).$completeInit$(first, rest);
        // now check compatibility (do this after initialization 
        // because Tuple$getType$ requires the tuple is initialized!
        Type firstMemberType = Metamodel.getModuleManager().getCachedType(getClassTypeDescriptor().getTypeArguments()[1]);
        Type firstInstanceType = Metamodel.getModuleManager().getCachedType(
                Metamodel.getTypeDescriptor(first));
        if (!firstInstanceType.isSubtypeOf(firstMemberType)) {
            throw notAssignable(firstMember, firstMemberType, firstInstanceType);
        }
        Type restInstanceType = Metamodel.getModuleManager().getCachedType(
                Metamodel.getTypeDescriptor(rest));
        Type restMemberType = Metamodel.getModuleManager().getCachedType(getClassTypeDescriptor().getTypeArguments()[2]);
        if (!restInstanceType.isSubtypeOf(restMemberType)) {
            throw notAssignable(restMember, restMemberType, restInstanceType);
        }
    }
    
    protected <Id> void initializeArray(DeserializationContextImpl<Id> context,
            ceylon.language.Array<?> instance) {
        NativeMap<ReachableReference, Id> state = (NativeMap<ReachableReference, Id>)getState();
        // In this case we statically know the $references$, and they are:
        // the array's size and each of its elements (as integers)
        ReachableReference sizeAttr = (ReachableReference)instance.$references$().iterator().next();//ceylon.language.String.instance("ceylon.language::Array.size");
        ceylon.language.Integer size = (ceylon.language.Integer)getReferredInstance(context, 
                state, 
                sizeAttr);
        if (size == null) {
            throw insufficiantState(sizeAttr);
        }
        instance.$set$(sizeAttr, size);
        int sz = Util.toInt(size.longValue());
        for (int ii = 0; ii < sz; ii++) {
            ElementImpl index = new ElementImpl(ii);
            Id id = state.get(index);
            if (id == null) {
                throw insufficiantState(index);
            }
            TypeDescriptor.Class arrayType = (TypeDescriptor.Class)Metamodel.getTypeDescriptor(instance);
            Type arrayElementType = Metamodel.getModuleManager().getCachedType(arrayType.getTypeArguments()[0]);
            Object element = getReferredInstance(context, id);
            Type elementType = Metamodel.getModuleManager().getCachedType(Metamodel.getTypeDescriptor(element));
            if (elementType.isSubtypeOf(arrayElementType)) {
                instance.$set$(index, element);
            } else {
                throw notAssignable(index, 
                        arrayElementType,
                        elementType);
            }
        }
        if (state.getSize() != sz + 1) {
            throw insufficiantState((ReachableReference)null);
        }
    }

    
    protected <Id> void initializeObject(
            DeserializationContextImpl<Id> context,
            Serializable instance) {
        NativeMap<ReachableReference, Id> state = (NativeMap<ReachableReference, Id>)getState();
        // TODO If it were a map of java.lang.String we'd avoid pointless extra boxing
        java.util.Collection<ReachableReference> reachables = instance.$references$();
        int numLate = 0;
        for (ReachableReference r : reachables) {
            if (r instanceof Member
                    && ((Member)r).getAttribute().getLate()) {
                numLate++;
            } else if (r instanceof Outer) {
                numLate++;
            }
        }
        if (state.getSize() < reachables.size()-numLate) {
            HashSet<ReachableReference> missingNames = new HashSet<ReachableReference>();
            java.util.Iterator<ReachableReference> it = reachables.iterator();
            while (it.hasNext()) {
                missingNames.add(it.next());
            }
            ceylon.language.Iterator<? extends ReachableReference> it2 = state.getKeys().iterator();
            Object next;
            while (((next = it2.next()) instanceof ReachableReference)) {
                missingNames.remove(next);
            }
            throw insufficiantState(missingNames);
        }
        for (ReachableReference reference : reachables) {
            if (reference instanceof Member) {
                Member member  = (Member)reference;
                
                if (member.getAttribute().getLate()
                        && !state.contains(member)
                        || state.get(member) == uninitializedLateValue_.get_()) {
                    continue;
                }
                
                TypeDescriptor.Class classTypeDescriptor = getClassTypeDescriptor();
                Entry<TypeDescriptor.Class,String> cacheKey = new Entry<TypeDescriptor.Class,String>(
                        TypeDescriptor.klass(TypeDescriptor.Class.class), String.$TypeDescriptor$, 
                        classTypeDescriptor, String.instance(member.getAttribute().getQualifiedName()));
                Type memberType = context.getMemberTypeCache().get(cacheKey);
                if (memberType == null) {
                    Type pt = Metamodel.getModuleManager().getCachedType(classTypeDescriptor);
                    while (!pt.getDeclaration().getQualifiedNameString().equals(((ClassDeclaration)member.getAttribute().getContainer()).getQualifiedName())) {
                        pt = pt.getExtendedType();
                    }
                    FunctionOrValue attributeDeclaration = (FunctionOrValue)((TypeDeclaration)pt.getDeclaration()).getMember(
                            member.getAttribute().getName(), null, false);
                    TypedReference attributeType = pt.getTypedMember(
                            attributeDeclaration, Collections.<Type>emptyList(), true);
                    memberType = attributeType.getType();
                    context.getMemberTypeCache().put(cacheKey, memberType);
                }
                
                Object referredInstance = getReferredInstance(context, state, 
                        member);
                Type instanceType = Metamodel.getModuleManager().getCachedType(
                        Metamodel.getTypeDescriptor(referredInstance));
                if (!instanceType.isSubtypeOf(memberType)) {
                    throw notAssignable(member, memberType, instanceType);
                }
                instance.$set$(member, referredInstance);
                // the JVM will check the assignability, but we need to 
                // check assignability at the ceylon level, so we need to know 
                /// type of the attribute an the type that we're assigning.
                // XXX this check is really expensive!
                // we should cache the attribute type on the context
                // when can we avoid this check.
                // XXX we can cache MethodHandle setters on the context!
            } else if (reference instanceof Outer) {
                // ignore it -- the DeserializationContext deals with
                // instantiating member classes
                continue;
            } else {
                throw new AssertionError("unexpected ReachableReference " + reference);
            }
        }
    }
    
    java.lang.String descriptor(ReachableReference reachable) {
        if (reachable instanceof Member) {
            return java.lang.String.valueOf(((Member)reachable).getAttribute());
        } else if (reachable instanceof Element) {
            return "index " + ((Element)reachable).getIndex();
        } else {
            return java.lang.String.valueOf(reachable);
        }
    }
    
    DeserializationException notAssignable(ReachableReference attributeOrIndex, Type attributeOrIndexType, Type instanceType) {
        return new DeserializationException("instance not assignable to " + descriptor(attributeOrIndex) + " of id " + getId() + ": "
                + instanceType.asString() + " is not assignable to " + attributeOrIndexType.asString());
    }
    
    DeserializationException insufficiantState(java.util.Collection<ReachableReference> missingNames) {
        StringBuilder sb = new StringBuilder();
        Iterator<ReachableReference> iterator = missingNames.iterator();
        while (iterator.hasNext()) {
            ReachableReference r = iterator.next();
            sb.append(descriptor(r));
            if (iterator.hasNext()) {
                sb.append(", ");
            }
        }
        return new DeserializationException("lacking sufficient state for instance with id " + getId() + ": " + sb.toString());
    }
    
    DeserializationException insufficiantState(ReachableReference missing) {
        return new DeserializationException("lacking sufficient state for instance with id " + getId() + (missing != null ?  ": " + descriptor(missing) : ""));
    }

    /** Get the (leaked, partially constructed) instance with the given name. */
    protected <Id> java.lang.Object getReferredInstance(
            DeserializationContextImpl<Id> context,
            NativeMap<ReachableReference, Id> state,
            ReachableReference reachable) {
        Id referredId = state.get(reachable);
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
    
    public java.lang.String toString() {
        return "Partial " + getId() + (getInitialized() ? "initialized" : getInstantiated() ? "instantiated" : "uninstantiated") + getClazz();
    }
    
    
}