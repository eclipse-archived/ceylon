function(a,b){
  asrt$(a>=0&&b>=0,'array index may not be negative','0:0','Array.ceylon');
  asrt$(a<this.length&&b<this.length,'array index must be less than size of array ' + this.length,'0:0','Array.ceylon');
  var c=this[a];
  this[a]=this[b];
  this[b]=c;
}
