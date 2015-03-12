import com.redhat.ceylon.compiler.java.test.structure.importIt.ctor{
    X=Ctor{
        Other,
        Y=Third
    }
}
@noanno
void importConstructor() {
    X();
    Other();
    Y();
    value x = X;
    value other = Other;
    value y = Y;
    
}