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
  return [];
}
$_Array.deser$$=function(a,cm,b){
  var targ=cm.$$targs$$.Type$Class.a.Element$Array;
  var tam=a.getValue(OpenValue$jsint(lmp$(ex$,'$'),$_Array.$$.prototype.$prop$getSize),{Instance$getValue:{t:Integer}});
  for (var i=0; i < tam; i++) {
    b.push(a.getElement(i,{Instance$getElement:targ}));
    if (is$(b[i],{t:Reference$serialization}))b[i]=b[i].leak();
  }
  b.rt$(targ);
}
