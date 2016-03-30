package com.redhat.ceylon.common.tool;

import java.io.Flushable;
import java.io.IOException;
import java.text.BreakIterator;
import java.util.TreeSet;

/**
 * Facility for producing nicely word-wrapped output.
 * @author tom
 */
public class WordWrap {

    private class LineOutput {
        private final Appendable out;
        /** The current column */
        private int pos = 0;
        /** true if we're at the beginning of a line*/
        private boolean bol = true;
        /** true if this is a first line */
        private boolean fl = true;
        private String prefix = null;
        
        public LineOutput(Appendable out) {
            this.out = out;
        }
        public void append(String word) {
            try {
                if (bol) {
                    spaces(fl ? indentFirstLine : indentRestLines);
                    prefix();
                }
                out.append(word);
            } catch (IOException e) {
                // ignore it
            }
            pos+=word.length();
        }
        private void prefix() throws IOException {
            if (prefix != null) {
                out.append(prefix);
                pos += prefix.length();
                bol = pos == 0;
            }
        }
        private void newline(boolean fl) {
            try {
                out.append(System.getProperty("line.separator"));
                pos = 0;
                bol = true;
                this.fl = fl;
            } catch (IOException e) {
                // ignore it
            } 
        }
        private void newlineHard() {
            newline(true);
        }
        private void newlineSoft() {
            newline(false);
        }
        private void spaces(int num) {
            for (int ii = 0; ii < num; ii++) {
                bol = false;
                try {
                    out.append(' ');
                } catch (IOException e) {
                    // ignore it
                }
            }
            pos += num;
        }
        public void spacesToColumn(int column) {
            spaces(Math.max(column - pos, 0));
        }
        public int column() {
            return pos;
        }
    }
    
    private final int width;
    private int rightIndent = 0;
    private TreeSet<Integer> tabstops = new TreeSet<Integer>();
    private int indentFirstLine;
    private int indentRestLines;
    private final LineOutput out;
    
    /** 
     * Creates a wordwrap instance wrapping 
     * {@linkplain #System.out standard output}.
     */
    public WordWrap() {
        this(System.out);
    }
    
    public WordWrap(Appendable out) {
        this(out, Integer.getInteger("com.redhat.ceylon.common.tool.terminal.width", 80));
    }
    
    public WordWrap(Appendable out, int width) {
        if (width <= 0) {
            throw new IllegalArgumentException();
        }
        this.width = width;
        this.out = new LineOutput(out);
    }
    
    private void boundsCheck(int column) {
        if (column < 0 || column >= width) {
            throw new IllegalArgumentException();
        }
    }
    
    /** 
     * Sets the indentation level. Equivalent to calling 
     * {@link #setIndentFirstLine(int) setIndentFirstLine(indent)} followed by
     * {@link #setIndentRestLines(int) setIndentRestLines(indent)}.
     */
    public void setIndent(int indent) {
        setIndentFirstLine(indent);
        setIndentRestLines(indent);
    }
    
    /** 
     * Sets the indentation level of first lines
     * @see #setIndentRestLines(int) 
     */
    public void setIndentFirstLine(int indent) {
        boundsCheck(indent);
        this.indentFirstLine = indent;
    }
    
    public int getIndentFirstLine() {
        return this.indentFirstLine;
    }
    
    /** 
     * Sets the indentation level of non-first lines
     * @see #setIndentFirstLines(int) 
     */
    public void setIndentRestLines(int indent) {
        boundsCheck(indent);
        this.indentRestLines = indent;
    }
    
    public int getIndentRestLines() {
        return this.indentRestLines;
    }
    
    /** 
     * Sets the right margin 
     */
    public void setRightIndent(int indent) {
        boundsCheck(indent);
        this.rightIndent = indent;
    }
    
    public int getRightIndent() {
        return this.rightIndent;
    }

    public void setPrefix(String prefix) {
        out.prefix = prefix;
    }
    
    public String getPrefix() {
        return out.prefix;
    }

    public int getWidth() {
        return width;
    }
    
    /**
     * Adds a tab stop.
     * @param stop
     * @see #tab()
     * @see #removeTabStop(int)
     */
    public void addTabStop(int stop) {
        boundsCheck(stop);
        tabstops.add(stop);
    }
    
    /**
     * Removes a tab stop.
     * @param stop
     */
    public void removeTabStop(int stop) {
        tabstops.remove(stop);
    }
    
    /**
     * Clears all tab stops.
     * @param stop
     */
    public void clearTabStops() {
        tabstops.clear();
    }
    
    private String collapseWs(String s) {
        return s.replaceAll("\\s+", " ");
    }
    
    private String trimRight(String s) {
        return s.replaceAll("\\s+$", "");
    }
    
    private String prepend = "";
    
    /**
     * Appends the given string to the output.
     * @param s The string.
     */
    public WordWrap append(String s) {
        s = collapseWs(s);
        String prependNext = s.endsWith(" ") ? " " : "";
        s = prepend + trimRight(s);
        prepend = prependNext;
        
        // TODO Some control over soft and hard hyphens
        // spaces and newlines
        BreakIterator bi = BreakIterator.getLineInstance();
        bi.setText(s);
        int endOfLast = 0;
        int ii;
        // iterate forwards through characters...
        for (ii = 0; ii < s.length(); ii++) {
            // ...until the line is too long...
            if (exceedsWidth(endOfLast, ii)) {
                endOfLast = addLineBreak(s, bi, endOfLast, ii);
            }
            
        }
        if (exceedsWidth(endOfLast, ii)) {
            ii--;
            endOfLast = addLineBreak(s, bi, endOfLast, ii);
        }
        s = s.substring(endOfLast);
        out.append(s);
        
        return this;
    }

    private int addLineBreak(String s, BreakIterator bi, int endOfLast, int ii) {
        int safe = ii;
        while (!bi.isBoundary(ii)) {
            ii--;
            if (ii < endOfLast) {
                ii = safe;
                break;
            }
        }
        out.append(trimRight(s.substring(endOfLast, ii)));
        out.newlineSoft();
        endOfLast = ii;
        return endOfLast;
    }

    private boolean exceedsWidth(int endOfLast, int ii) {
        return out.column() + (ii-endOfLast) >= width-rightIndent;
    }
    
    /**
     * Prints a hard new line (in other words, starts a new paragraph).
     */
    public WordWrap newline() {
        this.prepend = "";
        out.newlineHard();
        return this;
    }
    
    /**
     * Moves to the <em>next</em> tab stop, that is the one to the right of the 
     * {@linkplain #getColumn() current column}.
     * @see #addTabStop(int)
     */
    public WordWrap tab() {
        out.spacesToColumn(tabstops.higher(out.column()));
        return this;
    }
    
    /**
     * If {@link #getColumn()}{@code < column} then writes sufficient spaces 
     * so that the next chararacter will appear in that column. 
     * Otherwise does nothing.
     * @param column
     * @return
     */
    public WordWrap column(int column) {
        out.spacesToColumn(column);
        return this;
    }
    
    /**
     * Gets the column position where the next character will be written 
     * @return
     */
    public int getColumn() {
        return out.column();
    }
    
    /**
     * Flushes the output, if it is {@link Flushable}.
     */
    public void flush() {
        try {
            if (out instanceof Flushable) {
                ((Flushable)out).flush();
            }
        } catch (IOException e) {
            // ignore
        }
    }

}
