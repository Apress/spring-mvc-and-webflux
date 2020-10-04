<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<form:form method="GET" modelAttribute="bookSearchCriteria" id="bookSearchForm">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" id="csrf"/>
    <fieldset>
        <legend><spring:message code="book.searchcriteria"/></legend>
        <table>
            <tr>
                <td><form:label path="title"><spring:message code="book.title" /></form:label></td>
                <td><form:input path="title" /></td>
            </tr>
            <tr>
                <td><form:label path="category"><spring:message code="book.category" /></form:label></td>
                <td><form:select path="category" items="${categories}" itemValue="id" itemLabel="name"/></td></tr>
        </table>
    </fieldset>
    <button id="search"><spring:message code="button.search"/></button>
</form:form>

<script>

$(document).ready(function() {
    $('#bookSearchResults').hide();
});

function renderBooks(books) {
    console.log("doing this with books: " + books.length);
    var content = '';
    var baseDetailUrl = '<c:url value="/book/detail/"/>';
    var baseAddCartUrl = '<c:url value="/cart/add/" />';
    for (var i = 0; i<books.length; i++) {
        content += '<tr>';
        content += '<td><a href="'+ baseDetailUrl + books[i].id+'">'+books[i].title+'</a></td>';
        content += '<td>'+books[i].description+'</td>';
        content += '<td>'+books[i].price+'</td>';
        content += '<td><a href="'+ baseAddCartUrl +books[i].id+'"><spring:message code="book.addtocart"/></a></td>';
        var baseDeleteId = '<c:url value="/book/delete/"/>' + books[i].id;
        content += "<sec:authorize access="hasRole('ADMIN')"><td>" + '<a href="' + baseDeleteId + '"><spring:message code="label.delete"/></a></td></sec:authorize>';
        content += '</tr>';
    }
    $('#bookSearchResults tbody').html(content);
    $('#bookSearchResults').show();
}

$('#bookSearchForm').submit(function(evt){
	evt.preventDefault();
	var title = $('#title').val();
	var category = $('#category').val();
	var json = { "title" : title, "category" : { "id" : category}};
    var token =  $("#csrf").attr('value');

	$.ajax({
		url: $('#bookSearchForm').action,
        headers: {'X-CSRF-TOKEN': token},
		type: 'POST',
		dataType: 'json',
		contentType: 'application/json',
		data: JSON.stringify(json),
		success: function(responseData) { console.log(responseData); renderBooks(responseData);}
	});
});
</script>
<table id="bookSearchResults">
    <thead>
    <tr>
        <th><spring:message code="book.title"/></th>
        <th><spring:message code="book.description"/></th>
        <th><spring:message code="book.price" /></th>
        <th></th>
        <sec:authorize access="hasRole('ADMIN')"><th>Admin Op.</th> </sec:authorize>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${bookList}" var="book">
        <tr>
            <td><a href="<c:url value="/book/detail/${book.id}"/>">${book.title}</a></td>
            <td>${book.description}</td>
            <td>${book.price}</td>
            <td><a href="<c:url value="/cart/add/${book.id}"/>"><spring:message code="book.addtocart"/></a></td>
            <sec:authorize access="hasRole('ADMIN')"><td><a href="<c:url value="/book/delete/${book.id}"/>"><spring:message code="label.delete"/></a></td></sec:authorize>
        </tr>
    </c:forEach>
    </tbody>
</table>
