function(a,b){
  asrt$(a>=0&&b>=0,'array index may not be negative','0:0','Array.ceylon');
  asrt$(a<this.arr$.length&&b<this.arr$.length,'array index must be less than size of array ' + this.arr$.length,'0:0','Array.ceylon');
  var c=this.arr$[a];
  this.arr$[a]=this.arr$[b];
  this.arr$[b]=c;
}
