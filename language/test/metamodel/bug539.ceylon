import ceylon.language.meta.model { ClassOrInterface }

@test
shared void bug539() {
  value comparisons = `Comparison`.caseValues;
  assert(comparisons.size == 3 && larger in comparisons
         && smaller in comparisons && equal in comparisons);
  value bools = `Boolean`.caseValues;
  assert(bools.size==2 && true in bools && false in bools);
}
