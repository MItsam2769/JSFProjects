package com.jsf.task.db.operations;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.jsf.task.Product;
import com.jsf.task.User; 
public class UserDao {
    private Connection connection;

    public UserDao() {
    	System.out.println("connecting");
        // Initialize your Oracle database connection here
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection("jdbc:oracle:thin:@10.128.3.19:1621:MIBUAT", "MIB_TEST", "mib_test");
            System.out.println("connected to db");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean validateUser(String username, String password) {
    	System.out.println("Validating user");
        try {
            // Check if the username and password match in the database
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
//User Data
    public User getUserData(String username) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        User user = null;

        try {

            // Prepare a SQL query to retrieve user data based on the username
            String sql = "SELECT userid, role FROM users WHERE username = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, username);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                user = new User();
                user.setUsername(username);
                user.setUserId(resultSet.getString("userid"));
                user.setRole(resultSet.getString("role"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database connection or query errors here
        }
        return user;
    }
    public static void insertProductData(double productID, String name, String description, double price, String uploadedBy) {
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@10.128.3.19:1621:MIBUAT", "MIB_TEST", "mib_test");
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Product (ProductID, Name, Description, Price, UploadedBy) VALUES (?, ?, ?, ?, ?)")) {
            preparedStatement.setDouble(1, productID);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, description);
            preparedStatement.setDouble(4, price);
            preparedStatement.setString(5, uploadedBy);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database connection or query errors here
        }
    }

    public List<Product> loadProducts() {
        List<Product> products = new ArrayList<>();



        try {
            String query = "SELECT * FROM product";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Product product = new Product();
                product.setproductId(rs.getDouble("ProductID"));
                product.setproductName(rs.getString("Name"));
                product.setdescription(rs.getString("Description"));
                product.setprice(rs.getDouble("Price"));
                product.setuploadedBy(rs.getString("UploadedBy"));
                products.add(product);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }
    
    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}