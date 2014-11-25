@test
shared void testInlineExpressions() {
    Integer|String str = "xxx";
    Integer|String int = 1;
    check((if (is String str) then str.size else str)==3, "inline if 1");
    check((if (is Integer str) then str else str.size)==3, "inline if 2");
    check((if (is String int) then int.size else int)==1, "inline if 3");
    check((if (is Integer int) then int else int.size)==1, "inline if 4");
    check((switch (str) case (is String) str.size else str)==3, "inline switch 1");
    check((switch (str) case (is Integer) str else str.size)==3, "inline switch 2");
    check((switch (int) case (is String) int.size else int)==1, "inline switch 3");
    check((switch (int) case (is Integer) int else int.size)==1, "inline switch 4");
}