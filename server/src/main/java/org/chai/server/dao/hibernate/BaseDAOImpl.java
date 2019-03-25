package org.chai.server.dao.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.googlecode.genericdao.dao.hibernate.GenericDAOImpl;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;
import com.googlecode.genericdao.search.SearchResult;
import org.chai.shared.model.paging.FilterComparison;
import org.chai.shared.model.paging.FilterConfig;
import org.chai.shared.model.paging.PagingLoadConfig;
import org.chai.shared.model.paging.PagingLoadResult;
import org.chai.server.dao.BaseDAO;

abstract class BaseDAOImpl<T> extends GenericDAOImpl<T, Long> implements BaseDAO<T> {
		
    @Autowired
    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }
    
	@Override
	public List<T> searchByPropertyEqual(String property, Object value){
		Search search = new Search();
		search.addFilterEqual(property, value);
		return search(search);
	}
	
	public void merge(Object obj){
		_merge(obj);
	}

	@Override
    @SuppressWarnings("unchecked")
	public T searchUniqueByPropertyEqual(String property, Object value) {
        Search search = new Search();
        search.addFilterEqual(property, value);
        return (T) searchUnique(search);
    }
	
	@Override
	public PagingLoadResult<T> findAllByPage(PagingLoadConfig pagingLoadConfig, String defaultSortField) {
		Search userSearch = getSearchFromLoadConfig(pagingLoadConfig, defaultSortField);
	    SearchResult<T> result = searchAndCount(userSearch);
	    return getPagingLoadResult(pagingLoadConfig, result);
	}

	public Search getSearchFromLoadConfig(PagingLoadConfig loadConfig, String defaultSortField) {
		Search search = new Search();
		String sortField = loadConfig.getSortField();
		
		sortField = (sortField == null ? defaultSortField : sortField);
		if (sortField != null && loadConfig.isSortDescending() != null) {
			search.addSort(sortField, loadConfig.isSortDescending());
		} else {
			search.addSort(sortField, false);
		}
		
		List<FilterConfig> filterConfigs = loadConfig.getFilters();
		if (filterConfigs != null) {
			Filter[] filters = new Filter[filterConfigs.size()];
			for (int i=0, j=filterConfigs.size(); i<j; i++) {
				FilterConfig filter = filterConfigs.get(i);
				
				if (filter.getComparison() == FilterComparison.LIKE) {
					filters[i] = Filter.ilike(filter.getField(), "%" + filter.getValue() + "%");
				} else if (filter.getComparison() == FilterComparison.GREATER_THAN) {
					filters[i] = Filter.greaterThan(filter.getField(), filter.getValue());
				} else if (filter.getComparison() == FilterComparison.LESS_THAN) {
					filters[i] = Filter.lessThan(filter.getField(), filter.getValue());
				} else if (filter.getComparison() == FilterComparison.EQUAL_TO) {
					filters[i] = Filter.equal(filter.getField(), filter.getValue());
				} else if (filter.getComparison() == FilterComparison.GREATER_THAN_OR_EQUAL_TO) {
					filters[i] = Filter.greaterOrEqual(filter.getField(), filter.getValue());
				} else if (filter.getComparison() == FilterComparison.LESS_THAN_OR_EQUAL_TO) {
					filters[i] = Filter.lessOrEqual(filter.getField(), filter.getValue());
				}
			}
			search.addFilterAnd(filters); 
		}
		search.setMaxResults(loadConfig.getLimit());
		search.setFirstResult(loadConfig.getOffset());

		return search;
	}
	
	protected PagingLoadResult<T> getPagingLoadResult(PagingLoadConfig loadConfig, SearchResult<T> searchResult) {
		List<T> list = searchResult.getResult();
		int totalNum = searchResult.getTotalCount();
		int offset = loadConfig == null ? 0 : loadConfig.getOffset();
		return new PagingLoadResult<T>(list, offset, list.size(), totalNum);
	}
}
