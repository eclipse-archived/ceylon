void comprehensions() {
    String[] words = { "hello", "world", "goodbye" };
    String[] result = { for (w in words) if (w.size>2) w };
    
    String?[] wordsAnNulls = { "hello", "world", null, "goodbye" };
    String joined1 = " ".join(for (w in wordsAnNulls) if (exists w) w.uppercased);
    String joined2 = " ".join { for (w in wordsAnNulls) if (exists w) w.trimmed };
    Entry<String,Integer>[] entries = { for (s in words) for (n in 0..10) s->n };
    @type["Empty|Sequence<String>"] value seq = { for (x in {null, "hello", "goodbye"}) if (exists x) if (x.size>1) x.uppercased };
    @type["Array<String>"] array(for (x in {null, "hello", "goodbye"}) if (exists x) if (x.size>1) x.uppercased);
    
    value xxx = { array { seq... }... };
    
    value nulls = coalesce(for (c in "hElLo") null).sequence;
    value nullsAndChars = coalesce(for (c in "hElLo") c.uppercase then c else null).sequence;
    value nulls2 = {for (c in "hElLo") null}.coalesced;
    value nullsAndChars2 = {for (c in "hElLo") c.uppercase then c else null}.coalesced;
        
}