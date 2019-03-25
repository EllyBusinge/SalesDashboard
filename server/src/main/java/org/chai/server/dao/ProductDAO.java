package org.chai.server.dao;

import java.util.List;

import org.chai.shared.model.Product;

public interface ProductDAO extends BaseDAO<Product> {

	List<Product> getAllProducts();
	
	Product saveProduct(Product product);
	
	boolean deleteProduct(Product product);
	
	Product getProduct(String productDescription);
	
	Product getProduct(String productDescription, String productCode);
	
	Product getProductUsingCode(String productCode);
	
	Product getProduct(Integer productId);
}
