(function() {
  if ((typeof process !== "undefined") && (process.stdout !== undefined)) {
    return function(string) {
      if(string)process.stdout.write(string.valueOf());
    }
  } else if ((typeof console !== "undefined") && (console.log !== undefined)) {
    return function(line) {
      if (line==operatingSystem().newline) {
        console.log(ex$.p$w$stdout||'');
        ex$.p$w$stdout='';
      } else {
        ex$.p$w$stdout=(ex$.p$w$stdout||'')+line;
      }
    }
  }
  return function(){};
})()
