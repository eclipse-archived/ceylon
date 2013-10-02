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
package com.redhat.ceylon.tools.new_;

import java.io.File;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.redhat.ceylon.common.Constants;
import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.Hidden;
import com.redhat.ceylon.common.tool.Option;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.Rest;
import com.redhat.ceylon.common.tool.Subtool;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tool.Tool;
import com.redhat.ceylon.common.tool.ToolModel;

@Summary("Generates new ceylon projects")
@Description("Generates a new project, prompting for information as necessary")
public class CeylonNewTool implements Tool {

    private ToolModel<CeylonNewTool> model;
    
    private Project project;
    
    private File from = new File(System.getProperty(Constants.PROP_CEYLON_HOME_DIR), "templates");
    
    private Map<String, String> rest = new HashMap<String, String>();
    
    private boolean verbose = false;
    
    public void setToolModel(ToolModel<CeylonNewTool> model) {
        this.model = model;
    }
    
    @Subtool(argumentName="name", classes={HelloWorld.class}, order=2)
    public void setProject(Project project) {
        this.project = project;
    }
    
    @Hidden
    @OptionArgument(argumentName="dir")
    public void setFrom(File from) {
        this.from = from;
    }
    
    public boolean getVerbose() {
        return verbose;
    }
    
    @Hidden
    @Option
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }
    
    @Rest
    public void setRest(List<String> rest) {
        for (String optionArg : rest) {
            if (optionArg.startsWith("--")) {
                int idx = optionArg.indexOf("=");
                String option;
                String arg;
                if (idx == -1) {
                    option = optionArg.substring(2);
                    arg = "";
                } else {
                    option = optionArg.substring(2, idx);
                    arg = optionArg.substring(idx + 1);
                }
                this.rest.put(option, arg);
            }
        }
    }
    
    @Override
    public void run() throws Exception {
        File fromDir = getFromDir();
        if (!fromDir.exists() || !fromDir.isDirectory()) {
            throw new IllegalArgumentException(Messages.msg("from.nonexistent.or.nondir", from.getAbsolutePath()));
        }
        Environment env = buildPromptedEnv();
        List<Copy> resources = project.getResources(env);
        // Create base dir only once all the prompting has been done
        project.mkBaseDir();
        for (Copy copy : resources) {
            copy.run(env);
        }
    }

    private String getProjectName() {
        String projectName = model.getSubtoolModel().getToolLoader().getToolName(project.getClass().getName());
        return projectName;
    }

    private File getFromDir() {
        return new File(from, getProjectName());
    }
    
    private Environment buildPromptedEnv() {
        Environment env = new Environment();
        // TODO Tidy up how we create and what's in this initial environment
        env.put("base.dir", project.getDirectory().getAbsolutePath());
        env.put("ceylon.home", System.getProperty(Constants.PROP_CEYLON_HOME_DIR));
        //env.put("ceylon.system.repo", System.getProperty("ceylon.system.repo"));
        env.put("ceylon.version.number", Versions.CEYLON_VERSION_NUMBER);
        env.put("ceylon.version.major", Integer.toString(Versions.CEYLON_VERSION_MAJOR));
        env.put("ceylon.version.minor", Integer.toString(Versions.CEYLON_VERSION_MINOR));
        env.put("ceylon.version.release", Integer.toString(Versions.CEYLON_VERSION_RELEASE));
        env.put("ceylon.version.name", Versions.CEYLON_VERSION_NAME);
        
        Set<String> seenKeys = new HashSet<>();
        List<Variable> vars = new LinkedList<>(project.getVariables());
        while (!vars.isEmpty()) {
            Variable var = vars.remove(0);
            if (seenKeys.contains(var.getKey())) {
                throw new RuntimeException("Variables for project do not form a tree");
            }
            seenKeys.add(var.getKey());
            // TODO Use the value from rest if there is one, only prompting if 
            // there is not
            // TODO The problem with this is: "How does the user figure out 
            // what option they need to specify on the command line
            // in order to avoid being prompted for it interactively?"
            // Each subtool could provide their own getters and setters
            // but they requires we write a subtool for each project
            // It would be nice if we didn't have to do that, but could just
            // drive the whole thing from a script in the templates dir.
            vars.addAll(0, var.initialize(getProjectName(), env));
        }
        log(env);
        return env;
    }
    
    public Copy substituting(final String cwd, final String src, final String dst) {
        return substituting(cwd, new PathMatcher() {
            @Override
            public boolean matches(Path path) {
                return path.endsWith(src);
            }
        }, dst);
    }

    public Copy substituting(final String cwd, PathMatcher pathMatcher, String dst) {
        return new Copy(new File(getFromDir(), cwd), project.getDirectory(), pathMatcher, dst, true);
    }
    
    private void log(Object msg) {
        if (verbose) {
            System.out.println(msg);
        }
    }
    
    public class HelloWorld extends Project {

        private Variable moduleName = Variable.moduleName("module.name", "com.example.helloworld");
        private Variable moduleVersion = Variable.moduleVersion("module.version", "1.0.0");
        private Variable eclipseProjectName = new Variable("eclipse.project.name", null, new PromptedValue("eclipse.project.name", "@[module.name]"));
        private Variable eclipse = Variable.yesNo("eclipse", eclipseProjectName);
        private Variable ant = Variable.yesNo("ant");

        @Argument(argumentName="dir", multiplicity="1", order=1)
        public void setDirectory(File directory) {
            // TODO This sucks -- in general we want an @Argument
            // for the directory in which the project should be created
            // but we can't enforce that every Project declared a 
            // setDirectory() method with the annotation. Perhaps the 
            // ToolLoader should be looking at the inherited methods, not just 
            // the ones declared on the tool
            super.setDirectory(directory);
        }
        
        @OptionArgument
        public void setModuleName(String moduleName) {
            this.moduleName.setVariableValue(new GivenValue(moduleName));
        }
        
        @OptionArgument
        public void setModuleVersion(String moduleVersion) {
            this.moduleVersion.setVariableValue(new GivenValue(moduleVersion));
        }
        
        @OptionArgument
        public void setEclipse(boolean eclipse) {
            this.eclipse.setVariableValue(new GivenValue(Boolean.toString(eclipse)));
        }
        
        @OptionArgument
        public void setEclipseProjectName(String eclipseProjectName) {
            this.eclipseProjectName.setVariableValue(new GivenValue(eclipseProjectName));
        }
        
        @OptionArgument
        public void setAnt(boolean ant) {
            this.ant.setVariableValue(new GivenValue(Boolean.toString(ant)));
        }
        
        @Override
        public List<Variable> getVariables() {
            List<Variable> result = new ArrayList<>();
            result.add(moduleName);
            result.add(Variable.moduleDir("module.dir", "module.name"));
            result.add(moduleVersion);
            result.add(eclipse);
            result.add(ant);
            return result;
        }

        @Override
        public List<Copy> getResources(Environment env) {
            FileSystem fs = FileSystems.getDefault();
            List<Copy> result = new ArrayList<>();
            result.add(substituting("source",
                    fs.getPathMatcher("glob:**"), 
                    new Template("source/@[module.dir]").eval(env)));
            
            if (env.get("ant").equals("true")) {
                result.add(substituting("ant", "build.xml", "."));
            }
            if (env.get("eclipse").equals("true")) {
                result.add(substituting("eclipse",
                        fs.getPathMatcher("glob:**"),
                        "."));
            }
            return result;
        }
    }

}
