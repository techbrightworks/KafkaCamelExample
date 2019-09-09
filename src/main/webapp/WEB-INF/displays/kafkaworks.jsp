<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>My Works:KafkaCamelWorks</title>
    <script type="text/javascript"	src="<c:url value="jquery/jquery.min.js" />"></script>
    <script type="text/javascript"	src="<c:url value="jquery/jQuery.Validate.min.js" />"></script>
    <script type="text/javascript"	src="<c:url value="jquery/messageFunctions.js" />"></script>

</head>
<body>
<table>
<tr>
    <td>Your producer message here:</td>
    <td><input type="text" id="producerMessage"	name="producerMessage" placeholder="producerMessage" />
        <input	type="button" value="TypeYourMessage" onClick="doSendProducerMessage()" /></td>
    <td colspan="2"><div id="infoconsumermessage" style="color: blue;"></div></td>
</tr>
</table>

</body>
</html>