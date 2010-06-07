doc "A simple number guessing game"
class Guess(Process process) {

	Natural number = RandomNatural(1..100).next();
		
	mutable Number min := 1;
	mutable Number max := 100;
	
	for ( Natural count in (0..9).reversed ) {

		process.writeLine("I'm thinking of a number between " min " and " max ".");
		process.writeLine("You have " count " guesses.")

		try {
			String input = process.readLine();
			Natural guess = input.parseNatural();
			Comparison comparison = number <=> guess;
			if (comparison.equal) {
				process.writeLine("You guessed right!");
				break true
			}
			else {
				process.writeLine("The number is " comparison ".");
				if (comparison.smaller && guess<max) {
					max := guess;
				}
				else if (comparison.larger && guess>min) {
					min := guess;
				}
			}
		}
		catch (NumberFormatException nfe) {
			process.writeLine("Please enter a number.");
			retry
		}
		
	}
	fail {
		process.writeLine("You ran out of guesses!");
	}
	
	process.writeLine("Game over.");
		
}