function(name$2){
var tp=this._alias.$crtmm$;
var tpn=undefined;
if (tp.tp) {
  for (var ftn in tp.tp) {
    if (ftn.substring(0,name$2.size+1)==name$2+'$') {
      tpn=ftn;
    }
  }
}
return tpn?OpenTypeParam$jsint(this, tpn):null;
}
