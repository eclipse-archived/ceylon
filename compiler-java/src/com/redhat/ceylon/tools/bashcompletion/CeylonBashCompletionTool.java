/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package com.redhat.ceylon.tools.bashcompletion;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.Hidden;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.OptionModel;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tool.Tool;
import com.redhat.ceylon.common.tool.ToolLoader;
import com.redhat.ceylon.common.tool.ToolModel;
import com.redhat.ceylon.common.tool.OptionModel.ArgumentType;
import com.redhat.ceylon.tools.CeylonTool;

@Hidden
@Summary( 
value="A tool which provides completion suggestions for the Bash shell.")
@Description("The `<arguments>` are the elements of the `${COMP_WORDS}` bash array variable.\n" +
		"\n" +
		"The tool inspects the <arguments> and writes it completions to standard output." +
		"Currently the tool can complete\n" +
		"\n" +
		"* tool names (except tools names which are arguments to another tool),\n" +
		"* long option names," +
		"* long option values **if** the setter type is a `java.lang.File` or a subclass" +
		"  of `java.lang.Enum`.")
public class CeylonBashCompletionTool implements Tool {

    public static class CompletionResults {
        
        private List<String> results = new ArrayList<String>();
        private String prefix = "";
        private String partial = "";
        
        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }
        
        public void setPartial(String partial) {
            this.partial = partial;
        }
        
        public boolean addResult(String completion) {
            if (completion.startsWith(partial)) {
                results.add(completion);
                return true;
            }
            return false;
        }
        
        public void emitCompletions() {
            boolean appendSpace = results.size() == 1;
            for (String result : results) {
                String completion;
                if (prefix.isEmpty()) {
                    completion = escape(result);
                } else {
                    completion = escape(prefix + result.substring(partial.length()));
                }
                if (appendSpace) {
                    // If there's only one result, append a space
                    completion = completion + " ";
                }
                System.out.println(completion);
            }
            System.out.flush();
        }
        
        private String escape(String completion) {
            return completion.replace("\n", "\\\n");
        }
    }
    
    private int cword = -1;
    private List<String> arguments;
    private ToolLoader toolLoader;
    
    @OptionArgument
    @Description("The index in the `<arguments>` of the argument being " +
    		"completed, i.e. The value of `${COMP_CWORD}`.")
    public void setCword(int word) {
        this.cword = word;
    }
    
    @Argument(argumentName="arguments", multiplicity="*")
    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }
    
    public final void setToolLoader(ToolLoader toolLoader) {
        this.toolLoader = toolLoader;
    }
    
    @Override
    public void run() throws Exception {
        arguments.remove(0);// we don't care about arg0
        cword--;
        final CompletionResults results;
        if (cword == 0) {
            // We're completing the name of the tool to run
            results = completeToolNames(arguments.isEmpty() ? "" : arguments.get(cword));
        } else if (cword < arguments.size()) {
            String argument = arguments.get(cword);
            CeylonTool main = new CeylonTool();
            main.setArgs(arguments);
            main.setToolLoader(toolLoader);
            ToolModel<?> tool = main.getToolModel();
            if (!afterEoo()) {
                if (argument.startsWith("--")) {
                    if (argument.contains("=")) {
                        results = completeLongOptionArgument(tool, argument);
                    } else {
                        results = completeLongOption(tool, argument);
                    }
                } else if (argument.startsWith("-")) {
                    /*TODO for (OptionModel<?> option : tool.getOptions()) {
                        if (argument.charAt(argument.length()-1) == option.getShortName()) {
                            complete
                        }
                    }*/
                    results = new CompletionResults();
                } else {
                    // TODO it's argument completion unless the previous argument was a 
                    // non-pure short option
                    results = new CompletionResults();
                }
            } else {
                // TODO else it must be argument completion
                results = new CompletionResults();
            }
        } else {
            // TODO we don't know what we're completing. 
            // First assume it's an argument...
            // ... but if the tool doesn't have any arguments (or all the 
            // arguments are already specified) then assume it's an option
            results = new CompletionResults();
        }
        results.emitCompletions();
    }

    private CompletionResults completeLongOptionArgument(ToolModel<?> tool, final String argument) {
        int index = argument.indexOf('=');
        String optionName = argument.substring(2, argument.charAt(index-1) == '\\' ? index-1 : index);
        String partialValue = argument.substring(index+1);
        OptionModel<?> option = tool.getOption(optionName);
        Class<?> type = option.getArgument().getType();
        /*
         * In general,
         * 1. instantiate the tool, binding all arguments except
         *    the one being completed. do not invoke @PostConstruct
         * 2. If the option's setter has @CompletedBy(method=methodname) 
         *    call methodname(partial);
         * 3. If the option's setter has @CompletedBy(Completer)
         *    then instantiate the given completer and  
         *    call Completer.complete(tool, partial)
         * 4. If the setter type is annotated with @CompletedBy
         *    then instantiate the given completer and  
         *    call Completer.complete(tool, partial). 
         * 5. Revert to some build in completions.
         */
        if (File.class.isAssignableFrom(type)) {
            return completeFilename(argument, partialValue, true, null);
        } else if (Enum.class.isAssignableFrom(type)) {
            return completeEnum(argument, (Class<? extends Enum>)type, partialValue);
        }
        
        return new CompletionResults();
    }

    private <E extends Enum<E>> CompletionResults completeEnum(String argumentPrefix, Class<E> type, String partialValue) {
        try {
            CompletionResults result = new CompletionResults();
            result.setPartial(partialValue);
            result.setPrefix(argumentPrefix);
            Method method = type.getMethod("values", (Class[])null);
            E[] values = (E[])method.invoke(null);
            for (E value : values) {
                String enumElementName = value.toString();
                result.addResult(enumElementName);
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    private CompletionResults completeFilename(String argumentPrefix, String partialValue, boolean wantFiles, 
            final FileFilter fileFilter) {
        File file;
        final String partial;
        if (partialValue.isEmpty()) {
            file = new File(".").getAbsoluteFile();
            partial = "";
        } else {
            file = new File(partialValue).getAbsoluteFile();
            partial = file.getName();
        }
        
        File[] files = file.getParentFile().listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                String name = pathname.getName();
                if (pathname.isFile() && fileFilter != null) {
                    return name.startsWith(partial) && fileFilter.accept(pathname);
                }
                return name.startsWith(partial);
            }
        });
        CompletionResults results = new CompletionResults();
        results.setPrefix(argumentPrefix);
        results.setPartial(partialValue);
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory() && wantFiles) {
                    results.addResult(f.getName()+"/");
                } else {
                    results.addResult(f.getName());
                }
            }
        }
        return results;
    }

    private CompletionResults completeLongOption(ToolModel<?> tool, String argument) {
        Comparator<OptionModel<?>> comparator = new Comparator<OptionModel<?>>() {
            @Override
            public int compare(OptionModel<?> o1, OptionModel<?> o2) {
                return o1.getLongName().compareTo(o2.getLongName());
            }
        };
        CompletionResults results = new CompletionResults();
        results.setPartial(argument);
        TreeSet<OptionModel<?>> sorted = new TreeSet<OptionModel<?>>(comparator);
        sorted.addAll(tool.getOptions());
        for (OptionModel<?> option : sorted) {
            results.addResult("--" + option.getLongName() + (option.getArgumentType() == ArgumentType.NOT_ALLOWED ? "" : "\\="));
        }
        return results;
    }

    private boolean afterEoo() {
        boolean eoo = false;
        for (int ii = 0; ii < cword; ii++) {
            if (arguments.get(ii).equals("--")) {
                eoo = true;
                break;
            }
        }
        return eoo;
    }

    private CompletionResults completeToolNames(String partial) {
        CompletionResults results = new CompletionResults();
        results.setPartial(partial);
        for (String toolName : toolLoader.getToolNames()) {
            if (toolLoader.loadToolModel(toolName).isPorcelain()) {
                results.addResult(toolName);
            }
        }
        return results;
    }
    
    

}
