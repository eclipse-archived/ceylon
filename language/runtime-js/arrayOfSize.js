function arrayOfSize(size, elem, $$$mptypes) {
    var elems = [];
    if (size>0) {
        for (var i=0; i<size; i++) {
            elems.push(elem);
        }
    }
    return elems.reifyCeylonType($$$mptypes.Element$arrayOfSize);
}
