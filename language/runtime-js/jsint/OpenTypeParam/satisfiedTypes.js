var tp=getrtmm$$(this.container).$tp[this._fname];
if (tp.satisfies) {
  var a=[];
  for (var i=0;i<tp.satisfies.length;i++) {
    a.push(_openTypeFromTarg(tp.satisfies[i],this.container));
  }
  return a.length===0?getEmpty():ArraySequence(a,{Element$ArraySequence:{t:OpenType$meta$declaration}});
}
return getEmpty();
