function $_Array(elems,$$targs$$) {
    $init$$_Array();
    var e=[];
    if (!(elems === null || elems === undefined)) {
        var item;for(var iter=elems.iterator();(item=iter.next())!==finished();) {
            e.push(item);
        }
    }
    var t=$$targs$$.Element$Array;
    List({Element$List:t}, e);
    return e.rt$(t);
}
$_Array.inst$$=function(cm){
  return $_Array(undefined,cm.$$targs$$.Type$Class.a);
}
$_Array.deser$$=function(a,cm,b){
  var tam=a.getValue(OpenValue$jsint(lmp$(ex$,'$'),$_Array.$$.prototype.$prop$getSize),{Instance$getValue:{t:Integer}});
  for (var i=0; i < tam; i++) {
    var e=a.getElement(i,{Instance$getElement:targ});
    b.push(is$(e,{t:Reference$serialization})?e.leak():e);
  }
}
