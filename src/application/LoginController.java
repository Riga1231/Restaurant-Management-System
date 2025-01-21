package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;

import animatefx.animation.ZoomIn;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class LoginController implements Initializable {

	@FXML
	private Circle exit;
    @FXML
    private Button createButton1;
    @FXML
    private Button signupButton;
    @FXML
    private Button createButton2;
    @FXML
    private Button forgotPassword;
    @FXML
    private Button loginButton;
    @FXML
    private Button nextButton;
    @FXML
    private Button getStarted;
    @FXML
    private Button toSignupButton;
    @FXML
    private Button back;
    @FXML
    private Button changePassword;
    @FXML
    private PasswordField loginPassword;
    @FXML
    private PasswordField signupPassword;
    @FXML
    private TextField newPassword;
    @FXML
    private TextField confirmPassword;
    @FXML
    private TextField forgotUsername;
    @FXML
    private TextField pin11;
    @FXML
    private TextField pin22;
    @FXML
    private TextField pin33;
    @FXML
    private TextField pin44;
    @FXML
    private TextField loginUsername;
    @FXML
    private TextField signupUsername;
    @FXML
    private TextField pin1;
    @FXML
    private TextField pin2;
    @FXML
    private TextField pin3;
    @FXML
    private TextField pin4;
    @FXML
    private AnchorPane sidePane;
    @FXML
    private AnchorPane loginPane;
    @FXML
    private AnchorPane signupPane;
    @FXML
    private AnchorPane forgotPane1;
    @FXML
    private AnchorPane forgotPane2;
    @FXML
    private AnchorPane getstartedPane;
    
    Connection connection;
    PreparedStatement statement;
    ResultSet result;
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	public void exit() {
		Platform.exit();
	}
	public void switchForm(ActionEvent e) {
	    TranslateTransition slider = new TranslateTransition();
	    if (e.getSource() == createButton1) {
	        slider.setNode(sidePane);
	        slider.setDuration(Duration.seconds(0.5)); 
	        slider.setToX(400);
	    	new ZoomIn(signupPane).play();
	        slider.setOnFinished((ActionEvent ee) -> {
	        	createButton2.setVisible(true);
	        	createButton1.setVisible(false);
	        });
	        slider.play();
	    }else if(e.getSource() == createButton2) {	
	        slider.setNode(sidePane);
	        slider.setDuration(Duration.seconds(0.5)); 
	        slider.setToX(0);
	        new ZoomIn(loginPane).play();
	        slider.setOnFinished((ActionEvent ee) -> {
	        	createButton2.setVisible(false);
	        	createButton1.setVisible(true);
	        });
	        slider.play();
	    }
	}
	String suUsername;
	String suPassword;
	public void next() throws SQLException {
		  if (signupPassword.getText().isEmpty() || signupUsername.getText().isEmpty()) {
		        Alert alert = new Alert(AlertType.ERROR);
		        alert.setContentText("Please fill in all the fields.");
		        alert.show();
		        return;
		  }else {
			  	suUsername = signupUsername.getText();
			  	suPassword = signupPassword.getText();
			  	connection = Database.getConnection();
			  	statement = connection.prepareStatement("SELECT username FROM employee WHERE username = ?");
			  	statement.setString(1, signupUsername.getText());
			  	result = statement.executeQuery();
			  	if (result.next()) {
			  		Alert alert = new Alert(AlertType.ERROR);
		            alert.setContentText("Username already exists.");
		            alert.show();
			  	}else{
			  		signupUsername.setText("");
			  		signupPassword.setText("");
			  		signupPane.setVisible(false);
				  	getstartedPane.setVisible(true);
					new ZoomIn(getstartedPane).play(); 
			  	}
		  }
	}
	public void signup() throws SQLException {
		String pin = pin1.getText() + pin2.getText() + pin3.getText() + pin4.getText();
	    if (pin1.getText().isEmpty() || pin2.getText().isEmpty() || pin3.getText().isEmpty() || pin4.getText().isEmpty()) {
	        Alert alert = new Alert(AlertType.ERROR);
	        alert.setContentText("Please fill in all the fields.");
	        alert.show();
	        return;
	    }else {
	    	connection = Database.getConnection();
	    	statement = connection.prepareStatement("INSERT INTO employee (username, password, pin, date) VALUES (?,?,?,?)");
    		statement.setString(1, suUsername);
    		statement.setString(2, suPassword);
    		statement.setString(3, pin);
    		Date date = new Date();
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            statement.setString(4, String.valueOf(sqlDate));
            statement.executeUpdate();
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setContentText("Successfully signed up.");
            alert.show();
            TranslateTransition slider = new TranslateTransition();
            slider.setNode(sidePane);
            slider.setDuration(Duration.seconds(0.5));
            slider.setToX(0);
            getstartedPane.setVisible(false);
            signupPane.setVisible(true);
            pin1.setText("");
            pin2.setText("");
            pin3.setText("");
            pin4.setText("");
            new ZoomIn(signupPane).play();
            slider.setOnFinished((ActionEvent ee) -> {
                createButton2.setVisible(false);
                createButton1.setVisible(true);
            });
            slider.play();
	    }
	 
	}
	double xOffset, yOffset;
	public void login() throws SQLException, IOException {
		 if (loginPassword.getText().isEmpty() || loginUsername.getText().isEmpty()) {
	            Alert alert = new Alert(AlertType.ERROR);
	            alert.setContentText("Please fill in all the fields.");
	            alert.show();
	            return;
	        }else {
	        	connection = Database.getConnection();
	        	statement = connection.prepareStatement("SELECT username, password FROM employee WHERE username = ?");
	        	statement.setString(1, loginUsername.getText());
	        	result = statement.executeQuery();
	        	if (result.next()) {
	        		String retrievedPassword = result.getString("password");
	        		if (retrievedPassword.equals(loginPassword.getText())) {
	        			Data.username = loginUsername.getText();
	                	loginButton.getScene().getWindow().hide();
	                	FXMLLoader loader = new FXMLLoader(getClass().getResource("MainForm.fxml"));
	                	Parent root = loader.load();
	                	Stage stage = new Stage();
	                	root.setOnMousePressed(event -> {
	        	            xOffset = event.getSceneX();
	        	            yOffset = event.getSceneY();
	        	        });
	                	
	        	        root.setOnMouseDragged(event -> {
	        	            stage.setX(event.getScreenX() - xOffset);
	        	            stage.setY(event.getScreenY() - yOffset);
	        	        });
	                	stage.setResizable(false);
	                	Scene scene = new Scene(root);
	                	stage.setScene(scene);
	                	stage.setX(100);
	                	stage.setY(10);
	                	stage.show();

	        		}else {
	        			Alert alert = new Alert(AlertType.ERROR);
		                alert.setContentText("Incorrect Password.");
		                alert.show();
	        		}
	        	}else {
	        		Alert alert = new Alert(AlertType.ERROR);
	                alert.setContentText("Username does not exist.");
	                alert.show();
	        	}
	        }
	}
	public void forgotPassword() {
		loginPane.setVisible(false);
		forgotPane1.setVisible(true);
		new ZoomIn(forgotPane1).play();
	}
	public void toLoginPane() {
		forgotPane1.setVisible(false);
		loginPane.setVisible(true);
		new ZoomIn(loginPane).play();
	}
	public void toForgotPane() {
		forgotPane2.setVisible(false);
		forgotPane1.setVisible(true);
		new ZoomIn(forgotPane1).play();
	}
	String pin;
    String username;
	public void nextForgot() throws SQLException {
		username = forgotUsername.getText();
		pin = pin11.getText() + pin22.getText() + pin33.getText() + pin44.getText();
		if (forgotUsername.getText().isEmpty() || pin.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("Please fill in all the fields.");
            alert.show();
		}else {
			connection = Database.getConnection();
			statement = connection.prepareStatement("SELECT * FROM employee WHERE username = ? AND pin = ?");
			statement.setString(1, forgotUsername.getText());
			statement.setString(2, pin);
			result = statement.executeQuery();
			if (result.next()) {
				forgotPane1.setVisible(false);
				forgotPane2.setVisible(true);
				forgotUsername.setText("");
				pin11.setText("");
				pin22.setText("");
				pin33.setText("");
				pin44.setText("");
				new ZoomIn(forgotPane2).play();
			}else {
				Alert alert = new Alert(AlertType.ERROR);
	            alert.setContentText("Invalid username or PIN");
	            alert.show();
			}
		}
	}
	public void changePassword() throws SQLException {
		if(newPassword.getText().isEmpty() || confirmPassword.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("Please fill in all the fields");
            alert.show();
		}else {
			if (newPassword.getText().equals(confirmPassword.getText())) {
				connection = Database.getConnection();
				statement = connection.prepareStatement("UPDATE employee SET password = ? WHERE username = ? AND pin = ?");
				statement.setString(1, newPassword.getText());
				statement.setString(2, username);
				statement.setString(3, pin);
				statement.executeUpdate();
				 Alert alert = new Alert(AlertType.INFORMATION);
	             alert.setContentText("Password updated successfully.");
	             alert.show();
	             newPassword.setText("");
	             confirmPassword.setText("");
	             forgotPane2.setVisible(false);
	             loginPane.setVisible(true);
	             new ZoomIn(loginPane).play();
			}else {
				Alert alert = new Alert(AlertType.ERROR);
	            alert.setContentText("Passwords do not match");
	            alert.show();
			}
		}	
	}
	public void back() {
		getstartedPane.setVisible(false);
		signupPane.setVisible(true);
		new ZoomIn(signupPane).play();
	}
	
}
