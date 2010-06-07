doc "A more personalized greeting" 
class Hello(Process process) {
	
	sayHello( process.args.firstOrNull ? "World" );
	
	void sayHello(String name) {
		log.info("Hello, " name "!");
	}
	
}