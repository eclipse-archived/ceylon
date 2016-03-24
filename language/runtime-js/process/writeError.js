(function(){
  if ((typeof process !== "undefined") && (process.stderr !== undefined)) {
    return function(string) {
      if(string)process.stderr.write(string.valueOf());
    }
  } else if ((typeof console !== "undefined") && (console.error !== undefined)) {
    return function(line) {
        console.error(line?line.valueOf():'');
    }
  }
  return function(x){this.write(x);}
})()
