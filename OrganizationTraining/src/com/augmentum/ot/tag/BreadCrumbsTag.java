package com.augmentum.ot.tag;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts2.ServletActionContext;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.augmentum.ot.dataObject.constant.ConfigureConstants;
import com.opensymphony.xwork2.ActionContext;

/**
 * Implement the bread crumb
 * 
 * @Project OT
 * @Modificaion_history 2012-10-9
 * @Version 1.0
 */
public class BreadCrumbsTag extends TagSupport {
    private static final long serialVersionUID = -2211298724171181080L;

    private String nameSpace;
    private String actionName;
    private String href;
    private Element target;
    private static final String BREAD_CRUMBS_ZH = "breadCrumbs/breadCrumbs-zh.xml";
    private static final String BREAD_CRUMBS_EN = "breadCrumbs/breadCrumbs-en.xml";

    @Override
    public int doStartTag() throws JspException {
        ActionContext actionContext = ActionContext.getContext();
        String language = actionContext.getLocale().toString();
        nameSpace = ServletActionContext.getActionMapping().getNamespace();
        actionName = actionContext.getName();
        href = nameSpace.substring(1, nameSpace.length()) + "/" + actionName;
        InputStream inputStream = null;
        Element root = null;
        try {
            if (ConfigureConstants.LOCALE_ZH.equals(language)
                    || ConfigureConstants.LANGUAGE_ZH.equals(language)) {
                root = (Element) pageContext.getServletContext().getAttribute(
                        "language_zh");
                if (null == root) {
                    // read resource file.
                    inputStream = this.getClass().getClassLoader()
                            .getResourceAsStream(BREAD_CRUMBS_ZH);
                    // parse XML file.
                    SAXReader reader = new SAXReader();
                    Document document = reader.read(inputStream);
                    // get root node of document.
                    root = document.getRootElement();
                    pageContext.getServletContext().setAttribute("language_zh",
                            root);
                }
            }
            if (ConfigureConstants.LOCALE_EN.equals(language)
                    || ConfigureConstants.LANGUAGE_EN.equals(language)) {
                root = (Element) pageContext.getServletContext().getAttribute(
                        "language_en");
                if (null == root) {
                    // read resource file.
                    inputStream = this.getClass().getClassLoader()
                            .getResourceAsStream(BREAD_CRUMBS_EN);
                    // parse XML file.
                    SAXReader reader = new SAXReader();
                    Document document = reader.read(inputStream);
                    // get root node of document.
                    root = document.getRootElement();
                    pageContext.getServletContext().setAttribute("language_en",
                            root);
                }
            }
            parseParent(root);

            StringBuffer displayInfo = new StringBuffer();
            List<String> titles = new ArrayList<String>();
            List<String> hrefs = new ArrayList<String>();

            while (null != target) {
                // set tag's title attribute
                Attribute attTitle = target.attribute("title");
                if (null != attTitle) {
                    titles.add(attTitle.getText());
                }
                Attribute attHref = target.attribute("href");
                if (null != attHref) {
                    String href = attHref.getText();
                    if (href.indexOf("/") != -1) {
                        String[] temp = href.split("/");
                        hrefs.add(temp[temp.length - 1]);
                    } else {
                        hrefs.add(href);
                    }
                } else {
                    hrefs.add("");
                }
                target = target.getParent();
            }

            for (int i = titles.size() - 1; i >= 0; i--) {
                String itemHref = hrefs.get(i);
                if ("".equals(itemHref)) {
                    displayInfo.append(titles.get(i));
                    displayInfo.append(" &gt;&nbsp; ");
                } else if (i == 0) {
                    displayInfo.append("<a class='navigate'>");
                    displayInfo.append(titles.get(i));
                    displayInfo.append("</a>");
                } else {
                    displayInfo.append("<a class='navigate' href='" + itemHref
                            + "'>");
                    displayInfo.append(titles.get(i));
                    displayInfo.append("</a> &gt;&nbsp;");
                }
            }
            if (displayInfo.length() > 0) {
                this.pageContext.getOut().println(displayInfo);
            }

        } catch (Exception e) {
            throw new JspException(e);
        }

        return super.doStartTag();
    }

    /**
     * parse the xml file and get the element which attribute 'href' is the 
     * merger of nameSpace and actionName. 
     * @param Element parent
     * @return
     */
    @SuppressWarnings("unchecked")
    private void parseParent(Element parent) {
        Iterator<Element> iterator = parent.elementIterator();
        while (iterator.hasNext()) {
            Element tempElement = iterator.next();
            Attribute attribute = tempElement.attribute("href");
            if (null != attribute && attribute.getText().equals(href)) {
                target = tempElement;
                return;
            }
            parseParent(tempElement);
        }
    }
}
