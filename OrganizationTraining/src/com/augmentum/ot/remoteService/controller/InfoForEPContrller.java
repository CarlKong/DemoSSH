package com.augmentum.ot.remoteService.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.augmentum.ot.dataObject.constant.ConfigureConstants;
import com.augmentum.ot.remoteService.dataObject.Column;
import com.augmentum.ot.remoteService.dataObject.Content;
import com.augmentum.ot.remoteService.dataObject.Display;
import com.augmentum.ot.remoteService.dataObject.Group;
import com.augmentum.ot.remoteService.dataObject.Meta;
import com.augmentum.ot.remoteService.dataObject.Row;
import com.augmentum.ot.remoteService.dataObject.Screen;
import com.augmentum.ot.remoteService.dataObject.Screens;
import com.augmentum.ot.remoteService.constant.RemoteProperyConstants;
import com.augmentum.ot.remoteService.helper.CDataXSDriver;
import com.augmentum.ot.remoteService.helper.ColumnConvert;
import com.thoughtworks.xstream.XStream;

@Controller
@RequestMapping(value="/forEP")
public class InfoForEPContrller {

	@RequestMapping(value = "/notice", method = RequestMethod.GET)
    @ResponseBody
    public String getNoticeXML(@RequestParam(value = "size", required = false, defaultValue = "large") String size, 
    						   @RequestParam(value = "locale", required=false, defaultValue = "zh") String locale){
        return getNotice(size, locale);
    }
	
	/**
	 * 
	 * @param size
	 * @param locale
	 * @return
	 */
    public String getNotice(String size, String locale) {
        String info1 = "We have finished our phase2. Our system have comblined with E-learning. ";
        String info2 = "Please help us refine it.";
        String header = "Welcom to ot system";
        String systemName = "";
        //Enghlish info
        Screen screen1 = this.buildHeaderScreen(header, info1, info2);
        Screen screen2 = this.buildHeaderScreen(header, info1, info2);
        
        List<Screen> screenList1 = new ArrayList<Screen>();
        screenList1.add(screen1);
        screenList1.add(screen2);
        Screens screens1 = new Screens(RemoteProperyConstants.SCREENS_I10N_EN, RemoteProperyConstants.SCREENS_SLIDEEFFECT_LEFT, screenList1);
        //Chinese info
        Screen screen3 = this.buildHeaderScreen(header, info1, info2);
        Screen screen4 = this.buildHeaderScreen(header, info1, info2);
        List<Screen> screenList2 = new ArrayList<Screen>();
        screenList2.add(screen3);
        screenList2.add(screen4);
        Screens screens2 = new Screens(RemoteProperyConstants.SCREENS_I10N_ZH, RemoteProperyConstants.SCREENS_SLIDEEFFECT_LEFT, screenList2);
        
        Meta meta = new Meta(systemName, 2);
        Display display = new Display();
        display.setMeta(meta);
        if(RemoteProperyConstants.LARGE_XML.equals(size)) {
            if(ConfigureConstants.LANGUAGE_ZH.equals(locale)) {
                display.setScreens(screens2);
            } else {
                display.setScreens(screens1);
            }
            
        } 
        
        XStream xstream = new XStream(new CDataXSDriver());
        xstream.registerConverter(new ColumnConvert());
        xstream.alias("column", Column.class);
        xstream.alias("row", Row.class);
        xstream.alias("group", Group.class);
        xstream.alias("screen", Screen.class);
        xstream.alias("screens", Screens.class);
        xstream.alias("meta", Meta.class);
        xstream.alias("display", Display.class);

        xstream.useAttributeFor(Column.class, "type");
        xstream.useAttributeFor(Screen.class, "pattern");
        xstream.useAttributeFor(Screens.class, "i10n");
        xstream.useAttributeFor(Screens.class, "slideEffect");
        
        xstream.addImplicitCollection(Row.class, "columns");
        xstream.addImplicitCollection(Group.class, "rows");
        xstream.addImplicitCollection(Content.class, "groups");
        xstream.addImplicitCollection(Screens.class, "screenList");
        
        return xstream.toXML(display);
    }
    
    private Screen buildHeaderScreen(String header, String... info) {
        List<Row> rowList = new ArrayList<Row>();
        for(int i = 0; i < info.length; i++) {
            Column column = new Column(RemoteProperyConstants.COLUMN_TYPE_TEXT, info[i]);
            List<Column> columnList = new ArrayList<Column>();
            columnList.add(column);
            Row row = new Row(columnList);
            rowList.add(row);
        }
        Group group = new Group(rowList);
        List<Group> groupList = new ArrayList<Group>();
        groupList.add(group);
        Content content = new Content(groupList);
        Screen screen;
        if("".equals(header)) {
            screen = new Screen(header, content, RemoteProperyConstants.SCREEN_PATTERN_HEAD);
        } else {
            screen = new Screen(header, content, RemoteProperyConstants.SCREEN_PATTERN_HEAD);
        }
        
        return screen;
    }
    
    @SuppressWarnings("unused")
    private Screen buildTableScreen(String... info) {
        Integer count = 0;
        List<Row> rowList = new ArrayList<Row>();
        Integer size;
        if(info.length % 3 == 0) {
            size = info.length / 3;
        } else {
            size = info.length / 3 + 1;
        }
        for(int i = 0; i < size; i++) {
            List<Column> columnList = new ArrayList<Column>();
            if(count > info.length) {
                count = info.length;
            } else {
                count += 3;
            }
            for(int j = 3; j > 0 && count < info.length; j--) {
                Column column = new Column(RemoteProperyConstants.COLUMN_TYPE_TEXT, info[count - j]);
                columnList.add(column);
            }
            Row row = new Row(columnList);
            rowList.add(row);
        }
        Group group = new Group(rowList);
        List<Group> groupList = new ArrayList<Group>();
        groupList.add(group);
        Content content = new Content(groupList);
        Screen screen = new Screen("", content, RemoteProperyConstants.SCREEN_PATTERN_ONE_TABLE);
        return screen;
    }
}
