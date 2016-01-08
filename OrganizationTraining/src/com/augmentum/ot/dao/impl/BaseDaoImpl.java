package com.augmentum.ot.dao.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;

import com.augmentum.ot.dao.BaseDao;
import com.augmentum.ot.dataObject.constant.ConfigureConstants;
import com.augmentum.ot.util.PropertyUtils;





/**
 * implement all of class for the base function.
 * 
 * @version 0.1, 02/09/2012
 * @param <T>
 */

public abstract class BaseDaoImpl<T extends Serializable> implements BaseDao<T> {

    public abstract Class<T> getEntityClass();

    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    @Resource
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Session getSession() {
        Session session = null;
        try {
            session = sessionFactory.getCurrentSession();
        } catch (Exception e) {
            session = sessionFactory.openSession();
        }
        return session;
    }

    /**
     * delete the object<T>.
     * 
     * @param t
     * @return true|delete the object<t> is success, false|delete the object<t>
     *         is error.
     * @throws DaoException
     */
    public void deleteObject(T t){
        getSession().delete(t);
    }

    /**
     * find the object<T> by id
     * 
     * @param id
     * @return object<T>
     * @throws DaoException
     */
    @SuppressWarnings("unchecked")
    public T findByPrimaryKey(Serializable id) {
        return (T) getSession().get(getEntityClass(), id);
    }

    /**
     * find a collection include all of the object<T>
     * 
     * @return List<T>
     * @throws DaoException
     * @throws DaoException
     */
    @SuppressWarnings("unchecked")
    public List<T> loadAll() {
        return getSession().createQuery("from "+getEntityClass().getSimpleName()).list();
    }

    /**
     * find a collection in firstResult and maxResult
     * 
     * @param t
     * @param firstResult
     * @param maxResult
     * @return list<T>
     * @throws DaoException
     */
    @SuppressWarnings("unchecked")
    public List<T> loadPage(int firstResult, int maxResult) {
        return getSession().createCriteria(getEntityClass()).setFirstResult(
                firstResult).setMaxResults(maxResult).list();
    }

    /**
     * save the abject<T>.
     * 
     * @param t
     * @throws DaoException
     */
    public void saveObject(T t) {
    	Session session = getSession();
    	session.save(t);
    }

    /**
     * update the object<T>
     * 
     * @param t
     * @return object<T>
     * @throws DaoException
     */
    public void updateObject(T t) {
        getSession().update(t);
    }

    /**
     * save or update object<T>
     * 
     * @param t
     * @throws DaoException
     */
    public void saveOrUpdateObject(T t) {
        getSession().saveOrUpdate(t);
    }
    
    public void mergeObject(T t) {
    	getSession().merge(t);
    }

    /**
     * save or update object<T> index
     * @param t
     */
    public void saveOrUpdateObjectIndex(T t) {
        FullTextSession ftSession = Search.getFullTextSession(this.getSession());
        ftSession.index(t);
    }

    /**
     * delete object<T> index
     * @param t
     */
    @Override
    public void deleteObjectIndex(Serializable id) {
        FullTextSession ftSession = Search.getFullTextSession(this.getSession());
//        ftSession.setFlushMode(FlushMode.MANUAL);
//        ftSession.setCacheMode(CacheMode.IGNORE);
        ftSession.purge(getEntityClass(), id);
//        ftSession.flushToIndexes();
//        ftSession.clear();
    }

    /**
     * query object<T> from index
     * @param query
     * @param sortField
     * @param sortType
     * @param filter
     * @param firstResult
     * @param maxResult
     * @return List<T>
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<T> queryObjectFromIndex(Query query, String sortField,
            Boolean reverse, QueryWrapperFilter filter, int firstResult,
            int maxResult) {
        FullTextSession ftSession = Search.getFullTextSession(this.getSession());
        FullTextQuery fullTextQuery = ftSession.createFullTextQuery(query, getEntityClass());
        if(null != filter) {
            fullTextQuery.setFilter(filter);
        }
     // Sort
        Sort sort = null;
        if(null != sortField && !"".equals(sortField)) {
            if (sortField.endsWith("_zh")) {
                // This sort is by Chinese locale.
                Locale locale = Locale.CHINESE;
                sort = new Sort(new SortField(sortField, locale, reverse));
            } else {
                sort = new Sort(new SortField(sortField, SortField.STRING, reverse));
            }
        } else {
        	sort = new Sort();
        }
        fullTextQuery.setSort(sort);
        fullTextQuery.setFirstResult(firstResult);
        fullTextQuery.setMaxResults(maxResult);
        List<T> resultList = fullTextQuery.list();
        Transaction tr = ftSession.beginTransaction();
        tr.commit();
        ftSession.close();
        return resultList;
    }

    /**
     * query totalRecords from index
     * @param query
     * @param sortField
     * @param sortType
     * @param filter
     * @return int 
     */
    @Override
    public int queryTotalRecordsFromIndex(Query query, QueryWrapperFilter filter) {
        FullTextSession ftSession = Search.getFullTextSession(this.getSession());
        FullTextQuery fullTextQuery = ftSession.createFullTextQuery(query, getEntityClass());
        if(null != filter) {
            fullTextQuery.setFilter(filter);
        }
        int resultSize = fullTextQuery.getResultSize();
        Transaction tr = ftSession.beginTransaction();
        tr.commit();
        ftSession.close();
        return resultSize;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void rebuildAllEntitiesIndexes(String hql) {
        FullTextSession ftSession = Search.getFullTextSession(this.getSession());
        int maxResult = Integer.parseInt(PropertyUtils
                .readValue(ConfigureConstants.INDEX_PROPERTIES_PATH, 
                           ConfigureConstants.INDEX_BATCH_SIZE));
        org.hibernate.Query query = getSession().createQuery(hql)
                                    .setMaxResults(maxResult);
        int j = 0;
        int sum = 0;
        while (true) {
            List<T> list = query.setFirstResult(j).list();
            for (T t : list) {
                ftSession.index(t);
            }
            j += maxResult;
            sum += list.size();
            if (list.size() < maxResult)
                break;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> findEntityListByPropertyList(String property,
            Collection<Object> propertyValueList) {
        List<T> entityList = null;
        
        // The property is in propertyValueList.
        Criteria entityCriteria = this.getSession().createCriteria(getEntityClass());
        entityCriteria.add(Restrictions.in(property, propertyValueList));
        
        entityList = entityCriteria.list();
        return entityList;
    }
}
