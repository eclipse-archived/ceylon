var tps=this._alias.$crtmm$.tp;
if (tps) {
  var rv=[];
  for (var tp in tps) {
    rv.push(OpenTypeParam$jsint(this, tp));
  }
  var targ={t:TypeParameter$meta$declaration};
  return rv.length===0?empty():ArraySequence($arr$(rv,targ),{Element$ArraySequence:targ});
}
return empty();
