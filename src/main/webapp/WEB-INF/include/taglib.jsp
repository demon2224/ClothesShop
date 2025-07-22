<%@ page import="utils.PriceFormatter" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%-- Function format --%>
<c:set var="formatPrice" value="${PriceFormatter}" scope="page"/>

<fmt:setLocale value="vi_VN" />
<fmt:setBundle basename="messages" />
