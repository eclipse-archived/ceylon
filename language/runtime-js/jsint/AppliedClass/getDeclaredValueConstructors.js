function(anntypes){
  var mm=getrtmm$$(this.tipo);
  var pref=mm.d[mm.d.length-1]+"_";
  var cs=coiclsannconstrs$(this,anntypes,pref,this.$$targs$$.Type$AppliedClass);
  for (var i=0;i<cs.length;i++) {
    var r=AppliedValueConstructor$jsint(cs[i].tipo,{Type$AppliedValueConstructor:this.$$targs$$.Type$AppliedClass});
    r.cont$=this;
    cs[i]=r;
  }
  return cs.$sa$({t:ValueConstructor$meta$model,
    a:{Type$ValueConstructor:this.$$targs$$.Type$AppliedClass}});
}
