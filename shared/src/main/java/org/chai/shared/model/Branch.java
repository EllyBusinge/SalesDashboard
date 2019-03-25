package org.chai.shared.model;

import java.util.Date;

/**
 * Model Representation of a branch.
 * @author Elly
 *
 */
public class Branch extends BaseModel {
	
	private static final long serialVersionUID = -4632137594449464459L;
	
	private String branchCode;
	
	private String branchName;
	
	private String location;

	public Branch() {
		this.setDateCreated(new Date());
	}
	
	public Branch(Branch branch) {
		this.setId(branch.getId());
		this.setBranchCode(branch.getBranchCode());
		this.setBranchName(branch.getBranchName());
		this.setLocation(branch.getLocation());
		if (branch.getDateCreated() != null)
			this.setDateCreated(new Date(branch.getDateCreated().getTime()));
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLocation() {
		return location;
	}
}
