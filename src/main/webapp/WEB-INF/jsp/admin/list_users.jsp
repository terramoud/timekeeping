<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/fragments/directive/taglib.jspf" %>
<c:set var="language"
       value="${not empty sessionScope.language ? sessionScope.language :
         not empty cookie['defaultLocale'].getValue() ? cookie['defaultLocale'].getValue() : pageContext.request.locale}"
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
                                    <th scope="col">
                                        <a href="controller?command=admin_list_users&pageNumber=${requestScope.pageNumber}&order_by=login&desc=${requestScope.desc}">
                                            <fmt:message key="admin.page.user.login"/>
                                        </a>
                                    </th>
                                    <th scope="col">
                                        <a href="controller?command=admin_list_users&pageNumber=${requestScope.pageNumber}&order_by=email&desc=${requestScope.desc}">
                                            <fmt:message key="admin.page.user.email"/>
                                        </a>
                                    </th>
                                    <th scope="col">
                                        <a href="controller?command=admin_list_users&pageNumber=${requestScope.pageNumber}&order_by=role_id&desc=${requestScope.desc}">
                                            <fmt:message key="admin.page.user.role"/>
                                        </a>
                                    </th>
                                    <th scope="col"><fmt:message key="admin.page.requests.table.header.actions"/></th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="userRoleBean" items="${userRoleBeans}" varStatus="status">
                                    <tr>
                                        <td class="align-middle"><c:out value="${userRoleBean.user.login}"/></td>
                                        <td class="align-middle"><c:out value="${userRoleBean.user.email}"/></td>
                                        <td class="align-middle"><c:out value="${userRoleBean.role}"/></td>
                                        <td>
                                            <button type="submit" class="btn btn-info  me-1" data-bs-toggle="modal"
                                                    data-bs-target="#staticBackdrop${userRoleBean.user.id}">
                                                <fmt:message key="admin.page.button.edit"/>
                                            </button>
                                            <!-- Modal -->
                                            <div class="modal fade" id="staticBackdrop${userRoleBean.user.id}" data-bs-backdrop="static"
                                                 data-bs-keyboard="false" tabindex="-1"
                                                 aria-labelledby="staticBackdrop${userRoleBean.user.id}Label" aria-hidden="true">
                                                <div class="modal-dialog modal-dialog-centered modal-dialog-scrollable modal-lg">
                                                    <form name="formEditUser${userRoleBean.user.id}" method="POST" action="controller"
                                                          class="modal-content"
                                                          onSubmit="return editUserFormValidation(event, 'formEditUser${userRoleBean.user.id}')"
                                                          onkeydown="return event.key != 'Enter'">
                                                        <input type="hidden" name="command" value="admin_change_user">
                                                        <input type="hidden" name="user_id" value="${userRoleBean.user.id}">
                                                        <div class="modal-header">
                                                            <h5 class="modal-title" id="staticBackdrop${userRoleBean.user.id}Label">
                                                                <fmt:message key="admin.page.user.modal.change_user"/>
                                                            </h5>
                                                            <button type="button" class="btn-close" data-bs-dismiss="modal"
                                                                    aria-label="Close"></button>
                                                        </div>
                                                        <div class="modal-body pb-0">
                                                            <div class="card mb-0">
                                                                <div class="card-body">
                                                                    <div class="form-group row">
                                                                        <label class="col-sm-3 text-end control-label col-form-label">
                                                                            <fmt:message key="admin.page.user.login"/>
                                                                        </label>
                                                                        <div class="col-md-9">
                                                                            <input type="text" class="form-control"
                                                                                   name="login"
                                                                                   value="<c:out value="${userRoleBean.user.login}"/>"
                                                                                   placeholder="<fmt:message key="admin.page.user.login.placeholder"/>"/>
                                                                        </div>
                                                                    </div>
                                                                    <div class="form-group row">
                                                                        <label class="col-sm-3 text-end control-label col-form-label">
                                                                            <fmt:message key="admin.page.user.email"/>
                                                                        </label>
                                                                        <div class="col-sm-9">
                                                                            <input type="text" class="form-control"
                                                                                   name="email"
                                                                                   value="<c:out value="${userRoleBean.user.email}"/>"
                                                                                   placeholder="<fmt:message key="admin.page.user.email.placeholder"/>"/>
                                                                        </div>
                                                                    </div>
                                                                    <div class="form-group row">
                                                                        <label class="col-sm-3 text-end control-label col-form-label">
                                                                            <fmt:message key="admin.page.user.choose.role"/>
                                                                        </label>
                                                                        <div class="col-md-9">
                                                                            <select required="required" name="role_id" class="select2 form-select shadow-none"
                                                                                    style="width: 100%; height: 36px">
                                                                                <c:forEach var="userRoleEntry" items="${userRoles}" varStatus="status">
                                                                                    <c:if test="${userRoleBean.user.roleId == userRoleEntry.key}">
                                                                                        <option selected value="${userRoleEntry.key}">${userRoleEntry.value}</option>
                                                                                    </c:if>
                                                                                    <c:if test="${userRoleBean.user.roleId != userRoleEntry.key}">
                                                                                        <option value="${userRoleEntry.key}">${userRoleEntry.value}</option>
                                                                                    </c:if>
                                                                                </c:forEach>
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
                                                            <button type="submit" class="btn btn-primary">
                                                                <fmt:message key="admin.page.button.save"/>
                                                            </button>
                                                        </div>
                                                    </form>
                                                </div>
                                            </div>
                                                <%--Delete button--%>
                                            <form action="controller" method="POST" class="p-0 m-0 d-inline">
                                                <input type="hidden" name="command" value="admin_remove_user">
                                                <input type="hidden" name="user_id" value="${userRoleBean.user.id}">
                                                <button type="submit" class="btn btn-danger text-white">
                                                    <fmt:message key="admin.page.button.delete"/>
                                                </button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                            <div class="row float-end">
                                <nav aria-label="Page navigation example">
                                    <ul class="pagination">
                                        <show:pagination_prev_button pageNum="${pageNumber}"
                                                                     commandName="admin_list_users"
                                                                     paramsOtherPaginations="&order_by=${requestScope.order_by}&desc=${!requestScope.desc}"
                                                                     paramName="pageNumber"/>

                                        <c:forEach var="i" begin="1" end="${totalPages}">
                                            <c:if test="${i==pageNumber}">
                                                <li class="page-item active">
                                                    <a class="page-link"
                                                       href="controller?command=admin_list_users&order_by=${requestScope.order_by}&desc=${!requestScope.desc}&pageNumber=${i}">${i}</a>
                                                </li>
                                            </c:if>
                                            <c:if test="${i!=pageNumber}">
                                                <li class="page-item">
                                                    <a class="page-link"
                                                       href="controller?command=admin_list_users&order_by=${requestScope.order_by}&desc=${!requestScope.desc}&pageNumber=${i}">${i}</a>
                                                </li>
                                            </c:if>
                                        </c:forEach>

                                        <show:pagination_next_button pageNum="${pageNumber}"
                                                                     paramName="pageNumber"
                                                                     commandName="admin_list_users"
                                                                     paramsOtherPaginations="&order_by=${requestScope.order_by}&desc=${!requestScope.desc}"
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
<show:submit_result/>
<!-- ============================================================== -->
<!-- All Jquery -->
<!-- ============================================================== -->
<%@ include file="/WEB-INF/fragments/scripts.jspf" %>
<script>
    function editUserFormValidation(e, formName) {
        e.preventDefault();
        let login = document.querySelector('form[name="' + formName + '"]').login.value;
        let email = document.querySelector('form[name="' + formName + '"]').email.value;
        let regEmail = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/g;  // Javascript reGex for Email Validation.
        let regLogin = /^[\p{L}][\p{L}0-9]{4,29}$/gu;          // Javascript reGex for Login validation

        if (login === "" || !regLogin.test(login)) {
            window.alert(`<fmt:message key = "index_jsp.form.register.validate_login"/>`);
            return false;
        }

        if (email === "" || !regEmail.test(email)) {
            window.alert(`<fmt:message key = "index_jsp.form.register.validate_email"/>`);
            return false;
        }

        document.querySelector('form[name="' + formName + '"]').submit();
        return true;
    }
</script>
</body>
</html>



