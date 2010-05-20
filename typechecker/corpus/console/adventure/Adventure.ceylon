doc "A text-based adventure game."
package mutable class Adventure(Process process) {

	doc "The player's current location."
	mutable package Location currentLocation := World.initialLocation; 

    doc "A special location for things which 
         the player has picked up." 
	package Location backpack = new Location("your backpack", "Contains the things you have picked up.");
		
	package mutable Natural life := 100;
	
    Random<Natural> rand = RandomNatural(1..10);
	
	package Float backpackWeight {
		return Math.sum( backpack.things*.weight );
	}
	
	package void out(String message) {
		process.writeLine(message);
	}
	
	package Natural random() {
		return rand.next();
	}
	
	void go(String where) {
		//TODO!
	}
	
	void get(String name) {
		optional Thing thing = currentLocation.thing(name);
		if (exists thing) {
			if (is Artifact thing) {
				thing.get(this);
			}
			else {
				out("You can't pick up a ${name}.);
			}
		}
		else {
			out("You don't see a ${name} here.);
		}
	}
	
	void drop(String name) {
		optional Thing thing = backpack.thing(name);
		if (exists thing) {
			if (is Artifact thing) {
				thing.drop(this);
			}
			else {
				out("You can't drop a ${name}.);
			}
		}
		else {
			out("You don't have a ${name}.);
		}
	}
	
	void tell(String name, String where) {
		//TODO!
	}
	
	void kill(String name, String weaponName) {
		optional Thing thing = currentLocation.thing(name);
		if (exists thing) {
			if (is Creature thing) {
				optional Thing weapon = backpack.thing(name);
				if (exists weapon) {
					if (is Artifact weapon) {
						thing.kill(this, weapon);
					}
					else {
						out("You can't fight with a ${weaponName}.");
					}
				}
				else {
					out("You don't have a ${weaponName}.");
				}		
			}
			else {
				out("You can't kill a ${name}.");
			}
		}
		else {
			out("You don't see a ${name} here.");
		}
	}
	
	void use(String name) {
		optional Thing thing = backpack.thing(name);
		if (exists thing) {
			if (is Artifact thing) {
				thing.use(this);
			}
			else {
				out("You can't use a ${name}.");
			}
		}
		else {
			out("You don't have a ${name}.");
		}
	}
	
	while (life>0) {
		String input = process.readLine();
		Iterator<String> tokens = input.tokens();
		try {
			String command = tokens.next();
			switch(command)
			case ("go") {
				go( tokens.next() );
			}
			case ("get") {
				get( tokens.next() );
			}
			case ("drop") {
				drop( tokens.next() );
			}
			case ("use") {
				use( tokens.next() );
			}
			case ("tell") {
				String name = tokens.next();
				String go = tokens.next();
				String where = tokens.next();
				tell(name, where);
			}
			case ("kill") {
				String name = tokens.next();
				String with = tokens.next();
				String weaponName = tokens.next();
				kill(name, weaponName);
			}
			else {
				out("You don't know how to do that.");
			}
		}
		catch (ExhaustedIteratorException eie) {
			out("Give me a bit more information, please!");
		}
	}
	

}