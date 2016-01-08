package com.augmentum.ot.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "image_attachment")
public class ImageAttachment implements Serializable {

	private static final long serialVersionUID = -4798581790389727963L;
	
	/**id of image**/
	private Integer imageId;
	
	/**address of image**/
	private String imagePath;
	
	/**name of image**/
	private String imageName;
	
	/**if image is usable**/
	private Integer imageUsable;

	@GenericGenerator(name = "generator", strategy = "increment")
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "image_attachment_id", unique = true, nullable = true)
	public Integer getImageId() {
		return imageId;
	}

	public void setImageId(Integer imageId) {
		this.imageId = imageId;
	}

	@Column(name = "image_attachment_path", length = 200, nullable = false)
	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	@Column(name = "image_attachment_name", length = 100, nullable = false)
	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	@Column(name = "image_attachment_usable", length = 11, nullable = true)
	public Integer getImageUsable() {
		return imageUsable;
	}

	public void setImageUsable(Integer imageUsable) {
		this.imageUsable = imageUsable;
	}

	@Override
	public String toString() {
		return "ImageAttachment [imageId=" + imageId + ", imageName="
				+ imageName + ", imagePath=" + imagePath + ", imageUsable="
				+ imageUsable + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((imageId == null) ? 0 : imageId.hashCode());
		result = prime * result
				+ ((imageName == null) ? 0 : imageName.hashCode());
		result = prime * result
				+ ((imagePath == null) ? 0 : imagePath.hashCode());
		result = prime * result
				+ ((imageUsable == null) ? 0 : imageUsable.hashCode());
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
		ImageAttachment other = (ImageAttachment) obj;
		if (imageId == null) {
			if (other.imageId != null)
				return false;
		} else if (!imageId.equals(other.imageId))
			return false;
		if (imageName == null) {
			if (other.imageName != null)
				return false;
		} else if (!imageName.equals(other.imageName))
			return false;
		if (imagePath == null) {
			if (other.imagePath != null)
				return false;
		} else if (!imagePath.equals(other.imagePath))
			return false;
		if (imageUsable == null) {
			if (other.imageUsable != null)
				return false;
		} else if (!imageUsable.equals(other.imageUsable))
			return false;
		return true;
	}

	
}
