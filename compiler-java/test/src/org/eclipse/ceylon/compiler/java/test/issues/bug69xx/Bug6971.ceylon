import java.lang { CharSequence }

@noanno
void bug6971() {
    String? maybeString1 = Bug6971Java.getStringOrNull(1);
    value maybeString2 = Bug6971Java.getStringOrNull(1);
    String maybeString3 = Bug6971Java.getStringOrNull(0);
    Bug6971Java.soWithCharSequence(maybeString1);
    Bug6971Java.soWithCharSequence(maybeString2);
    Bug6971Java.soWithCharSequence(maybeString3);
}