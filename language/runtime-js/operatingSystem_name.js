if (typeof process !== "undefined" && typeof process.platform === 'string') {
    return $_String(process.platform);
}
return "Unknown";
