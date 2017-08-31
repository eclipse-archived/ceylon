var $elf=this;
var _p=$elf.path_;
var key = _p.substring(1).replace(/\/|\./g,"$$$$");
var str = $elf.mod_[key];
if (str!=null) {
  // byte length of utf8 string
  var s = str.length;
  for (var i=str.length-1; i>=0; i--) {
    var code = str.charCodeAt(i);
    if (code > 0x7f && code <= 0x7ff) s++;
    else if (code > 0x7ff && code <= 0xffff) s+=2;
    if (code >= 0xDC00 && code <= 0xDFFF) i--;
  }
  return s;
}
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
