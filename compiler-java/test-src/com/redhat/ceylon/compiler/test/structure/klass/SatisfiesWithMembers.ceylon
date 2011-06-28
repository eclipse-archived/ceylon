interface SatisfiesWithMembers {
    formal shared Boolean b;
    formal shared variable Boolean b2;
    formal shared void m();
}

class SatisfiesWithMembersClass() satisfies SatisfiesWithMembers {
    actual shared Boolean b = true;
    actual default shared variable Boolean b2 := false;
    actual shared void m() {
        return;
    }
} 
