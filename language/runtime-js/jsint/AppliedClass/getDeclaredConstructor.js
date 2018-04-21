function(nm,$mpt){
  var mm=getrtmm$$(this.tipo);
  var tname=mm.d[mm.d.length-1];
  if (reservedWords$.indexOf(tname)>=0) {
    tname='$_'+tname;
  }
  var fnOld=tname+"_"+(nm===''?'$c$':nm);
  var fnNew=tname+"$c_"+(nm===''?'$c$':nm);
  var cn=this.tipo[fnNew]||this.tipo[fnOld];
  if (cn) {
    mm=getrtmm$$(cn);
    if (mm.d[mm.d.length-2]==='$cn') {
      validate$params(mm.ps,$mpt.Arguments$getDeclaredConstructor,"Wrong number of Arguments for getConstructor");
      var args=mm.ps?tupleize$params(mm.ps,this.$targs):empty();
      var r=AppliedCallableConstructor$jsint(cn,{Type$AppliedCallableConstructor:this.$a$.Type$AppliedClass,
            Arguments$AppliedCallableConstructor:args},undefined,this.$targs);
      r.cont$=this;
      return r;
    }
  }
  return null;
}
