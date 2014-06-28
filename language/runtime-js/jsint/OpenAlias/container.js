var cont=this.$$targs$$.Container;
if (cont===undefined) {
  cont=this._alias.$crtmm$.$cont;
  if (cont)cont={t:cont};
}
if (cont) {
  return typeLiteral$meta({Type$typeLiteral:cont});
}
return null;
