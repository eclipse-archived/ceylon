var tps=this._alias.$crtmm$.tp;
if (tps) {
  var rv=[];
  for (var tp in tps) {
    rv.push(OpenTypeParam$jsint(this, tp));
  }
  return rv.$sa$({t:TypeParameter$meta$declaration});
}
return empty();
