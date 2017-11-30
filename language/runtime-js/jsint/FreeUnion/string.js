var s="";
var first=true;
for (var i=0;i<this.caseTypes.size;i++) {
  if (first)first=false;else s+="|";
  s+=this.caseTypes.$_get(i).string;
}
return s;
