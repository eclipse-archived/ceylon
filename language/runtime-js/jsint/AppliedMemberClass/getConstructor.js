function(nm,$mpt){
  var mm=getrtmm$$(this.tipo);
  //Member Type Name
  var mtn=mm.d[mm.d.length-1];
  if (mtn.indexOf('$')>0)mtn=mtn.substring(0,mtn.indexOf('$'));
  if (nm==='')nm='$c$';
  var startingType=this.container;
  while (is$(startingType,{t:Class$meta$model})) {
    var pn=startingType.declaration.name;
    mtn+='$'+startingType.declaration.name;
    startingType=startingType.container;
  }
  var fn=mtn+"_"+nm;
  var cn=this.tipo[fn];
  if (cn) {
    mm=getrtmm$$(cn);
    if (mm.d[mm.d.length-2]==='$cn') {
      var args=mm.ps?tupleize$params(mm.ps,this.$targs):empty();
      return AppliedMemberClassCallableConstructor$jsint(cn,
             {Type$AppliedMemberClassCallableConstructor:this.$$targs$$.Type$AppliedMemberClass,
              Container$AppliedMemberClassCallableConstructor:this.$$targs$$.Container$AppliedMemberClass,
              Arguments$AppliedMemberClassCallableConstructor:args},undefined,this.$targs);
    }
  }
  return null;
}
