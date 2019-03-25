package org.chai.server.dao.hibernate;

import java.util.List;

import org.chai.server.dao.ProductDAO;
import org.chai.shared.model.Product;
import org.springframework.stereotype.Repository;

import com.googlecode.genericdao.search.Search;

@Repository("productDAO")
public class HibernateProductDAO extends BaseDAOImpl<Product> implements ProductDAO {

	@Override
	public boolean deleteProduct(Product Product) {
		return remove(Product);
	}

	@Override
	public List<Product> getAllProducts() {
		return findAll();
	}

	@Override
	public Product getProduct(String productDescription) {
		Search search = new Search();
		search.addFilterEqual("productDescription", productDescription);
		List<Product> Products =  search(search);
		if (Products != null && Products.size() > 0) {
			return Products.get(0);
		}
		return null;
	}

	@Override
	public Product getProduct(String productDescription, String productCode) {
		Search search = new Search();
		search.addFilterEqual("productDescription", productDescription);
		search.addFilterEqual("productCode", productCode);
		List<Product> products =  search(search);
		if (products != null && products.size() > 0) {
			return products.get(0);
		}
		return null;
	}

	@Override
	public Product getProduct(Integer productId) {
		Search search = new Search();
		search.addFilterEqual("id", productId);
		List<Product> products =  search(search);
		if (products != null && products.size() > 0) {
			return products.get(0);
		}
		return null;
	}

	@Override
	public Product getProductUsingCode(String productCode) {
		Search search = new Search();
		search.addFilterEqual("productCode", productCode);
		List<Product> products =  search(search);
		if (products != null && products.size() > 0) {
			return products.get(0);
		}
		return null;
	}

	@Override
	public Product saveProduct(Product product) {
		boolean isSaved = save(product);
		if (isSaved) {
			product = getProductUsingCode(product.getProductCode());
		} 
		return product;
	}
}
