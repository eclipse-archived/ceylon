shared sealed interface Wrapper<Wrapped> {
    shared formal Wrapped wrapped1;
    shared formal Wrapped wrapped2;
}

shared interface ProgressMonitor<NativeMonitor> {
    shared formal class Progress()
            satisfies Wrapper<NativeMonitor> {}
}

shared abstract class ProgressMonitorImpl<Monitor>()
        satisfies ProgressMonitor<Monitor> 
        & Wrapper<Monitor> {
    shared default actual class Progress()
            extends super.Progress() {
        wrapped1 => outer.wrapped1;
        shared actual Monitor wrapped2 => outer.wrapped2;
    }
}
