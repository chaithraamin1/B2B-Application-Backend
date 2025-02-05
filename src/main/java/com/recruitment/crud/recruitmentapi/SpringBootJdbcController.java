package com.recruitment.crud.recruitmentapi;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;

import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.StreamingHttpOutputMessage.Body;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController  
public class SpringBootJdbcController{ 

   @Autowired  
   JdbcTemplate jdbc;
   
   public static String uploadDirectory= System.getProperty("user.dir")+"/src/main/resources/static/images";
   
  	
/*    ------------------------------------   Buyers API  -------------------------------------------------------------*/
	
	@PostMapping("/api/register")
	public HashMap<String,Object> registerApi(@RequestBody users user){
	            HashMap<String, Object> registerResultMap = new HashMap<String, Object>();
	            
	            String query="select email from users where email=?";
	            List<Map<String, Object>> resultEmail = jdbc.queryForList(query,new Object[] {user.getEmail()});
	            
	            
	           if(resultEmail.size()>0) {
	        	   
	        	   registerResultMap.put("error", "User already exists");
	                return registerResultMap;    	    
	        	     
	           }
	           else {
	            String url = "jdbc:mysql://localhost:3306/buyers_schema";
	            String username = "root";
	            String password = "root";
	            
		    	String sql="insert into users (name, email, mobile, password, created_at, updated_at, userType) VALUES (?, ?, ?, ?, ?, ?, ?)";

		        try (Connection conn = DriverManager.getConnection(url, username, password);
		             PreparedStatement userTb = conn.prepareStatement(sql)) {

		        	userTb.setString(1, user.getName());
		        	userTb.setString(2, user.getEmail());
		        	userTb.setString(3, user.getMobile()); 
		        	userTb.setString(4, user.getPassword());
		        	userTb.setTimestamp(5, user.getCreated_at());
		        	userTb.setTimestamp(6, user.getUpdated_at()); 
		        	userTb.setString(7, user.getUserType());
		        	
		            int rowsInserted = userTb.executeUpdate();
		            if (rowsInserted > 0) {
		            	registerResultMap.put("success","user successfully added");
		  				return registerResultMap;
		            }

		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		      
	           }
			return registerResultMap;
	}
	

	
	@PostMapping("/api/login")
	public HashMap<String, Object> login(@RequestBody users user) throws SQLException{
		
		
		String token=null;
		jwt jwtClassObj=new jwt();
		token=jwtClassObj.generateJWTToken("abcd");
		HashMap<String, Object> loginResultMap = new HashMap<String, Object>();
		
		String sql="select * from users where email=?";
		List<Map<String, Object>> userResult = jdbc.queryForList(sql,new Object[] {user.getEmail()});
		
		
		/*---------------------------User found and not found----------------------------------------------*/
		
		if(userResult.size()<=0) {
			
			loginResultMap.put("error","Login Failed");
			return loginResultMap;
		}
		   
		else {
			
			Map<String, Object> result=userResult.get(0);
			
			String password=(String) result.get("password");
			String emailId=(String) result.get("email");
			
			/*------------------------Password is incorrect------------------------------------------*/
			if(password.matches(user.getPassword())!=true) {
			
			    loginResultMap.put("error","entered Wrong Password");
			
			    return loginResultMap;
			}
			/*---------------------------------------------------------------------------------------*/
			
			/*------------------------incorrect mail id ----------------------------------------------*/
			if(emailId.matches(user.getEmail())!=true)
			{
				loginResultMap.put("error","User is not registered");
				 
				return loginResultMap;
			}
			/*-------------------------------------------------------------------------------------------------*/
			
			loginResultMap.put("success","Login Success");
			loginResultMap.put("token",token);
			loginResultMap.put("loginUserData",userResult.get(0));
			
		} 	
		/*-------------------------------------------------------------------------------------------------*/
		return 	loginResultMap;
	}
	
	
	// seller add new product
//	@PostMapping("/api/add/product")
//	public HashMap<String,Object> productUpload(@RequestBody product productBody) {
//		
//		 String url = "jdbc:mysql://localhost:3306/buyers_schema";
//         String username = "root";
//         String password = "root";
//         HashMap<String,Object> productResultMap=new HashMap<String,Object> ();
//         
//        
//	    	String sql="insert into product (product_name, product_description, product_image, product_category, price, available_count) VALUES (?, ?, ?, ?, ?, ?)";
//
//	        try (Connection conn = DriverManager.getConnection(url, username, password);
//	             PreparedStatement productStatement = conn.prepareStatement(sql)) {
//
//	        	productStatement.setString(1, productBody.getProduct_name());
//	        	productStatement.setString(2, productBody.getProduct_description());
//	        	productStatement.setString(3, productBody.getProduct_image());
//	        	productStatement.setString(4, productBody.getProduct_category()); 
//	        	productStatement.setDouble(5, productBody.getPrice());
//	        	productStatement.setDouble(6, productBody.getAvailable_quantity_count());
//	        
//	   
//	        	
//	            int rowsInserted = productStatement.executeUpdate();
//	            if (rowsInserted > 0) {
//	            	productResultMap.put("result","product added successfully ");
//	  				return productResultMap;
//	            }
//	            else {
//	            	productResultMap.put("result","product is not added successfully ");
//	  				return productResultMap;
//	            }
//
//	        } catch (Exception e) {
//	            e.printStackTrace();
//	        }
//		
//		return null;
//		
//		
//	}
	
//	 get product category for seller screen and buyer screen
	
	@GetMapping("/api/get/product/category")
	public  List<Map<String,Object>> getAllProductCategoryList() {
		
	    String sql="select * from product_category";
		List<Map<String, Object>> productCategoryList = jdbc.queryForList(sql);      
        return productCategoryList;
         
	}
	
//    get product
	@GetMapping("/api/get/product")
	public  List<Map<String,Object>> getAllProductList() {
		
	    String sql="select * from product order by created_at desc";
		List<Map<String, Object>> productList = jdbc.queryForList(sql);      
       return productList;
        
	}
	
	
	
//	place order
	@PostMapping("/api/placeOrder")
	public HashMap<String,Object> placeorder(){
	String url = "jdbc:mysql://localhost:3306/buyers_schema";
    String username = "root";
    String password = "root";
    HashMap<String,Object> productResultMap=new HashMap<String,Object> ();
    
   
   	String sql="insert into product (product_name, product_description, product_image, product_category, price, available_count) VALUES (?, ?, ?, ?, ?, ?)";

       try (Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement productStatement = conn.prepareStatement(sql)) {

//       	productStatement.setString(1, productBody.getProduct_name());
//       	productStatement.setString(2, productBody.getProduct_description());
//       	productStatement.setString(3, productBody.getProduct_image());
//       	productStatement.setString(4, productBody.getProduct_category()); 
//       	productStatement.setDouble(5, productBody.getPrice());
//       	productStatement.setDouble(6, productBody.getAvailable_count());
       
  
       	
           int rowsInserted = productStatement.executeUpdate();
           if (rowsInserted > 0) {
           	productResultMap.put("result","product added successfully ");
 				return productResultMap;
           }
           else {
           	productResultMap.put("result","product is not added successfully ");
 				return productResultMap;
           }

       } catch (Exception e) {
           e.printStackTrace();
       }
	
	return null;
	
	}
	
	@PostMapping("/api/add/companyDetails")
	public HashMap<String,Object> addCompanyDetails(@RequestBody buyer_comapany_details company_details){
		
		 String url = "jdbc:mysql://localhost:3306/buyers_schema";
         String username = "root";
         String password = "root";
         HashMap<String,Object> companyDetailsMap=new HashMap<String,Object>();
         
	    	String sql="insert into buyer_comapany_details (buyer_id, company_name, company_address) VALUES (?, ?, ?)";

	        try (Connection conn = DriverManager.getConnection(url, username, password);
	             PreparedStatement companyDetailsStatement = conn.prepareStatement(sql)) {

	        	companyDetailsStatement.setInt(1, company_details.getBuyer_id()); 
	        	companyDetailsStatement.setString(2, company_details.getCompany_name());
	        	companyDetailsStatement.setString(3, company_details.getCompany_address());
	        	
	        	
	            int rowsInserted = companyDetailsStatement.executeUpdate();
	            if (rowsInserted > 0) {
	            	companyDetailsMap.put("success","user added successfully");
	  				return companyDetailsMap;
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
			return companyDetailsMap;
	      
}
	
	@GetMapping("/api/get/comapanyDetails")
	public List<Map<String, Object>> getCompanyDetails(int buyer_id){
		
			 String sql="select * from buyer_comapany_details where buyer_id=?";
			 List<Map<String, Object>> CompanyDetailsResult = jdbc.queryForList(sql,new Object[] {buyer_id});      
			 return CompanyDetailsResult;

		
	}
	
	// cart product
	
	@PostMapping("/api/product/add/cart")
	public HashMap<String,Object> addProductTocart(@RequestBody buyer_cart cartData){
		
		 String url = "jdbc:mysql://localhost:3306/buyers_schema";
         String username = "root";
         String password = "root";
         HashMap<String,Object> addCartMap=new HashMap<String,Object>();
         
         
         
         String query="select * from product where id=?";
         List<Map<String,Object>> ProductListResult = jdbc.queryForList(query,new Object[] {cartData.getProductID()});
         Map<String, Object> result= ProductListResult.get(0);
         
         int availableCount= (int) result.get("available_quantity_count");
         
         if(availableCount <= 0) {
        	 addCartMap.put("error", "Product Out of Stock");
        	 return addCartMap;
         }
        	 
         
         double totalprice=(double) result.get("price") * cartData.getQuantity();
         int remaining_quantity= (int) result.get("available_quantity_count") - cartData.getQuantity();
         
		
          String sql="insert into buyer_cart (buyerID, productID, quantity, product_name, product_description, product_image, product_category,total_price,remaining_quantity,price) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	        try (Connection conn = DriverManager.getConnection(url, username, password);
	             PreparedStatement addCartStatement = conn.prepareStatement(sql)) {

	        	addCartStatement.setInt(1, cartData.getBuyerID()); 
	        	addCartStatement.setInt(2, cartData.getProductID());
	        	addCartStatement.setInt(3, cartData.getQuantity());
	        	addCartStatement.setString(4,(String) result.get("product_name")); 
	        	addCartStatement.setString(5,(String) result.get("product_description"));
	        	addCartStatement.setString(6, (String) result.get("product_image"));
	        	addCartStatement.setString(7, (String) result.get("product_category")); 
	        	addCartStatement.setDouble(8, totalprice);
	        	addCartStatement.setDouble(9, remaining_quantity);
	           	addCartStatement.setDouble(10, (double) result.get("price"));
	        	
	      String  updateQuery="update product set available_quantity_count = ? where id = ?";
	      jdbc.update(updateQuery, remaining_quantity, cartData.getProductID());
	      
	      
	      String  getQuery="select * from buyer_cart where buyerID="+cartData.getBuyerID();
	      List<Map<String,Object>> buyerCartList =  jdbc.queryForList(getQuery);  
	 
	            int rowsInserted = addCartStatement.executeUpdate();
	            if (rowsInserted > 0) {
	            	addCartMap.put("success","product added to cart");
	            	addCartMap.put("buyerCartList",buyerCartList);
	  			    return  addCartMap;         }

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
			return addCartMap;
	      
}
	
	   @PostMapping("/api/add/product")
	    public HashMap<String,Object> addProduct(
	            @RequestParam("product_name") String product_name,
	            @RequestParam("product_description") String product_description,
	            @RequestParam("product_category") String product_category,
	            @RequestParam("product_image") MultipartFile image_file,
	            @RequestParam("price") double price,
	            @RequestParam("available_quantity_count") int available_quantity_count) throws IOException, SerialException, SQLException
	     {

		   HashMap<String,Object> productResultMap=new HashMap<String,Object> ();
		   
		   
		   
		   String originalFilename=image_file.getOriginalFilename();
		   Path filenameAndPath=Paths.get(uploadDirectory,originalFilename);
		   Files.write(filenameAndPath,image_file.getBytes());
		   
	

		  	Date currentDate = new Date();
	        Timestamp timestampCurrentDate = new Timestamp(currentDate.getTime());
	    	
			 String url = "jdbc:mysql://localhost:3306/buyers_schema";
	         String username = "root";
	         String password = "root";
	        
	         
	        
		    	String sql="insert into product (product_name, product_description, product_image, product_category, price, available_quantity_count,created_at) VALUES (?, ?, ?, ?, ?, ?, ?)";

		        try (Connection conn = DriverManager.getConnection(url, username, password);
		             PreparedStatement productStatement = conn.prepareStatement(sql)) {

		        	productStatement.setString(1, product_name);
		        	productStatement.setString(2, product_description);
		        	productStatement.setString(3,originalFilename);
		        	productStatement.setString(4, product_category); 
		        	productStatement.setDouble(5, price);
		        	productStatement.setInt(6, available_quantity_count);
		         	productStatement.setTimestamp(7, timestampCurrentDate);
		        
		   
		        	
		            int rowsInserted = productStatement.executeUpdate();
		            if (rowsInserted > 0) {
		            	productResultMap.put("result","product added successfully ");
		  				return productResultMap;
		            }
		            else {
		            	productResultMap.put("result","product is not added successfully ");
		  				return productResultMap;
		            }

		        } catch (Exception e) {
		            e.printStackTrace();
		        }
				return productResultMap;
	        
	        
	        
	    }
	
@GetMapping("/api/product/search")
public List<Map<String,Object>> searchProductByName(String product_name){
	 String sql="select * from product where product_name like \""+product_name+"%\"";
	 
	 List<Map<String,Object>> productList =  jdbc.queryForList(sql);      
	 return productList;
	 
}
@GetMapping("/api/product/filter")
public List<Map<String,Object>> filterProductByProductName(String category_name){
	 String sql="select * from product where product_category="+"'"+category_name+"'";
	 List<Map<String,Object>> productList =  jdbc.queryForList(sql);      
	 return productList;
	 
}
@GetMapping("/api/get/cart")
public List<Map<String,Object>> getCartData(int userid){
	 String sql="select * from buyer_cart where buyerID="+ userid;
	 List<Map<String,Object>> productList =  jdbc.queryForList(sql);      
	 return productList;
	 
}

@DeleteMapping("/api/remove/cart")
public HashMap<String,Object> deleteCartProduct(int buyer_id,int product_id,int quantity) {
	

HashMap<String,Object> cartListMap=new HashMap<String,Object> ();

String query="select * from product where id=?";
List<Map<String,Object>> ProductListResult = jdbc.queryForList(query,new Object[] {product_id});
Map<String, Object> result= ProductListResult.get(0);

int qnty=(int) result.get("available_quantity_count") + quantity;


String deleteQuery = "delete from buyer_cart where buyerID=? and productID=? and quantity=?";
jdbc.update(deleteQuery,new Object[] {buyer_id,product_id,quantity});

String  updateQuery="update product set available_quantity_count = ? where id = ?";
jdbc.update(updateQuery,qnty,product_id );


String  getQuery="select * from buyer_cart where buyerID="+buyer_id;
List<Map<String,Object>> buyerCartList =  jdbc.queryForList(getQuery);

cartListMap.put("success","product removed to cart");
cartListMap.put("buyerCartList",buyerCartList);


return cartListMap;
}

@DeleteMapping("/api/product/remove")
public HashMap<String,Object> deleteProductById(int productId){
	
	String deleteQuery = "delete from product where id = ?";
	jdbc.update(deleteQuery,new Object[] {productId});
	
	HashMap<String,Object> responseMap=new HashMap<String,Object>();
	responseMap.put("success", "product delete successfully");
	
	return responseMap;

	
	
	
}




}
	
	
	
	    




