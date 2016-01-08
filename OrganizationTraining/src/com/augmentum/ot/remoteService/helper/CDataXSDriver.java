package com.augmentum.ot.remoteService.helper;

import java.io.Writer;

import com.augmentum.ot.remoteService.constant.RemoteProperyConstants;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

public class CDataXSDriver extends XppDriver{

    public HierarchicalStreamWriter createWriter(Writer out) {
        return new PrettyPrintWriter(out){
            @Override
            protected void writeText(QuickWriter writer, String text) {
                if(text.startsWith(RemoteProperyConstants.PREFIX_CDATA) 
                     && text.endsWith(RemoteProperyConstants.SUFFIX_CDATA)) {
                         writer.write(text);
                  }else{
                      super.writeText(writer, text);
                  }
            }
            
        };
    }
}
