package com.redhat.ceylon.common;

import java.io.Console;

/**
 * Implementation of {@link Password} which prompts the user for the password 
 * via a pluggable {@link PasswordPrompt}. {@link ConsolePasswordPrompt} 
 * provides an implementation for prompting via the system console. 
 * Applications without access to a system console must provide their own
 * {@link PasswordPrompt}.  
 * @author tom
 */
public class PromptedPassword implements Password {

    /** A way of getting a password interactively from the user */
    public static interface PasswordPrompt {
        public char[] getPassword(String prompt);
    }

    /** Prompts the user for a password on the system console */
    public static class ConsolePasswordPrompt implements PasswordPrompt {

        private final Console console;
        
        public ConsolePasswordPrompt() {
            console = System.console();
            if (console == null) {
                throw new RuntimeException("No console available");
            }
        }
        
        @Override
        public char[] getPassword(String prompt) {
            return console.readPassword("%s: ", prompt);
        }
    }
    
    private final PasswordPrompt prompter;
    private final String prompt;
    
    public PromptedPassword(PasswordPrompt prompter, String prompt) {
        this.prompter = prompter;
        this.prompt = prompt;
    }
    
    @Override
    public char[] getPassword() {
        return prompter.getPassword(prompt);
    }

}
