var tp=getrtmm$$(this.container.tipo).tp[this._fname];
if (tp.sts) {
  var a=[];
  for (var i=0;i<tp.sts.length;i++) {
    a.push(_openTypeFromTarg(tp.sts[i],this.container.tipo));
  }
  return $arr$sa$(a,{t:OpenType$meta$declaration});
}
return empty();
