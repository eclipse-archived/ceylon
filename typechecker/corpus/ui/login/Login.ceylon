import org.ceylon.ui { ... }

doc "The application login screen"
shared Window login(Session session) {
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
						onInit = get(session.username);
						onUpdate = set(session.username);
					},
					TextInput {
						size = 15;
						void onUpdate(String value) {
							session.hashedPassword:=Util.hash(value); 
						}
					}
				}
			}
		},
		Button {
			description="Sign in";
			void onClick() {
				close();
				session.begin();
			}
		}
	}
}