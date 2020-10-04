<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:if test="${exception ne null}">
    <div class="error">
        <spring:message code="authentication.required" text="${e.getMessage}" htmlEscape="true"/>
    </div>
</c:if>
<c:if test="${not empty param.auth_error}">
    <div id="errors" class="error">
        <p><spring:message code="label.login.failed"/>: ${SPRING_SECURITY_LAST_EXCEPTION.message}</p>
    </div>
</c:if>
<form action="<c:url value="/login"/>" method="post">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <fieldset>
        <legend><spring:message code="login.title" /></legend>
        <table>
        <tr>
            <td><spring:message code="account.username"/></td>
            <td><input type="text" id="username" name="username" placeholder="<spring:message code="account.username"/>"/></td>
        </tr>
        <tr>
            <td><spring:message code="account.password"/></td>
            <td><input type="password" id="password" name="password" placeholder="<spring:message code="account.password"/>"/></td>
        </tr>
        <tr><td colspan="2" align="center"><button id="login"><spring:message code="button.login"/></button></td></tr>
        </table>
    </fieldset>
</form>