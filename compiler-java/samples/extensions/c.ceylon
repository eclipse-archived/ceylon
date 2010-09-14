// Widen the left hand side of an expression.

public void c(ceylon.Process process) {
    Float f = 1 + 1.5;
}

// Should compile to:
//   Float f =
//     ceylon.Natural.instance(1L).floatXXX().plus(ceylon.Float.instance(1.5));
