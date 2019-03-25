package org.chai.shared.model;

import java.util.Date;

/**
 * Model Representation of a product type.
 * @author Elly
 */
public class ProductType extends BaseModel {
	
	private static final long serialVersionUID = 5206825167682751618L;
	
	private String productTypeName;
	
	public ProductType() {
		this.setDateCreated(new Date());
	}
	
	public ProductType(ProductType productType) {
		this.setId(productType.getId());
		this.setProductTypeName(productType.getProductTypeName());
		if (productType.getDateCreated() != null)
			this.setDateCreated(new Date(productType.getDateCreated().getTime()));
	}

	public void setProductTypeName(String productTypeName) {
		this.productTypeName = productTypeName;
	}

	public String getProductTypeName() {
		return productTypeName;
	}
}