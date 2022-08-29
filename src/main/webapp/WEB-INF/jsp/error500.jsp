<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/fragments/directive/taglib.jspf" %>
<c:set var="language"
       value="${not empty sessionScope.language ? sessionScope.language :
         not empty cookie['defaultLocale'].getValue() ? cookie['defaultLocale'].getValue() :pageContext.request.locale}"
       scope="session"/>
<fmt:setLocale value="${language}"/>

<!DOCTYPE html>
<html lang="${language}">
<c:set var="title" value="500" scope="page"/>
<%@ include file="/WEB-INF/fragments/head.jspf" %>

<body>
<div class="main-wrapper">
    <div class="preloader">
        <div class="lds-ripple">
            <div class="lds-pos"></div>
            <div class="lds-pos"></div>
        </div>
    </div>

    <div class="error-box">
        <div class="error-body text-center">
            <h1 class="error-title text-danger">500</h1>
            <h3 class="text-uppercase error-subtitle">
                <fmt:message key="error_jsp.header"/>
            </h3>
            <p class="text-muted mt-4 mb-4">
                <c:set var="message" value="${requestScope['javax.servlet.error.message']}"/>
                <c:set var="exception" value="${requestScope['javax.servlet.error.exception']}"/>
                <c:if test="${not empty message}">
            <h4><fmt:message key="error_jsp.error.message"/> ${message}</h4>
            </c:if>
            <c:if test="${not empty exception}">
                <hr/>
                <h4>Stack trace:</h4>
                <c:forEach var="stackTraceElement" items="${exception.stackTrace}">
                    ${stackTraceElement}
                </c:forEach>
            </c:if>
            </p>
            <a href="controller?command=index_page" class="btn btn-danger btn-roundedwaves-effect waves-lightmb-5text-white">
                <fmt:message key="error_jsp.button.back"/>
            </a>
        </div>
    </div>
</div>
<!-- ============================================================== -->
<!-- All Required js -->
<!-- ============================================================== -->
<script src="js/jquery.min.js"></script>
<!-- Bootstrap tether Core JavaScript -->
<script src="js/bootstrap.bundle.min.js"></script>
<!-- ============================================================== -->
<!-- This page plugin js -->
<!-- ============================================================== -->
<script>
    $(".preloader").fadeOut();
</script>
</body>
</html>
