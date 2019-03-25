package org.chai.client.service;

import java.util.List;

import org.chai.shared.model.ProductType;
import org.chai.shared.model.Sale;
import org.chai.shared.model.Product;
import org.chai.shared.model.exceptions.DataAccessException;
import org.chai.shared.model.paging.PagingLoadConfig;
import org.chai.shared.model.paging.PagingLoadResult;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("salesService")
public interface SalesService extends RemoteService {
	
	List<Sale> getAllSales() throws DataAccessException;
	
	boolean saveSale(Sale sale) throws DataAccessException;
	
	boolean deleteSale(Sale sale) throws DataAccessException;
	
	Sale getSale(Integer saleId) throws DataAccessException;
	
	List<ProductType> getAllProductTypes() throws DataAccessException;
	
	ProductType saveProductType(ProductType productType) throws DataAccessException;
	
	boolean deleteProductType(ProductType productType) throws DataAccessException;
	
	ProductType getProductType(String productType) throws DataAccessException;
	
	List<Product> getAllProducts() throws DataAccessException;
	
	Product saveProduct(Product product) throws DataAccessException;
	
	boolean deleteProduct(Product product) throws DataAccessException;
	
	Product getProduct(String productDescription) throws DataAccessException;
	
	Product getProduct(String productDescription, String ProductCode) throws DataAccessException;
	
	Product getProductUsingCode(String productCode) throws DataAccessException;
	
	Product getProduct(Integer productId) throws DataAccessException;
	
	PagingLoadResult<Product> getProductsList(PagingLoadConfig pagingLoadConfig) throws DataAccessException;
}
