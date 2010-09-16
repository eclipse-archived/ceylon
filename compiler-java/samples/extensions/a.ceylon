// A simple widening extension.

public void a(Process process) {
    Integer i = 1;
}

// Should compile to:
//   Integer i = ceylon.Natural.instance(1L).integer();
