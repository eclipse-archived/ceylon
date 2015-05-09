dynamic DynamicInterface {
    shared formal Integer fun();
    shared formal Integer val;
}

dynamic WithConcreteMembers 
        satisfies DynamicInterface {
    @error String name1 = "Gavin";
    @error shared String name2 = "Gavin";
    @error shared formal class Inner() {}
    shared formal String name3;
}

@error dynamic WithConcreteSupertypes 
        satisfies Category<Boolean> {}