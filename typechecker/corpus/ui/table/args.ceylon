Table args {
	title = "Command line named arguments";
	rows = process.switches.size;
	border = Border(1,1);
	Column {
	    heading = "name";
		width = 5;
        String content(Natural row) {
            return process.switches[row]?.key;
        }
	},
	Column {
	    heading = "value";
		width = 5;
        String content(Natural row) {
            return process.switches[row]?.value ? "";
        }
	}
}