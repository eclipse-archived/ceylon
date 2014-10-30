function(t,$mpt){
  if (this.supertypeOf(t))return this;
  var _t={t:'u',l:[]};
  for (var i=0; i<this.tipo.l.length;i++)_t.l.push(this.tipo.l[i]);
  _t.l.push($mpt.Other$union);
  return AppliedUnionType$jsint(this.tipo,this.caseTypes.sequence().withTrailing(t,{Other$withTrailing:$mpt.Other$union}),
    {Union$AppliedUnionType:_t});
}
