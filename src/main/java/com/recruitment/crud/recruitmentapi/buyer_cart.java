package com.recruitment.crud.recruitmentapi;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="buyer_cart")
public class buyer_cart {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	
	private int id;
	
	private int buyerID;
	
	private int productID;
	
	private String product_name;
	
	private double price;
	
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getProduct_description() {
		return product_description;
	}

	public void setProduct_description(String product_description) {
		this.product_description = product_description;
	}

	public String getProduct_image() {
		return product_image;
	}

	public void setProduct_image(String product_image) {
		this.product_image = product_image;
	}

	public String getProduct_category() {
		return product_category;
	}

	public void setProduct_category(String product_category) {
		this.product_category = product_category;
	}


	public double getTotal_price() {
		return total_price;
	}

	public int getRemaining_quantity() {
		return remaining_quantity;
	}

	public void setRemaining_quantity(int remaining_quantity) {
		this.remaining_quantity = remaining_quantity;
	}

	public void setTotal_price(double total_price) {
		this.total_price = total_price;
	}

	private String product_description;
	
	private String product_image; 
	
	private String product_category;
	
    private double total_price;
	
	private int remaining_quantity;
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getBuyerID() {
		return buyerID;
	}

	public void setBuyerID(int buyerID) {
		this.buyerID = buyerID;
	}

	public int getProductID() {
		return productID;
	}

	public void setProductID(int productID) {
		this.productID = productID;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	private int quantity;
	

}
