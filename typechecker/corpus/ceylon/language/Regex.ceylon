shared extension class Regex(Quoted this)
        satisfies Matcher<String> {

    doc "Return the substrings of the given string which
         match the parenthesized groups of the regex,
         ordered by the position of the opening parenthesis
         of the group."
    shared Match? matchList(String string)() { return ... }

    doc "Determine if the given string matches the regex."
    shared actual Boolean matches(String string) { return ... }

    //TODO finish

}