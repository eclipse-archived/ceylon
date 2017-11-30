if (this.cont$===undefined) {
  var cc=getrtmm$$(this.tipo).$cont;
  var mm=getrtmm$$(cc);
  var _t={t:cc};
  if (this.$targs)_t.a=this.$targs;
  this.cont$=AppliedClass$jsint(cc,{Type$AppliedClass:_t,Arguments$AppliedClass:{t:Nothing}},undefined,this.$targs);
}
return this.cont$;
