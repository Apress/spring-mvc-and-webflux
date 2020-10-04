<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:if test="${not empty accounts}">
    <table id="accountList">
        <thead>
        <tr>
            <th><spring:message code="account.firstname"/></th>
            <th><spring:message code="account.lastname"/></th>
            <th><spring:message code="account.username" /></th>
            <th><spring:message code="account.email" /></th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${accounts}" var="account">
            <tr>
                <td>${account.firstName}</td>
                <td>${account.lastName}</td>
                <td>${account.username}</td>
                <td>${account.emailAddress}</td>
                <td><a href="<c:url value="/customer/delete/${account.username}"/>"><spring:message code="label.delete"/></a></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</c:if>