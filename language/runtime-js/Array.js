function $_Array(elems,$$targs$$) {
    $init$$_Array();
    var e=[];
    if (!(elems === null || elems === undefined)) {
        var iter=elems.iterator();
        var item;while((item=iter.next())!==getFinished()) {
            e.push(item);
        }
    }
    e.$$targs$$=$$targs$$;
    var t=$$targs$$.Element$Array;
    List({Element$List:t}, e);
    return e;
}
