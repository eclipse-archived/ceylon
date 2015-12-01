package com.redhat.ceylon.langtools.tools.javac.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.redhat.ceylon.javax.tools.JavaFileObject;
import com.redhat.ceylon.langtools.tools.javac.file.JavacFileManager.Archive;
import com.redhat.ceylon.langtools.tools.javac.file.RelativePath.RelativeDirectory;
import com.redhat.ceylon.langtools.tools.javac.util.List;
import com.redhat.ceylon.langtools.tools.javac.util.ListBuffer;

public class JImageZipFile implements Archive {

	public class JImageFileObject extends BaseFileObject {

		private Path path;

		public JImageFileObject(Path path) {
			super(JImageZipFile.this.fileManager);
			this.path = path;
		}

		@Override
		public URI toUri() {
			return path.toUri();
		}

		@Override
		public String getName() {
			return path.toString();
		}

		@Override
		public InputStream openInputStream() throws IOException {
			return Files.newInputStream(path);
		}

		@Override
		public OutputStream openOutputStream() throws IOException {
            throw new UnsupportedOperationException();
		}

		@Override
		public Kind getKind() {
            return getKind(getName());
		}

		@Override
		public boolean isNameCompatible(String cn, Kind k) {
            cn.getClass();
            // null check
            if (k == Kind.OTHER && getKind() != k) {
                return false;
            }
            return getName().equals(cn + k.extension);
		}

		@Override
		public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
            CharBuffer cb = fileManager.getCachedContent(this);
            if (cb == null) {
                InputStream in = openInputStream();
                try {
                    ByteBuffer bb = fileManager.makeByteBuffer(in);
                    JavaFileObject prev = fileManager.log.useSource(this);
                    try {
                        cb = fileManager.decode(bb, ignoreEncodingErrors);
                    } finally {
                        fileManager.log.useSource(prev);
                    }
                    fileManager.recycleByteBuffer(bb);
                    if (!ignoreEncodingErrors) {
                        fileManager.cache(this, cb);
                    }
                } finally {
                    in.close();
                }
            }
            return cb;
		}

		@Override
		public Writer openWriter() throws IOException {
            throw new UnsupportedOperationException();
		}

		@Override
		public long getLastModified() {
			try {
				return Files.getLastModifiedTime(path).toMillis();
			} catch (IOException e) {
				return 0;
			}
		}

		@Override
		public boolean delete() {
            throw new UnsupportedOperationException();
		}

		@Override
		public String getShortName() {
			return path.toString();
		}

		@Override
		protected String inferBinaryName(Iterable<? extends File> path) {
            String entryName = getName();
            return removeExtension(entryName).replace('/', '.');
		}

		@Override
		public boolean equals(Object other) {
			if(other == null)
				return false;
			if(other instanceof JImageFileObject == false)
				return false;
			return path.equals(((JImageFileObject)other).path);
		}

		@Override
		public int hashCode() {
			return path.hashCode();
		}


	}

	private static class Entry {
		Map<String, Entry> entries = new HashMap<>();
		boolean isFolder;
		String name;
		Entry parent;
		String module;
		
		Entry(Entry parent, String name, boolean isFolder, String module){
			this.module = module;
			this.parent = parent;
			this.name = name;
			this.isFolder = isFolder;
		}

		public void addEntry(Entry entry) {
			entries.put(entry.name, entry);
		}

		public Entry getEntry(String name) {
			return entries.get(name);
		}

		public String getAbsolutePath() {
			return parent != null 
					? parent.getAbsolutePath() + "/" + name
							: "/" + name;
		}

		public String getFullPathWithModule() {
			return "/modules/"+module+getAbsolutePath();
		}
	}
	
	private FileSystem fileSystem;
	private JavacFileManager fileManager;
	private Map<String, Entry> roots = new HashMap<>();
	Map<String, Entry> absoluteEntries = new HashMap<String, Entry>();

	public JImageZipFile(JavacFileManager fm, File fileName) {
		fileManager = fm;
		fileSystem = FileSystems.getFileSystem(URI.create("jrt:/"));
		try {
			loadModules();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void loadModules() throws IOException {
		for(Path module : Files.newDirectoryStream(fileSystem.getPath("/modules"))){
			for(Path root : Files.newDirectoryStream(module)){
				scan(root, null, "", module.getFileName().toString());
			}
		}
	}

	public void scan(Path path, Entry container, String parentPath, String module) throws IOException {
		Entry entry;
		String name = path.getFileName().toString();
		String thisPath = parentPath+name;
		boolean newEntry = false;
		if(Files.isDirectory(path)){
			thisPath += "/";
			if(container != null)
				entry = container.getEntry(name);
			else
				entry = roots.get(name);
			if(entry == null){
				entry = new Entry(container, name, true, module);
				newEntry = true;
			}
			for(Path child : Files.newDirectoryStream(path)){
				scan(child, entry, thisPath, module);
			}
		}else{
			// in theory we can't have a single file more than once
			entry = new Entry(container, path.getFileName().toString(), false, module);
			newEntry = true;
		}
		if(newEntry){
			//System.err.println("Adding path "+thisPath);
			absoluteEntries.put(thisPath, entry);
			if(container != null)
				container.addEntry(entry);
			else
				roots.put(entry.name, entry);
		}
	}

	@Override
	public void close() throws IOException {
		// DO NOT call that: 
		// java.lang.UnsupportedOperationException
		// at jdk.internal.jrtfs.JrtFileSystemProvider$1.close(java.base@9.0/JrtFileSystemProvider.java:130)

//		fileSystem.close();
	}

	@Override
	public boolean contains(RelativePath name) {
		//System.err.println("contains " + name.getPath());
		return absoluteEntries.containsKey(name.getPath());
	}

	@Override
	public JavaFileObject getFileObject(RelativeDirectory subdirectory, String file) {
		//System.err.println("getFileObject " + subdirectory.getPath()+file);
		Entry entry = absoluteEntries.get(subdirectory.getPath()+file);
		if(entry != null && !entry.isFolder){
			String path = entry.getFullPathWithModule();
			//System.err.println(" => " + path);
			return new JImageFileObject(fileSystem.getPath(path));
		}
		return null;
	}

	@Override
	public List<String> getFiles(RelativeDirectory subdirectory) {
		//System.err.println("getFiles " + subdirectory.getPath());
		Entry entry = absoluteEntries.get(subdirectory.getPath());
		if(entry != null && entry.isFolder){
	        ListBuffer<String> ret = new ListBuffer<String>();
	        for (String child : entry.entries.keySet()) {
	        	ret.add(child);
	        }
	        return ret.toList();
		}
		return List.nil();
	}

	@Override
	public Set<RelativeDirectory> getSubdirectories() {
		//System.err.println("getSubdirectories");
		final Set<RelativeDirectory> ret = new HashSet<>();
		for(Entry entry : absoluteEntries.values()){
			if(entry.isFolder)
				ret.add(new RelativeDirectory(entry.getAbsolutePath()));
		}
		return ret;
	}

}
