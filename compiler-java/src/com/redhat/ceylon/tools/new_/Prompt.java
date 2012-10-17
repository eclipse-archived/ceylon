package com.redhat.ceylon.tools.new_;

import java.io.Console;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

public interface Prompt {
    public String readLine(String prompt);
}

class ConsoleConsoley implements Prompt {

    private final Console console;

    public ConsoleConsoley(Console console) {
        this.console = console;
    }
    
    @Override
    public String readLine(String prompt) {
        return console.readLine("%s", prompt);
    }
    
}

class StandardSteamConsoley implements Prompt {

    private final InputStream in;
    private final PrintStream out;
    private char last;
    public StandardSteamConsoley() {
        this.in = System.in;
        this.out = System.out;
    }
    
    @Override
    public String readLine(String prompt) {
        out.print(prompt);
        int ch = next();
        StringBuilder sb = new StringBuilder();
        while (true) {
            if (ch == -1) {
                return null;
            }
            char c = (char)ch;
            if (c == '\n' 
                    || c == '\r') {
                if (c == last
                        || sb.length() == 0) {
                    last = c;
                    return sb.toString();
                }
            }
            sb.append(c);
            ch = next();
        }
    }

    private int next() throws IOError {
        try {
            return in.read();
        } catch (IOException e) {
            throw new IOError(e);
        }
    }
    
}