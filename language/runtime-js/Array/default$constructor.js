function $_Array_$c$(elems,$$targs$$) {
    $init$$_Array();
    var e=[];
    if (!(elems === null || elems === undefined)) {
        var item;for(var iter=elems.iterator();(item=iter.next())!==finished();) {
            e.push(item);
        }
    }
    return $_Array$$c(e,$$targs$$);
}
$_Array.inst$$=function(cm){
  return $_Array(undefined,cm.$$targs$$.Type$Class.a);
}
$_Array.deser$$=function(a,cm,b){
  var tam=a.getValue(OpenValue$jsint(lmp$(ex$,'$'),$_Array.$$.prototype.$prop$getSize),{Instance$getValue:{t:Integer}});
  var elemtarg={Instance$getElement:b.$$targs$$.Element$Array};
  for (var i=0; i < tam; i++) {
    var e=a.getElement(i,elemtarg);
    b.push(is$(e,{t:Reference$serialization})?e.leak():e);
  }
}
