doc "The application login screen"
public class Login(Session session, Environment env) extends Window(env) {
	VerticalLayout {
		alignVertically = center;
		alignHorizontally = center;
		margin = 10;
		Panel {
			title = "Your account";
			HorizontalLayout {
				margin = 20;
				VerticalLayout {
					alignVertically=right;
					spacing = 10;
					margin = 5;
					Label("Username"),
					Label("Password")
				},
				VerticalLayout {
					alignVertically=left;
					spacing = 10;
					margin = 5;
					TextInput {
						size = 15;
						model = ref session.username;
					},
					TextInput {
						size = 15;
						model = ref session.password;
					}
				}
			}
		},
		Button {
			description="Sign in";
			onClick() {
				close();
				session.begin();
			}
		}
	}
}