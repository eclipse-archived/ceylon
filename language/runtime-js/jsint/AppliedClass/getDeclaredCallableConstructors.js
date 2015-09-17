function(anntypes,$m) {
  var mm=getrtmm$$(this.tipo);
  var pref=mm.d[mm.d.length-1]+"_";
  var cs=[];
  var ats=coi$get$anns(anntypes);
  for (var cn in this.tipo) {
    if (cn.startsWith(pref)) {
      mm=getrtmm$$(this.tipo[cn]);
      if (mm.d[mm.d.length-2]==='$cn' && coi$is$anns(allann$(mm),ats)
          && validate$params(mm.ps,$m.Arguments$getDeclaredCallableConstructors,"",1)) {
        var args=mm.ps?tupleize$params(mm.ps,this.$targs):empty();
        var r=AppliedCallableConstructor$jsint(this.tipo[cn],{Type$AppliedCallableConstructor:this.$$targs$$.Type$AppliedClass,
              Arguments$AppliedCallableConstructor:args},undefined,this.$targs);
        r.cont$=this;
        cs.push(r);
      }
    }
  }
  return cs.length===0?empty():ArraySequence(cs,{Element$ArraySequence:{t:CallableConstructor$meta$model,
    a:{Type$CallableConstructor:this.$$targs$$.Type$AppliedClass,Arguments$CallableConstructor:{t:Nothing}}}});
}
