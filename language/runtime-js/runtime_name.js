if (typeof process !== "undefined" && process.execPath && process.execPath.match(/node(js)?(\.exe)?$/)) {
  return "node.js";
} else if (typeof window === 'object') {
  return "Browser";
}
return "Unknown JavaScript environment";
