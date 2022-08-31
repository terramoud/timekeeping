<%@ include file="/WEB-INF/fragments/directive/taglib.jspf" %>
<%@ attribute name="time" type="ua.epam.akoreshev.finalproject.model.entity.Time" required="true" %>
<c:set var="language"
       value="${not empty sessionScope.language ? sessionScope.language :
         not empty cookie['defaultLocale'].getValue() ? cookie['defaultLocale'].getValue() :pageContext.request.locale}"
       scope="session"/>
<fmt:setLocale value="${language}"/>

<c:choose>
    <c:when test="${time.seconds == 0 && time.minutes == 0 && time.hours == 0}">
        <fmt:message key="admin.timekeeping_report.no_spent_time"/>
    </c:when>
    <c:otherwise>
        <c:out value="${time.hours} "/><fmt:message key="admin.timekeeping_report.spent_hours"/>
        <c:out value=" ${time.minutes} "/><fmt:message key="admin.timekeeping_report.spent_minutes"/>
        <c:out value=" ${time.seconds} "/><fmt:message key="admin.timekeeping_report.spent_seconds"/>
    </c:otherwise>
</c:choose>
