function(nm,$mpt){
  var mm=getrtmm$$(this.tipo);
  var fn=mm.d[mm.d.length-1]+"_"+(nm===''?'$c$':nm);
  var cn=this.tipo[fn];
  if (cn) {
    mm=getrtmm$$(cn);
    if (mm.d[mm.d.length-2]==='$cn') {
      validate$params(mm.ps,$mpt.Arguments$getConstructor,"Wrong number of Arguments for getConstructor");
      var args=mm.ps?tupleize$params(mm.ps,this.$targs):empty();
      return AppliedConstructor$jsint(cn,{Type$AppliedConstructor:this.$$targs$$.Type$AppliedClass,
                                Arguments$AppliedConstructor:args},undefined,this.$targs);
    }
  }
  return null;
}
