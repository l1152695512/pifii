package com.yf.tradecontrol;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;
import org.xml.sax.InputSource;

public class JDomHandler {
	private Document doc=null;

	public static final String ENCODE_GB2312 = "GBK";

	private static Format format = Format.getPrettyFormat();

	private static XMLOutputter outputter = new XMLOutputter(format);

	private Logger logger = Logger.getLogger(this.getClass().getName());

	
	
	
	public Document addNodeVaueWithCheckAllPathNodes(String parentElementPath, String elementName,
			String value) throws JDOMException, JDomHandlerException {
		checkPathNodes(parentElementPath);
		addNodeVaue(parentElementPath, elementName, value);
		
		return doc;
	}

	/***************************************************************************
	 * 增加任意节点的值，父节点和设置节点不能为空
	 * 
	 * 
	 * @param parentElementPath
	 *            父节点路径
	 * @param elementName
	 *            添加的节点的路径
	 * @param value
	 *            添加的节点的值
	 * @return 返回Document
	 * @throws JDOMException
	 * @throws JDomHandlerException
	 * 
	 * —————————————————————————————————————————————————————————— 修改列表：
	 * —————————————————————————————————————————————————————————— 时间： 作者： 描述：
	 * —————————————————————————————————————————————————————————— 2009-01-05 姚辉
	 * 添加了错误处理机制，与容错机制
	 * ——————————————————————————————————————————————————————————
	 **************************************************************************/
	public Document addNodeVaue(String parentElementPath, String elementName,
			String value) throws JDOMException, JDomHandlerException {
		Element parentElement = null;
		if (parentElementPath == null || "".equals(parentElementPath)) {
			throw new JDomHandlerException("添加节点值异常：父节点输入不能为空，或者不能为null");
		}
		if (elementName == null || "".equals(elementName)) {
			throw new JDomHandlerException("添加节点值异常：设置的节点输入不能为空，或者不能为null");
		}
		if (value == null) {
			value = "";
		}
		
		parentElement = (Element) XPath
				.selectSingleNode(doc, parentElementPath);
		Element element=new Element(elementName);
		element.setText(value);
		parentElement.addContent(element);
		
		return doc;
	}

	/***************************************************************************
	 * 获得指定报文节点的相关属性
	 * 
	 * @param path
	 *            输入的指定报文节点的路径
	 * @param attribute
	 *            指定的报文节点的属性
	 * @return
	 * @throws JDOMException
	 * @throws JDomHandlerException
	 * 
	 * ———————————————————————————————————————————————————————————— 修改列表：
	 * ———————————————————————————————————————————————————————————— 时间： 作者： 描述：
	 * ———————————————————————————————————————————————————————————— 2009-01-05
	 * 姚辉 添加了错误处理机制，与容错机制
	 * ————————————————————————————————————————————————————————————
	 **************************************************************************/
	public String getNodeAttribute(String path, String attribute)
			throws JDOMException, JDomHandlerException {
		Element visitElment = null;
		String attribute_Str = "";
		visitElment = (Element) XPath.selectSingleNode(doc, path);
		if (visitElment != null) {
			Attribute attr = visitElment.getAttribute(attribute);
			if (attr == null) {
				throw new JDomHandlerException("获取xml节点属性异常：当前报文节点不存在当前属性");

			}
			attribute_Str = attr.getValue();
		} else {
			throw new JDomHandlerException("获取xml节点属性异常：不存在当前报文节点");
		}
		return attribute_Str;
	}

	/***************************************************************************
	 * 获得指定报文节点的相关属性
	 * 
	 * @param path
	 *            输入的指定报文节点的路径
	 * @param attribute
	 *            指定的报文节点的属性
	 * @return
	 * @throws JDOMException
	 * @throws JDomHandlerException
	 * 
	 * ———————————————————————————————————————————————————————————— 修改列表：
	 * ———————————————————————————————————————————————————————————— 时间： 作者： 描述：
	 * ———————————————————————————————————————————————————————————— 2009-01-05
	 * 姚辉 添加了错误处理机制，与容错机制
	 * ————————————————————————————————————————————————————————————
	 **************************************************************************/
	public String getNodeAttributeWithIndex(String path,int index, String attribute)
			throws JDOMException, JDomHandlerException {
		String value = "";
		this.checkPathNodes(path);
		List<Element> valueList = this.getNodeValues(path);
		if(valueList!=null){
			try{
				Attribute attr = valueList.get(index).getAttribute(attribute);
				if (attr == null) {
					throw new JDomHandlerException("获取xml节点属性异常：当前报文节点不存在当前属性");
				}
				value=attr.getValue();
			}catch (Exception e) {
				logger.debug("index>size");
			}
		}
		return value;
	}
	
	/***************************************************************************
	 * 设置指定报文节点的相关属性的值，
	 * 
	 * @param path
	 *            输入的指定报文节点的路径
	 * @param attribute
	 *            指定的报文节点的属性
	 * @param value
	 *            设定相关属性的值
	 * @return
	 * @throws JDOMException
	 * @throws JDomHandlerException
	 *             ————————————————————————————————————————————————————————————
	 *             修改列表：
	 *             ————————————————————————————————————————————————————————————
	 *             时间： 作者： 描述：
	 *             ————————————————————————————————————————————————————————————
	 *             2009-01-10 姚辉 新增加功能，用于设置指定报文节点的相关属性
	 *             ————————————————————————————————————————————————————————————
	 **************************************************************************/
	public void addNodeAttribute(String path, String attributeName, String value)
			throws JDOMException, JDomHandlerException {
		Element visitElment = null;
		visitElment = (Element) XPath.selectSingleNode(doc, path);
		if (visitElment != null) {
			visitElment.setAttribute(attributeName, value);
		} else {
			throw new JDomHandlerException("获取xml节点属性异常：不存在当前报文节点");
		}
	}

	public Document setNodeAttribute(String path, String attributeName, String attributeValue) {
		Element visitElment = null;
		String attribute_Str = null;
		try {
			visitElment = (Element) XPath.selectSingleNode(doc, path);
			visitElment.setAttribute(attributeName, attributeValue);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println();

		}
		return doc;
	}
	
	/***************************************************************************
	 * 修改指定报文节点的相关属性
	 * 
	 * @param path
	 *            输入的指定报文节点的路径
	 * @param attribute
	 *            指定的报文节点的属性
	 * @return
	 * @throws JDOMException
	 * @throws JDomHandlerException
	 * 
	 * ———————————————————————————————————————————————————————————— 修改列表：
	 * ———————————————————————————————————————————————————————————— 时间： 作者： 描述：
	 * ———————————————————————————————————————————————————————————— 2009-01-05
	 * 姚辉 添加了错误处理机制，与容错机制
	 * ————————————————————————————————————————————————————————————
	 **************************************************************************/
	public Document setNodeAttributeWithIndex(String path,String attribute, String value,int index)
			throws JDOMException, JDomHandlerException {
		this.checkPathNodes(path);
		List<Element> valueList = this.getNodeValues(path);
		if(valueList!=null){
			try{
				Attribute attr = valueList.get(index).getAttribute(attribute);
				attr.setValue(value);
				if (attr == null) {
					throw new JDomHandlerException("获取xml节点属性异常：当前报文节点不存在当前属性");
				}
			}catch (Exception e) {
				logger.debug("index>size");
			}
		}
		return doc;
	}
	
	/***************************************************************************
	 * 修改指定报文节点的相关属性
	 * 
	 * @param path
	 *            输入的指定报文节点的路径
	 * @param attribute
	 *            指定的报文节点的属性
	 * @return
	 * @throws JDOMException
	 * @throws JDomHandlerException
	 * 
	 * ———————————————————————————————————————————————————————————— 修改列表：
	 * ———————————————————————————————————————————————————————————— 时间： 作者： 描述：
	 * ———————————————————————————————————————————————————————————— 2009-01-05
	 * 姚辉 添加了错误处理机制，与容错机制
	 * ————————————————————————————————————————————————————————————
	 **************************************************************************/
	public Document setNodeValueWithIndex(String path,String name, String value,int index)
			throws JDOMException, JDomHandlerException {
		this.checkPathNodes(path);
		List<Element> valueList = this.getNodeValues(path);
		if(valueList!=null){
			try{
				Element elemenet = valueList.get(index);
				elemenet.setText(value);
				if (elemenet == null) {
					throw new JDomHandlerException("获取xml节点属性异常：当前报文节点不存在当前属性");
				}
			}catch (Exception e) {
				logger.debug("index>size");
			}
		}
		return doc;
	}
	
	public void addNodeAttribute(String path, Map<String, String> KV)
			throws JDOMException, JDomHandlerException {
		Element visitElment = null;
		visitElment = (Element) XPath.selectSingleNode(doc, path);
//		String attributeName = "";
		String value = "";
		if (visitElment != null) {
//			Set s = KV.keySet();
//			Iterator itr = s.iterator();
			for(String key:KV.keySet()){
				value = KV.get(key);
				visitElment.setAttribute(key, value);
			}
//			while (itr.hasNext()) {
//				attributeName = (String) itr.next();
//				value = KV.get(attributeName);
//				visitElment.setAttribute(attributeName, value);
//			}
		} else {
			throw new JDomHandlerException("获取xml节点属性异常：不存在当前报文节点");
		}
	}

	/**
	 * 得到报文节点的值
	 * 
	 * @param path
	 * @return
	 * @throws JDOMException
	 * @throws JDomHandlerException
	 */
	public String getNodeValue(String path) throws JDOMException,
			JDomHandlerException {
		Element visitElement = null;
		String value = "";
		visitElement = (Element) XPath.selectSingleNode(doc, path);
		if (visitElement != null) {
			value = visitElement.getValue();
		} else {
			// throw new JDomHandlerException("获取报文节点的值出错：当前节点不存在");

		}
		return value;
	}
	/**
	 * 得到报文节点的值
	 * 
	 * @param path
	 * @return
	 * @throws JDomHandlerException 
	 * @throws JDOMException 
	 * @throws JDOMException
	 * @throws JDomHandlerException
	 */
	@SuppressWarnings("unchecked")
	public String getNodeValueWithIndex(String path,int index) throws JDOMException, JDomHandlerException  {
		String value = "";
		this.checkPathNodes(path);
		List<Element> valueList = this.getNodeValues(path);
		if(valueList!=null){
			try{
				value = valueList.get(index).getValue();
			}catch (Exception e) {
				// TODO: handle exception
				logger.debug("index>size");
			}
		}
		return value;
	}
	/**
	 * 可以获取路径下所有节点
	 * @param path
	 * @return
	 * @throws JDOMException
	 */
	public List getNodeValues(String path) throws JDOMException {
		List list = XPath.selectNodes(doc, path);
		return list;
	}
	
	/**
	 * 获得结点下所有的节点 (因使用if 而不是for 导致只能获取第一个节点下的所有子节点)
	 * 
	 * @param path
	 * @return
	 * @throws JDOMException
	 */
	public List getNodeNames(String path) throws JDOMException {
		Element visitElement = null;
		List l = new ArrayList();
		visitElement = (Element) XPath.selectSingleNode(doc, path);
		if (visitElement != null) {
			l = visitElement.getChildren();
		}
		return l;
	}
	
	/**
	 * 获得结点下所有的节点
	 * 
	 * @param path
	 * @return
	 * @throws JDOMException
	 */
	public List getSingleNodeChildren(String path) throws JDOMException {
		Element visitElement = null;
		List l =null;
		visitElement = (Element) XPath.selectSingleNode(doc, path);
		if (visitElement != null) {
			l = visitElement.getChildren();
		}
		return l;
	}


	/**
	 * 判断获取指定Document的路径下是否有指定的节点名称
	 * 
	 * @param path
	 * @return
	 */
	public boolean hasNode(String path) throws JDOMException {
		Element element = (Element) XPath.selectSingleNode(doc, path);
		if (element == null) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 设置指定节点的值
	 * 
	 * @param message
	 *            要操作的Document对象
	 * @param path
	 *            路径
	 * @param nodeName
	 *            节点名称
	 * @return 设置指定节点值完成后的Doucument对象
	 * @throws JDomHandlerException
	 * @throws JDOMException
	 */
	public Document setNodeValues(String path, String nodeName, String value)
			throws JDOMException, JDomHandlerException {
		if (hasNode(path + "/" + nodeName)) {
			modifyNodeValue(path + "/" + nodeName, value);
		} else {
			addNodeVaue(path, nodeName, value);
		}
		return doc;
	}
	/**
	 * 批量添加 数据
	 * @param path
	 * @param elementName
	 * @param nodeMap
	 * @return
	 * @throws JDOMException
	 * @throws JDomHandlerException
	 */
	public Document addBatchNodeValues(String path,String elementName, Map<String,String> nodeMap) throws JDOMException, JDomHandlerException{
		Element parentElement = null;
		if (path == null || "".equals(path)) {
			throw new JDomHandlerException("添加节点值异常：父节点输入不能为空，或者不能为null");
		}

		// 判断节点是否存在,如果存在则不添加，否则添加新节点
		if (!hasNode(path)) {
			throw new JDomHandlerException("添加节点值异常：父节点不能为null");
		}
		Element pathelement=(Element) XPath.selectSingleNode(doc, path);
		Element pelement=new Element(elementName);
		pathelement.addContent(pelement);
		if (pelement != null) {
			for(String key:nodeMap.keySet()){
				pelement.addContent(new Element(key).setText(nodeMap.get(key)));
			}
		}
		return doc;
	}

	public Document loadXmlByPath(String path) {
		try {
			SAXBuilder builder = new SAXBuilder(false);
			doc = builder.build(new File(path));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doc;
	}
	
	public Document loadXmlByUrl(String url) {
		try {
			SAXBuilder builder = new SAXBuilder(false);
			doc = builder.build(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doc;
	}

	public Document modifyNodeValue(String path, String value)
			throws JDOMException {
		((Element) XPath.selectSingleNode(doc, path)).setText(value);
		return doc;
	}

	public Document removeSubNodes(String path, Document doc)
			throws JDOMException {
		List<Element> l = this.getNodeNames(path);
		String removePath = null;
		for(Element e:l){
			removePath = path + "/" + e.getName();
			doc = this.removeNode(removePath);
		}
		return doc;
	}

	/**
	 * 
	 * @param removeNodeName
	 *            删除结点的路径
	 * @return 删除一个结点后的xml报文
	 * @throws JDOMException
	 */

	public Document removeNode(String removeNodeName) throws JDOMException {
		Element visitElement = null;
		visitElement = (Element) XPath.selectSingleNode(doc, removeNodeName);
		if (visitElement != null)
			visitElement.getParent().removeContent(visitElement);
		return doc;
	}

	
	public void setDoc(Document doc) {
		this.doc = doc;
	}

	
	public Document loadXmlByString(String xml) throws JDOMException,
			IOException {
		StringReader read = new StringReader(xml);
		InputSource source = new InputSource(read);
		SAXBuilder sb = new SAXBuilder();
		doc = sb.build(source);
		return doc;
	}

	public String toString(String ENCODE) {
		Format forMat = Format.getPrettyFormat();
		forMat.setEncoding(ENCODE);
		XMLOutputter out = new XMLOutputter(forMat);
		String xml = out.outputString(doc);
		return xml;
	}
	
	public String toString() {
		return toString();
	}

	public Document getDoc() {
		return doc;
	}

	/**
	 * 添加新节点
	 * 
	 * @param parentElementPath
	 * @param elementName
	 * @param value
	 * @return
	 * @throws JDOMException
	 * @throws JDomHandlerException
	 */
	public Document addNode(String parentElementPath, String elementName)
			throws JDOMException, JDomHandlerException {
		Element parentElement = null;
		if (parentElementPath == null || "".equals(parentElementPath)) {
			throw new JDomHandlerException("添加节点值异常：父节点输入不能为空，或者不能为null");
		}
		if (elementName == null || "".equals(elementName)) {
			throw new JDomHandlerException("添加节点值异常：设置的节点输入不能为空，或者不能为null");
		}
		// 判断节点是否存在,如果存在则不添加，否则添加新节点
		if (!hasNode(parentElementPath + "/" + elementName)) {
			parentElement = (Element) XPath.selectSingleNode(doc,
					parentElementPath);
			parentElement.addContent(new Element(elementName));
		}
		return doc;
	}
	
	/**
	 * 设置指定节点的值,先检测所在路径的节点是否存在，如果不存在则添加节点
	 * 
	 * @param message
	 *            要操作的Document对象
	 * @param path
	 *            路径
	 * @param nodeName
	 *            节点名称
	 * @return 设置指定节点值完成后的Doucument对象
	 * @throws JDomHandlerException
	 * @throws JDOMException
	 */
	public Document setNodeValueWithCheckAllPathNodes(String path, String nodeName, String value) throws JDOMException, JDomHandlerException{
		checkPathNodes(path);
		
		setNodeValues(path, nodeName, value);
		
		return doc;
	}
	
	
	/**
	 * 添加批量节点
	 * @param path
	 * @param nodeMap
	 * @return
	 * @throws JDOMException
	 * @throws JDomHandlerException
	 */
	public Document addBatchNodes(String path, Map<String, String> nodeMap) throws JDOMException, JDomHandlerException{
		this.checkPathNodes(path);
		Element varElement =  (Element) XPath.selectSingleNode(doc, path);
		if (varElement != null) {
			for(String key:nodeMap.keySet()){
				varElement.addContent(new Element(key).setText(nodeMap.get(key)));
			}
		}
		return doc;
	}
	
	public Document addNodeAndAttr(String path, String nodeName,Map<String,String> attrMap) throws JDOMException, JDomHandlerException{
		this.checkPathNodes(path);
		Element praElement = (Element) XPath.selectSingleNode(doc, path);
		Element element = new Element(nodeName);
		String value = "";
		if (element != null) {
			for(String key:attrMap.keySet()){
				value = attrMap.get(key);
				element.setAttribute(key, value);
			}
		}
		praElement.addContent(element);
		return doc;
	}
	
	/**
	 * 路径检查
	 * @param path
	 * @throws JDOMException
	 * @throws JDomHandlerException
	 */
	public void checkPathNodes(String path) throws JDOMException,JDomHandlerException{
		String[] pathNames = path.split("/");
		String headPath = "";
		String tailName = "";
		
		for(int i=0;i<pathNames.length-1;i++){
			if(i==0){
				headPath = pathNames[i];
			}else{
				headPath = headPath + "/" + pathNames[i];
			}
			tailName = pathNames[i+1];
			
			if(!hasNode(headPath+"/"+tailName)){
				addNode(headPath,tailName);
			}
		}
	}
	/**
	 *         获得CPS返回的数据field下的值
	 *         只能获取第一个收款帐户
	 * @param workflowFields 
	 * @throws JDOMException
	 * @throws JDomHandlerException
	 */
	public String  getNodeAttributeCPS( String name) throws JDOMException,JDomHandlerException{
		 String     pathWorkFlowFields="ROOT/BODY/PRIVATE/MessageInfo/workflowFields";
		 String value="";
		 String attibute="";
		  List listcps=this.getNodeNames(pathWorkFlowFields);
			for(int i=0;i<listcps.size();i++){
        		Element et=(Element)listcps.get(i);
//             	System.out.print("cps返回的键："+et.getAttributeValue("name"));
//        		System.out.print("传入的值:"+name);
        		if(name.equalsIgnoreCase(et.getAttributeValue("name"))){
//        			System.out.println("    对应值："+et.getChildText("value"));
        			value= et.getChildText("value");	
        		}
    		}
			return value;
		
	}
	
	public final static String CPS_WORK_PATH="ROOT/BODY/PRIVATE/MessageInfo/workflowFields";
	public void setNodeAttributeCPS(String name,String attname,String value) throws JDOMException{
		List cpslist=this.getNodeValues(CPS_WORK_PATH);
		for (Iterator iter = cpslist.iterator(); iter.hasNext();) {
			Element element = (Element) iter.next();
			List fieldList=element.getChildren();
			for (int i = 0; i < fieldList.size(); i++) {
				Element field=(Element)fieldList.get(i);
				if(name.equalsIgnoreCase(field.getAttributeValue("name"))){
					field.setAttribute(attname, value);
				}
			}
		}
	}
	
	public void setNodeValueCPS(String name,String value) throws JDOMException{
		List cpslist=this.getNodeValues(CPS_WORK_PATH);
		for (Iterator iter = cpslist.iterator(); iter.hasNext();) {
			Element element = (Element) iter.next();
			List fieldList=element.getChildren();
			for (int i = 0; i < fieldList.size(); i++) {
				Element field=(Element)fieldList.get(i);
				if(name.equalsIgnoreCase(field.getAttributeValue("name"))){
					field.getChild("value").setText(value);
				}
			}
		}
	}
}
