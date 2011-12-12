void comprehensions() {
    String[] words = { "hello", "world", "goodbye" };
    String[] result = { for (w in words) if (w.size>2) w };
    
    String?[] wordsAnNulls = { "hello", "world", null, "goodbye" };
    String joined1 = " ".join(for (w in wordsAnNulls) if (exists w) w.uppercased);
    String joined2 = " ".join { for (w in wordsAnNulls) if (exists w) w.trimmed };
    Entry<String,Integer>[] entries = { for (s in words) for (n in 0..10) s->n };
        
}