/**
 * Areas for users to login/signup accounts
 * @authors: Hengsocheat Pok, Parker Hines
 */


package controller_view;
 
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
 
public class LoginCreateAccountPane extends AnchorPane {
    private Label usernameLabel = new Label("Username: ");
    private Label passwordLabel = new Label("Password: ");
    private TextField usernameTextField;
    private PasswordField passwordField;
    private CheckBox passwordCheckBox;
    private TextField fieldWithActualPassword;
    private int type;
 
    public LoginCreateAccountPane(int type) { // type = 1 or 2 or 3
 
        this.type = type;
        // for the signUp pane
        if (this.type == 2){
            createSignUpPane();
        }
        else if (this.type == 1){
            createLogInPane();
        }
    }
 
    private void createSignUpPane() {
        // init:
        usernameTextField = new TextField();
        passwordField = new PasswordField();
        passwordCheckBox = new CheckBox("Show Password");
        fieldWithActualPassword = new TextField();
        
 
        // add to the anchor pane, the mid area of the GUI: username label
        AnchorPane.setTopAnchor(usernameLabel, 20.00);
        AnchorPane.setLeftAnchor(usernameLabel, 100.00);
        // add to the anchor pane, the mid area of the GUI: username text field
        AnchorPane.setTopAnchor(usernameTextField, 15.00);
        AnchorPane.setLeftAnchor(usernameTextField, 250.00);
        // add to the anchor pane, the mid area of the GUI: password label
        AnchorPane.setTopAnchor(passwordLabel, 60.00);
        AnchorPane.setLeftAnchor(passwordLabel, 100.00);
        // add to the anchor pane, the mid area of the GUI: password text field
        AnchorPane.setTopAnchor(passwordField, 60.00);
        AnchorPane.setLeftAnchor(passwordField, 250.00);
        // add to the anchor pane, the mid area of the GUI: text field with non-disguised password
        AnchorPane.setTopAnchor(fieldWithActualPassword, 60.00);
        AnchorPane.setLeftAnchor(fieldWithActualPassword, 250.00);
        fieldWithActualPassword.setVisible(false);
        // add to the anchor pane, the mid area of the GUI: password toggle button
        AnchorPane.setTopAnchor(passwordCheckBox, 65.00);
        AnchorPane.setLeftAnchor(passwordCheckBox, 410.00);
 
 
        // when the button is clicked, the password is revealed/disguised
        passwordCheckBox.setOnAction(event -> {
            if (passwordCheckBox.isSelected()) {
                fieldWithActualPassword.setText(passwordField.getText());
                fieldWithActualPassword.setVisible(true);
                passwordField.setVisible(false);
            } else {
                passwordField.setText(fieldWithActualPassword.getText());
                passwordField.setVisible(true);
                fieldWithActualPassword.setVisible(false);
            }
        });
 
        //change the labels:
        usernameLabel.setText("Enter a new username: ");
        passwordLabel.setText("Enter a new password: ");
        this.getChildren().addAll(
                usernameLabel,
                usernameTextField,
                passwordLabel,
                passwordField,
                fieldWithActualPassword,
                passwordCheckBox
        );
    }
 
    private void createLogInPane() {
        // init:
        usernameTextField = new TextField();
        passwordField = new PasswordField();
        passwordCheckBox = new CheckBox("Show Password");
        fieldWithActualPassword = new TextField();
 
        // add to the anchor pane, the mid area of the GUI: username label
        AnchorPane.setTopAnchor(usernameLabel, 20.00);
        AnchorPane.setLeftAnchor(usernameLabel, 250 + 100.00);
        // add to the anchor pane, the mid area of the GUI: username text field
        AnchorPane.setTopAnchor(usernameTextField, 15.00);
        AnchorPane.setLeftAnchor(usernameTextField, 250 + 200.00);
        // add to the anchor pane, the mid area of the GUI: password label
        AnchorPane.setTopAnchor(passwordLabel, 60.00);
        AnchorPane.setLeftAnchor(passwordLabel, 250 + 100.00);
        // add to the anchor pane, the mid area of the GUI: password text field
        AnchorPane.setTopAnchor(passwordField, 60.00);
        AnchorPane.setLeftAnchor(passwordField, 250 + 200.00);
        // add to the anchor pane, the mid area of the GUI: text field with non-disguised password
        AnchorPane.setTopAnchor(fieldWithActualPassword, 60.00);
        AnchorPane.setLeftAnchor(fieldWithActualPassword, 250 + 200.00);
        fieldWithActualPassword.setVisible(false);
        // add to the anchor pane, the mid area of the GUI: password toggle button
        AnchorPane.setTopAnchor(passwordCheckBox, 65.00);
        AnchorPane.setLeftAnchor(passwordCheckBox, 250 + 360.00);
 
        // when the button is clicked, the password is revealed/disguised
        passwordCheckBox.setOnAction(event -> {
            if (passwordCheckBox.isSelected()) {
                fieldWithActualPassword.setText(passwordField.getText());
                fieldWithActualPassword.setVisible(true);
                passwordField.setVisible(false);
            } else {
                passwordField.setText(fieldWithActualPassword.getText());
                passwordField.setVisible(true);
                fieldWithActualPassword.setVisible(false);
            }
        });
 
        this.getChildren().addAll(
                usernameLabel,
                usernameTextField,
                passwordLabel,
                passwordField,
                fieldWithActualPassword,
                passwordCheckBox
        );
    }
 
    public int getType (){
        return type;
    }
 
    public TextField getUsernameTextField(){
        return usernameTextField;
    }
 
    public PasswordField getPasswordField(){
 
        if (fieldWithActualPassword.isVisible())
            passwordField.setText(fieldWithActualPassword.getText());
        return passwordField;
    }
    
    public void switchToDark() {
	usernameLabel.setStyle("-fx-text-fill: rgb(235, 235, 235);");
	passwordLabel.setStyle("-fx-text-fill: rgb(235, 235, 235);");
	passwordCheckBox.setStyle("-fx-text-fill: rgb(235, 235, 235);");
    }
 
 
}