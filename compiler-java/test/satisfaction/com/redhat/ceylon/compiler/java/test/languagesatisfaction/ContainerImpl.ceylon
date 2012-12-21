class ContainerImpl() satisfies Container<Integer> {
    shared actual Boolean empty = true;
    shared actual Boolean contains(Object element){ return false; }
    shared actual Integer first { return bottom; }
    shared actual Integer last { return bottom; }
}