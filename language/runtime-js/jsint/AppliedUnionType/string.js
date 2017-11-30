var qn="";
var first=true;
for (var i=0;i<this.caseTypes.size;i++) {
  if (first)first=false;else qn+="|";
  qn+=this.caseTypes.$_get(i).string;
}
return qn;
