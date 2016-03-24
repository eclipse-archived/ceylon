package com.redhat.ceylon.common;

public class StatusPrinter {
    private int lineWidth;
    private int remaining;
    private final static char abbreviationChar = '…';
    private final static String abbreviationString = "…";
    private StringBuilder currentLine;
    private String capturedLine;

    public StatusPrinter(){
        this(Integer.getInteger("com.redhat.ceylon.common.tool.terminal.width", 80));
    }
    
    public StatusPrinter(int i) {
        this.lineWidth = i;
        this.remaining = i;
        this.currentLine = new StringBuilder(lineWidth);
    }

    public void clearLine(){
        if(currentLine.length() == 0)
            return;
        StringBuilder sb = new StringBuilder(lineWidth+2);
        sb.append('\r');
        for(int i=0;i<lineWidth;i++){
            sb.append(' ');
        }
        sb.append('\r');
        System.err.print(sb.toString());
        System.err.flush();
        currentLine.setLength(0);
        remaining = lineWidth;
    }

    private void print(String part) {
        System.err.print(part);
        currentLine.append(part);
        remaining -= part.codePointCount(0, part.length());
        System.err.flush();
    }

    public void log(String str){
        print(part(str));
    }

    public void log(String str, int sizeLimit){
        print(part(str, sizeLimit));
    }

    public void logRight(String str){
        print(partRight(str));
    }

    public void logRight(String str, int sizeLimit){
        print(partRight(str, sizeLimit));
    }

    public String part(String str, int sizeLimit){
        if(remaining == 0)
            return "";
        int codePoints = str.codePointCount(0, str.length());
        int max = Math.min(codePoints, sizeLimit);
        if(remaining >= max){
            if(codePoints != max)
                return limit(str, max);
            else
                return str;
        }else{
            return limit(str, remaining);
        }
    }

    public String partRight(String str, int sizeLimit){
        if(remaining == 0)
            return "";
        int codePoints = str.codePointCount(0, str.length());
        int max = Math.min(codePoints, sizeLimit);
        if(remaining >= max){
            if(codePoints != max)
                return leftPad(limit(str, max), remaining - max);
            else
                return leftPad(str, remaining - codePoints);
        }else{
            return limit(str, remaining);
        }
    }

    public String part(String str){
        if(remaining == 0)
            return "";
        int codePoints = str.codePointCount(0, str.length());
        if(remaining >= codePoints){
            return str;
        }else{
            return limit(str, remaining);
        }
    }

    public String partRight(String str){
        if(remaining == 0)
            return "";
        int codePoints = str.codePointCount(0, str.length());
        if(remaining >= codePoints){
            return leftPad(str, remaining-codePoints);
        }else{
            return limit(str, remaining);
        }
    }

    private String leftPad(String str, int count) {
        if(count == 0)
            return str;
        // normal length works here because we dont change its code point length, every
        // space is 1 code point
        StringBuilder b = new StringBuilder(str.length()+count);
        for(int i=0;i<count;i++)
            b.append(' ');
        b.append(str);
        return b.toString();
    }

    public int remainingForPercentage(double percentage){
        return (int) Math.floor(remaining * percentage);
    }
    
    public String limit(String str, int sizeLimit){
        int codePoints = str.codePointCount(0, str.length());
        if(codePoints <= sizeLimit)
            return str;
        if(sizeLimit == 0)
            return "";
        if(sizeLimit == 1)
            return abbreviationString;
        StringBuilder sb = new StringBuilder();
        double half = (sizeLimit-1) / 2.0;
        int before = (int) Math.ceil(half);
        int after = (int) Math.floor(half);
        for(int c=0;c<before;c++)
            sb.appendCodePoint(str.codePointAt(c));
        sb.append(abbreviationChar);
        for(int c=codePoints-after;c<codePoints;c++)
            sb.appendCodePoint(str.codePointAt(c));
        return sb.toString();
    }

    public void captureLine() {
        capturedLine = currentLine.toString();
    }
    
    public void logCapturedLine(){
        clearLine();
        log(capturedLine);
    }

    public static boolean canPrint() {
        return System.console() != null;
    }

    public int getRemaining() {
        return remaining;
    }
}
