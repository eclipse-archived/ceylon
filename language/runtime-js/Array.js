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
$_Array.deser$$=function(a){
  var targ={t:a.getTypeArgument(OpenTypeParam$jsint($_Array,'Element$Array')).tipo};
  if (targ.t==='i'||targ.t==='u'||targ.t==='T')targ=targ.t;
  var tam=a.getValue(OpenValue$jsint(lmp$(ex$,'$'),$_Array.$$.prototype.$prop$getSize),{Instance$getValue:{t:Integer}});
  var b = new Array(tam);
  for (var i=0; i < tam; i++) {
    b[i]=a.getElement(i,{Instance$getElement:targ});
  }
  return b.rt$(targ);
}
