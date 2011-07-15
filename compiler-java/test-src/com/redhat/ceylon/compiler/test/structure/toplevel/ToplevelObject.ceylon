@nomodel
object x {
    shared void y() {
    }
}

@nomodel
void accessX() {
    x.y();
}