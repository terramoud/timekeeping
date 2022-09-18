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
                                        <th>
                                            <a href="controller?command=admin_dashboard&pageNumTableArchive=${pageNumTableArchive}&pageNumTableRequests=${pageNumTableRequests}&archiveOrder_by=${requestScope.archiveOrder_by}&order_by=login&desc=${requestScope.desc}&archiveDesc=${!requestScope.archiveDesc}">
                                                <fmt:message key="admin.page.requests.table.header.user"/>
                                            </a>
                                        </th>
                                        <th>
                                            <a href="controller?command=admin_dashboard&pageNumTableArchive=${pageNumTableArchive}&pageNumTableRequests=${pageNumTableRequests}&archiveOrder_by=${requestScope.archiveOrder_by}&order_by=${language == 'en' ? 'a.name_en' : 'a.name_uk'}&desc=${requestScope.desc}&archiveDesc=${!requestScope.archiveDesc}">
                                                <fmt:message key="admin.page.requests.table.header.activity"/>
                                            </a>
                                        </th>
                                        <th>
                                            <a href="controller?command=admin_dashboard&pageNumTableArchive=${pageNumTableArchive}&pageNumTableRequests=${pageNumTableRequests}&archiveOrder_by=${requestScope.archiveOrder_by}&order_by=${language == 'en' ? 't.name_en' : 't.name_uk'}&desc=${requestScope.desc}&archiveDesc=${!requestScope.archiveDesc}">
                                                <fmt:message key="admin.page.requests.table.header.type"/>
                                            </a>
                                        </th>
                                        <th><fmt:message key="admin.page.requests.table.header.status"/></th>
                                        <th><fmt:message key="admin.page.requests.table.header.actions"/></th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach var="pendingRequest" items="${requestsByStatusPending}" varStatus="status">
                                        <tr>
                                            <td><c:out value="${pendingRequest.user.login}"/></td>
                                            <td><c:out
                                                    value="${language == 'en' ? pendingRequest.activity.nameEn : pendingRequest.activity.nameUk}"/></td>
                                            <td><c:out
                                                    value="${language == 'en' ? pendingRequest.type.nameEn : pendingRequest.type.nameUk}"/></td>
                                            <td class="text-warning"><c:out
                                                    value="${language == 'en' ? pendingRequest.status.nameEn : pendingRequest.status.nameUk}"/></td>
                                            <td>
                                                <form action="controller" method="POST" class="p-0 m-0 d-inline">
                                                    <input type="hidden" name="command" value="admin_approve_request">
                                                    <input type="hidden" name="request_id" value="${pendingRequest.requestId}">
                                                    <input type="hidden" name="user_id" value="${pendingRequest.user.id}">
                                                    <input type="hidden" name="activity_id" value="${pendingRequest.activity.id}">
                                                    <input type="hidden" name="type_id" value="${pendingRequest.type.id}">
                                                    <button type="submit" class="border-0 bg-white text-primary"
                                                            data-toggle="tooltip" data-placement="top"
                                                            title="<fmt:message key="admin.page.button.approve.title"/>"
                                                            data-bs-original-title="Update">
                                                        <em class="mdi mdi-check"></em>
                                                    </button>
                                                    <show:submit_result/>
                                                </form>
                                                <form action="controller" method="POST" class="p-0 m-0 d-inline">
                                                    <input type="hidden" name="command" value="admin_deny_request">
                                                    <input type="hidden" name="request_id" value="${pendingRequest.requestId}">
                                                    <button type="submit" class="border-0 bg-white text-primary"
                                                            data-toggle="tooltip" data-placement="top"
                                                            title="<fmt:message key="admin.page.button.cancel.title"/>"
                                                            data-bs-original-title="Delete">
                                                        <em class="mdi mdi-close"></em>
                                                    </button>
                                                </form>
                                                <show:submit_result/>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                            <%--Pagination--%>
                            <div class="row float-end">
                                <nav aria-label="Page navigation example">
                                    <ul class="pagination">
                                        <show:pagination_prev_button pageNum="${pageNumTableRequests}"
                                                                     commandName="admin_dashboard"
                                                                     paramName="pageNumTableRequests"
                                                                     paramsOtherPaginations="&pageNumTableArchive=${pageNumTableArchive}&archiveOrder_by=${requestScope.archiveOrder_by}&order_by=${requestScope.order_by}&desc=${!requestScope.desc}&archiveDesc=${!requestScope.archiveDesc}"/>

                                        <c:forEach var="i" begin="1" end="${totalPagesForTableRequests}">
                                            <c:if test="${i==pageNumTableRequests}">
                                                <li class="page-item active">
                                                    <a class="page-link"
                                                       href="controller?command=admin_dashboard&pageNumTableArchive=${pageNumTableArchive}&pageNumTableRequests=${i}&archiveOrder_by=${requestScope.archiveOrder_by}&order_by=${requestScope.order_by}&desc=${!requestScope.desc}&archiveDesc=${!requestScope.archiveDesc}">${i}</a>
                                                </li>
                                            </c:if>
                                            <c:if test="${i!=pageNumTableRequests}">
                                                <li class="page-item">
                                                    <a class="page-link"
                                                       href="controller?command=admin_dashboard&pageNumTableArchive=${pageNumTableArchive}&pageNumTableRequests=${i}&archiveOrder_by=${requestScope.archiveOrder_by}&order_by=${requestScope.order_by}&desc=${!requestScope.desc}&archiveDesc=${!requestScope.archiveDesc}">${i}</a>
                                                </li>
                                            </c:if>
                                        </c:forEach>

                                        <show:pagination_next_button pageNum="${pageNumTableRequests}"
                                                                     commandName="admin_dashboard"
                                                                     paramName="pageNumTableRequests"
                                                                     totalPages="${totalPagesForTableRequests}"
                                                                     paramsOtherPaginations="&pageNumTableArchive=${pageNumTableArchive}&archiveOrder_by=${requestScope.archiveOrder_by}&order_by=${requestScope.order_by}&desc=${!requestScope.desc}&archiveDesc=${!requestScope.archiveDesc}"/>
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
                                        <th>
                                            <a href="controller?command=admin_dashboard&pageNumTableArchive=${pageNumTableArchive}&pageNumTableRequests=${pageNumTableRequests}&order_by=${requestScope.order_by}&archiveOrder_by=login&desc=${!requestScope.desc}&archiveDesc=${requestScope.archiveDesc}">
                                                <fmt:message key="admin.page.requests.table.header.user"/>
                                            </a>
                                        </th>
                                        <th>
                                            <a href="controller?command=admin_dashboard&pageNumTableArchive=${pageNumTableArchive}&pageNumTableRequests=${pageNumTableRequests}&order_by=${requestScope.order_by}&archiveOrder_by=${language == 'en' ? 'a.name_en' : 'a.name_uk'}&desc=${!requestScope.desc}&archiveDesc=${requestScope.archiveDesc}">
                                                <fmt:message key="admin.page.requests.table.header.activity"/>
                                            </a>
                                        </th>
                                        <th>
                                            <a href="controller?command=admin_dashboard&pageNumTableArchive=${pageNumTableArchive}&pageNumTableRequests=${pageNumTableRequests}&order_by=${requestScope.order_by}&archiveOrder_by=${language == 'en' ? 't.name_en' : 't.name_uk'}&desc=${!requestScope.desc}&archiveDesc=${requestScope.archiveDesc}">
                                                <fmt:message key="admin.page.requests.table.header.type"/>
                                            </a>
                                        </th>
                                        <th><fmt:message key="admin.page.requests.table.header.status"/></th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="completedRequest" items="${archiveOfRequests}" varStatus="status">
                                            <tr>
                                                <td><c:out value="${completedRequest.user.login}"/></td>
                                                <td><c:out
                                                        value="${language == 'en' ? completedRequest.activity.nameEn : completedRequest.activity.nameUk}"/></td>
                                                <td><c:out
                                                        value="${language == 'en' ? completedRequest.type.nameEn : completedRequest.type.nameUk}"/></td>
                                                <td class="text-warning"><c:out
                                                        value="${language == 'en' ? completedRequest.status.nameEn : completedRequest.status.nameUk}"/></td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                            <div class="row float-end">
                                <nav aria-label="Page navigation example">
                                    <ul class="pagination">
                                        <show:pagination_prev_button pageNum="${pageNumTableArchive}"
                                                                     commandName="admin_dashboard"
                                                                     paramName="pageNumTableArchive"
                                                                     paramsOtherPaginations="&pageNumTableRequests=${pageNumTableRequests}&order_by=${requestScope.order_by}&archiveOrder_by=${requestScope.archiveOrder_by}&desc=${!requestScope.desc}&archiveDesc=${!requestScope.archiveDesc}"/>

                                        <c:forEach var="j" begin="1" end="${totalPagesForTableArchive}">
                                            <c:if test="${j==pageNumTableArchive}">
                                                <li class="page-item active">
                                                    <a class="page-link"
                                                       href="controller?command=admin_dashboard&pageNumTableRequests=${pageNumTableRequests}&pageNumTableArchive=${j}&order_by=${requestScope.order_by}&archiveOrder_by=${requestScope.archiveOrder_by}&desc=${!requestScope.desc}&archiveDesc=${!requestScope.archiveDesc}">${j}</a>
                                                </li>
                                            </c:if>
                                            <c:if test="${j!=pageNumTableArchive}">
                                                <li class="page-item">
                                                    <a class="page-link"
                                                       href="controller?command=admin_dashboard&pageNumTableRequests=${pageNumTableRequests}&pageNumTableArchive=${j}&order_by=${requestScope.order_by}&archiveOrder_by=${requestScope.archiveOrder_by}&desc=${!requestScope.desc}&archiveDesc=${!requestScope.archiveDesc}">${j}</a>
                                                </li>
                                            </c:if>
                                        </c:forEach>

                                        <show:pagination_next_button pageNum="${pageNumTableArchive}"
                                                                     commandName="admin_dashboard"
                                                                     paramName="pageNumTableArchive"
                                                                     totalPages="${totalPagesForTableArchive}"
                                                                     paramsOtherPaginations="&pageNumTableRequests=${pageNumTableRequests}&order_by=${requestScope.order_by}&archiveOrder_by=${requestScope.archiveOrder_by}&desc=${!requestScope.desc}&archiveDesc=${!requestScope.archiveDesc}"/>
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

