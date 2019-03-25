package org.chai.server.dao.hibernate;

import java.util.List;

import org.chai.server.dao.ProductTypeDAO;
import org.chai.shared.model.ProductType;
import org.springframework.stereotype.Repository;

import com.googlecode.genericdao.search.Search;

@Repository("productTypeDAO")
public class HibernateProductTypeDAO extends BaseDAOImpl<ProductType> implements ProductTypeDAO {

	@Override
	public boolean deleteProductType(ProductType Product) {
		return remove(Product);
	}

	@Override
	public List<ProductType> getAllProductTypes() {
		return findAll();
	}

	@Override
	public ProductType getProductType(String productTypeName) {
		Search search = new Search();
		search.addFilterEqual("productTypeName", productTypeName);
		List<ProductType> productTypes =  search(search);
		if (productTypes != null && productTypes.size() > 0) {
			return productTypes.get(0);
		}
		return null;
	}

	@Override
	public ProductType getProductType(Integer productTypeId) {
		Search search = new Search();
		search.addFilterEqual("id", productTypeId);
		List<ProductType> productTypes =  search(search);
		if (productTypes != null && productTypes.size() > 0) {
			return productTypes.get(0);
		}
		return null;
	}

	@Override
	public ProductType saveProductType(ProductType productType) {
		boolean isSaved = save(productType);
		if (isSaved) {
			productType = getProductType(productType.getProductTypeName());
		} 
		return productType;
	}
}
