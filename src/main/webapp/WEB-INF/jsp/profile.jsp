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
                <a class="navbar-brand" href="index.html">
                    <span class="logo-text ms-2">Timekeeping app</span>
                </a>
            </div>
            <div class="navbar-collapse collapse d-flex justify-content-end" data-navbarbg="skin5">
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
                <h4 class="page-title">Hello userName. You can start keeping time</h4>

                <div class="ms-auto text-end">
                    <nav aria-label="breadcrumb">
                        <ol class="breadcrumb">
                            <li class="breadcrumb-item"><a class="text-orange" href="#">Home</a></li>
                            <li class="breadcrumb-item text-light active" aria-current="page">Library</li>
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
                            <h4 class="card-title mb-0">Profile</h4>
                        </div>
                        <div class="comment-widgets scrollable border-bottom border-1">
                            <div class="d-flex flex-row comment-row">
                                <div class="comment-text w-100">
                                    <div class="d-flex align-items-baseline mb-2">
                                        <h5 class="pe-2 mb-0">Login:</h5>
                                        <span class="font-16">User1</span>
                                    </div>
                                    <div class="d-flex align-items-baseline mb-2">
                                        <h5 class="pe-2 mb-0">Email:</h5>
                                        <span class="font-16">emal@test.com</span>
                                    </div>
                                    <form class="mt-2">
                                        <h5 class="pe-2">Change password:</h5>
                                        <div class="form-group row">
                                            <div class="col-sm-9">
                                                <input
                                                        type="password"
                                                        class="form-control"
                                                        placeholder="Old password"
                                                />
                                            </div>
                                        </div>
                                        <div class="form-group row">
                                            <div class="col-sm-9">
                                                <input
                                                        type="password"
                                                        class="form-control"
                                                        placeholder="New password"
                                                />
                                            </div>
                                        </div>
                                        <div class="form-group row">
                                            <div class="col-sm-9">
                                                <input
                                                        type="password"
                                                        class="form-control"
                                                        placeholder="Confirm new password"
                                                />
                                            </div>
                                        </div>
                                        <button type="submit" type="button"
                                                class="start_timekeeping btn btn-primary btn-md text-white font-bold">
                                            Change password
                                        </button>
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
<!-- ============================================================== -->
<!-- All Jquery -->
<!-- ============================================================== -->
<%@ include file="/WEB-INF/fragments/scripts.jspf" %>
</body>
</html>
