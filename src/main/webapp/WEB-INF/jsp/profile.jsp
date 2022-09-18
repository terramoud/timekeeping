<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/fragments/directive/taglib.jspf" %>
<c:set var="language"
       value="${not empty sessionScope.language ? sessionScope.language :
                not empty cookie['defaultLocale'].getValue() ? cookie['defaultLocale'].getValue() :
                pageContext.request.locale}"
       scope="session"/>
<fmt:setLocale value="${language}"/>

<!DOCTYPE html>
<html lang="${language}">
<c:set var="title" value="User profile page" scope="page"/>
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
    <!-- ============================================================== -->
    <!-- Topbar header -->
    <!-- ============================================================== -->
    <header class="topbar" data-navbarbg="skin5">
        <nav class="navbar top-navbar navbar-expand-md navbar-dark">
            <div class="navbar-header" data-logobg="skin5">
                <a class="navbar-brand" href="controller?command=user_page">
                    <span class="logo-text ms-2">
                        <fmt:message key="project.name"/>
                    </span>
                </a>
                <div class="d-flex d-md-none align-items-center">
                    <%@ include file="/WEB-INF/fragments/profile_dropdown.jspf" %>
                    <%@ include file="/WEB-INF/fragments/language_switcher.jspf" %>
                </div>
            </div>
            <div class="navbar-collapse collapse d-flex justify-content-end d-none d-md-block" data-navbarbg="skin5">
                <ul class="navbar-nav float-end">
                    <li class="nav-item dropdown">
                        <div class="nav-link dropdown-toggle d-flex align-items-center">
                            <c:set var="currentPageCommandName" value="profile" scope="page"/>
                            <%@ include file="/WEB-INF/fragments/language_switcher.jspf" %>
                        </div>
                    </li>
                    <!-- ============================================================== -->
                    <!-- User profile -->
                    <!-- ============================================================== -->
                    <li class="nav-item dropdown">
                        <%@ include file="/WEB-INF/fragments/profile_dropdown.jspf" %>
                    </li>
                </ul>
            </div>
        </nav>
    </header>
    <!-- ============================================================== -->
    <!-- End Topbar header -->
    <!-- ============================================================== -->

    <div class="page-wrapper mx-auto bg-dark">
        <div class="page-breadcrumb">
            <div class="row col-12 col-md-6 d-flex no-block align-items-center text-light">
                <h4 class="page-title">
                    <fmt:message key="profile.page.title"/>
                </h4>
                <div class="ms-auto text-end">
                    <nav aria-label="breadcrumb">
                        <ol class="breadcrumb">
                            <li class="breadcrumb-item">
                                <a class="text-orange" href="controller?command=index_page">
                                    <fmt:message key="project.home.page"/>
                                </a>
                            </li>
                            <li class="breadcrumb-item text-light active" aria-current="page">
                                <fmt:message key="breadcrumb.user.page"/>
                            </li>
                        </ol>
                    </nav>
                </div>
            </div>
        </div>
        <div class="container-fluid">
            <div class="row">
                <div class="col-md-3"></div>
                <div class="col-md-6">
                    <div class="card">
                        <div class="card-body">
                            <h4 class="card-title mb-0">
                                <fmt:message key="profile"/>
                            </h4>
                        </div>
                        <div class="comment-widgets scrollable border-bottom border-1">
                            <div class="d-flex flex-row comment-row">
                                <div class="comment-text w-100">
                                    <div class="d-flex align-items-baseline mb-2">
                                        <h5 class="pe-2 mb-0">
                                            <fmt:message key="login"/>
                                        </h5>
                                        <span class="font-16">${sessionScope.user.login}</span>
                                    </div>
                                    <div class="d-flex align-items-baseline mb-2">
                                        <h5 class="pe-2 mb-0">
                                            <fmt:message key="email"/>
                                        </h5>
                                        <span class="font-16">${sessionScope.user.email}</span>
                                    </div>

                                    <form method="POST" action="controller" class="mt-2"
                                          name="changePasswordForm"
                                          onsubmit="return validateChangePasswordForm(event)">
                                        <input type="hidden" name="command" value="change_password">
                                        <input type="hidden" name="user_id" value="${sessionScope.user.id}">
                                        <h5 class="pe-2">
                                            <fmt:message key = "title.change_password.form"/>
                                        </h5>
                                        <div class="form-group row">
                                            <div class="col-sm-9">
                                                <input  required type="password"
                                                        name="old_password"
                                                        class="form-control"
                                                        placeholder="<fmt:message key = "profile.page.placeholder.current_password"/>"
                                                />
                                            </div>
                                        </div>
                                        <div class="form-group row">
                                            <div class="col-sm-9">
                                                <input  required type="password"
                                                        name="new_password"
                                                        class="form-control"
                                                        placeholder="<fmt:message key = "profile.page.placeholder.new_password"/>"
                                                />
                                            </div>
                                        </div>
                                        <div class="form-group row">
                                            <div class="col-sm-9">
                                                <input  required type="password"
                                                        name="confirm_new_password"
                                                        class="form-control"
                                                        placeholder="<fmt:message key = "profile.page.placeholder.confirm_password"/>"
                                                />
                                            </div>
                                        </div>
                                        <button type="submit" type="button"
                                                class="start_timekeeping btn btn-primary btn-md text-white font-bold">
                                            <fmt:message key="button.change.password"/>
                                        </button>
                                        <button type="button" form="deleteAccount"
                                                class="btn btn-danger text-white" data-bs-toggle="modal"
                                                data-bs-target="#staticBackdrop">
                                            <fmt:message key="button.delete.account"/>
                                        </button>
                                    </form>

                                    <%--Delete button--%>
                                    <form id="deleteAccount" action="controller" method="GET" class="p-0 m-0 d-inline">
                                        <input type="hidden" name="command" value="remove_account">
                                        <input type="hidden" name="user_id" value="${sessionScope.user.id}">
                                        <!-- Modal -->
                                        <div class="modal fade" id="staticBackdrop" data-bs-backdrop="static"
                                             data-bs-keyboard="false" tabindex="-1"
                                             aria-labelledby="staticBackdropLabel" aria-hidden="true">
                                            <div class="modal-dialog modal-dialog-centered modal-dialog-scrollable modal-sm">
                                                <div class="modal-content">
                                                    <div class="modal-header">
                                                        <h5 class="modal-title" id="staticBackdropLabel">
                                                            <fmt:message key="delete.accout.warning.text"/>
                                                        </h5>
                                                    </div>

                                                    <div class="modal-footer">
                                                        <button type="button" class="btn btn-secondary"
                                                                data-bs-dismiss="modal">
                                                            <fmt:message key="admin.page.button.close"/>
                                                        </button>
                                                        <button type="submit" class="btn btn-danger">
                                                            <fmt:message key="profile.page.button.confirm"/>
                                                        </button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-3"></div>
            </div>
        </div>
        <footer class="footer text-center text-light">
            Timekeeping, EPAM Java final project, 2022
        </footer>
    </div>
</div>
<show:submit_result/>
<fmt:message key="password.change.failed"/>
<fmt:message key="password.change.success"/>
<!-- ============================================================== -->
<!-- All Jquery -->
<!-- ============================================================== -->
<%@ include file="/WEB-INF/fragments/scripts.jspf" %>
<script>
    function validateChangePasswordForm(e) {
        e.preventDefault();
        let oldPassword = document.forms.changePasswordForm.old_password.value;
        let newPassword = document.forms.changePasswordForm.new_password.value;
        let confirmNewPassword = document.forms.changePasswordForm.confirm_new_password.value;
        let patternPasswordOld = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,32}$/g;
        let patternPasswordNew = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,32}$/g;

        if (!patternPasswordOld.test(oldPassword)) {
            alert(`<fmt:message key = "index_jsp.form.register.validate_password"/>`);
            return false;
        }

        if (!patternPasswordNew.test(newPassword)) {
            alert(`<fmt:message key = "index_jsp.form.register.validate_password"/>`);
            return false;
        }

        if (confirmNewPassword !== newPassword) {
            alert(`<fmt:message key = "index_jsp.form.register.validate_password.confirm"/>`);
            return false;
        }

        setTimeout(()=>{
            document.forms.changePasswordForm.submit();
        }, 500)
        return true;
    }
</script>
</body>
</html>
