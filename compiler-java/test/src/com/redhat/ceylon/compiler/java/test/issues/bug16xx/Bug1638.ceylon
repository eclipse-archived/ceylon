String bug1638(Integer i)(Character c) {
    return "``i````c``";
}
void bug1638run() {
    print(bug1638(1)('a'));
    value fd = `function bug1638`;
    value fm = fd.apply<String(Character), [Integer]>();
    
}