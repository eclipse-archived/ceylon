function addSuppressed(sup) {
    if (this.$sups$===undefined) {
        this.$sups$=[].rt$({t:Throwable});
    }
    if (sup.getT$name === undefined) sup = NativeException(sup);
    this.$sups$.push(sup);
}
