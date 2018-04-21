var tps=this._alias.$m$.tp;
if (tps) {
  var rv=[];
  for (var tp in tps) {
    rv.push(OpenTypeParam$jsint(this, tp));
  }
  return $arr$sa$(rv,{t:TypeParameter$meta$declaration});
}
return empty();
