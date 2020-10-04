<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:if test="${exception ne null}">
    <div class="error">
        <spring:message code="account.upload.rejected" text="${e.getMessage}" htmlEscape="true"/>
    </div>
</c:if>