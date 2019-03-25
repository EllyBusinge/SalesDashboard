package org.chai.server.dao;

import java.util.List;

import org.chai.shared.model.Branch;

public interface BranchDAO extends BaseDAO<Branch> {

	List<Branch> getAllBranches();
	
	Branch saveBranch(Branch branch);
	
	boolean deleteBranch(Branch branch);
	
	Branch getBranch(String branchName);
	
	Branch getBranch(String branchName, String branchCode);
	
	Branch getBranchUsingCode(String branchCode);
	
	Branch getBranch(Integer branchId);
}
