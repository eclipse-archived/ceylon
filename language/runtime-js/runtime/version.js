if (typeof process !== "undefined" && typeof process.version === 'string') {
  return process.version;
}
return "Unknown";
