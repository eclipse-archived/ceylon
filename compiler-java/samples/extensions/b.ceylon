// Widen the right hand side of an expression.

public void b(ceylon.Process process) {
    Float f = 1.5 + 1;
}

// Should compile to:
//   Float f =
//     ceylon.Float.instance(1.5).plus(ceylon.Natural.instance(1L).floatXXX());
