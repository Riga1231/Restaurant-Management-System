package application;


import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class CardProductController implements Initializable{

		@FXML
	    private Button addButton;

	    @FXML
	    private AnchorPane cardProductPane;

	    @FXML
	    private ImageView productImage;

	    @FXML
	    private ProductData prodData;
	    @FXML
	    private Label productName;

	    @FXML
	    private Label productPrice;

	    @FXML
	    private Spinner<Integer> productSpinner;
	    private SpinnerValueFactory<Integer> spin;
	    Image image;
	 
	    private String prodID;
	    private String type;
	    private String prod_date;
	    private String prod_image;
	    
	    private double totalP;
	    double pr;
	    private int qty;
	    
	    PreparedStatement statement;
	    ResultSet result;
	    Connection connection;
	    
	    public void setData(ProductData prodData) {
	        this.prodData = prodData;

	        prod_image= prodData.getImage();
	        prod_date = String.valueOf(prodData.getDate());
	        type = prodData.getType();
	        prodID = prodData.getProductId();
	        productName.setText(prodData.getProductName());
	        productPrice.setText("$" + String.valueOf(prodData.getPrice()));
	        String path = "File:" + prodData.getImage();
	        image = new Image(path, 200, 135, false, true);
	        productImage.setImage(image);
	        pr = prodData.getPrice();
	    }

	    public void setQuantity() {
	        spin = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0);
	        productSpinner.setValueFactory(spin);
	    }
	    public void add() {

	        MainFormController mForm = new MainFormController();
	        mForm.customerID();

	        qty = productSpinner.getValue();
	        String check = "";
	        String checkAvailable = "SELECT status FROM product WHERE productId = '"
	                + prodID + "'";

	        connection = Database.getConnection();

	        try {
	            int checkStck = 0;
	            String checkStock = "SELECT stock FROM product WHERE productId = '"
	                    + prodID + "'";

	            statement = connection.prepareStatement(checkStock);
	            result = statement.executeQuery();

	            if (result.next()) {
	                checkStck = result.getInt("stock");
	            }
	            
	            if(checkStck == 0){
	                
	                String updateStock = "UPDATE product SET productName = '"
	                            + productName.getText() + "', type = '"
	                            + type + "', stock = 0, price = " + pr
	                            + ", status = 'Unavailable', image = '"
	                            + prod_image + "', date = '"
	                            + prod_date + "' WHERE productId = '"
	                            + prodID + "'";
	                statement = connection.prepareStatement(updateStock);
	                statement.executeUpdate();
	                
	            }

	            statement = connection.prepareStatement(checkAvailable);
	            result = statement.executeQuery();

	            if (result.next()) {
	                check = result.getString("status");
	            }
	            Alert alert;
	            if (!check.equals("Available") || qty == 0) {
	                alert = new Alert(AlertType.ERROR);
	                alert.setTitle("Error Message");
	                alert.setHeaderText(null);
	                alert.setContentText("Something Wrong :3");
	                alert.showAndWait();
	            } else {

	                if (checkStck < qty) {
	                    alert = new Alert(AlertType.ERROR);
	                    alert.setTitle("Error Message");
	                    alert.setHeaderText(null);
	                    alert.setContentText("Invalid. This product is out of stock");
	                    alert.showAndWait();
	                } else {
	                    prod_image = prod_image.replace("\\", "\\\\");
	                    
	                    String insertData = "INSERT INTO customer "
	                            + "(customerId, productId, productName, type, quantity, price, date, employeeName) "
	                            + "VALUES(?,?,?,?,?,?,?,?)";
	                    statement = connection.prepareStatement(insertData);
	                    statement.setString(1, String.valueOf(Data.customerId));
	                    statement.setString(2, prodID);
	                    statement.setString(3, productName.getText());
	                    statement.setString(4, type);
	                    statement.setString(5, String.valueOf(qty));

	                    totalP = (qty * pr);
	                    statement.setString(6, String.valueOf(totalP));

	                    Date date = new Date();
	                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());
	                    statement.setString(7, String.valueOf(sqlDate));

	                  
	                    statement.setString(8, Data.username);

	                    statement.executeUpdate();

	                    int upStock = checkStck - qty;

	                    

	                    String updateStock = "UPDATE product SET productName = '"
	                            + productName.getText() + "', type = '"
	                            + type + "', stock = " + upStock + ", price = " + pr
	                            + ", status = '"
	                            + check + "', image = '"
	                            + prod_image + "', date = '"
	                            + prod_date + "' WHERE productId = '"
	                            + prodID + "'";
	                    
	                    statement = connection.prepareStatement(updateStock);
	                    statement.executeUpdate();
	                    mForm.menuGetTotal();
	                    FXMLLoader loader = new FXMLLoader(getClass().getResource("MainForm.fxml"));
	                    Parent root = loader.load(); // Load the FXML file and retrieve the root node

	                    MainFormController mc = loader.getController(); // Retrieve the controller after loading the FXML file
	                    mc.setPage(); // Call the setPage() method on the controller

	                    Scene scene = new Scene(root); // Create a new scene with the loaded root node
	                    Stage stage = (Stage) addButton.getScene().getWindow(); // Accessing the current stage
	                    stage.setScene(scene); // Set the scene to display the main form
	                    stage.show(); // Show the stage

	                    
	                }
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	    }

		public void initialize(URL arg0, ResourceBundle arg1) {
			setQuantity();
			
		}
}
