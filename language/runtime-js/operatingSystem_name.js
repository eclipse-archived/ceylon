if (typeof process !== "undefined" && typeof process.platform === 'string') {
    return String$(process.platform);
}
return "Unknown";
