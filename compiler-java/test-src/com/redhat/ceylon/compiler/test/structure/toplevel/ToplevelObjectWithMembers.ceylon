@nomodel
object x {
    Boolean val = true;
    shared Boolean valShared = true;
    Boolean valCaptured = true;
    variable Boolean var := false;
    shared variable Boolean varShared := false;
    variable Boolean varCaptured := false;
    Boolean y() { return valCaptured; }
    shared Boolean yShared() { return varCaptured; }
}
