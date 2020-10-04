
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<p>Welcome to the Book Store(in debug mode)</p>


<c:if test="${not empty beans}">
    <ul>
        <c:forEach items="${beans}" var="beanName">
            <li>${beanName}</li>
        </c:forEach>
    </ul>
</c:if>
