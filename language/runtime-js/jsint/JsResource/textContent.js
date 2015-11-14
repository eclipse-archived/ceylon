function(encoding$2){
  var $elf=this;
  if(encoding$2===undefined){encoding$2=$elf.textContent$defs$encoding(encoding$2);}
  if (runtime().name === 'node.js') {
    var _fr=require;
    var fs=_fr('fs');
    encoding$2=encoding$2.toLowerCase();
    if(encoding$2.initial(4)==="utf-")encoding$2='utf'+encoding$2.substring(4);
    var _fp=$elf.uri;
    if (_fp.substring(0,5)==='file:')_fp=_fp.substring(_fp.indexOf(':')+1);
    var t = fs.readFileSync(_fp, encoding$2);
    return $_String(t);
  } else if (runtime().name === 'Browser') {
    alert("Resource.textContent() not implemented yet");
  } else {
    throw Error("Resource handling unsupported in this JS platform");
  }
}
