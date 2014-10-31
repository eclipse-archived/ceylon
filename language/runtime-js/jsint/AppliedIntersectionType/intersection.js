function(t,$mpt){
  if (t.supertypeOf(this))return this;
  var _t={t:'i',l:[]};
  for (var i=0;i<this.tipo.l.length;i++)_t.l.push(this.tipo.l[i]);
  _t.l.push($mpt.Other$intersection);
  return AppliedIntersectionType$jsint(_t,this.satisfiedTypes.sequence().withTrailing(t,{Other$withTrailing:$mpt.Other$intersection}),
    {Union$AppliedIntersectionType:_t});
}
