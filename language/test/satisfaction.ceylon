//Simple implementation of single interfaces to verify correct compilation and behavior
class MyCategory() satisfies Category {
    shared actual Boolean contains(Object e) {
        if (is Integer e) {
            return e>0 && e<11;
        }
        return false;
    }
}

void testSatisfaction() {
    value category = MyCategory();
    assert(5 in category, "Category.contains [1]");
    assert(!20 in category, "Category.contains [2]");
    assert(category.containsEvery(1,2,3,4,5), "Category.containsEvery [1]");
    assert(!category.containsEvery(0,1,2,3), "Category.containsEvery [2]");
    assert(category.containsAny(0,1,2,3), "Category.containsAny [1]");
    assert(!category.containsAny(0,11,12,13), "Category.containsAny [2]");
}
