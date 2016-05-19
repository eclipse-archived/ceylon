package com.redhat.ceylon.cmr.impl;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.CmrRepository;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.spi.Node;
import com.redhat.ceylon.cmr.spi.OpenNode;
import com.redhat.ceylon.model.cmr.ArtifactResult;
import com.redhat.ceylon.model.cmr.ArtifactResultType;
import com.redhat.ceylon.model.cmr.RepositoryException;

import net.minidev.json.JSONValue;

public class NpmRepository extends AbstractRepository {

    public static final String NAMESPACE = "npm";
    
    public NpmRepository(OpenNode root) {
        super(root);
    }

    @Override
    public String getNamespace() {
        return NAMESPACE;
    }

    protected List<String> getDefaultParentPathInternal(ArtifactContext context) {
        //npm simply creates a dir with the module name and puts files inside
        final String name = context.getName();
        //TODO is it ok to run npm install here?
        //This is such an ugly hack. I'm pretty sure this doesn't belong here
        //but I have no idea where this code goes
        Node kid = getRoot().getChild(name);
        if (kid == null) {
            try {
                final File parent = getRoot().getContent(File.class).getParentFile();
                System.out.println("installing " + name + "@" + context.getVersion() + " in " + parent);
                ProcessBuilder pb = new ProcessBuilder().command("/usr/local/bin/npm", "install", name + "@" + context.getVersion())
                    .directory(parent).inheritIO();
                String path = pb.environment().get("PATH");
                if (path == null) {
                    path = "/usr/local/bin";
                } else if (!path.contains("/usr/local/bin")) {
                    path = path + ":/usr/local/bin";
                }
                pb.environment().put("PATH", path);
                pb.start().waitFor();
                kid = getRoot().addNode(name);
            } catch (InterruptedException | IOException ex) {
                System.out.println("npm died or something");
                ex.printStackTrace();
            }
        }
        return Collections.singletonList(name);
    }

    public String[] getArtifactNames(ArtifactContext context) {
        final String name = context.getName();
        Node kid = getRoot().getChild(name);
        try {
            File dir = kid.getContent(File.class);
            File json = new File(dir, "package.json");
            if (json.exists() && json.isFile() && json.canRead()) {
                //TODO parse json, get "main", that's the file we need
                try (FileReader reader = new FileReader(json)){
                    @SuppressWarnings("unchecked")
                    Map<String,Object> descriptor = (Map<String,Object>)JSONValue.parse(reader);
                    Object main = descriptor.get("main");
                    if (main instanceof String) {
                        File mainFile = new File(dir, (String)main);
                        kid = kid.getChild(mainFile.getName());
                        if (kid != null) {
                            context.toNode(kid);
                            return new String[]{ mainFile.getName() };
                        }
                    } else {
                        System.out.println("WTF 'main' is " + main);
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println("Trying to read package.json");
        }
        return new String[0];
    }

    @Override
    protected ArtifactResult getArtifactResultInternal(
            RepositoryManager manager, Node node) {
        ArtifactContext context = ArtifactContext.fromNode(node);
        return new NpmArtifactResult(this, manager, context.getName(), context.getVersion(), node);
    }

    @Override
    public String getDisplayString() {
        return "[NPM] " + super.getDisplayString();
    }

    private static class NpmArtifactResult extends AbstractArtifactResult {
        private Node node;

        private NpmArtifactResult(CmrRepository repository, RepositoryManager manager, String name, String version, Node node) {
            super(repository, name, version);
            this.node = node;
            System.out.println("NPM artifact " + repository + ", " + name + " , " + version + ": " + node);
        }

        @Override
        public String namespace() {
            return NAMESPACE;
        }

        @Override
        public ArtifactResultType type() {
            return ArtifactResultType.OTHER;
        }

        @Override
        protected File artifactInternal() throws RepositoryException {
            try {
                return node.getContent(File.class);
            } catch (IOException e) {
                throw new RepositoryException(e);
            }
        }

        @Override
        public List<ArtifactResult> dependencies() throws RepositoryException {
            //Get the package.json file
            return Collections.emptyList(); // dunno how to grab deps
        }
        
        @Override
        public String repositoryDisplayString() {
            return NodeUtils.getRepositoryDisplayString(node);
        }
    }
}
