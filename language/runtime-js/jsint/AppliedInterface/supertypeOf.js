function(t){
  if (is$(t,{t:AppliedUnionType$jsint}) || is$(t,{t:AppliedIntersectionType$jsint})) {
    return extendsType(t.tipo, {t:this.tipo});
  }
  return extendsType({t:t.tipo}, {t:this.tipo});
}
