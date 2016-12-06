var uno6740 = {
};
var dos6740 = {
  uno:uno6740
};
uno6740.dos=dos6740;
var tres6740={
  hijos:[]
}
for (var i=0;i<500;i++) {
  tres6740.hijos[i]={uno:tres6740};
}

function dyn6810() {
  var tot=0;
  for (var i=0;i<arguments.length;i++) {
    tot+=arguments[i];
  }
  return tot;
}
