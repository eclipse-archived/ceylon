import java.util{Arrays, List}

void bug6746() {
    variable List<Integer> cubes = Arrays.asList<Integer>(for (i in 0..2) i^3);
    assert("[0, 1, 8]" == cubes.string);
    cubes = Arrays.asList<Integer>(null, for (i in 0..2) i^3);
    assert("[null, 0, 1, 8]" == cubes.string);
    cubes = Arrays.asList<Integer>(null, null, for (i in 0..2) i^3);
    assert("[null, null, 0, 1, 8]" == cubes.string);
}