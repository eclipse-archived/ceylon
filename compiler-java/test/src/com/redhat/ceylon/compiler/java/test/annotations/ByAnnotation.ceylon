@nomodel
class Authors(shared String[] authors) {
    shared actual String string => ", ".join(authors); 
}

@nomodel
annotation Authors by(String* authors) => Authors(authors);