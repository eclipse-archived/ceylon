function annotatedMembers($$$mptypes){
  var ms=this.members({Kind$members:$$$mptypes.Kind$annotatedMembers});
  if (ms.size>0) {
    var rv=[];
    var mask = getAnnotationBitmask($$$mptypes.Annotation$annotatedMembers);
    for (var i=0; i < ms.size; i++) {
      var _mem=ms.$_get(i);
      if (_mem.tipo && _mem.tipo.$crtmm$) {
        var mm=getrtmm$$(_mem.tipo);
        if (mm.pa&mask) {
          rv.push(_mem);
          continue;
        }
        var ans=mm.an;
        if (typeof(ans)==='function'){ans=ans();mm.an=ans;}
        if (ans) for (var j=0; j<ans.length;j++) {
          if (is$(ans[j],$$$mptypes.Annotation$annotatedMembers)) {
            rv.push(_mem);
            break;
          }
        }
      }
    }
    return rv.$sa$($$$mptypes.Kind$annotatedMembers);
  }
  return empty();
}
