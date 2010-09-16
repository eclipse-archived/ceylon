doc "A utility class for parsing unix-style
     command-line arguments."
public class CommandLine(Process process) {

	public alias NamedArguments satisfies Map<String, String>;
	public alias ListedArguments satisfied List<String>;

	OpenMap<String,String> namedArgs = HashMap<String,String>();
	OpenList<String> listedArgs = ArrayList<String>();
	
	Iterator<String> tokens = process.args.iterator();
	while (tokens.more) {
		String token = tokens.next();
		if ( token.first == `-` ) {
			String name = token[1...];
			if (tokens.more) {
				namedArgs[name]:=tokens.next();
			}
			else {
				throw Exception("No parameter specified for " name ".");
			}
		}
		else {
			paths.add(tokens.next());
		}
	}
	
	doc "Named arguments given in the form 
		 |-name value| at the command line."
	public NamedArguments namedArguments {
		return namedArgs;
	}
	
	doc "Arguments listed at the command line."
	public ListedArguments listedArguments {
		return listedArgs;
	}

}