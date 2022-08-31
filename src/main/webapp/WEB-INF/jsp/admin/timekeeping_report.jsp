<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/fragments/directive/taglib.jspf" %>
<c:set var="language"
       value="${not empty sessionScope.language ? sessionScope.language :
         not empty cookie['defaultLocale'].getValue() ? cookie['defaultLocale'].getValue() :pageContext.request.locale}"
       scope="session"/>
<fmt:setLocale value="${language}"/>

<!DOCTYPE html>
<html lang="${language}">
<c:set var="title" value="Admin page" scope="page"/>
<%@ include file="/WEB-INF/fragments/head.jspf" %>

<body>
<div class="preloader">
    <div class="lds-ripple">
        <div class="lds-pos"></div>
        <div class="lds-pos"></div>
    </div>
</div>

<div id="main-wrapper" data-layout="vertical" data-navbarbg="skin5" data-sidebartype="full"
     data-sidebar-position="absolute" data-header-position="absolute" data-boxed-layout="full">
    <%@ include file="/WEB-INF/fragments/admin_header.jspf" %>

    <%@ include file="/WEB-INF/fragments/admin_sidebar.jspf" %>

    <!-- ============================================================== -->
    <!-- Page wrapper  -->
    <!-- ============================================================== -->
    <div class="page-wrapper">
        <div class="page-breadcrumb">
            <div class="row">
                <div class="col-12 d-flex no-block align-items-center">
                    <h4 class="page-title">
                        <fmt:message key="admin.page.header"/>
                    </h4>
                    <div class="ms-auto text-end">
                        <nav aria-label="breadcrumb">
                            <ol class="breadcrumb">
                                <li class="breadcrumb-item">
                                    <a href="controller?command=index_page">
                                        <fmt:message key="project.home.page"/>
                                    </a>
                                </li>
                                <li class="breadcrumb-item active" aria-current="page">
                                    <fmt:message key="breadcrumb.admin.page"/>
                                </li>
                            </ol>
                        </nav>
                    </div>
                </div>
            </div>
        </div>
        <div class="container-fluid">
            <!-- ============================================================== -->
            <!-- Start Page Content -->
            <!-- ============================================================== -->
            <div class="row">
                <div class="col-md-12">
                    <div class="card">
                        <div class="card-body">
                            <h5 class="card-title">
                                <fmt:message key="admin.page.requests.table.report.header"/>
                            </h5>
                            <div class="table-responsive">
                                <table id="zero_config" class="table table-striped table-bordered">
                                    <thead>
                                    <tr>
                                        <th><fmt:message key="admin.page.requests.table.header.user"/></th>
                                        <th><fmt:message key="admin.page.requests.table.header.activity"/></th>
                                        <th><fmt:message key="admin.page.requests.table.header.summary_time"/></th>
                                        <th><fmt:message key="admin.page.requests.table.header.attempts"/></th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="spentTime" items="${spentTimeForActivities}" varStatus="status">
                                            <tr>
                                                <td><c:out value="${spentTime.user.login}"/></td>
                                                <td><c:out value="${language == 'en' ? spentTime.activity.nameEn : spentTime.activity.nameUk}"/></td>
                                                <td><show:spent_time_formater time="${spentTime.total}"/></td>
                                                <td class="text-warning"><c:out value="${spentTime.attempts}"/></td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                            <div class="row float-end">
                                <nav aria-label="Page navigation example">
                                    <ul class="pagination">
                                        <show:pagination_prev_button pageNum="${pageNumber}"
                                                                     commandName="admin_timekeeping_report"
                                                                     paramName="pageNumber"/>

                                        <c:forEach var="i" begin="1" end="${totalPages}">
                                            <c:if test="${i==pageNumber}">
                                                <li class="page-item active">
                                                    <a class="page-link"
                                                       href="controller?command=admin_timekeeping_report&pageNumber=${i}">${i}</a>
                                                </li>
                                            </c:if>
                                            <c:if test="${i!=pageNumber}">
                                                <li class="page-item">
                                                    <a class="page-link"
                                                       href="controller?command=admin_timekeeping_report&pageNumber=${i}">${i}</a>
                                                </li>
                                            </c:if>
                                        </c:forEach>

                                        <show:pagination_next_button pageNum="${pageNumber}"
                                                                     paramName="pageNumber"
                                                                     commandName="admin_timekeeping_report"
                                                                     totalPages="${totalPages}"/>
                                    </ul>
                                </nav>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <%@ include file="/WEB-INF/fragments/footer.jspf" %>
    </div>
</div>
<!-- ============================================================== -->
<!-- All Jquery -->
<!-- ============================================================== -->
<%@ include file="/WEB-INF/fragments/scripts.jspf" %>
</body>
</html>

