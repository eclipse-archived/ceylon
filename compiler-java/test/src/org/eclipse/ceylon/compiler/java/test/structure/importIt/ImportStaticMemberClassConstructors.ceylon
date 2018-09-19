import org.eclipse.ceylon.compiler.java.test.structure.importIt.ctor {
    NestedCtor
}

void importStaticMemberClassConstructors() {
    value item1 = NestedCtor.Ctor.instance;
    //value item2 = NestedCtor.Ctor.create();
}