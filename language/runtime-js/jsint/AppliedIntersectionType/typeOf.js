function typeOf(instance$23){
  var tipos=[];
  for (var i=0; i < this.satisfiedTypes.size;i++) {
    var _t = this.satisfiedTypes.$_get(i);
    if (_t.tipo) {
      _t={t:_t.tipo};
    } else if (_t.t===undefined) {
    _t={t:_t};
    }
    tipos.push(_t);
  }
  return is$(instance$23,{t:'i',l:tipos});
}
