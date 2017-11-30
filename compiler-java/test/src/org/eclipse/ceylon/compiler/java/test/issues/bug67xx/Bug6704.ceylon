shared interface Bug6704Type<out Target> {
    shared formal Bug6704Type<Target|Other> union<Other>(Bug6704Type<Other> other);
}
