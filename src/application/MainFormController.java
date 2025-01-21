package application;

import java.io.File;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import animatefx.animation.FadeIn;
import animatefx.animation.SlideInDown;
import animatefx.animation.SlideInLeft;
import animatefx.animation.SlideInRight;
import animatefx.animation.SlideInUp;
import animatefx.animation.ZoomIn;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.stage.FileChooser.ExtensionFilter;

public class MainFormController implements Initializable {

	@FXML
	private Button customerButton, homeButton, importImage, inventoryButton, menuButton, productAdd, productClear,
			productRemove, productUpdate, menuPayButton, removeButton, receiptButton, logout, mealButton, dessertButton, drinkButton;

	@FXML
	private BarChart<String, Float> customerChart;

	@FXML
	private ScrollPane MealPane, DrinkPane,DessertPane;
	@FXML
	private AnchorPane customerPage, homePage, inventoryPage, menuPage, NOC, IT, NOSP, TI, inventorySide, downPane;

	@FXML
	private AreaChart<String, Float> incomeChart;

	@FXML
	private Label incomeToday, numOfCustomers, numOfSoldProduct, usernameLabel, menuTotal, changeLabel, totalIncome, pageLabel;

	@FXML
	private TextField productIdField, productNameField, productPriceField, productStockField, amountField;

	@FXML
	private ImageView productImage;

	@FXML
	private ComboBox<String> productStatusField, productTypeField;
	
	@FXML
	private TableColumn<ProductData, String> productIdColumn, productNameColumn, productTypeColumn, productStockColumn, productPriceColumn, productStatusColumn,
	productDateColumn;

	@FXML
	private TableView<ProductData> productView;
	@FXML
	private TableView<ProductData> menuTableView;
	@FXML
	private TableColumn<ProductData, String> menuNameColumn;
	@FXML
	private TableColumn<ProductData, Integer> menuQuantityColumn;
	@FXML
	private TableColumn<ProductData, Double> menuPriceColumn;
	@FXML
	private GridPane Meals, Drinks, Desserts;
	@FXML
	private TableView<CustomerData> customerView;
	@FXML
	private TableColumn<CustomerData, String> customerIdColumn;
	@FXML
	private TableColumn<CustomerData, String> customerTotalColumn;
	@FXML
	private TableColumn<CustomerData, String> customerDateColumn;
	@FXML
	private TableColumn<CustomerData, String> customerCashierColumn;
	 private ObservableList<ProductData> cardListData = FXCollections.observableArrayList();
	    
	
	Alert alert;
	Connection connection;
	PreparedStatement statement;
	ResultSet result;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		  PauseTransition delay = new PauseTransition(Duration.seconds(1));
		    
		    // Set an event handler to execute when the delay is finished
		    delay.setOnFinished(event -> {
		        // Play the ZoomIn animation after the delay
		    	new ZoomIn(incomeChart).play();
		    	new ZoomIn(customerChart).play();
		    	new ZoomIn(NOC).play();
		    	new ZoomIn(IT).play();
		    	new ZoomIn(TI).play();
		    	new ZoomIn(NOSP).play();
		    	
		    });
		    
		    // Start the delay
		    delay.play();
		customerID();
		customerChart();
		incomeChart();
		NC();
		IT();
		TI();
		NSP();
		try {
			
			menuDisplayCard();
			menuShowOrderData();
			menuDisplayTotal();
			inventoryShowData();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		usernameLabel.setText(Data.username);
		String[] type = { "Meal", "Drink", "Dessert" };
		ObservableList<String> typeList = FXCollections.observableArrayList(type);
		productTypeField.setItems(typeList);
		String[] status = { "Available", "Unavailable" };
		ObservableList<String> statusList = FXCollections.observableArrayList(status);
		productStatusField.setItems(statusList);
	}

	public void switchPage(ActionEvent e) throws SQLException {
		if (e.getSource() == homeButton) {
			new ZoomIn(incomeChart).play();
	    	new ZoomIn(customerChart).play();
	    	new ZoomIn(NOC).play();
	    	new ZoomIn(IT).play();
	    	new ZoomIn(TI).play();
	    	new ZoomIn(NOSP).play();
			customerChart();
			incomeChart();
			NC();
			IT();
			TI();
			NSP();
			pageLabel.setText("DASHBOARD");
			homePage.setVisible(true);
			customerPage.setVisible(false);
			inventoryPage.setVisible(false);
			menuPage.setVisible(false);
		} else if (e.getSource() == customerButton) {
			pageLabel.setText("CUSTOMERS");
			new FadeIn(customerView).play();
			customersShowData();
			homePage.setVisible(false);
			customerPage.setVisible(true);
			inventoryPage.setVisible(false);
			menuPage.setVisible(false);
		} else if (e.getSource() == inventoryButton) {
			pageLabel.setText("INVENTORY");
			new SlideInUp(downPane).play();
	    	new FadeIn(productView).play();
			inventoryShowData();
			homePage.setVisible(false);
			customerPage.setVisible(false);
			inventoryPage.setVisible(true);
			menuPage.setVisible(false);
		} else if (e.getSource() == menuButton) {
			pageLabel.setText("MENU");
			new ZoomIn(mealButton).play();
	    	new ZoomIn(drinkButton).play();
	    	new ZoomIn(dessertButton).play();
	    	new SlideInRight(inventorySide).play();
	    	new FadeIn(MealPane).play();
			menuDisplayCard();
			 menuShowOrderData();
			homePage.setVisible(false);
			customerPage.setVisible(false);
			inventoryPage.setVisible(false);
			menuPage.setVisible(true);
		}
	}
    public void logout() {
        
        try {
            
            alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to logout?");
            Optional<ButtonType> option = alert.showAndWait();
            
            if (option.get().equals(ButtonType.OK)) {

                // TO HIDE MAIN FORM 
                logout.getScene().getWindow().hide();

                // LINK YOUR LOGIN FORM AND SHOW IT 
                Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
                
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                
               
                stage.setScene(scene);
                stage.show();
                
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
	//SELECTION FUNCTION
	  public void inventorySelectData() {
	        
		  	ProductData prodData = productView.getSelectionModel().getSelectedItem();
		  	//checks if a data is selected
	        int num = productView.getSelectionModel().getSelectedIndex();
	        
	        if ((num - 1) < -1) {
	            return;
	        }
	        
	        productIdField.setText(prodData.getProductId());
	        productNameField.setText(prodData.getProductName());
	        productStockField.setText(String.valueOf(prodData.getStock()));
	        productPriceField.setText(String.valueOf(prodData.getPrice()));
	        
	        Data.path = prodData.getImage();
	        
	        String path = "File:" + prodData.getImage();
	        Data.date = String.valueOf(prodData.getDate());
	        Data.id = prodData.getId();
	        
	        Image image = new Image(path, 210, 150, false, true);
	        productImage.setImage(image);
	        productTypeField.setValue(prodData.getType());
	        productStatusField.setValue(prodData.getStatus());
	    }
	  
	//IMAGE IMPORT
	 public void inventoryImport() {
	        
	        FileChooser openFile = new FileChooser();
	        openFile.getExtensionFilters().add(new ExtensionFilter("Open Image File", "*png", "*jpg"));
	        
	        File file = openFile.showOpenDialog(importImage.getScene().getWindow());
	        
	        if (file != null) {
	            
	            Data.path = file.getAbsolutePath();
	            Image image = new Image(file.toURI().toString(), 120, 127, false, true);
	            
	            productImage.setImage(image);
	        }
	    }
	 //ADD AN ITEM IN THE INVENTORY / PROUDCT
	public void inventoryAdd() throws SQLException {

		if (productIdField.getText().isEmpty() || productNameField.getText().isEmpty()
				|| productTypeField.getSelectionModel().getSelectedItem() == null
				|| productStockField.getText().isEmpty() || productPriceField.getText().isEmpty()
				|| productStatusField.getSelectionModel().getSelectedItem() == null || Data.path == null) {

			alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Message");
			alert.setHeaderText(null);
			alert.setContentText("Please fill all blank fields");
			alert.showAndWait();

		} else {
			connection = Database.getConnection();
			statement = connection.prepareStatement("SELECT productId FROM product WHERE productId = ?");
			statement.setString(1, productIdField.getText());
			result = statement.executeQuery();
			if (result.next()) {
				alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Message");
				alert.setHeaderText(null);
				alert.setContentText(productIdField.getText() + " is already taken");
				alert.showAndWait();
			} else {
				statement = connection.prepareStatement(
						"INSERT INTO product (productId, productName, type, stock, price, status, image, date) "
								+ "VALUES(?,?,?,?,?,?,?,?)");
				statement.setString(1, productIdField.getText());
				statement.setString(2, productNameField.getText());
				statement.setString(3, (String) productTypeField.getSelectionModel().getSelectedItem());
				statement.setString(4, productStockField.getText());
				statement.setString(5, productPriceField.getText());
				statement.setString(6, (String) productStatusField.getSelectionModel().getSelectedItem());

				String path = Data.path;
				path = path.replace("\\", "\\\\");

				statement.setString(7, path);

				// TO GET CURRENT DATE
				Date date = new Date();
				java.sql.Date sqlDate = new java.sql.Date(date.getTime());

				statement.setString(8, String.valueOf(sqlDate));

				statement.executeUpdate();

				alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Error Message");
				alert.setHeaderText(null);
				alert.setContentText("Successfully Added!");
				alert.showAndWait();
				inventoryClear();
				inventoryShowData();
			}
		}
	}

	public void inventoryUpdate() throws SQLException {

		if (productIdField.getText().isEmpty() || productNameField.getText().isEmpty()
				|| productTypeField.getSelectionModel().getSelectedItem() == null
				|| productStockField.getText().isEmpty() || productPriceField.getText().isEmpty()
				|| productStatusField.getSelectionModel().getSelectedItem() == null || Data.path == null
				|| Data.id == 0) {

			alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Message");
			alert.setHeaderText(null);
			alert.setContentText("Please fill all blank fields");
			alert.showAndWait();

		} else {

			String path = Data.path;
			path = path.replace("\\", "\\\\");

			String updateData = "UPDATE product SET " + "productId = '" + productIdField.getText() + "', productName = '"
					+ productNameField.getText() + "', type = '"
					+ productTypeField.getSelectionModel().getSelectedItem() + "', stock = '"
					+ productStockField.getText() + "', price = '" + productPriceField.getText() + "', status = '"
					+ productStatusField.getSelectionModel().getSelectedItem() + "', image = '" + path + "', date = '"
					+ Data.date + "' WHERE id = " + Data.id;

			connection = Database.getConnection();

			statement = connection.prepareStatement(updateData);
			statement.executeUpdate();

			alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Error Message");
			alert.setHeaderText(null);
			alert.setContentText("Successfully Updated!");
			alert.showAndWait();

			// TO UPDATE YOUR TABLE VIEW
			inventoryShowData();
			// TO CLEAR YOUR FIELDS
			inventoryClear();

		}
	}

	public void inventoryDelete() throws SQLException {
		if (Data.id == 0) {

			alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Message");
			alert.setHeaderText(null);
			alert.setContentText("Please fill all blank fields");
			alert.showAndWait();

		} else {
			alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Error Message");
			alert.setHeaderText(null);
			alert.setContentText("Are you sure you want to DELETE Product ID: " + productIdField.getText() + "?");
			Optional<ButtonType> option = alert.showAndWait();

			if (option.get().equals(ButtonType.OK)) {
				String deleteData = "DELETE FROM product WHERE id = " + Data.id;

				statement = connection.prepareStatement(deleteData);
				statement.executeUpdate();
				alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("DELETED");
				alert.setHeaderText(null);
				alert.setContentText("successfully Deleted!");
				alert.showAndWait();

				// TO UPDATE YOUR TABLE VIEW
				inventoryShowData();
				// TO CLEAR YOUR FIELDS
				inventoryClear();

			} else {
				alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Message");
				alert.setHeaderText(null);
				alert.setContentText("Cancelled");
				alert.showAndWait();
			}
		}
	}
	
	//CLEAR THE FIELDS INPUT OF INVENTORY
	public void inventoryClear() {
        
        productIdField.setText("");
        productNameField.setText("");
        productTypeField.getSelectionModel().clearSelection();
        productStockField.setText("");
        productPriceField.setText("");
        productStatusField.getSelectionModel().clearSelection();
        Data.path = null;
        Data.id = 0;
        productImage.setImage(null);	
        
    }
	
	//TO RETRIEVE DATA OF PRODUCTS IN SQL
	public ObservableList<ProductData> inventoryDataList() throws SQLException {
	
	        ObservableList<ProductData> listData = FXCollections.observableArrayList();
	        connection = Database.getConnection();
	            statement = connection.prepareStatement("SELECT * FROM product");
	            result = statement.executeQuery();
	            
	            ProductData prodData;
	            
	            while (result.next()) {
	                
	                prodData = new ProductData(result.getInt("id"),
	                        result.getString("productId"),
	                        result.getString("productName"),
	                        result.getString("type"),
	                        result.getInt("stock"),
	                        result.getDouble("price"),
	                        result.getString("status"),
	                        result.getString("image"),
	                        result.getDate("date"));
	                
	                listData.add(prodData);
	    
	            }
	        return listData;
	    }

	    // TO SHOW DATA ON OUR TABLE
	    private ObservableList<ProductData> inventoryListData;
	    public void inventoryShowData() throws SQLException {
	        inventoryListData = inventoryDataList();
	        
	        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
	        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
	        productTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
	        productStockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
	        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
	        productStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
	        productDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
	        
	        productView.setItems(inventoryListData);
	        
	    }
	    
	    public ObservableList<ProductData> menuGetData() {
	  
	        ObservableList<ProductData> listData = FXCollections.observableArrayList();
	        connection = Database.getConnection();
	        
	        try {
	            statement = connection.prepareStatement("SELECT * FROM product");
	            result = statement.executeQuery();
	            
	            ProductData prod;
	            
	            while (result.next()) {
	                prod = new ProductData(result.getInt("id"),
	                        result.getString("productId"),
	                        result.getString("productName"),
	                        result.getString("type"),
	                        result.getInt("stock"),
	                        result.getDouble("price"),
	                        result.getString("image"),
	                        result.getDate("date"));
	                
	                listData.add(prod);
	            }
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        
	        return listData;
	    }
	    
	    public void menuDisplayCard() {
	        cardListData.clear();
	        cardListData.addAll(menuGetData());

	        // Variables to manage row and column for each category
	        int mealRow = 0, mealColumn = 0;
	        int drinkRow = 0, drinkColumn = 0;
	        int dessertRow = 0, dessertColumn = 0;

	        Meals.getChildren().clear();
	        Meals.getColumnConstraints().clear();
	        Meals.getRowConstraints().clear();
	        Drinks.getChildren().clear();
	        Drinks.getColumnConstraints().clear();
	        Drinks.getRowConstraints().clear();
	        Desserts.getChildren().clear();
	        Desserts.getColumnConstraints().clear();
	        Desserts.getRowConstraints().clear();
	      

	        for (int q = 0; q < cardListData.size(); q++) {
	            try {
	                FXMLLoader load = new FXMLLoader();
	                load.setLocation(getClass().getResource("CardProduct.fxml"));
	                AnchorPane pane = load.load();
	                CardProductController cardC = load.getController();
	                cardC.setData(cardListData.get(q));

	                // Determine the target grid and appropriate column and row
	                if (cardListData.get(q).getType().equals("Meal")) {
	                    if (mealColumn == 3) {
	                        mealColumn = 0;
	                        mealRow++;
	                    }
	                    Meals.add(pane, mealColumn++, mealRow);
	                    GridPane.setMargin(pane, new Insets(10));

	                } else if (cardListData.get(q).getType().equals("Drink")) {
	                    if (drinkColumn == 3) {
	                        drinkColumn = 0;
	                        drinkRow++;
	                    }
	                    Drinks.add(pane, drinkColumn++, drinkRow);
	                    GridPane.setMargin(pane, new Insets(10));

	                } else if (cardListData.get(q).getType().equals("Dessert")) {
	                    if (dessertColumn == 3) {
	                        dessertColumn = 0;
	                        dessertRow++;
	                    }
	                    Desserts.add(pane, dessertColumn++, dessertRow);
	                    GridPane.setMargin(pane, new Insets(10));
	                }
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	    }


	    
	    
	    public ObservableList<ProductData> menuGetOrder() {
	        customerID();
	        ObservableList<ProductData> listData = FXCollections.observableArrayList();
	        
	        String sql = "SELECT * FROM customer WHERE customerId = " + cID;
	        
	        connection = Database.getConnection();
	        
	        try {
	            
	            statement = connection.prepareStatement(sql);
	            result = statement.executeQuery();
	            
	            ProductData prod;
	            
	            while (result.next()) {
	            	int quantity = result.getInt("quantity");
	                System.out.println("Quantity: " + quantity); // Debugging statement
	                prod = new ProductData(result.getInt("id"),
	                        result.getString("productId"),
	                        result.getString("productName"),
	                        result.getString("type"),
	                        result.getInt("quantity"),
	                        result.getDouble("price"),
	                        result.getDate("date"));
	                listData.add(prod);
	            }
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        
	        return listData;
	    }
	    private ObservableList<ProductData> menuOrderListData;
	    
	    public void menuShowOrderData() {
	        menuOrderListData = menuGetOrder();
	        
	        menuNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
	        menuQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
	        menuPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
	        
	        menuTableView.setItems(menuOrderListData);
	    }

	    private int getid;
	    
	    public void menuSelectOrder() {
	        ProductData prod = menuTableView.getSelectionModel().getSelectedItem();
	        int num = menuTableView.getSelectionModel().getSelectedIndex();
	        
	        if ((num - 1) < -1) {
	            return;
	        }
	        // TO GET THE ID PER ORDER
	        getid = prod.getId();
	        
	    }
	    private double totalP = 0;
	    
	    public void menuGetTotal() {
	       
	        String total = "SELECT SUM(price) FROM customer WHERE customerId = " + cID;
	        
	        connection = Database.getConnection();
	        
	        try {
	            
	            statement = connection.prepareStatement(total);
	            result = statement.executeQuery();
	            
	            if (result.next()) {
	                totalP = result.getDouble("SUM(price)");
	            }
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        
	    }
	    
	    public void menuDisplayTotal() {
	        menuGetTotal();
	        menuTotal.setText("$" + totalP);
	    }
	    private double amount;
	    private double change;
	    
	    public void menuAmount() {
	        menuGetTotal();
	        if (amountField.getText().isEmpty() || totalP == 0) {
	            alert = new Alert(AlertType.ERROR);
	            alert.setTitle("Error Message");
	            alert.setHeaderText(null);
	            alert.setContentText("Invalid");
	            alert.showAndWait();
	        } else {
	            amount = Double.parseDouble(amountField.getText());
	            if (amount < totalP) {
	                amountField.setText("");
	            } else {
	                change = (amount - totalP);
	                changeLabel.setText("Change: " + change);
	            }
	        }
	        
	    }
	    
	    public void menuPay() {
	        
	        if (totalP == 0) {
	            alert = new Alert(AlertType.ERROR);
	            alert.setTitle("Error Message");
	            alert.setHeaderText(null);
	            alert.setContentText("Please choose your order first!");
	            alert.showAndWait();
	        } else {
	            menuGetTotal();
	            String insertPay = "INSERT INTO receipt (customerId, total, date, employeeName) "
	                    + "VALUES(?,?,?,?)";
	            
	            connection = Database.getConnection();
	            
	            try {
	                
	                if (amount == 0) {
	                    alert = new Alert(AlertType.ERROR);
	                    alert.setTitle("Error Messaged");
	                    alert.setHeaderText(null);
	                    alert.setContentText("Invalid");
	                    alert.showAndWait();
	                } else {
	                    alert = new Alert(AlertType.CONFIRMATION);
	                    alert.setTitle("Confirmation Message");
	                    alert.setHeaderText(null);
	                    alert.setContentText("Are you sure?");
	                    Optional<ButtonType> option = alert.showAndWait();
	                    
	                    if (option.get().equals(ButtonType.OK)) {
	                        customerID();
	                        menuGetTotal();
	                        statement = connection.prepareStatement(insertPay);
	                        statement.setString(1, String.valueOf(cID));
	                        statement.setString(2, String.valueOf(totalP));
	                        
	                        Date date = new Date();
	                        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
	                        
	                        statement.setString(3, String.valueOf(sqlDate));
	                        statement.setString(4, Data.username);
	                        
	                        statement.executeUpdate();
	                        
	                        alert = new Alert(AlertType.INFORMATION);
	                        alert.setTitle("Infomation Message");
	                        alert.setHeaderText(null);
	                        alert.setContentText("Successful.");
	                        alert.showAndWait();
	                        
	                        
	                        menuShowOrderData();
	                        amountField.setDisable(true);
	                        menuPayButton.setDisable(true);
	                        removeButton.setDisable(true);
	                    } else {
	                        alert = new Alert(AlertType.WARNING);
	                        alert.setTitle("Infomation Message");
	                        alert.setHeaderText(null);
	                        alert.setContentText("Cancelled.");
	                        alert.showAndWait();
	                    }
	                }
	                
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	        
	    }
	    public void menuRemove() throws SQLException {
	        if (getid == 0) {
	            // Show error message if no order is selected
	            Alert alert = new Alert(AlertType.ERROR);
	            alert.setTitle("Error Message");
	            alert.setHeaderText(null);
	            alert.setContentText("Please select the order you want to remove");
	            alert.showAndWait();
	        } else {
	       

	                // Retrieve product ID and quantity from the customer table
	                String getProductDetails = "SELECT productId, quantity FROM customer WHERE id = ?";
	                connection = Database.getConnection();
	                statement = connection.prepareStatement(getProductDetails);
	                statement.setInt(1, getid);
	                ResultSet resultSet = statement.executeQuery();

	                if (resultSet.next()) {
	                    String productId = resultSet.getString("productId");
	                    int quantityToRemove = resultSet.getInt("quantity");

	                    // Update the product's stock
	                    String updateStock = "UPDATE product SET stock = stock + ? WHERE productId = ?";
	                    PreparedStatement updateStatement = connection.prepareStatement(updateStock);
	                    updateStatement.setInt(1, quantityToRemove);
	                    updateStatement.setString(2, productId);
	                    updateStatement.executeUpdate();
	                }

	                // Delete the order from the customer table
	                String deleteData = "DELETE FROM customer WHERE id = ?";
	                statement = connection.prepareStatement(deleteData);
	                statement.setInt(1, getid);
	                statement.executeUpdate();

	                // Show confirmation message and update order data
	                Alert alert = new Alert(AlertType.CONFIRMATION);
	                alert.setTitle("Confirmation Message");
	                alert.setHeaderText(null);
	                alert.setContentText("Order removed successfully.");
	                alert.showAndWait();

	                menuShowOrderData();
	        }
	            
	    }
	    public void receipt() throws IOException {
	    	
	    	if (totalP == 0 || amountField.getText().isEmpty()) {
	            alert = new Alert(AlertType.ERROR);
	            alert.setTitle("Error Message");
	            alert.setContentText("Please order first");
	            alert.showAndWait();
	        }else {
	        	  FXMLLoader loader = new FXMLLoader(getClass().getResource("Receipt.fxml"));
	                Parent root = loader.load();
	                
	                // Get the controller and set the customer ID
	                ReceiptController receiptController = loader.getController();
	                receiptController.setData(change); // Replace with the actual customer ID variable

	                // Create a new stage for the receipt
	                Stage stage = new Stage();
	                
	                stage.setTitle("Receipt");
	                stage.setScene(new Scene(root));
	                stage.show();
	                amountField.setDisable(false);
                    menuPayButton.setDisable(false);
                    removeButton.setDisable(false);
	            menuRestart();
	            customerID();
		    	 
	        }
	    	
	    }

	    public void menuRestart() {
	        totalP = 0;
	        change = 0;
	        amount = 0;
	        menuTotal.setText("0.0");
	        amountField.setText("");
	        changeLabel.setText("$0.0");
	    }
	    private int cID;	
	    public void customerID() {
	        
	        String sql = "SELECT MAX(customerId) FROM customer";
	        connection = Database.getConnection();
	        
	        try {
	            statement = connection.prepareStatement(sql);
	            result = statement.executeQuery();
	            
	            if (result.next()) {
	                cID = result.getInt("MAX(customerId)");
	            }
	            
	            String checkCID = "SELECT MAX(customerId) FROM receipt";
	            statement = connection.prepareStatement(checkCID);
	            result = statement.executeQuery();
	            int checkID = 0;
	            if (result.next()) {
	                checkID = result.getInt("MAX(customerId)");
	            }
	            
	            if (cID == 0) {
	                cID += 1;
	            } else if (cID == checkID) {
	                cID += 1;
	            }
	            
	            Data.customerId = cID;
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    public ObservableList<CustomerData> customersDataList() {
	        
	        ObservableList<CustomerData> listData = FXCollections.observableArrayList();
	        String sql = "SELECT * FROM receipt";
	        connection = Database.getConnection();
	        
	        try {
	            
	            statement = connection.prepareStatement(sql);
	            result = statement.executeQuery();
	            CustomerData cData;
	            
	            while (result.next()) {
	                cData = new CustomerData(result.getInt("id"),
	                        result.getInt("customerId"),
	                        result.getDouble("total"),
	                        result.getDate("date"),
	                        result.getString("employeeName"));
	                
	                listData.add(cData);
	            }
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return listData;
	    }
	    
	    private ObservableList<CustomerData> customersListData;
	    
	    public void customersShowData() {
	        customersListData = customersDataList();
	        
	        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
	        customerTotalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));
	        customerDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
	        customerCashierColumn.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
	        
	        customerView.setItems(customersListData);
	    }

	    public void NC() {
	        
	        String sql = "SELECT COUNT(id) FROM receipt";
	        connection = Database.getConnection();
	        
	        try {
	            int nc = 0;
	            statement = connection.prepareStatement(sql);
	            result = statement.executeQuery();
	            
	            if (result.next()) {
	                nc = result.getInt("COUNT(id)");
	            }
	            numOfCustomers.setText(String.valueOf(nc));
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        
	    }
	    public void IT() {
	        Date date = new Date();
	        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
	        
	        String sql = "SELECT SUM(total) FROM receipt WHERE date = '"
	                + sqlDate + "'";
	        
	        connection = Database.getConnection();
	        
	        try {
	            double ti = 0;
	            statement = connection.prepareStatement(sql);
	            result = statement.executeQuery();
	            
	            if (result.next()) {
	                ti = result.getDouble("SUM(total)");
	            }
	            
	            incomeToday.setText(String.valueOf(ti));
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    public void TI() {
	        String sql = "SELECT SUM(total) FROM receipt";
	        
	        connection = Database.getConnection();
	        
	        try {
	            float ti = 0;
	            statement = connection.prepareStatement(sql);
	            result = statement.executeQuery();
	            
	            if (result.next()) {
	                ti = result.getFloat("SUM(total)");
	            }
	            totalIncome.setText(String.valueOf(ti));
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    public void NSP() {
	        
	        String sql = "SELECT COUNT(quantity) FROM customer";
	        
	        connection = Database.getConnection();
	        
	        try {
	            int q = 0;
	            statement = connection.prepareStatement(sql);
	            result = statement.executeQuery();
	            
	            if (result.next()) {
	                q = result.getInt("COUNT(quantity)");
	            }
	            numOfSoldProduct.setText(String.valueOf(q));
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    public void customerChart() {
	        customerChart.getData().clear();
	        
	        String sql = "SELECT date, SUM(total) FROM receipt GROUP BY date ORDER BY TIMESTAMP(date)";
	        connection = Database.getConnection();
	        
	        // Parameterizing the XYChart.Series
	        XYChart.Series<String, Float> chart = new XYChart.Series<>();
	        
	        try {
	            statement = connection.prepareStatement(sql);
	            result = statement.executeQuery();
	            
	            while (result.next()) {
	                // Adding data to the chart series
	                chart.getData().add(new XYChart.Data<>(result.getString(1), result.getFloat(2)));
	            }
	            
	            // Adding the chart series to the customerChart
	            customerChart.getData().add(chart);
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    public void incomeChart() {
	        incomeChart.getData().clear();
	        
	        String sql = "SELECT date, SUM(total) FROM receipt GROUP BY date ORDER BY TIMESTAMP(date)";
	        connection = Database.getConnection();
	        XYChart.Series<String, Float> chart = new XYChart.Series<>();
	        try {
	            statement = connection.prepareStatement(sql);
	            result = statement.executeQuery();
	            
	            while (result.next()) {
	                chart.getData().add(new XYChart.Data<>(result.getString(1), result.getFloat(2)));
	            }
	            
	            incomeChart.getData().add(chart);
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    public void setPage() {
	    	menuDisplayCard();
			 menuShowOrderData();
			homePage.setVisible(false);
			customerPage.setVisible(false);
			inventoryPage.setVisible(false);
			menuPage.setVisible(true);
	    }
	    public void changeGrid(ActionEvent e) {
	    	if (e.getSource() == mealButton) {
	    		MealPane.setVisible(true);
	    		DrinkPane.setVisible(false);
	    		DessertPane.setVisible(false);
	    	}else if (e.getSource() == drinkButton) {
	    		MealPane.setVisible(false);
	    		DrinkPane.setVisible(true);
	    		DessertPane.setVisible(false);
	    	}else if (e.getSource() == dessertButton) {
	    		MealPane.setVisible(false);
	    		DrinkPane.setVisible(false);
	    		DessertPane.setVisible(true);
	    	}
	    }
	    

}
