package com.recruitment.crud.recruitmentapi;

import java.sql.Blob;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="product")
public class product {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	
	private int id;
	
	private String product_name;
	
	private String product_description;
	
	private String product_image;
	
	private String product_category;
	
//	public List<Integer> getProduct_category_ids() {
//		return product_category_ids;
//	}
//
//	public void setProduct_category_ids(List<Integer> product_category_ids) {
//		this.product_category_ids = product_category_ids;
//	}

	private double price;
	
	private  int available_quantity_count;
	
	private Timestamp created_at;
	
	public Timestamp getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Timestamp created_at) {
		this.created_at = created_at;
	}

	private  List<Integer>  product_category_ids;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getAvailable_quantity_count() {
		return available_quantity_count;
	}

	public void setAvailable_quantity_count(int available_quantity_count) {
		this.available_quantity_count = available_quantity_count;
	}


	
	
	
	

}
