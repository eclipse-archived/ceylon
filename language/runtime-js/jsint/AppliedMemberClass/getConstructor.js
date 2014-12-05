function(nm,$mpt){
  var mm=getrtmm$$(this.tipo);
  var fn=mm.d[mm.d.length-1]+"_"+nm;
  var cn=this.tipo[fn];
  if (cn) {
    mm=getrtmm$$(cn);
    if (mm.d[mm.d.length-2]==='$cn') {
      var args=mm.ps?tupleize$params(mm.ps,this.$targs):getEmpty();
      return AppliedMemberConstructor(cn,
             {Type$AppliedMemberConstructor:this.$$targs$$.Type$AppliedMemberClass,
              Container$AppliedMemberConstructor:this.$$targs$$.Container$AppliedMemberClass,
              Arguments$AppliedMemberConstructor:args},undefined,this.$targs);
    }
  }
  return null;
}
