package com.redhat.ceylon.common.tool;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import com.redhat.ceylon.common.Constants;
import com.redhat.ceylon.common.FileUtil;
import com.redhat.ceylon.common.OSUtil;
import com.redhat.ceylon.common.tool.ToolLoader;

/******************************************************
 * IMPORTANT There are two exact copies of this file
 * that need to be kept in sync! One in ceylon-compiler
 * (com.redhat.ceylon.launcher) and on in ceylon-common
 * (com.redhat.ceylon.common.tool)
 ******************************************************/

/**
 * Finds command plugins for the "ceylon" command 
 */
public class PathPlugins {
    private Set<String> pathPlugins;

    public Set<String> getPathPlugins() {
        if(pathPlugins == null){
            pathPlugins = new TreeSet<String>();
            findPathPlugins();
        }
        return pathPlugins;
    }

    private void findPathPlugins() {
        Set<String> names = new HashSet<String>();
        // First in the project dir hierarchy, that is ./.ceylon/bin and  ./.ceylon/bin/{moduleName}/
        // and then going up into the parent folder until we reach the root
        File projectDir = (new File("")).getAbsoluteFile();
        while (projectDir != null) {
            File configBin = new File(new File(projectDir, Constants.CEYLON_CONFIG_DIR), Constants.CEYLON_BIN_DIR);
            findPathPlugins(configBin, names);
            projectDir = projectDir.getParentFile();
        }
        // Then look in ~/.ceylon/bin and ~/.ceylon/bin/{moduleName}/
        File defUserDir = new File(FileUtil.getDefaultUserDir(), Constants.CEYLON_BIN_DIR);
        findPathPlugins(defUserDir, names);
        // Then look in /etc/ceylon/bin and /etc/ceylon/bin/{moduleName}/
        // (or their equivalents on Windows and MacOS)
        File systemDir = new File(FileUtil.getSystemConfigDir(), Constants.CEYLON_BIN_DIR);
        findPathPlugins(systemDir, names);
        // Then the ones from CEYLON_HOME/bin and CEYLON_HOME/bin/{moduleName}/
        File ceylonHome = FileUtil.getInstallDir();
        if (ceylonHome != null) {
            findPathPlugins(new File(ceylonHome, Constants.CEYLON_BIN_DIR), names);
        }
        // And finally in the user's PATH
        File[] paths = FileUtil.getExecPath();
        for (File part : paths) {
            findPluginInPath(part, names);
        }
    }

    private void findPathPlugins(File dir, Set<String> names) {
        // Look in dir 
        findPluginInPath(dir, names);
        // And in every installed script plugin in <dir>/{moduleName}/
        if(dir.isDirectory() && dir.canRead()){
            for(File scriptPluginDir : dir.listFiles()){
                if(scriptPluginDir.isDirectory()){
                    findPluginInPath(scriptPluginDir, names);
                }
            }
        }
    }

    private void findPluginInPath(File dir, final Set<String> names) {
        if(dir.isDirectory() && dir.canRead()){
            // listing /usr/bin with >2k entries takes about 100ms using File.listFiles(Filter) and 39ms with NIO2
            // and checking for file name before file type
            DirectoryStream.Filter<Path> filter = new DirectoryStream.Filter<Path>() {
                public boolean accept(Path f) throws IOException {
                    String fileName = f.getFileName().toString();
                    if(fileName.toLowerCase().startsWith("ceylon-") && Files.isRegularFile(f)){
                        String name = fileName.substring(7);
                        // Is it a plugin file?
                        if(name.toLowerCase().endsWith(".plugin")){
                            name = name.substring(0, name.length()-7);
                            // we're good if it's unique
                            if (names.add(name)) {
                                pathPlugins.add(ToolLoader.PLUGIN_PREFIX+f.toAbsolutePath().toString());
                                return true;
                            }
                        }
                        // Is is a shell/batch script?
                        if(Files.isExecutable(f)){
                            if(OSUtil.isWindows()){
                                // script must end with ".bat"
                                if(!name.toLowerCase().endsWith(".bat"))
                                    return false;
                                // strip it
                                name = name.substring(0, name.length()-4);
                            }
                            // refuse any name with dots in there (like ceylon-completion.bash)
                            if(name.indexOf('.') != -1)
                                return false;
                            // also refuse ceylon-sh-setup
                            if(name.equalsIgnoreCase("sh-setup"))
                                return false;
                            // we're good if it's unique
                            if (names.add(name)) {
                                pathPlugins.add(ToolLoader.SCRIPT_PREFIX+f.toAbsolutePath().toString());
                                return true;
                            }
                        }
                    } else {
                        
                    }
                    return false;
                }
            };
            
            DirectoryStream<Path>  stream = null;
            try {
                stream = Files.newDirectoryStream(dir.toPath(), filter);
                for(@SuppressWarnings("unused") Path sub : stream){
                    // Nothing to do, just iterating
                }
            } catch (IOException e) {
                e.printStackTrace();
                // too bad, give up
            } finally {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                    }
                }
            }
        }
    }

}
