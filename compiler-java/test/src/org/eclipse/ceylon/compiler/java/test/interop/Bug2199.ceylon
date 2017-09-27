import java.lang {
    StringBuilder,
    JCharacter=Character
}

@noanno
shared void bug2199(Integer codepoint) {
    String s = JCharacter.UnicodeScript.\iof(codepoint).name();
}