shared object bug6704NothingType satisfies Bug6704Type<Nothing> {
    shared actual Bug6704Type<Other> union<Other>(Bug6704Type<Other> type) => type;
}
