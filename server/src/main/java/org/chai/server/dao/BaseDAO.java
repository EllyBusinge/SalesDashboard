package org.chai.server.dao;

import java.util.List;

import com.googlecode.genericdao.dao.hibernate.GenericDAO;
import org.chai.shared.model.paging.PagingLoadConfig;
import org.chai.shared.model.paging.PagingLoadResult;

public interface BaseDAO<T> extends GenericDAO<T, Long> {

    List<T> searchByPropertyEqual(String property, Object value);

    T searchUniqueByPropertyEqual(String property, Object value);
    
    PagingLoadResult<T> findAllByPage(PagingLoadConfig pagingLoadConfig, String defaultSortField);

    void merge(Object obj);
}
