"A value getter of type `Nothing`. The expression `nothing`
 is formally assignable to any type, but produces an 
 exception when evaluated.
 
 (This is most useful for tool-generated implementations of
 `formal` members.)"
throws (`class AssertionException`, 
        "when evaluated")
shared Nothing nothing {
    "nothing may not be evaluated"
    assert (false);
}
