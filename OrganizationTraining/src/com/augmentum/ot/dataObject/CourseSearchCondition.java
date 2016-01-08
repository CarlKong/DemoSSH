package com.augmentum.ot.dataObject;

/**
 * 
 * @version 0.1, 07/13/2012
 */
public class CourseSearchCondition extends SearchCondition {
    
	/** All selected type names MUST BE: "typeId1 typeId2 typeId3" */
    private String typeIds;
    
    /** Is Certificated, MUST BE: "1"; isn't Certificated MUST BE: "0"; both MUST BE: "1 0"*/
    private String isCertificateds;

    /** Field use to sort. MUST USE one string declared in class ModelColumnNameConstant*/
    private String sortField;
    private String sortName; // sortName for selectList plugin
    
    private String sortSign; // sortSign for selectList plugin, "asc" "desc"
    
    private String pageNum; // pageNum for selectList plugin
    
	public String getTypeIds() {
        return typeIds;
    }

    public void setTypeIds(String typeIds) {
        this.typeIds = typeIds;
    }

    public String getIsCertificateds() {
        return isCertificateds;
    }

    public void setIsCertificateds(String isCertificateds) {
        this.isCertificateds = isCertificateds;
    }


    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }



	public String getSortName() {
		return sortName;
	}

	public void setSortName(String sortName) {
		this.sortName = sortName;
		this.sortField = this.sortName;
	}

	public String getSortSign() {
		return sortSign;
	}

	public void setSortSign(String sortSign) {
		this.sortSign = sortSign;
		if (this.sortSign.equals("desc")) {
			setReverse(true);
		} else {
			setReverse(false);
		}
	}

	public String getPageNum() {
		return pageNum;
	}

	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
		setNowPage(Integer.parseInt(this.pageNum));
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CourseSearchCondition [isCertificateds=");
		builder.append(isCertificateds);
		builder.append(", pageNum=");
		builder.append(pageNum);
		builder.append(", sortField=");
		builder.append(sortField);
		builder.append(", sortName=");
		builder.append(sortName);
		builder.append(", sortSign=");
		builder.append(sortSign);
		builder.append(", typeIds=");
		builder.append(typeIds);
		builder.append("]");
		return builder.toString();
	}
	
}
