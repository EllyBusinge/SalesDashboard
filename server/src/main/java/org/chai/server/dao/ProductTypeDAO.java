package org.chai.server.dao;

import java.util.List;

import org.chai.shared.model.ProductType;

public interface ProductTypeDAO extends BaseDAO<ProductType> {

	List<ProductType> getAllProductTypes();
	
	ProductType saveProductType(ProductType productType);
	
	boolean deleteProductType(ProductType productType);
	
	ProductType getProductType(String productTypeName);
	
	ProductType getProductType(Integer productTypeId);
}
