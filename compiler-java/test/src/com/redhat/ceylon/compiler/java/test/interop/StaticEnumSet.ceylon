import java.util{EnumSet}
import java.lang{Thread{State}}
import ceylon.interop.java{javaClass}

void staticEnumSet() {
    EnumSet<State> s = EnumSet<State>.noneOf(javaClass<State>());
}