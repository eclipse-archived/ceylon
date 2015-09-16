function(f,t){
  asrt$(f>=0&&t>=0,"array index may not be negative",'Array.move','Array.ceylon');
  asrt$(f<this.length&&t<this.length,"array index must be less than size of array " + this.length,'Array.move','Array.ceylon');
  if (f===t)return;
  var x=this[f];
  if (f>t) {
    for (var i=f;i>t;i--) {
      this[i]=this[i-1];
    }
  } else {
    for (var i=f;i<t;i++) {
      this[i]=this[i+1];
    }
  }
  this[t] = x;
}
