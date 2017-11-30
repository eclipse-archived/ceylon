class Display() satisfies Observer<Model,Display> {
    shared actual void update(Model m) {
        print("model has changed");
    }
}
