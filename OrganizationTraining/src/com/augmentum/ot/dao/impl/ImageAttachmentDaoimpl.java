package com.augmentum.ot.dao.impl;

import org.springframework.stereotype.Component;

import com.augmentum.ot.dao.ImageAttachmentDao;
import com.augmentum.ot.model.ImageAttachment;

@Component("imageAttachmentDao")
public class ImageAttachmentDaoimpl extends BaseDaoImpl<ImageAttachment>
		implements ImageAttachmentDao {

	@Override
	public Class<ImageAttachment> getEntityClass() {
		return ImageAttachment.class;
	}
}
