doc "Return a sequence containing the given values which are
     not null. If there are no values which are not null,
     return an empty sequence."
shared Iterable<Element&Object> coalesce<Element>(Element... values) {
    return values.coalesced;
}
