"Initial lowercase form of [[name]]."
throws (`class AssertionError`, "If [[name]] is empty.")
String initLCase(String name) {
    assert (exists first = name.first);
    return first.lowercased.string + name.rest;
}

"Initial uppercase form of [[name]]."
throws (`class AssertionError`, "If [[name]] is empty.")
String initUCase(String name) {
    assert (exists first = name.first);
    return first.uppercased.string + name.rest;
}
