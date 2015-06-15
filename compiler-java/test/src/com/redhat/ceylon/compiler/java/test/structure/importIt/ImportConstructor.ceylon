import com.redhat.ceylon.compiler.java.test.structure.importIt.ctor{
    X=Ctor{
        other,
        y=third
    }
}
@noanno
void importConstructor() {
    X();
    other();
    y();
    value x1 = X;
    value other1 = other;
    value y1 = y;
    
}