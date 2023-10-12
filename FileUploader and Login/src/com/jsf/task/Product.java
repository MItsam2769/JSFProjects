package com.jsf.task;

import java.io.Serializable;

public class Product implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double productId;
	private String productName;
    private String description;
    private double price;
    private String uploadedBy;

    // Getter and setter methods for productName, description, and uploadedBy
    
    public double getproductId() {
        return productId;
    }

    public void setproductId(double productId) {
        this.productId = productId;
    }

    public String getproductName() {
        return productName;
    }

    public void setproductName(String productName) {
        this.productName = productName;
    }

    public String getdescription() {
        return description;
    }

    public void setdescription(String description) {
        this.description = description;
    }

    public double getprice() {
        return price;
    }

    public void setprice(double price) {
        this.price = price;
    }

    public String getuploadedBy() {
        return uploadedBy;
    }

    public void setuploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }
}