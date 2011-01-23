doc "A thing which can move between locations."
shared abstract class Thing(String name, String description, Location location) {
	shared String name = name;
	shared String description = description;
	variable shared Location location := location;
}
