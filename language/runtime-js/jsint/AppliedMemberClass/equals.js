function(o){
  var eq=is$(o,{t:AppliedMemberClass$jsint}) && o.tipo===tipo;
  if (that.$bound)eq=eq && o.$bound && o.$bound.equals(that.$bound);else eq=eq && o.$bound===undefined;
  return eq && this.typeArguments.equals(o.typeArguments);
}
