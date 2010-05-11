doc "A text-based adventure game."
package mutable class Adventure(Process process) {

    package Random<Natural> rand = RandomNatural(1..10);
	
	doc "The player's current location."
	mutable package Location currentLocation := World.initialLocation; 

    doc "A special location for things which 
         the player has picked up." 
	package Location backpack = new Location("your backpack", "Contains the things you have picked up.");
		
	package mutable Natural life := 100;
	
	package Float backpackWeight {
		return Math.sum( backpack.things*.weight );
	}
	
	package void out(String message) {
		process.writeLine(message);
	}
	
	package Natural random() {
		return rand.next();
	}
	
	do {
		String input = process.readLine();
		Iterable<String> tokens = input.tokens();
		String command = tokens.next();
		switch(command)
		case ("go") {
			//TODO!
		}
		case ("get") {
			String name = tokens.next();
			optional Thing thing = currentLocation.thing(name);
			if (exists thing) {
				if (is Artifact thing) {
					thing.get(this);
				}
				else {
					out("You can't pick that up.);
				}
			}
			else {
				out("You don't see it here.);
			}
		}
		case ("drop") {
			String name = tokens.next();
			optional Thing thing = backpack.thing(name);
			if (exists thing) {
				if (is Artifact thing) {
					thing.drop(this);
				}
				else {
					out("You can't drop that.);
				}
			}
			else {
				out("You don't have it.);
			}
		}
		case ("tell") {
			//TODO!
		}
		case ("kill") {
			String name = tokens.next();
			optional Thing thing = currentLocation.thing(name);
			if (exists thing) {
				if (is Creature thing) {
					String with = tokens.next();
					String weaponName = tokens.next();
					optional Thing weapon = backpack.thing(name);
					if (exists weapon) {
						if (is Artifact weapon) {
							thing.kill(this, weapon);
						}
						else {
							out("You can't fight with that.);
						}
					}
					else {
						out("You don't have it.);
					}
					
				}
				else {
					out("You can't kill that.);
				}
			}
			else {
				out("You don't see it here.);
			}
		}
		case ("use") {
			String name = tokens.next();
			optional Thing thing = backpack.thing(name);
			if (exists thing) {
				if (is Artifact thing) {
					thing.use(this);
				}
				else {
					out("You can't use that.);
				}
			}
			else {
				out("You don't have it.);
			}
		}
	}
	while (life>0)

}