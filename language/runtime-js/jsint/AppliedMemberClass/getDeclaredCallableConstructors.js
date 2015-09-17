function(anntypes,$m) {
  var mm=getrtmm$$(this.tipo);
  //Member Type Name
  var mtn=mm.d[mm.d.length-1];
  if (mtn.indexOf('$')>0)mtn=mtn.substring(0,mtn.indexOf('$'));
  var startingType=this.container;
  while (is$(startingType,{t:ClassOrInterface$meta$model})) {
    var pn=startingType.declaration.name;
    mtn+='$'+startingType.declaration.name;
    startingType=startingType.container;
  }
  var pref=mtn+"_";
  var ats=coi$get$anns(anntypes);
  var cs=[]
  for (var cn in this.tipo) {
    if (cn.startsWith(pref)) {
      mm=getrtmm$$(this.tipo[cn]);
      if (mm.d[mm.d.length-2]==='$cn' && coi$is$anns(allann$(mm),ats)) {
        var parms=mm.ps;
        if (parms && $m.Arguments$getDeclaredCallableConstructors.t!==Nothing && this.$$targs$$.Type$AppliedClass.a) {
          for (var i=0;i<parms.length;i++) {
            parms[i] = {$t:restype$(this.$$targs$$.Type$AppliedClass,parms[i].$t)};
          }
        }
        if (validate$params(parms,$m.Arguments$getDeclaredCallableConstructors,"",1)) {
          var args=mm.ps?tupleize$params(parms,this.$targs):empty();
          cs.push(AppliedMemberClassCallableConstructor$jsint(this.tipo[cn],
               {Type$AppliedMemberClassCallableConstructor:this.$$targs$$.Type$AppliedMemberClass,
                Container$AppliedMemberClassCallableConstructor:this.$$targs$$.Container$AppliedMemberClass,
                Arguments$AppliedMemberClassCallableConstructor:args},undefined,this.$targs));
        }
      }
    }
  }
  return cs.length===0?empty():ArraySequence(cs,{Element$ArraySequence:{t:MemberClassCallableConstructor$meta$model,
    a:{Type$MemberClassCallableConstructor:this.$$targs$$.Type$AppliedClass,Arguments$MemberClassCallableConstructor:{t:Nothing},
       Container$MemberClassCallableConstructor:this.$$targs$$.Container$AppliedMemberClass}}});
}
