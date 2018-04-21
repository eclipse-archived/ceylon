function(nm,$m){
  var mm=getrtmm$$(this.tipo);
  //Member Type Name
  var mtn=mm.d[mm.d.length-1];
  if (mtn.indexOf('$')>0)mtn=mtn.substring(0,mtn.indexOf('$'));
  if (nm==='')nm='$c$';
  var startingType=this.container;
  while (is$(startingType,{t:ClassOrInterface$meta$model})) {
    var pn=startingType.declaration.name;
    mtn+='$'+startingType.declaration.name;
    startingType=startingType.container;
  }
  var fnOld=mtn+"_"+nm;
  var fnNew=mtn+"$c_"+nm;
  var cn=this.tipo[fnNew]||this.tipo[fnOld];
  if (cn) {
    mm=getrtmm$$(cn);
    if (mm.d[mm.d.length-2]==='$cn' && mm.ps!==undefined) {
      var args=mm.ps?tupleize$params(mm.ps,this.$targs):empty();
      return AppliedMemberClassCallableConstructor$jsint(cn,
             {Type$AppliedMemberClassCallableConstructor:this.$a$.Type$AppliedMemberClass,
              Container$AppliedMemberClassCallableConstructor:this.$a$.Container$AppliedMemberClass,
              Arguments$AppliedMemberClassCallableConstructor:args},undefined,this.$targs);
    }
  }
  return null;
}
