<%@ include file="/WEB-INF/fragments/directive/taglib.jspf" %>
<%@ attribute name="pageNum" type="java.lang.Integer" required="true" %>
<%@ attribute name="paramName" type="java.lang.String" required="true" %>
<%@ attribute name="commandName" type="java.lang.String" required="true" %>
<%@ attribute name="totalPages" type="java.lang.Integer" required="true" %>
<%@ attribute name="paramsOtherPaginations" type="java.lang.String" required="false" %>

<c:if test="${(pageNum + 1) <= totalPages}">
    <li class="page-item">
        <a class="page-link"
           href="controller?command=${commandName}${paramsOtherPaginations}&${paramName}=${pageNum + 1}">
            <fmt:message key="admin.page.requests.table.pagination.next"/>
        </a>
    </li>
</c:if>
<c:if test="${(pageNum + 1) > totalPages}">
    <li class="page-item disabled">
        <a class="page-link" href="#">
            <fmt:message key="admin.page.requests.table.pagination.next"/>
        </a>
    </li>
</c:if>