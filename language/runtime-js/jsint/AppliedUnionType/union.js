function(t,$mpt){
  if (this.supertypeOf(t))return this;
  return AppliedUnionType$jsint(this.tipo,this.caseTypes.sequence().withTrailing(t,{Other$withTrailing:$mpt.Other$Union}));
}
