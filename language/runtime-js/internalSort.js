function internalSort(comp, elems, $$$mptypes) {
    if (elems===undefined) {return getEmpty();}
    var arr = [];
    var it = elems.iterator();
    var e;
    while ((e=it.next()) !== getFinished()) {arr.push(e);}
    if (arr.length === 0) {return getEmpty();}
    arr.sort(function(a, b) {
        var cmp = comp(a,b);
        return (cmp===getLarger()) ? 1 : ((cmp===getSmaller()) ? -1 : 0);
    });
    return ArraySequence(arr, {Element$Iterable:$$$mptypes.Element$internalSort});
}
