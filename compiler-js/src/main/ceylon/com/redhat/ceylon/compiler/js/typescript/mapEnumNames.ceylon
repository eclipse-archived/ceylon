import ceylon.collection {
    HashSet,
    MutableSet
}

"Usage: Call `mapEnumNames()` once per enum and then use the returned function for each member."
String mapEnumNames(MutableSet<String> usedNames = HashSet<String>())(String name) {
    // Ceylon port of NamingBase.getJavaBeanName
    Array<Character> chars = Array(name);
    for (i->char in chars.indexed) {
        if (!char.uppercase) {
            if (i > 1) {
                assert (exists previousChar = chars[i-1]);
                chars[i-1] = previousChar.uppercased;
            }
            break;
        }
        chars[i] = char.lowercased;
    }
    for (potentialName in { String(chars), name }.chain((1..runtime.maxIntegerValue).map(Object.string).map(name.plus))) {
        if (!potentialName in usedNames) {
            usedNames.add(potentialName);
            return potentialName;
        }
    }
    throw AssertionError("Cannot find free name for ``name``");
}
