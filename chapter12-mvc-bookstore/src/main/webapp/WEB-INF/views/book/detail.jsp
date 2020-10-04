<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:url value="/resources/images/books/${book.isbn}/book_front_cover.png" var="bookImage"/>
<img src="${bookImage}" align="left" alt="${book.title}" width="250"/>


<table>
    <tr><td><spring:message code="book.title"/></td><td>${book.title}</td></tr>
    <tr><td><spring:message code="book.description"/></td><td>${book.description}</td></tr>
    <tr><td>Author</td><td>${book.author}</td></tr>    
    <tr><td>Year</td><td>${book.year}</td></tr>    
    <tr><td>ISBN</td><td>${book.isbn}</td></tr>    
    <tr><td><spring:message code="book.price" /></td><td>${book.price}</td></tr>
    <tr><td></td>
        <td>
            <sec:authorize url="/*/edit/*">
                <a href="<c:url value="/book/edit/${book.id}"/>">
                    <spring:message code="label.edit"/>
                </a>
            </sec:authorize>
        </td>
    </tr>
</table>