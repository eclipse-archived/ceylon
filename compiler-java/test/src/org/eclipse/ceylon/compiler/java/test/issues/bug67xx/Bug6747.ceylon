import java.lang {
    ObjectArray,
    JString = String
}

@noanno
shared void bug6747() {
    Anything(ObjectArray<JString>) cons = nothing;
    Bug6747Java.load(2, -1, "", cons);
}