import java.lang {
    ObjectArray
}
import java.lang.reflect {
    Method
}
import net.bytebuddy {
    ByteBuddy
}
import net.bytebuddy.implementation {
    InvocationHandlerAdapter
}
import net.bytebuddy.matcher {
    ElementMatchers { ... }
}
import net.bytebuddy.description.method {
    MethodDescription
}
import ceylon.interop.java {
    javaClassFromInstance
}
import ceylon.language.meta {
    type
}

"An interceptor for method calls and attribute evaluations."
shared interface Interceptor<Instance> {
    
    "Intercepts method invocations. Delegate to the method
     of [[target]] by calling `method(args)`."
    shared default Return call<Return,Args>
            (Instance target, String name, Args args, Return method(Args args))
            given Args satisfies Anything[]
            => method(args);
    
    "Intercepts attribute evaluation. The current attribute
     value for [[target]] is [[attribute]]."
    shared default Type get<Type>
            (Instance target, String name, Type attribute)
            => attribute;
    
}

"Create a typesafe proxy for the given [[instance]], with
 the given [[Interceptor]] for method calls and attribute
 evaluations."
shared 
Instance proxy<Instance>
        (Instance instance, Interceptor<Instance> handler)
        given Instance satisfies Object {
    
    
    function methodHandler(Object proxy, Method method, ObjectArray<Object?> arguments)
            => handler.call {
        target = instance;
        name = method.name;
        args = [*arguments];
        method(Object?[] arguments)
                => method.invoke(instance, *arguments) of Object?;
    };
    
    function attributeHandler(Object proxy, Method method, ObjectArray<Object?> arguments)
            => handler.get {
        target = instance;
        name = let (name = method.name.removeInitial("get"))
        name[0..0].lowercased + name[1...];
        attribute = method.invoke(instance) of Object?;
    };
    
    return ByteBuddy()
            .subclass(type(instance))
            .method(not(isGetter<MethodDescription>()))
            .intercept(InvocationHandlerAdapter.\iof(methodHandler))
            .method(isGetter<MethodDescription>())
            .intercept(InvocationHandlerAdapter.\iof(attributeHandler))
            .make()
            .load(javaClassFromInstance(instance).classLoader)
            .loaded
            .newInstance();
    
}
