<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<body>
Hello world!${message},${number},${sex}
<br/>
<s:form> 
  <s:textfield label="number" name="number"/>
  <s:password label="message" name="message" />
  <s:textfield label="dateTest"  name="dateTest"/>
  <s:submit/>
</s:form>
<s:property value="getText('aa.test1')" />
</body>
</html>
