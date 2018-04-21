function(anntypes,$m) {
  var mm=getrtmm$$(this.tipo);
  var pref=this.tipo.toString().substring(9);
  pref=pref.substring(0,pref.indexOf('('));
  var cs=coiclsannconstrs$(this,anntypes,pref+"$c_",this.$a$.Type$AppliedClass,$m.Arguments$getDeclaredCallableConstructors);
  if (cs.length===0) {
    cs=coiclsannconstrs$(this,anntypes,pref+"_",this.$a$.Type$AppliedClass,$m.Arguments$getDeclaredCallableConstructors);
  }
  for (var i=0;i<cs.length;i++) {
    var r=AppliedCallableConstructor$jsint(cs[i].tipo,{Type$AppliedCallableConstructor:this.$a$.Type$AppliedClass,
                Arguments$AppliedCallableConstructor:cs[i].args},undefined,this.$targs);
    r.cont$=this;
    if (cs[i].fakeConstr)r.fakeConstr$=true;
    cs[i]=r;
  }
  return $arr$sa$(cs,{t:CallableConstructor$meta$model,
    a:{Type$CallableConstructor:this.$a$.Type$AppliedClass,Arguments$CallableConstructor:{t:Nothing}}});
}
