function $_Array$c_$c$(elems,$a$) {
    $i$$_Array();
    var e=[];
    if (!(elems === null || elems === undefined)) {
        var item;for(var iter=elems.iterator();(item=iter.next())!==finished();) {
            e.push(item);
        }
    }
    return $_Array$$c(e,$a$);
}
$_Array.inst$$=function(cm){
  return $_Array(undefined,cm.$a$.Type$Class.a);
}
$_Array.ser$set$=function(ref,o,i){
  if (typeof(ref)==='number') {
    o.arr$[ref]=i;
  }
}
$_Array.ser$get$=function(ref,o){
  if (is$(ref,{t:Element$serialization})) {
    return o.$_get(ref.index);
  }
  return o.size;
}
$_Array.ser$refs$=function(o){
  return [MemberImpl$impl(OpenValue$jsint(lmp$(x$,'$'),o.$prop$getSize))];
}
