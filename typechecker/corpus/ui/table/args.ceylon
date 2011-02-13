Table args {
	title = "Command line named arguments";
	rows = process.switches.size;
	border = Border(1,1);
	Column {
		title = "name";
		width = 5;
        String content(Natural row) {
            return process.switches[row].name;
        }
	},
	Column {
		title = "value";
		width = 5;
        String content(Natural row) {
            return process.switches[row].value ? "";
        }
	}
}