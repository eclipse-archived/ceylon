function typeOf(instance$20){
  var tipos=[];
  for (var i=0; i < this.caseTypes.size;i++) {
    var _t = this.caseTypes.$_get(i);
    if (_t.tipo) {
      _t={t:_t.tipo};
    } else if (_t.t===undefined) {
    _t={t:_t};
    }
    tipos.push(_t);
  }
  return is$(instance$20,{t:'u',l:tipos});
}
