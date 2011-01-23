doc "An inanimate thing that the player 
     can pick up and drop in a different 
     location."
shared class Artifact(String name, String description, Location location, 
								Float weight, Natural damage) 
		extends Thing(name, description, location) {
		
	shared Float weight = weight;
	shared Natural damage = damage;
		
	doc "Get the artifact and put it
	     in the player's backpack."
	shared void get(Adventure game) {
		if (game.backpackWeight + weight < 100) {
			location.remove(this);
			game.backpack.put(this);
			location := game.backpack;
			game.display("You put the " name " in your backpack.);
		}
		else {
			game.display("The " name " is too heavy. Try dropping something else.");
		}
	}

		
	doc "Get the artifact and put it
	     in the player's backpack."
	shared void drop(Adventure game) {
	    if (location == game.backpack) {
			game.backpack.remove(this);
			game.currentLocation.put(this);
			location := game.currentLocation;
			game.display("You dropped the " name ".");
		}
		else {
	    	game.display("You don't have the " name " in your backpack.");
	    }
	}
	
	doc "Override this to do something when
	     the artifact is used."
	shared void use(Adventure game) {}
		
}