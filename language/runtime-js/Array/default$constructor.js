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
$_Array.ser$set$=function(ref,o,i){
  if (typeof(ref)==='number') {
    o[ref]=i;
  }
}
$_Array.ser$get$=function(ref,o){
  if (is$(ref,{t:Element$serialization})) {
    return o.$_get(ref.index);
  }
  return o.size;
}
$_Array.ser$refs$=function(o){
  return [MemberImpl$impl(OpenValue$jsint(lmp$(ex$,'$'),o.$prop$getSize))];
}
