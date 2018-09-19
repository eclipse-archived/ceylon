import org.eclipse.ceylon.compiler.java.test.structure.importIt.ctor {
    NestedCtor { Ctor }
}

void importStaticMemberClassConstructors() {
    value item1 = Ctor.instance;
    value item2 = Ctor.create();
    value item3 = NestedCtor.Ctor.instance;
    //value item4 = NestedCtor.Ctor.create();
}