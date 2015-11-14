import com.redhat.ceylon.compiler.typechecker.test.mod.internal {
    Unshared
}

Unshared un1 = Unshared();
@error shared Unshared un2 = Unshared();

class Cl1() extends Unshared() {}
@error shared class Cl2() extends Unshared() {}

void fu1(Unshared u) {}
shared void fu2(@error Unshared u) {}