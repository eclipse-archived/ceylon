function (i,e) {
  //TODO: handle codepoints correctly
  return is$(e, {t:Character}) && this.charCodeAt(i)==e.integer;
}