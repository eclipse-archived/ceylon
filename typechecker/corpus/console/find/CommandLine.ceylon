doc "A utility class for parsing unix-style
     command-line arguments."
public class CommandLine(Process process) {

	OpenMap<String,String> namedArgs = HashMap<String,String>();
	OpenList<String> listedArgs = ArrayList<String>();
	
	do (Iterator<String> tokens = process.args.iterator) 
	while (tokens.more) {
		String token = tokens.next();
		if ( '-w*'.matches(token) ) {
			String name = token.substring(1);
			if (tokens.more) {
				namedArgs[name]:=tokens.next();
			}
			else {
				log.error("No parameter specified for ${name}.");
				process.exit(1);
			}
		}
		else {
			paths.add(tokens.next());
		}
	}
	
	doc "Named arguments given in the form 
		 |-name value| at the command line."
	public Map namedArguments {
		return namedArgs;
	}
	
	doc "Arguments listed at the command line."
	public List listedArguments {
		return listedArgs;
	}

}