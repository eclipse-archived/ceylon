function(t,$mpt){
  if (this.supertypeOf(t))return this;
  var _t={t:'u',l:this.tipo.l.slice(0)};
  _t.l.push(t.$a$.Target$Type);
  return AppliedUnionType$jsint(_t,this.caseTypes.sequence().withTrailing(t,{Other$withTrailing:t.$a$.Target$Type}),
    {Union$AppliedUnionType:_t});
}
