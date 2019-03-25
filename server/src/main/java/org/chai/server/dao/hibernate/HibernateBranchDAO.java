package org.chai.server.dao.hibernate;

import java.util.List;

import org.chai.server.dao.BranchDAO;
import org.chai.shared.model.Branch;
import org.springframework.stereotype.Repository;

import com.googlecode.genericdao.search.Search;

@Repository("branchDAO")
public class HibernateBranchDAO extends BaseDAOImpl<Branch> implements BranchDAO {

	@Override
	public boolean deleteBranch(Branch branch) {
		return remove(branch);
	}

	@Override
	public List<Branch> getAllBranches() {
		return findAll();
	}

	@Override
	public Branch getBranch(String branchName) {
		Search search = new Search();
		search.addFilterEqual("branchName", branchName);
		List<Branch> branches =  search(search);
		if (branches != null && branches.size() > 0) {
			return branches.get(0);
		}
		return null;
	}

	@Override
	public Branch getBranch(String branchName, String branchCode) {
		Search search = new Search();
		search.addFilterEqual("branchName", branchName);
		search.addFilterEqual("branchCode", branchCode);
		List<Branch> branches =  search(search);
		if (branches != null && branches.size() > 0) {
			return branches.get(0);
		}
		return null;
	}

	@Override
	public Branch getBranch(Integer branchId) {
		Search search = new Search();
		search.addFilterEqual("id", branchId);
		List<Branch> branches =  search(search);
		if (branches != null && branches.size() > 0) {
			return branches.get(0);
		}
		return null;
	}

	@Override
	public Branch getBranchUsingCode(String branchCode) {
		Search search = new Search();
		search.addFilterEqual("branchCode", branchCode);
		List<Branch> branches =  search(search);
		if (branches != null && branches.size() > 0) {
			return branches.get(0);
		}
		return null;
	}

	@Override
	public Branch saveBranch(Branch branch) {
		boolean isSaved = save(branch);
		if (isSaved) {
			branch = getBranchUsingCode(branch.getBranchCode());
		} 
		return branch;
	}
}
