var cont=this.$a$.Container;
if (cont===undefined) {
  cont=this._alias.$m$.$cont;
  if (cont)cont={t:cont};
}
if (cont) {
  return typeLiteral$meta({Type$typeLiteral:cont});
}
return null;
