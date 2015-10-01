if (this.$defcons$===undefined) {
  var mm=getrtmm$$(this.tipo);
  //Member Type Name
  var mtn=mm.d[mm.d.length-1];
  if (mtn.indexOf('$')>0)mtn=mtn.substring(0,mtn.indexOf('$'));
  var startingType=this.container;
  while (is$(startingType,{t:Class$meta$model})) {
    var pn=startingType.declaration.name;
    mtn+='$'+startingType.declaration.name;
    startingType=startingType.container;
  }
  var fn=mtn+'_$c$';
  var cn=this.tipo[fn];
  var fc=false;
  if (!cn) {
    var m2=get_model(mm);
    if (m2 && m2.$cn===undefined) {
      cn=this.tipo;
      fc=true;
    }
  }
  if (cn) {
    mm=getrtmm$$(cn).ps;
    var args=tupleize$params(getrtmm$$(cn).ps,this.$$targs$$.Type$AppliedMemberClass.a);
    var r=AppliedMemberClassCallableConstructor$jsint(cn,
          {Type$AppliedMemberClassCallableConstructor:this.$$targs$$.Type$AppliedMemberClass,
           Container$AppliedMemberClassCallableConstructor:this.$$targs$$.Container$AppliedMemberClass,
           Arguments$AppliedMemberClassCallableConstructor:args},undefined,this.$targs);
    r.cont$=this;
    this.$defcons$=r;
    if(fc)r.fakeConstr$=true;
    return r;
  }
  this.$defcons$=null;
}
return this.$defcons$;
