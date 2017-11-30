if (this.cont$===undefined) {
  var cc=getrtmm$$(this.tipo).$cont;
  var mm=getrtmm$$(cc);
  var _t={t:cc};
  if (this.$targs)_t.a=this.$targs;
  this.cont$=AppliedMemberClass$jsint(cc,{Type$AppliedMemberClass:_t,Arguments$AppliedMemberClass:{t:Nothing},
    Container$AppliedMemberClass:this.$$targs$$.Container$AppliedMemberClassValueConstructor},undefined,this.$targs);
}
return this.cont$;
