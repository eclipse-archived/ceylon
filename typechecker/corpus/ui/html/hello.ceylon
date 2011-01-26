import ceylon.html { ... }

doc "A web page that displays a greeting" 
page '/hello.html' 
Html hello(Request request) {
	Head head { 
		title = "Hello World"; 
		cssStyleSheet = 'hello.css';
	}
	Body body { 
		Div {
			cssClass = "greeting"; 
			Hello( request.parameters["name"] ).greeting
		}, 
		Div {
			cssClass = "footer"; 
			"Powered by Ceylon"
		}
	}
}