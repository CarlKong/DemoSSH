package com.augmentum.ot.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;



/**
 * A base interface, other interface we need to extend it
 * 
 * @version 0.1, 02/09/2012
 * @param <T>
 */
public interface BaseDao<T extends Serializable> {

    /**
     * delect the object<T>.
     * 
     * @param t
     * @return true|delete the object<t> is success, false|delete the object<t>
     *         is error.
     */
    public void deleteObject(T t);

    /**
     * find the objece<T> by id
     * 
     * @param id
     * @return object<T>
     */
    public T findByPrimaryKey(Serializable id);

    /**
     * find a collection include all of the object<T>
     * 
     * @return List<T>
     */
    public List<T> loadAll();

    /**
     * find a collection in firstResult and maxResult
     * 
     * @param t
     * @param firstResult
     * @param maxResult
     * @return list<T>
     */
    public List<T> loadPage(int firstResult, int maxResult);

    /**
     * save the abject<T>.
     * 
     * @param t
     */
    public void saveObject(T t);

    /**
     * update the object<T>
     * 
     * @param t
     * @return object<T>
     */
    public void updateObject(T t);

    /**
     * save or update object<T>
     * 
     * @param t
     * @throws DaoException
     */
    public void saveOrUpdateObject(T t);
    
    public void mergeObject(T t);
    
    /**
     * save or update object<T> index
     * @param t
     */
    public void saveOrUpdateObjectIndex(T t);
    
    /**
     * delete object<T> index
     * @param t
     */
    public void deleteObjectIndex(Serializable id);
    
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
    public List<T> queryObjectFromIndex(Query query, String sortField,
            Boolean reverse, QueryWrapperFilter filter, int firstResult,
            int maxResult);
    
    /**
     * query totalRecords from index
     * @param query
     * @param sortField
     * @param sortType
     * @param filter
     * @return int 
     */
    public int queryTotalRecordsFromIndex(Query query, QueryWrapperFilter filter);
    
     /** 
     * @Description: for entity rebuild by detachedCriteria
     * @param detachedCriteria 
     */ 
    public void rebuildAllEntitiesIndexes(String hql);
    
    /**
     * Find the entity list which's property is in propertyValueList.
     * 
     * @param property  The field in entity.
     * @param propertyValueList  The field value collection.
     * @return  The entity list.
     */
    public List<T> findEntityListByPropertyList(String property, Collection<Object> propertyValueList);
}
