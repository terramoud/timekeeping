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
                                <fmt:message key="admin.page.requests.table.title"/>
                            </h5>
                            <div class="table-responsive">
                                <table id="zero_config" class="table table-striped table-bordered">
                                    <thead>
                                    <tr>
                                        <th><fmt:message key="admin.page.requests.table.header.user"/></th>
                                        <th><fmt:message key="admin.page.requests.table.header.activity"/></th>
                                        <th><fmt:message key="admin.page.requests.table.header.type"/></th>
                                        <th><fmt:message key="admin.page.requests.table.header.status"/></th>
                                        <th><fmt:message key="admin.page.requests.table.header.actions"/></th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach var="userActivReq" items="${userActivityRequests}" varStatus="status">
                                        <tr>
                                            <td><c:out value="${userActivReq.login}"/></td>
                                            <td><c:out
                                                    value="${language == 'en' ? userActivReq.activityEn : userActivReq.activityUk}"/></td>
                                            <td><c:out
                                                    value="${language == 'en' ? userActivReq.typeEn : userActivReq.typeUk}"/></td>
                                            <td class="text-warning"><c:out
                                                    value="${language == 'en' ? userActivReq.statusEn : userActivReq.statusUk}"/></td>
                                            <td>
                                                <form action="controller" method="POST" class="p-0 m-0 d-inline">
                                                    <button type="submit" class="border-0 bg-white text-primary"
                                                            data-toggle="tooltip" data-placement="top"
                                                            title="<fmt:message key="admin.page.button.approve.title"/>"
                                                            data-bs-original-title="Update">
                                                        <em class="mdi mdi-check"></em>
                                                    </button>
                                                    <show:request_result/>
                                                </form>
                                                <form action="controller" method="POST" class="p-0 m-0 d-inline">
                                                    <button type="submit" class="border-0 bg-white text-primary"
                                                            data-toggle="tooltip" data-placement="top"
                                                            title="<fmt:message key="admin.page.button.cancel.title"/>"
                                                            data-bs-original-title="Delete">
                                                        <em class="mdi mdi-close"></em>
                                                    </button>
                                                </form>
                                                <show:request_result/>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                    <tfoot>
                                    <tr>
                                        <th><fmt:message key="admin.page.requests.table.header.user"/></th>
                                        <th><fmt:message key="admin.page.requests.table.header.activity"/></th>
                                        <th><fmt:message key="admin.page.requests.table.header.type"/></th>
                                        <th><fmt:message key="admin.page.requests.table.header.status"/></th>
                                        <th><fmt:message key="admin.page.requests.table.header.actions"/></th>
                                    </tr>
                                    </tfoot>
                                </table>
                            </div>
                            <%--Pagination--%>
                            <div class="row float-end">
                                <nav aria-label="Page navigation example">
                                    <ul class="pagination">
                                        <show:pagination_prev_button pageNum="${pageNumTableRequests}" paramName="pageNumTableRequests" paramsOtherPaginations="&pageNumTableArchive=${pageNumTableArchive}"/>
                                        <c:forEach var="i" begin="1" end="${totalPagesForTableRequests}">
                                            <c:if test="${i==pageNumTableRequests}">
                                                <li class="page-item active">
                                                    <a class="page-link"
                                                       href="controller?command=admin_dashboard&pageNumTableArchive=${pageNumTableArchive}&pageNumTableRequests=${i}">${i}</a>
                                                </li>
                                            </c:if>
                                            <c:if test="${i!=pageNumTableRequests}">
                                                <li class="page-item">
                                                    <a class="page-link"
                                                       href="controller?command=admin_dashboard&pageNumTableArchive=${pageNumTableArchive}&pageNumTableRequests=${i}">${i}</a>
                                                </li>
                                            </c:if>
                                        </c:forEach>
                                        <show:pagination_next_button pageNum="${pageNumTableRequests}" paramName="pageNumTableRequests" totalPages="${totalPagesForTableRequests}" paramsOtherPaginations="&pageNumTableArchive=${pageNumTableArchive}"/>
                                    </ul>
                                </nav>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-md-12">
                    <div class="card">
                        <div class="card-body">
                            <h5 class="card-title">
                                <fmt:message key="admin.page.completed_requests.table.title"/>
                            </h5>
                            <div class="table-responsive">
                                <table id="zero_config_archive" class="table table-striped table-bordered">
                                    <thead>
                                    <tr>
                                        <th><fmt:message key="admin.page.requests.table.header.user"/></th>
                                        <th><fmt:message key="admin.page.requests.table.header.activity"/></th>
                                        <th><fmt:message key="admin.page.requests.table.header.type"/></th>
                                        <th><fmt:message key="admin.page.requests.table.header.status"/></th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach var="userActivReqArch" items="${userActivityRequestsArchive}"
                                               varStatus="status">
                                        <tr>
                                            <td><c:out value="${userActivReq.login}"/></td>
                                            <td><c:out
                                                    value="${language == 'en' ? userActivReq.activityEn : userActivReq.activityUk}"/></td>
                                            <td><c:out
                                                    value="${language == 'en' ? userActivReq.typeEn : userActivReq.typeUk}"/></td>
                                            <td class="text-warning"><c:out
                                                    value="${language == 'en' ? userActivReq.statusEn : userActivReq.statusUk}"/></td>
                                        </tr>
                                    </c:forEach>
                                    <tr>
                                        <td>User1</td>
                                        <td>Reading</td>
                                        <td>Add</td>
                                        <td class="text-warning">Pending...</td>
                                    </tr>
                                    <tr>
                                        <td>User1</td>
                                        <td>Reading</td>
                                        <td>Add</td>
                                        <td class="text-warning">Pending...</td>
                                    </tr>
                                    </tbody>
                                    <tfoot>
                                    <tr>
                                        <th><fmt:message key="admin.page.requests.table.header.user"/></th>
                                        <th><fmt:message key="admin.page.requests.table.header.activity"/></th>
                                        <th><fmt:message key="admin.page.requests.table.header.type"/></th>
                                        <th><fmt:message key="admin.page.requests.table.header.status"/></th>
                                    </tr>
                                    </tfoot>
                                </table>
                            </div>
                            <div class="row float-end">
                                <nav aria-label="Page navigation example">
                                    <ul class="pagination">
                                        <show:pagination_prev_button pageNum="${pageNumTableArchive}" paramName="pageNumTableArchive" paramsOtherPaginations="&pageNumTableRequests=${pageNumTableRequests}"/>
                                        <c:forEach var="j" begin="1" end="${totalPagesForTableArchive}">
                                            <c:if test="${j==pageNumTableArchive}">
                                                <li class="page-item active">
                                                    <a class="page-link"
                                                       href="controller?command=admin_dashboard&pageNumTableRequests=${pageNumTableRequests}&pageNumTableArchive=${j}">${j}</a>
                                                </li>
                                            </c:if>
                                            <c:if test="${j!=pageNumTableArchive}">
                                                <li class="page-item">
                                                    <a class="page-link"
                                                       href="controller?command=admin_dashboard&pageNumTableRequests=${pageNumTableRequests}&pageNumTableArchive=${j}">${j}</a>
                                                </li>
                                            </c:if>
                                        </c:forEach>
                                        <show:pagination_next_button pageNum="${pageNumTableArchive}" paramName="pageNumTableArchive" totalPages="${totalPagesForTableArchive}" paramsOtherPaginations="&pageNumTableRequests=${pageNumTableRequests}"/>
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

