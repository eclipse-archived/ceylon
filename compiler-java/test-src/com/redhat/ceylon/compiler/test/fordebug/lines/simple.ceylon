String name(String firstName, String lastName) {
    return "My name is " firstName " " lastName ". Hello !";
}

shared class Main2() {  
    String firstName = "David";
    String lastName = "Festal";
    process.writeLine(name(firstName, lastName));
}