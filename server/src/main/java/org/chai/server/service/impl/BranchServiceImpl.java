package org.chai.server.service.impl;

import java.util.List;

import org.chai.server.dao.BranchDAO;
import org.chai.server.service.BranchService;
import org.chai.shared.model.Branch;
import org.chai.shared.model.exceptions.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service("branchService")
public class BranchServiceImpl implements BranchService {
	
	@Autowired
	private BranchDAO branchDAO;

	@Override
	public boolean deleteBranch(Branch branch) throws DataAccessException {
		return branchDAO.deleteBranch(branch);
	}

	@Override
	public List<Branch> getAllBranches() throws DataAccessException {
		return branchDAO.getAllBranches();
	}

	@Override
	public Branch getBranch(String branchName, String branchCode) throws DataAccessException {
		return branchDAO.getBranch(branchName, branchCode);
	}

	@Override
	public Branch getBranch(Integer branchId) throws DataAccessException {
		return branchDAO.getBranch(branchId);
	}

	@Override
	public Branch getBranch(String branchName) throws DataAccessException {
		return branchDAO.getBranch(branchName);
	}

	@Override
	public Branch getBranchUsingCode(String branchCode) throws DataAccessException {
		return branchDAO.getBranchUsingCode(branchCode);
	}

	@Override
	public Branch saveBranch(Branch branch) throws DataAccessException {
		return branchDAO.saveBranch(branch);
	}
}