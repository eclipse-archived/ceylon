function annotatedMembers($$$mptypes){
  var ms=this.members({Kind$members:$$$mptypes.Kind$annotatedMembers});
  if (ms.size>0) {
    var rv=[];
    for (var i=0; i < ms.size; i++) {
      var _mem=ms.$_get(i);
      if (_mem.tipo && _mem.tipo.$crtmm$) {
        var mm=getrtmm$$(_mem.tipo);
        var ans=mm.$an;
        if (typeof(ans)==='function'){ans=ans();mm.$an=ans;}
        if (ans) for (var j=0; j<ans.length;j++) {
          if (is$(ans[j],$$$mptypes.Annotation$annotatedMembers)) {
            rv.push(_mem);
            break;
          }
        }
      }
    }
    return rv.length===0?getEmpty():ArraySequence(rv,{Element$ArraySequence:$$$mptypes.Kind$annotatedMembers});
  }
  return getEmpty();
}
