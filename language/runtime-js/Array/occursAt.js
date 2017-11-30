function(i,e){
  if (i<0||i>=this.arr$.length)return false;
  if (e===null)return this.arr$[i]===null;
  return this.arr$[i]!==null && $eq$(this.arr$[i],e);
}
