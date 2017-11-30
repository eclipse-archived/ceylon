function(anntypes){
  var mm=getrtmm$$(this.tipo);
  var pref=this.tipo.toString().substring(9);
  pref=pref.substring(0,pref.indexOf('('));
  var cs=coiclsannconstrs$(this,anntypes,pref+"$c_",this.$$targs$$.Type$AppliedClass);
  if (cs.length===0) {
    cs=coiclsannconstrs$(this,anntypes,pref+"_",this.$$targs$$.Type$AppliedClass);
  }
  for (var i=0;i<cs.length;i++) {
    var r=AppliedValueConstructor$jsint(cs[i].tipo,{Type$AppliedValueConstructor:this.$$targs$$.Type$AppliedClass});
    r.cont$=this;
    cs[i]=r;
  }
  return $arr$sa$(cs,{t:ValueConstructor$meta$model,
    a:{Type$ValueConstructor:this.$$targs$$.Type$AppliedClass}});
}
