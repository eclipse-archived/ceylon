function (e) {
  return is$(e, {t:Character}) && this.indexOf(e.string)>=0;
}