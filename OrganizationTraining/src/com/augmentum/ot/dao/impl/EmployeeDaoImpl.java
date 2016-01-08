package com.augmentum.ot.dao.impl;


import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

import com.augmentum.ot.dao.EmployeeDao;
import com.augmentum.ot.dataObject.EmployeeSearchCondition;
import com.augmentum.ot.dataObject.RoleListSearchCondition;
import com.augmentum.ot.model.CustomColumns;
import com.augmentum.ot.model.Employee;
import com.augmentum.ot.model.PageSize;



/**
 * 
 * @version 0.1, 07/13/2012
 */
@Component("employeeDao")
public class EmployeeDaoImpl extends BaseDaoImpl<Employee> implements EmployeeDao {
    
    
    @Override
    public Class<Employee> getEntityClass() {
        return Employee.class;
    }

 
	@SuppressWarnings("unchecked")
	@Override
	public List<CustomColumns> findDefaultCustomColumns(Integer categoryFlag) {
		List<CustomColumns> list = getSession().createCriteria(CustomColumns.class).
		add(Restrictions.eq("categoryFlag", categoryFlag)).
		add(Restrictions.eq("customColumnDefault", 1)).list();
		return list;
	}

	@Override
	public CustomColumns findCustomColumnById(Integer id) {
		CustomColumns customColumn = 
			  (CustomColumns)getSession().get(CustomColumns.class, id);
		return customColumn;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PageSize> findAllPageSizesByCategory(Integer categoryFlag) {
		List<PageSize> list = getSession().createCriteria(PageSize.class).
		add(Restrictions.eq("categoryFlag", categoryFlag)).list();
		return list;
	}

	@Override
	public PageSize findOneOptionalPageSize(Integer categoryFlag,
			Integer pageSizeValue) {
		PageSize pageSize = (PageSize) getSession().createCriteria(PageSize.class).
		add(Restrictions.eq("categoryFlag", categoryFlag)).
		add(Restrictions.eq("pageSizeValue", pageSizeValue)).uniqueResult();
		return pageSize;	}


	@Override
	public Employee findEmployeeByName(String augUserName) {
		return (Employee)getSession().createQuery(
				"from " + getEntityClass().getSimpleName()
				+ " where augUserName=:augUserName").setString("augUserName", augUserName).uniqueResult();
	}


	@Override
	public CustomColumns findCustomCloumnByFieldName(int categoryFlag, String fieldName) {
		return (CustomColumns) getSession().createQuery(
				"from CustomColumns where fieldName=:fieldName and categoryFlag=:categoryFlag").setString("fieldName", fieldName).setInteger("categoryFlag", categoryFlag).uniqueResult();
	}
	
    @Override
    public String spellRemoteSqlForEmployeeList(
            RoleListSearchCondition criteria, List<String> employeeIds) {
        StringBuilder result = new StringBuilder("(");
        String queryString = criteria.getQueryString();
        if (queryString == null || "".equals(queryString)) {
            queryString = "%";
        } else {
            queryString = queryString.replaceAll("\\*", "%");
        }
        for (String str : criteria.getSearchFields().split(",")) {
            if ("".equals(str)) {
                continue;
            }
            result.append(str);
            result.append(" like ");
            result.append("'%" + queryString + "%'");
            result.append(" or ");
        }
        result.delete(result.length() - 4, result.length());
        if (employeeIds != null && (!employeeIds.isEmpty())) {
            result.append(") and (");
            for (String str : employeeIds) {
                result.append("employeeEmployeeId");
                result.append("=");
                result.append("'" + str + "'");
                result.append(" or ");
            }
            result.delete(result.length() - 4, result.length());
            result.append(")");
        } else {
            result.delete(0, 1);
        }
        return result.toString();
    }

    @Override
    public Employee findEmployeeByJobNumber(String augEmpId) {
        return (Employee) getSession().createQuery(
                "from " + getEntityClass().getSimpleName()
                        + " where augEmpId=:augEmpId").setString("augEmpId",
                augEmpId).uniqueResult();
    }


	@Override
	public Employee findEmployeeByNameAndPwd(String userName, String password) {
		return (Employee) getSession().createQuery(
                "from " + getEntityClass().getSimpleName()
                        + " where augUserName=:userName and password=:password")
                        .setString("userName", userName)
                        .setString("password", password)
                        .uniqueResult();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Employee> findEmployeesByConditions(
			EmployeeSearchCondition condition) {
		// TODO
        List<Employee> result = new ArrayList<Employee>();  
        Criteria criteria = getSession().createCriteria(getEntityClass());
        int length = condition.getSearchFields().length;
        if (length == 2) {
        	criteria.add(Restrictions.or(Restrictions.like("augUserName", condition.getQueryString(), MatchMode.ANYWHERE), 
        				 Restrictions.like("augEmpId", condition.getQueryString(), MatchMode.ANYWHERE)));
        }
        if (length == 1) {
        	if (condition.getSearchFields()[0].equals("employeeId")) {
        		criteria.add(Restrictions.like("augEmpId", condition.getQueryString(), MatchMode.ANYWHERE));
        	}
        	if (condition.getSearchFields()[0].equals("employeeName")) {
        		criteria.add(Restrictions.like("augUserName", condition.getQueryString(), MatchMode.ANYWHERE));
        	}
        }
        criteria.setFirstResult(condition.getFirstResult());  
        criteria.setMaxResults(condition.getPageSize());  

        result = criteria.list();  
		return result;
	}


	@Override
	public int findEmployeesByConditionsCount(
			EmployeeSearchCondition condition) {
		Criteria criteria = getSession().createCriteria(getEntityClass());  
		int length = condition.getSearchFields().length;
        if (length == 2) {
        	criteria.add(Restrictions.or(Restrictions.like("augUserName", condition.getQueryString(), MatchMode.ANYWHERE), 
        				 Restrictions.like("augEmpId", condition.getQueryString(), MatchMode.ANYWHERE)));
        }
        if (length == 1) {
        	if (condition.getSearchFields()[0].equals("employeeId")) {
        		criteria.add(Restrictions.like("augEmpId", condition.getQueryString(), MatchMode.ANYWHERE));
        	}
        	if (condition.getSearchFields()[0].equals("employeeName")) {
        		criteria.add(Restrictions.like("augUserName", condition.getQueryString(), MatchMode.ANYWHERE));
        	}
        }
        criteria.setFirstResult(0);  
        criteria.setProjection(null);
        criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));  
        long count = (Long)criteria.uniqueResult();
		return (int)count;
	}

}
