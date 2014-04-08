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
}

