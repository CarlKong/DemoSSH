package com.augmentum.ot.dataObject;

import java.util.ArrayList;
import java.util.List;

import com.augmentum.ot.dataObject.constant.ErrorCodeConstants;
import com.augmentum.ot.exception.DataWarningException;

/**
 * Page Util
 *
 */
public class Page<T> {
    private Integer nowPager = 1;
    private Integer firstResult;
    private Integer maxResult;
    private Integer lastResult;
    private Integer pageSize = 10;
    private Integer totalRecords;
    private Integer totalPage;
    private List<T> list;
	private Boolean isHasPrePager;
	private Boolean isHasNextPager;;

    public Integer getNowPager() {
        return nowPager;
    }

    public void setNowPager(Integer nowPager) {
        this.nowPager = nowPager;
    }

    
    
    public Integer getFirstResult() {
        firstResult = (nowPager-1)*pageSize;
        if(firstResult <= 0) {
            firstResult = 0;
        }
        return firstResult;
    }

    public void setFirstResult(Integer firstResult) {
        this.firstResult = firstResult;
    }

    public Integer getMaxResult() {
        maxResult = pageSize;
        return maxResult;
    }

    public void setMaxResult(Integer maxResult) {
        this.maxResult = maxResult;
    }

    
    public Integer getLastResult() {
    	if(this.getFirstResult() == null || this.getMaxResult() == null){
    		return 0;
    	}
        lastResult = this.getFirstResult() + this.getMaxResult();
        return lastResult;
    }

    public void setLastResult(Integer lastResult) {
        this.lastResult = lastResult;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(Integer totalRecords) {
        this.totalRecords = totalRecords;
    }

    public Integer getTotalPage() {
    	if(totalRecords == null || pageSize == null){
    		return 0;
    	}
        if (totalRecords % pageSize == 0) {
            totalPage = totalRecords / pageSize;
        } else {
            totalPage = totalRecords / pageSize + 1;
        }
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

	public Boolean getIsHasPrePager() {
		if(nowPager <=1){
			isHasPrePager = false;
		}else{
			isHasPrePager = true;
		}
		return isHasPrePager;
	}

	public void setIsHasPrePager(Boolean isHasPrePager) {
		this.isHasPrePager = isHasPrePager;
	}

	public Boolean getIsHasNextPager() {
		if(nowPager == null || this.getTotalPage() == null){
			return false;
		}
		if(nowPager >= this.getTotalPage()){
			isHasNextPager = false;
		}else{
			isHasNextPager = true;
		}
		 return isHasNextPager;
	}
	

	public void setIsHasNextPager(Boolean isHasNextPager) {
		this.isHasNextPager = isHasNextPager;
	}

	@Override
	public String toString() {
		return "Page [firstResult=" + firstResult + ", isHasNextPager="
				+ isHasNextPager + ", isHasPrePager=" + isHasPrePager
				+ ", lastResult=" + lastResult + ", list=" + list
				+ ", maxResult=" + maxResult + ", nowPager=" + nowPager
				+ ", pageSize=" + pageSize + ", totalPage=" + totalPage
				+ ", totalRecords=" + totalRecords + "]";
	}

	/**
	 * Util: change from list to page
	 * @param objList
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws DataWarningException
	 */
	public Page<T> listToPage (List<T> objList, 
				int pageNo, int pageSize) throws DataWarningException {
		Page<T> objPage = new Page<T>();
		objPage.setTotalRecords(objList.size());
		objPage.setPageSize(pageSize);
		int totalPage = (objList.size() % pageSize == 0) ? (objList.size() / pageSize) : (objList.size() / pageSize + 1);
		if (totalPage != 0 && pageNo > totalPage) {
			throw new DataWarningException(ErrorCodeConstants.SERVER_ERROR);
		}
		objPage.setTotalPage(totalPage);
		objPage.setNowPager(pageNo);
		objPage.setFirstResult(pageSize*(pageNo - 1));
		int maxResult = (pageNo == totalPage && (objList.size() % pageSize != 0)) ? (objList.size() % pageSize) : pageSize;
		objPage.setMaxResult(maxResult);
		int lastResult = objPage.getFirstResult() + maxResult - 1;
		objPage.setLastResult(lastResult);
		if (pageNo <= 1) {
			objPage.setIsHasPrePager(false);
		} else {
			objPage.setIsHasPrePager(true);
		}
		if (pageNo == totalPage) {
			objPage.setIsHasNextPager(false);
		} else {
			objPage.setIsHasNextPager(true);
		}
		List<T> thisPageList = new ArrayList<T>();
		if (objList.size()>0) {
			for (int i = objPage.getFirstResult(); i<= lastResult; i++) {
				thisPageList.add(objList.get(i));
			}
		}
		objPage.setList(thisPageList);
		return objPage;
	}

}
