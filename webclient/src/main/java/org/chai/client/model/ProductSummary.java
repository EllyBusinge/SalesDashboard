package org.chai.client.model;

import org.chai.shared.model.Branch;
import org.chai.shared.model.Product;
import org.chai.shared.model.Sale;

import com.extjs.gxt.ui.client.data.BaseModel;

public class ProductSummary extends BaseModel {

	private static final long serialVersionUID = -483245623678099579L;
	private Product product;

	public ProductSummary() {
		product = new Product();
	}

	public ProductSummary(Product product) {
		setProduct(product);

		setId(product.getId());
		setProductCode(product.getProductCode());
		setProductDescription(product.getProductDescription());
		setProductTypeName(product.getProductType().getProductTypeName());
		setBuyingPrice(product.getBuyingPrice());
		setSellingPrice(product.getSellingPrice());
		setProfitMade(product.getProfitMade());
		setUnitsSold(product.getUnitsSold());
		
		Sale sale = product.getSale();
		
		if (sale != null) {
			setSalePeriod(sale.getSalePeriod());
			Branch branch = sale.getBranch();
			if (branch != null) {
				setBranchCode(branch.getBranchCode());
			}
		}
		if (product.getDateCreated() != null)
			setDateCreated(product.getDateCreated().toString());
		if (product.getDateChanged() != null)
			setDateChanged(product.getDateChanged().toString());
	}

	public void setId(int id) {
		set("id", id);
	}

	public String getId() {
		return get("id");
	}

	public void setProductCode(String productCode) {
		set("productCode", productCode);
	}

	public String getProductCode() {
		return get("productCode");
	}
	
	public void setProductDescription(String productDescription) {
		set("productDescription", productDescription);
	}

	public String getProductDescription() {
		return get("productDescription");
	}
	
	public void setProductTypeName(String productTypeName) {
		set("productTypeName", productTypeName);
	}

	public String getProductTypeName() {
		return get("productTypeName");
	}
	
	public void setBuyingPrice(double buyingPrice) {
		set("buyingPrice", buyingPrice);
	}

	public double getBuyingPrice() {
		Double buyingPrice = (Double) get("buyingPrice");
		return buyingPrice.doubleValue();
	}
	
	public void setSellingPrice(double sellingPrice) {
		set("sellingPrice", sellingPrice);
	}

	public double getSellingPrice() {
		Double sellingPrice = (Double) get("sellingPrice");
		return sellingPrice.doubleValue();
	}
	
	public void setProfitMade(double profitMade) {
		set("profitMade", profitMade);
	}

	public double getProfitMade() {
		Double profitMade = (Double) get("profitMade");
		return profitMade.doubleValue();
	}
	
	public void setUnitsSold(int unitsSold) {
		set("unitsSold", unitsSold);
	}

	public int getUnitsSold() {
		Integer unitsSold = (Integer) get("unitsSold");
		return unitsSold.intValue();
	}

	public void setSalePeriod(String salePeriod) {
		set("salePeriod", salePeriod);
	}

	public String getSalePeriod() {
		return get("salePeriod");
	}
	
	public void setBranchCode(String branchCode) {
		set("branchCode", branchCode);
	}

	public String getBranchCode() {
		return get("branchCode");
	}

	public void setDateCreated(String dateCreated) {
		set("dateCreated", dateCreated);
	}

	public String getDateCreated() {
		return get("dateCreated");
	}

	public void setDateChanged(String dateChanged) {
		set("dateChanged", dateChanged);
	}

	public String getDateChanged() {
		return get("dateChanged");
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Product getProduct() {
		return product;
	}
}