package com.augmentum.ot.remoteService.helper;

import com.augmentum.ot.remoteService.dataObject.Column;
import com.augmentum.ot.remoteService.constant.RemoteProperyConstants;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class ColumnConvert implements Converter {

    @Override
    public void marshal(Object object, HierarchicalStreamWriter writer, MarshallingContext context) {
        Column column = (Column) object;
        if (null != column) {
            if (null != column.getType()) {
                writer.addAttribute("type", column.getType());
            }
            if (null != column.getInformation()) {
                writer.setValue(RemoteProperyConstants.PREFIX_CDATA + column.getInformation() + RemoteProperyConstants.SUFFIX_CDATA);
            }
        }
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader arg0, UnmarshallingContext arg1) {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean canConvert(Class type) {
        return type == Column.class;
    }
}
