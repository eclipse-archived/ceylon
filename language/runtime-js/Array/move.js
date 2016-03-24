function(f,t){
  asrt$(f>=0&&t>=0,"array index may not be negative",'Array.move','Array.ceylon');
  asrt$(f<this.arr$.length&&t<this.arr$.length,"array index must be less than size of array " + this.arr$.length,'Array.move','Array.ceylon');
  if (f===t)return;
  var x=this.arr$[f];
  if (f>t) {
    for (var i=f;i>t;i--) {
      this.arr$[i]=this.arr$[i-1];
    }
  } else {
    for (var i=f;i<t;i++) {
      this.arr$[i]=this.arr$[i+1];
    }
  }
  this.arr$[t] = x;
}
