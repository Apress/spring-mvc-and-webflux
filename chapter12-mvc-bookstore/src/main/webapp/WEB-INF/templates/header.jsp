<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<div class="header">
    <c:url value="/index.htm" var="homeUrl"/>
    <div class="logo"><a href="${homeUrl}"><img src="<c:url value="/resources/images/logo.gif"/>" alt="" title="" border="0" /></a></div>            
    <div class="nav">
        <ul style="float: left;">                                                                       
            <li class="selected"><a href="${homeUrl}"><spring:message code="nav.home"/></a></li>
            <li><a href="<c:url value="/book/search"/>"><spring:message code="nav.books"/></a></li>
            <li><a href="<c:url value="/customer/account"/>"><spring:message code="nav.account"/></a></li>
            <li><a href="<c:url value="/cart/checkout"/>"><spring:message code="nav.checkout"/></a></li>
            <sec:authorize access="!isAuthenticated()">
                <li><a href="<c:url value="/customer/register"/>"><spring:message code="nav.register"/></a></li>
                <li><a href="<c:url value="/login"/>"><spring:message code="nav.login"/></a></li>
            </sec:authorize>
            <sec:authorize access="hasRole('ADMIN')">
                <li><a href="<c:url value="/customer/list"/>"><spring:message code="nav.admin"/></a></li>
            </sec:authorize>
            <sec:authorize access="isAuthenticated()">
                <li><a href="#" onclick="document.getElementById('logout').submit();"><spring:message code="nav.logout"/></a></li>
                <spring:url value="/logout" var="logoutUrl"/>
                <form action="${logoutUrl}" id="logout" method="post">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </form>
            </sec:authorize>
        </ul>
        <ul style="float: right;">
            <sec:authorize access="isAuthenticated()"><li>(<em><sec:authentication property="principal" /></em>)</li></sec:authorize>
            <li><a href="?lang=en" class="selected"><img src="<c:url value="/resources/images/gb.gif"/>" alt="" title="" border="0" /></a></li>
            <li><a href="?lang=nl"><img src="<c:url value="/resources/images/nl.gif"/>" alt="" title="" border="0" /></a></li>
        </ul>
    </div>
</div>