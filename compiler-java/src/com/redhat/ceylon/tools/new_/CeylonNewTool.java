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
import com.redhat.ceylon.common.config.CeylonConfig;
import com.redhat.ceylon.common.config.DefaultToolOptions;
import com.redhat.ceylon.common.tool.CeylonBaseTool;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.Hidden;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.Rest;
import com.redhat.ceylon.common.tool.Subtool;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tool.ToolModel;
import com.redhat.ceylon.common.tools.CeylonTool;

@Summary("Generates a new Ceylon project")
@Description("Generates a new project, prompting for information as necessary")
public class CeylonNewTool extends CeylonBaseTool {

    private ToolModel<CeylonNewTool> model;
    
    private Project project;
    
    private File from = new File(System.getProperty(Constants.PROP_CEYLON_HOME_DIR), "templates");
    
    private Map<String, String> rest = new HashMap<String, String>();
    
    public void setToolModel(ToolModel<CeylonNewTool> model) {
        this.model = model;
    }
    
    @Subtool(argumentName="name", classes={Simple.class, HelloWorld.class, JavaInterop.class}, order=2)
    public void setProject(Project project) {
        this.project = project;
    }
    
    @Hidden
    @OptionArgument(argumentName="dir")
    public void setFrom(File from) {
        this.from = from;
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
    public void initialize(CeylonTool mainTool) {
    }
    
    @Override
    public void run() throws Exception {
        File fromDir = getFromDir();
        if (!fromDir.exists() || !fromDir.isDirectory()) {
            throw new IllegalArgumentException(Messages.msg("from.nonexistent.or.nondir", fromDir));
        }
        Environment env = buildPromptedEnv();
        List<Copy> resources = project.getResources(env);
        // Create base dir only once all the prompting has been done
        project.mkBaseDir(getCwd());
        for (Copy copy : resources) {
            copy.run(env);
        }
    }

    private String getProjectName() {
        String projectName = model.getSubtoolModel().getToolLoader().getToolName(project.getClass().getName());
        return projectName;
    }

    private File getFromDir() {
        return new File(applyCwd(from), getProjectName());
    }
    
    private Environment buildPromptedEnv() {
        Environment env = new Environment();
        // TODO Tidy up how we create and what's in this initial environment
        if (project.getDirectory() != null) {
            env.put("base.dir", applyCwd(project.getDirectory()).getAbsolutePath());
        }
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
        
        String sourceFolder = Constants.DEFAULT_SOURCE_DIR;
        String baseDir = env.get("base.dir");
        if (project.getDirectory() == null) {
            project.setDirectory(new File(baseDir));
        }
        try {
            CeylonConfig config = CeylonConfig.createFromLocalDir(new File(baseDir));
            List<File> srcs = DefaultToolOptions.getCompilerSourceDirs(config);
            if (!srcs.isEmpty()) {
                sourceFolder = srcs.get(0).getPath();
            } else {
                sourceFolder = Constants.DEFAULT_SOURCE_DIR;
            }
        } catch (Exception e) {
            // Ignore any errors
        }
        env.put("source.folder", sourceFolder);
        
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
        return new Copy(new File(getFromDir(), cwd), applyCwd(project.getDirectory()), pathMatcher, dst, true);
    }
    
    private void log(Object msg) {
        if (verbose != null) {
            System.out.println(msg);
        }
    }
    
    public abstract class BaseProject extends Project {

        private final Variable baseDir;
        private final Variable moduleName;
        private final Variable moduleVersion;
        private final Variable eclipseProjectName;
        private final Variable eclipse;
        private final Variable ant;

        public BaseProject(String defBaseDir, String defModName) {
            baseDir = Variable.directory("base.dir", shouldCreateProject() ? defBaseDir : ".");
            moduleName = Variable.moduleName("module.name", defModName);
            moduleVersion = Variable.moduleVersion("module.version", "1.0.0");
            eclipseProjectName = new Variable("eclipse.project.name", null, new PromptedValue("eclipse.project.name", "@[module.name]"));
            eclipse = Variable.yesNo("eclipse", Messages.msg("mnemonic.yes"), eclipseProjectName);
            ant = Variable.yesNo("ant", Messages.msg("mnemonic.yes"));
        }
        
        @OptionArgument
        @Description("Specifies the name for the new module.")
        public void setModuleName(String moduleName) {
            this.moduleName.setVariableValue(new GivenValue(moduleName));
        }
        
        @OptionArgument
        @Description("Specifies the version for the new module.")
        public void setModuleVersion(String moduleVersion) {
            this.moduleVersion.setVariableValue(new GivenValue(moduleVersion));
        }
        
        @OptionArgument
        @Description("Indicates if an Eclipse project should be generated or not.")
        public void setEclipse(boolean eclipse) {
            this.eclipse.setVariableValue(new GivenValue(Boolean.toString(eclipse)));
        }
        
        @OptionArgument
        @Description("Specifies the name for the Eclipse project.")
        public void setEclipseProjectName(String eclipseProjectName) {
            this.eclipseProjectName.setVariableValue(new GivenValue(eclipseProjectName));
        }
        
        @OptionArgument
        @Description("Indicates if an Ant build file should be generated or not.")
        public void setAnt(boolean ant) {
            this.ant.setVariableValue(new GivenValue(Boolean.toString(ant)));
        }
        
        @Override
        public List<Variable> getVariables() {
            List<Variable> result = new ArrayList<>();
            if (getDirectory() == null) {
                result.add(baseDir);
            }
            result.add(moduleName);
            result.add(Variable.moduleQuotedName("module.quoted.name", "module.name"));
            result.add(Variable.moduleDir("module.dir", "module.name"));
            result.add(moduleVersion);
            if (shouldCreateProject()) {
                result.add(eclipse);
                result.add(ant);
            }
            return result;
        }

        // Check if the target directory is not a project
        protected boolean shouldCreateProject() {
            // If it has a ".ceylon" folder we assume a project exists
            File ceylonDir = new File(applyCwd(getDirectory()), ".ceylon");
            if (ceylonDir.isDirectory()) {
                return false;
            }
            // If there's no ".ceylon" folder but there is a "source" folder
            // we also assume a project exists
            File sourcesDir = new File(applyCwd(getDirectory()), Constants.DEFAULT_SOURCE_DIR);
            if (sourcesDir.isDirectory()) {
                return false;
            }
            return true;
        }

        @Override
        public List<Copy> getResources(Environment env) {
            FileSystem fs = FileSystems.getDefault();
            List<Copy> result = new ArrayList<>();
            result.add(substituting("source",
                    fs.getPathMatcher("glob:**"), 
                    new Template("@[source.folder]/@[module.dir]").eval(env)));
            
            if ("true".equals(env.get("ant"))) {
                result.add(substituting("ant", "build.xml", "."));
            }
            if ("true".equals(env.get("eclipse"))) {
                result.add(substituting("eclipse",
                        fs.getPathMatcher("glob:**"),
                        "."));
            }
            return result;
        }
    }
    
    @Description("Generates a 'Hello World' style project." +
            "\n\n" +
            "Takes a `<dir>` argument to indicate in which directory the new project should be created.")
    public class HelloWorld extends BaseProject {

        public HelloWorld() {
            super("helloworld", "com.example.helloworld");
        }
    }
    
    @Description("Generates a very simple empty project" +
            "\n\n" +
            "Takes a `<dir>` argument to indicate in which directory the new project should be created.")
    public class Simple extends BaseProject {

        public Simple() {
            super("simple", "com.example.simple");
        }
    }
    
    @Description("Generates a project that is able to use Java legacy code" +
            "\n\n" +
            "Takes a `<dir>` argument to indicate in which directory the new project should be created.")
    public class JavaInterop extends BaseProject {

        public JavaInterop() {
            super("javainterop", "com.example.javainterop");
        }
    }

}
