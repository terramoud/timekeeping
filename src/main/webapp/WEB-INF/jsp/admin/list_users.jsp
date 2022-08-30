<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/fragments/directive/taglib.jspf" %>
<c:set var="language"
       value="${not empty sessionScope.language ? sessionScope.language :
         not empty cookie['defaultLocale'].getValue() ? cookie['defaultLocale'].getValue() :pageContext.request.locale}"
       scope="session"/>
<fmt:setLocale value="${language}"/>

<!DOCTYPE html>
<html lang="${language}">
<c:set var="title" value="List users" scope="page"/>
<%@ include file="/WEB-INF/fragments/head.jspf" %>

<body>
<div class="preloader">
    <div class="lds-ripple">
        <div class="lds-pos"></div>
        <div class="lds-pos"></div>
    </div>
</div>

<div
        id="main-wrapper"
        data-layout="vertical"
        data-navbarbg="skin5"
        data-sidebartype="full"
        data-sidebar-position="absolute"
        data-header-position="absolute"
        data-boxed-layout="full"
>
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
                            <h5 class="card-title mb-0">
                                <fmt:message key="admin.page.table.users.name"/>
                            </h5>
                            <table class="table">
                                <thead>
                                <tr>
                                    <th scope="col"><fmt:message key="admin.page.user.login"/></th>
                                    <th scope="col"><fmt:message key="admin.page.user.email"/></th>
                                    <th scope="col"><fmt:message key="admin.page.user.role"/></th>
                                    <th scope="col"><fmt:message key="admin.page.user.activities"/></th>
                                    <th scope="col"><fmt:message key="admin.page.requests.table.header.actions"/></th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td class="align-middle">User1</td>
                                    <td class="align-middle">ssd@emai.com</td>
                                    <td class="align-middle">user</td>
                                    <td class="align-middle">10</td>
                                    <td>
                                        <button type="submit" class="btn btn-info  me-1" data-bs-toggle="modal"
                                                data-bs-target="#staticBackdrop">
                                            <fmt:message key="admin.page.button.edit"/>
                                        </button>
                                        <!-- Modal -->
                                        <div class="modal fade" id="staticBackdrop" data-bs-backdrop="static"
                                             data-bs-keyboard="false" tabindex="-1"
                                             aria-labelledby="staticBackdropLabel" aria-hidden="true">
                                            <div class="modal-dialog modal-dialog-centered modal-dialog-scrollable modal-lg">
                                                <form class="modal-content">
                                                    <div class="modal-header">
                                                        <h5 class="modal-title" id="staticBackdropLabel">
                                                            <fmt:message key="admin.page.user.modal.change_user"/>
                                                        </h5>
                                                        <button type="button" class="btn-close" data-bs-dismiss="modal"
                                                                aria-label="Close"></button>
                                                    </div>
                                                    <div class="modal-body pb-0">
                                                        <div class="card mb-0">
                                                            <div class="card-body">
                                                                <%--                                                                <h5 class="card-title">Edit activity</h5>--%>
                                                                <div class="form-group row">
                                                                    <label class="col-sm-3 text-end control-label col-form-label">
                                                                        <fmt:message key="admin.page.user.login"/>
                                                                    </label>
                                                                    <div class="col-md-9">
                                                                        <input type="text" class="form-control"
                                                                               id="fname"
                                                                               placeholder="<fmt:message key="admin.page.user.login.placeholder"/>"/>
                                                                    </div>
                                                                </div>
                                                                <div class="form-group row">
                                                                    <label class="col-sm-3 text-end control-label col-form-label">
                                                                        <fmt:message key="admin.page.user.email"/>
                                                                    </label>
                                                                    <div class="col-sm-9">
                                                                        <input type="text" class="form-control"
                                                                               id="fnameEN"
                                                                               placeholder="<fmt:message key="admin.page.user.email.placeholder"/>"/>
                                                                    </div>
                                                                </div>
                                                                <div class="form-group row">
                                                                    <label class="col-sm-3 text-end control-label col-form-label">
                                                                        <fmt:message key="admin.page.user.choose.role"/>
                                                                    </label>
                                                                    <div class="col-md-9">
                                                                        <select class="select2 form-select shadow-none"
                                                                                style="width: 100%; height: 36px">
                                                                            <option selected disabled>Select</option>
                                                                            <option value="AK">Admin</option>
                                                                            <option value="HI">User</option>
                                                                        </select>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="modal-footer">
                                                        <button type="button" class="btn btn-secondary"
                                                                data-bs-dismiss="modal">
                                                            <fmt:message key="admin.page.button.close"/>
                                                        </button>
                                                        <button type="button" class="btn btn-primary">
                                                            <fmt:message key="admin.page.button.save"/>
                                                        </button>
                                                    </div>
                                                </form>
                                            </div>
                                        </div>
                                        <%--Delete button--%>
                                        <button type="submit" class="btn btn-danger text-white">
                                            <fmt:message key="admin.page.button.delete"/>
                                        </button>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                            <div class="row float-end">
                                <nav aria-label="Page navigation example">
                                    <ul class="pagination">
                                        <li class="page-item">
                                            <a class="page-link" href="#">
                                                <fmt:message key="admin.page.requests.table.pagination.previous"/>
                                            </a>
                                        </li>
                                        <li class="page-item">
                                            <a class="page-link" href="#">1</a>
                                        </li>
                                        <li class="page-item">
                                            <a class="page-link" href="#">2</a>
                                        </li>
                                        <li class="page-item">
                                            <a class="page-link" href="#">3</a>
                                        </li>
                                        <li class="page-item">
                                            <a class="page-link" href="#">
                                                <fmt:message key="admin.page.requests.table.pagination.next"/>
                                            </a>
                                        </li>
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

