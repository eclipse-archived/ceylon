function cmpSubString(str, subStr, offset) {
    for (var i=0; i<subStr.length; ++i) {
        if (str.charCodeAt(offset+i)!==subStr.charCodeAt(i)) {return false}
    }
    return true;
}
function StringIterator(string) {
    var that = new StringIterator.$$;
    that.str = string;
    that.index = 0;
    return that;
}
StringIterator.$crtmm$=function(){return{nm:'StringIterator',mt:'c',ps:[{nm:'string',$t:{t:$_String}}],pa:1,mod:$CCMM$,d:['$','Iterator']}};

initTypeProto(StringIterator, 'ceylon.language::StringIterator', $init$Basic(), Iterator);
var StringIterator$proto = StringIterator.$$.prototype;
StringIterator$proto.$$targs$$={Element$Iterator:{t:Character}, Absent$Iterator:{t:Null}};
StringIterator$proto.next = function() {
    if (this.index >= this.str.length) { return finished(); }
    var first = this.str.charCodeAt(this.index++);
    if ((first&0xfc00) !== 0xd800 || this.index >= this.str.length) {
        return Character(first);
    }
    return Character((first<<10) + this.str.charCodeAt(this.index++) - 0x35fdc00);
}

function countCodepoints(str) {
    var count = 0;
    for (var i=0; i<str.length; ++i) {
        ++count;
        if ((str.charCodeAt(i)&0xfc00) === 0xd800) {++i}
    }
    return count;
}
function codepointToString(cp) {
    if (cp <= 0xffff) {
        return String.fromCharCode(cp);
    }
    return String.fromCharCode((cp>>10)+0xd7c0, (cp&0x3ff)+0xdc00);
}
function codepointFromString(str, index) {
    var first = str.charCodeAt(index);
    if ((first&0xfc00) !== 0xd800) {return first}
    var second = str.charCodeAt(index+1);
    return isNaN(second) ? first : ((first<<10) + second - 0x35fdc00);
}
ex$.codepointFromString=codepointFromString;

function stringToUtf16(str) {
  var out = [], p = 0;
  out[p++] = 0xFE;
  out[p++] = 0xFF;
  for (var i = 0; i < str.length; i++) {
    var c = str.charCodeAt(i);
    out[p++] = (c & 0xFF00) >>> 8;
    out[p++] = c & 0xff;
  }
  return out;
}

function stringToUtf8(str) {
  var out = [], p = 0;
  for (var i = 0; i < str.length; i++) {
    var c = str.charCodeAt(i);
    if (c < 128) {
      out[p++] = c;
    } else if (c < 2048) {
      out[p++] = (c >> 6) | 192;
      out[p++] = (c & 63) | 128;
    } else if (
        ((c & 0xFC00) == 0xD800) && (i + 1) < str.length &&
        ((str.charCodeAt(i + 1) & 0xFC00) == 0xDC00)) {
      // Surrogate Pair
      c = 0x10000 + ((c & 0x03FF) << 10) + (str.charCodeAt(++i) & 0x03FF);
      out[p++] = (c >> 18) | 240;
      out[p++] = ((c >> 12) & 63) | 128;
      out[p++] = ((c >> 6) & 63) | 128;
      out[p++] = (c & 63) | 128;
    } else {
      out[p++] = (c >> 12) | 224;
      out[p++] = ((c >> 6) & 63) | 128;
      out[p++] = (c & 63) | 128;
    }
  }
  return out;
}

function utf8ToString(bytes) {
  var out = [], pos = 0, c = 0;
  while (pos < bytes.length) {
    var c1 = bytes[pos++];
    if (c1 < 128) {
      out[c++] = String.fromCharCode(c1);
    } else if (c1 > 191 && c1 < 224) {
      var c2 = bytes[pos++];
      out[c++] = String.fromCharCode((c1 & 31) << 6 | c2 & 63);
    } else if (c1 > 239 && c1 < 365) {
      // Surrogate Pair
      var c2 = bytes[pos++];
      var c3 = bytes[pos++];
      var c4 = bytes[pos++];
      var u = ((c1 & 7) << 18 | (c2 & 63) << 12 | (c3 & 63) << 6 | c4 & 63) -
          0x10000;
      out[c++] = String.fromCharCode(0xD800 + (u >> 10));
      out[c++] = String.fromCharCode(0xDC00 + (u & 1023));
    } else {
      var c2 = bytes[pos++];
      var c3 = bytes[pos++];
      out[c++] =
          String.fromCharCode((c1 & 15) << 12 | (c2 & 63) << 6 | c3 & 63);
    }
  }
  return out.join('');
}

function utf16ToString(bytes) {
  var ints = [];
  if (bytes.length>=2 && bytes[0]==0xFF && bytes[1]==0xFE) {
    //little endian
    for (var i = 2, j = 0; i < bytes.length;) {
      var low = bytes[i++];
      var high = bytes[i++];
      ints[j++] = (high<<8) + low;
    }
  }
  else if (bytes.length>=2 && bytes[0]==0xFE && bytes[1]==0xFF) {
    //big endian
    for (var i = 2, j = 0; i < bytes.length;) {
      var high = bytes[i++];
      var low = bytes[i++];
      ints[j++] = (high<<8) + low;
    }
  }
  else {
    //big endian
    for (var i = 0, j = 0; i < bytes.length;) {
      var high = bytes[i++];
      var low = bytes[i++];
      ints[j++] = (high<<8) + low;
    }
  }

  var CHUNK_SIZE = 8192;

  // Special-case the simple case for speed's sake.
  if (ints.length <= CHUNK_SIZE) {
    return String.fromCharCode.apply(null, ints);
  }

  // The remaining logic splits conversion by chunks since
  // Function#apply() has a maximum parameter count.
  // See discussion: http://goo.gl/LrWmZ9

  var str = '';
  for (var i = 0; i < ints.length; i += CHUNK_SIZE) {
    var chunk = Array.prototype.slice.call(ints, i, i + CHUNK_SIZE);
    str += String.fromCharCode.apply(null, chunk);
  }
  return str;
}
