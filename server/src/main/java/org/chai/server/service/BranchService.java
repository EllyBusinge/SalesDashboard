package org.chai.server.service;

import java.util.List;

import org.chai.shared.model.Branch;
import org.chai.shared.model.exceptions.DataAccessException;

public interface BranchService {
	
	Branch saveBranch(Branch branch) throws DataAccessException;
	
	boolean deleteBranch(Branch branch) throws DataAccessException;
	
	Branch getBranch(String branchName, String branchCode) throws DataAccessException;
	
	Branch getBranch(Integer branchId) throws DataAccessException;
	
	Branch getBranchUsingCode(String branchCode) throws DataAccessException;
	
	Branch getBranch(String branchName) throws DataAccessException;
	
	List<Branch> getAllBranches() throws DataAccessException;
}
