function $_Array_OfSize(s,e,t$) {
  asrt$(s>=0 && s<=runtime().maxArraySize,
        "Invalid array size","0:0","Array.ceylon");
  $init$$_Array();
  var a=new Array(s);
  for (var i=0;i<s;i++) {
    a[i]=e;
  }
  return $_Array$$c(a,t$);
}
