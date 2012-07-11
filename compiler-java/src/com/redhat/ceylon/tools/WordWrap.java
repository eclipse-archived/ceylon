package com.redhat.ceylon.tools;

import java.io.Flushable;
import java.io.IOException;
import java.util.TreeSet;

/**
 * Facility for producing nicely word-wrapped output.
 * @author tom
 */
public class WordWrap {

    private final Appendable out;
    private final int width;
    private int rightIndent = 0;
    private int pos = 0;
    private TreeSet<Integer> tabstops = new TreeSet<Integer>();
    private boolean bol;
    private int indentFirstLine;
    private int indentRestLines;
    private boolean fl;
    
    public WordWrap() {
        width = Integer.getInteger("com.redhat.ceylon.tools.terminal.width", 80);
        out = System.out;
    }
    
    public void setIndent(int indent) {
        setIndentFirstLine(indent);
        setIndentRestLines(indent);
    }
    
    public void setIndentFirstLine(int indent) {
        this.indentFirstLine = indent;
    }
    
    public void setIndentRestLines(int indent) {
        this.indentRestLines = indent;
    }
    
    public void setRightIndent(int indent) {
        this.rightIndent = indent;
    }
    
    public void addTabStop(int stop) {
        tabstops.add(stop);
    }
    
    public void removeTabStop(int stop) {
        tabstops.remove(stop);
    }
    
    private void p(String word) {
        try {
            out.append(word);
        } catch (IOException e) {
            // ignore it
        }
        pos+=word.length();
    }
    
    private void n(boolean fl) {
        try {
            out.append(System.getProperty("line.separator"));
            pos = 0;
            bol = true;
            s(fl ? indentFirstLine : indentRestLines);
            this.fl = fl;
        } catch (IOException e) {
            // ignore it
        }
    }

    private void s(int num) {
        for (int ii = 0; ii < num; ii++) {
            bol = false;
            p(" ");
        }
         
    }
    
    public WordWrap print(String s) {
        if (bol) {
            s(fl ? indentFirstLine : indentRestLines);
        }
        // TODO Some control over soft and hard hyphens
        // spaces and newlines
        String[] words = s.split("[\\s]+");
        for (int ii = 0; ii < words.length; ii++) {
            String word = words[ii];
            if (pos + word.length() >= width-rightIndent) {
                n(false);
            }
            p(word);
            // Add a space if the next word will be on the same line
            if (ii+1 < words.length
                    && pos + words[ii+1].length() < width-rightIndent) {
                p(" ");
            }
        }
        return this;
    }
    
    public WordWrap println() {
        n(true);
        return this;
    }
    
    public WordWrap tab() {
        s(tabstops.higher(pos) - pos);
        return this;
    }
    
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
