@noanno
shared interface Bug6267 {
    
    shared formal void newProposal(String desc,
        Anything region = null);
}
@noanno
shared interface Sub6267
        satisfies Bug6267 {
    
    shared void m() {
        newProposal { desc = "Join 'if' statements at 'else'"; };
    }
}