class Display() satisfies Observer<Model,Display> {
    shared actual void update(Model m) {
        writeLine("model has changed");
    }
}
