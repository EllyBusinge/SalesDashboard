package org.chai.server.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.chai.server.dao.ProductDAO;
import org.chai.server.dao.ProductTypeDAO;
import org.chai.server.dao.SaleDAO;
import org.chai.server.service.SalesService;
import org.chai.shared.model.Product;
import org.chai.shared.model.ProductType;
import org.chai.shared.model.Sale;
import org.chai.shared.model.exceptions.DataAccessException;
import org.chai.shared.model.paging.PagingLoadConfig;
import org.chai.shared.model.paging.PagingLoadResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service("salesService")
public class SalesServiceImpl implements SalesService {

	@Autowired
	private ProductDAO productDAO;
	
	@Autowired
	private ProductTypeDAO productTypeDAO;
	
	@Autowired
	private SaleDAO saleDAO;
	
	@Override
	public boolean deleteProduct(Product product) throws DataAccessException {
		return productDAO.deleteProduct(product);
	}

	@Override
	public boolean deleteSale(Sale sale) throws DataAccessException {
		return saleDAO.deleteSale(sale);
	}

	@Override
	public List<Product> getAllProducts() throws DataAccessException {
		List<Product> products = productDAO.getAllProducts();
		List<Product> newList = new ArrayList<Product>();	
		for (Product product : products) {
			newList.add(new Product(product));
		}
		return newList;
	}

	@Override
	public List<Sale> getAllSales() throws DataAccessException {
		List<Sale> sales = saleDAO.getAllSales();
		List<Sale> newList = new ArrayList<Sale>();	
		for (Sale sale : sales) {
			newList.add(new Sale(sale));
		}
		return newList;
	}

	@Override
	public Product getProduct(String ProductDescription) throws DataAccessException {
		return productDAO.getProduct(ProductDescription);
	}

	@Override
	public Product getProduct(String productDescription, String productCode) throws DataAccessException {
		return productDAO.getProduct(productDescription, productCode);
	}

	@Override
	public Product getProduct(Integer productId) throws DataAccessException {
		return productDAO.getProduct(productId);
	}

	@Override
	public Product getProductUsingCode(String productCode) throws DataAccessException {
		return productDAO.getProductUsingCode(productCode);
	}

	@Override
	public Sale getSale(Integer saleId) throws DataAccessException {
		return saleDAO.getSale(saleId);
	}

	@Override
	public Product saveProduct(Product product) throws DataAccessException {
		return productDAO.saveProduct(product);
	}

	@Override
	public boolean saveSale(Sale sale) throws DataAccessException {
		return saleDAO.saveSale(sale);
	}

	@Override
	public PagingLoadResult<Product> getProductsList(PagingLoadConfig pagingLoadConfig) throws DataAccessException {
		PagingLoadResult<Product> result = productDAO.findAllByPage(pagingLoadConfig, "id");
		List<Product> newList = new ArrayList<Product>();	
		List<Product> products = result.getData();
		for (Product product : products) {
			Product newProduct = new Product(product);
			newList.add(newProduct);
		}
		return new PagingLoadResult<Product>(newList, result.getOffset(), result.getLength(), result.getTotalLength());
	}

	@Override
	public boolean deleteProductType(ProductType productType) throws DataAccessException {
		return productTypeDAO.deleteProductType(productType);
	}

	@Override
	public List<ProductType> getAllProductTypes() throws DataAccessException {
		List<ProductType> productTypes = productTypeDAO.getAllProductTypes();
		List<ProductType> newList = new ArrayList<ProductType>();	
		for (ProductType productType : productTypes) {
			newList.add(new ProductType(productType));
		}
		return newList;
	}

	@Override
	public ProductType getProductType(String productType) throws DataAccessException {
		return productTypeDAO.getProductType(productType);
	}

	@Override
	public ProductType saveProductType(ProductType productType) throws DataAccessException {
		return productTypeDAO.saveProductType(productType);
	}
}