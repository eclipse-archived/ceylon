var qn="";
var first=true;
for (var i=0;i<this.satisfiedTypes.size;i++) {
  if (first)first=false;else qn+="&";
  qn+=this.satisfiedTypes.$_get(i).string;
}
return qn;
