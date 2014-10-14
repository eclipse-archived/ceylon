function(t,$mpt){
  if (this.subtypeOf(t))return this;
  return AppliedIntersectionType$jsint(this.tipo,this.satisfiedTypes.sequence().withTrailing(t,{Other$withTrailing:$mpt.Other$intersection}));
}
