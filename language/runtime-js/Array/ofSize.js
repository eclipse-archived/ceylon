function $_Array$c_ofSize(s,e,t$) {
  asrt$(s<=runtime().maxArraySize,
        "Invalid array size","0:0","Array.ceylon");
  $i$$_Array();
  if (s>0) {
    var a=new Array(s);
    for (var i=0;i<s;i++) {
      a[i]=e;
    }
  } else {
    var a=[];
  }
  return $_Array$$c(a,t$);
}
