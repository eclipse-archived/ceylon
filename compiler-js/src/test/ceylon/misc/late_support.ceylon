import check { check, fail }

class LateTestChild() {
    shared late LateTestParent parent;
}

class LateTestParent(children) {
    shared LateTestChild* children;
    for (child in children) {
        child.parent = this;
    }
}

class VarLate() {
  shared late VarLate v1;
  shared late variable VarLate v2;
}

void testLate() {
    value kids = [LateTestChild(), LateTestChild()];
    LateTestParent(*kids);
    try {
        LateTestParent(*kids);
        fail("reassigning to late attribute should fail");
    } catch (InitializationError ex) {
        check(true);
    } catch (Exception ex) {
        fail("wrong exception thrown for late attribute");
    }
    try {
        print(LateTestChild().parent);
        fail("Reading uninitialized late attribute should fail");
    } catch (InitializationError ex) {
        check(true);
    } catch (Exception ex) {
        fail("wrong exception thrown for late attribute");
    }
    //#467
    value vl=VarLate();
    vl.v1=vl;
    vl.v2=vl;
    try {
        vl.v2=VarLate();
        check(vl.v2.hash!=vl.hash, "late variable reassign");
    } catch (InitializationError ex) {
        fail("late variables should be reassignable");
    }
}

