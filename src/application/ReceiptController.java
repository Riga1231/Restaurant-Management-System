package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class ReceiptController implements Initializable {
    @FXML
    private Label totalPrice;

    @FXML
    private Label changeLabel;
    @FXML
    private TextArea receiptTextArea;

    private Connection connection;

    public void setChange(Double change) {
        setData(change);
    }

    // Method to fetch data from the database and set the receipt details
    public void setData(Double change) {
        StringBuilder receiptBuilder = new StringBuilder();
        double total = 0;
System.out.println(Data.customerId - 1);
        try {
            connection = Database.getConnection();
            String query = "SELECT productName, quantity, price FROM customer WHERE customerId = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, Data.customerId - 1);  // Use the Data.customerId

            ResultSet resultSet = statement.executeQuery();

            receiptBuilder.append("Receipt:\n");

            while (resultSet.next()) {
                String productName = resultSet.getString("productName");
                int quantity = resultSet.getInt("quantity");
                double price = resultSet.getDouble("price");

                // Debugging statements
                System.out.println("Product: " + productName + ", Quantity: " + quantity + ", Price: " + price);

                receiptBuilder.append(productName).append("\t").append(quantity).append("\t").append(price).append("\n");
                total += price * quantity;
            }

            // Debugging statement
            System.out.println("Total calculated: " + total);

            receiptBuilder.append("\nTotal Price: ").append(total).append("\n");
            receiptBuilder.append("Change: ").append(change).append("\n");

            receiptTextArea.setText(receiptBuilder.toString());
            totalPrice.setText(String.valueOf(total));
            changeLabel.setText(String.valueOf(change));

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialization code, if needed
    }
}
