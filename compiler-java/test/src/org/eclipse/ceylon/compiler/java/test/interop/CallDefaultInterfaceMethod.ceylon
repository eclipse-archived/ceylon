import java.lang{JIterable=Iterable}

@noanno
void callDefaultInterfaceMethod(JIterable<Anything> iterable) {
    iterable.forEach(nothing);
}