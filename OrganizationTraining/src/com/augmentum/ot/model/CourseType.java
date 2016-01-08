package com.augmentum.ot.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Fields;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

import com.augmentum.ot.util.InternationalizationUtils;



/**
 * @version 0.2, 07/12/2012
 */
@Entity
@Table(name = "course_type")
public class CourseType implements Serializable {

    private static final long serialVersionUID = 1L;

    /** id of courseType */
    private Integer courseTypeId;

    /** name of courseName */
    private String typeName;
    

    @GenericGenerator(name = "generator", strategy = "increment")
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "id", unique = true, nullable = false)
    @DocumentId(name="type_id")
    public Integer getCourseTypeId() {
        return courseTypeId;
    }

    public void setCourseTypeId(Integer courseTypeId) {
        this.courseTypeId = courseTypeId;
    }

    @Column(name = "name", length = 100, nullable = false, unique = true)
    @Fields({
    	@Field(name="type_name", index=Index.TOKENIZED, store=Store.YES),
        @Field(name="type_name_sort", index=Index.UN_TOKENIZED, store=Store.YES)
    })
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    
    @Transient
    @Fields({
        @Field(name="type_name_zh", index=Index.TOKENIZED, store=Store.YES),
        @Field(name="type_name_sort_zh", index=Index.UN_TOKENIZED, store=Store.YES)
    })
    public String getTypeNameZH() {
        String temp = InternationalizationUtils.getValueByKeyZH(this.getTypeName()); 
        return  temp;
    }
    
    
    @Transient
    @Fields({
        @Field(name="type_name_en", index=Index.TOKENIZED, store=Store.YES),
        @Field(name="type_name_sort_en", index=Index.UN_TOKENIZED, store=Store.YES)
    })
    public String getTypeNameEN() {
        String temp = InternationalizationUtils.getValueByKeyEN(this.getTypeName());
        return temp;
    }


    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((courseTypeId == null) ? 0 : courseTypeId.hashCode());
		result = prime * result
				+ ((typeName == null) ? 0 : typeName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CourseType other = (CourseType) obj;
		if (courseTypeId == null) {
			if (other.courseTypeId != null)
				return false;
		} else if (!courseTypeId.equals(other.courseTypeId))
			return false;
		if (typeName == null) {
			if (other.typeName != null)
				return false;
		} else if (!typeName.equals(other.typeName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CourseType [courseTypeId=");
		builder.append(courseTypeId);
//		builder.append(", courses.size()=");
//		builder.append(courses.size());
		builder.append(", typeName=");
		builder.append(typeName);
		builder.append("]");
		return builder.toString();
	}

}
