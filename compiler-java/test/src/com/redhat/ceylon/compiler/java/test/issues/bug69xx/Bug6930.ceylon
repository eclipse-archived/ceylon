import java.awt.event { ActionEvent }

@noanno
void bug6930() {
    value listeners = [(ActionEvent event) => process.writeLine("listener called"),
        (ActionEvent event) => process.writeLine("listener called")];
    value listener = (ActionEvent event) => process.writeLine("listener called");
    Bug6930MweJava("test");
    Bug6930MweJava("test", (ActionEvent event) => process.writeLine("listener called"));
    Bug6930MweJava("test", 
        (ActionEvent event) => process.writeLine("listener called"),
        (ActionEvent event) => process.writeLine("listener called"));
    Bug6930MweJava("test", listener);
    @error
    Bug6930MweJava("test", *listeners);
}