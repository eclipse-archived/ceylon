var $elf=this;
if (runtime().name === 'node.js') {
  var _fr=require;
  var _fp=$elf.uri;
  if (_fp.substring(0,5)==='file:')_fp=_fp.substring(_fp.indexOf(':')+1);
  return _fr('fs').statSync(_fp).size;
} else if (runtime().name === 'Browser') {
  alert('Resource.size not implemented yet');
} else {
  print("Resource handling unsupported in this JS platform.");
}
return -1;
