var s="";
var first=true;
for (var i=0;i<this.satisfiedTypes.size;i++) {
  if (first)first=false;else s+="&";
  s+=this.satisfiedTypes.$_get(i).string;
}
return s;
