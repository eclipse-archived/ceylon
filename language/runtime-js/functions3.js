function addSuppressedException(/*Exception*/sup,/*Exception*/e) {
    if (e.$sups$===undefined) {
        e.$sups$=[];
    }
    if (sup.getT$name === undefined) sup = NativeException(sup);
    e.$sups$.push(sup);
}
exports.addSuppressedException=addSuppressedException;
function suppressedExceptions(/*Exception*/e) {
    return e.$sups$===undefined?getEmpty():e.$sups$;
}
exports.suppressedExceptions=suppressedExceptions;
