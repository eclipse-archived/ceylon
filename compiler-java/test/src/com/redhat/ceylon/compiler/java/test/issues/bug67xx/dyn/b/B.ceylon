import com.redhat.ceylon.compiler.java.test.issues.bug67xx.dyn.a { A }
import ceylon.language.meta.declaration {
    NestableDeclaration
}

shared class B(A a){
    shared void test(){
        print("In Ceylon, with Java object: ``a``");
        assert(`B`.string == "com.redhat.ceylon.compiler.java.test.issues.bug67xx.dyn.b::B");
        assert(`class B`.string == "class com.redhat.ceylon.compiler.java.test.issues.bug67xx.dyn.b::B");
        assert(`class B`.containingPackage.name == "com.redhat.ceylon.compiler.java.test.issues.bug67xx.dyn.b");
        assert(`class B` in `class B`.containingPackage.members<NestableDeclaration>());
        assert(`class B`.containingPackage.members<NestableDeclaration>().size == 1);
        assert(`class B`.containingModule.string == "module com.redhat.ceylon.compiler.java.test.issues.bug67xx.dyn.b/1");
        assert(`package` in `class B`.containingModule.members);
        assert(`class B`.containingModule.members.size == 1);
        assert(`class B`.containingModule == `module`);

        assert(`A`.string == "com.redhat.ceylon.compiler.java.test.issues.bug67xx.dyn.a::A");
        assert(`class A`.string == "class com.redhat.ceylon.compiler.java.test.issues.bug67xx.dyn.a::A");
        assert(`class A`.containingPackage.name == "com.redhat.ceylon.compiler.java.test.issues.bug67xx.dyn.a");
        assert(`class A` in `class A`.containingPackage.members<NestableDeclaration>());
        assert(`class A`.containingPackage.members<NestableDeclaration>().size == 1);
        assert(`class A`.containingModule.string == "module default/unversioned");
        assert(`package com.redhat.ceylon.compiler.java.test.issues.bug67xx.dyn.a` in `class A`.containingModule.members);
        assert(`class A`.containingModule.members.size == 2);
        
        // Test reified generics
        Object setB = set{this};
        assert(is Set<B> setB);

        Object setA = set{a};
        assert(is Set<A> setA);
    }
}