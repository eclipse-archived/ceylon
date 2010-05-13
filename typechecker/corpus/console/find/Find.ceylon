doc "A command-line utility for searching for regular
     expression matches in files."
class Find(Process process) {
	
	try {
	
 		CommandLine commandLine = CommandLine(process);
 	
		List<String> paths = commandLine.listedArguments;
		String pattern = commandLine.namedArguments["pattern"];
		
		for ( File file in files(paths) ) {
			do (Stream stream = file.stream)
			while (stream.more) {
				String line = stream.readLine();
				if ( pattern.matches(line) ) {
					log.info("${file.name}:${stream.currentLineNumber} ${line}");
				}
			}
		}
	
	
		List<File> files(String... paths) {
			OpenList<File> files = ArrayList<File>();
			if (paths.empty) {
				log.error("No paths specified.");
				process.exit(1);
			}
			else {
				for (String path in paths) {
					files.add( Path(path).files );
				}
			}
			return files;
		}

 	}
 	catch (Exception e) {
 		log.error(e.message);
 		process.exit(1);
 	}
 	
}