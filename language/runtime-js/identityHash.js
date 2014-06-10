function identityHash(x) {
    var hash = x.BasicID;
    return (hash !== undefined)
            ? hash : (x.BasicID = BasicID++);
}
