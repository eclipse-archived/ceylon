function(nm,$mpt){
  var mm=getrtmm$$(this.tipo);
  var fn=mm.d[mm.d.length-1]+"_"+nm;
  var cn=this.tipo[fn];
  if (cn) {
    mm=getrtmm$$(cn);
    if (mm.d[mm.d.length-2]==='$cn') {
      var args=mm.ps?tupleize$params(mm.ps,this.$targs):getEmpty();
      return AppliedConstructor(cn,{Type$AppliedConstructor:this.$$targs$$.Type$AppliedClass,
                                Arguments$AppliedConstructor:args},undefined,this.$targs);
    }
  }
  return null;
}
