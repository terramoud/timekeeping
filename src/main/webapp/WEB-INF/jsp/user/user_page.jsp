<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/fragments/directive/taglib.jspf" %>
<c:set var="language"
value="${not empty sessionScope.language ? sessionScope.language :
         not empty cookie['defaultLocale'].getValue() ? cookie['defaultLocale'].getValue() :pageContext.request.locale}"
scope="session"/>
<fmt:setLocale value="${language}"/>

<!DOCTYPE html>
<html lang="${language}">
<c:set var="title" value="User page" scope="page"/>
<%@ include file="/WEB-INF/fragments/head.jspf" %>

<body>
<div id="main-wrapper" data-layout="vertical" data-navbarbg="skin5" data-sidebartype="full"
     data-sidebar-position="absolute" data-header-position="absolute" data-boxed-layout="full">
    <!-- ============================================================== -->
    <!-- Topbar header -->
    <!-- ============================================================== -->
    <header class="topbar" data-navbarbg="skin5">
        <nav class="navbar top-navbar navbar-expand-md navbar-dark">
            <div class="navbar-header" data-logobg="skin5">
                <a class="navbar-brand" href="controller?command=user_page">
                    <span class="logo-text ms-2"><fmt:message key = "project.name"/></span>
                </a>
            </div>
            <div class="navbar-collapse collapse d-flex justify-content-end" data-navbarbg="skin5">
                <ul class="navbar-nav float-end">
                    <!-- ============================================================== -->
                    <!-- Language switcher -->
                    <!-- ============================================================== -->
                    <li class="nav-item dropdown">
                        <c:set var="currentPageCommandName" value="user_page" scope="page"/>
                        <%@ include file="/WEB-INF/fragments/language_switcher.jspf" %>
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
                <h4 class="page-title"><fmt:message key = "user.page.title"/></h4>
                <div class="ms-auto text-end">
                    <nav aria-label="breadcrumb">
                        <ol class="breadcrumb">
                            <li class="breadcrumb-item">
                                <a class="text-orange" href="controller?command=index_page">
                                    <fmt:message key = "project.home.page"/>
                                </a>
                            </li>
                            <li class="breadcrumb-item text-light active" aria-current="page">
                                <fmt:message key = "breadcrumb.user.page"/>
                            </li>
                        </ol>
                    </nav>
                </div>
            </div>
        </div>
        <div class="container-fluid">
            <div class="row">
                <div class="col-md-1"></div>
                <div class="col-md-5">
                    <div class="card">
                        <div class="card-body">
                            <h4 class="card-title mb-0">
                                <fmt:message key = "user.page.list.user.activities"/>
                            </h4>
                        </div>
                        <div class="comment-widgets scrollable border-bottom border-1">
                            <div class="d-flex flex-row comment-row">
                                <div class="comment-text w-100">
                                    <div class="d-flex align-items-baseline mb-2">
                                        <h5 class="pe-2 mb-0"><fmt:message key = "user.page.activity.name"/></h5>
                                        <span class="font-16">Reading</span>
                                    </div>
                                    <div class="activity-status d-flex my-2">
                                        <div class="d-flex me-3">
                                            <span class="me-1 font-bold"><fmt:message key = "user.page.activity.start.time"/></span>
                                            <span class="started-time">08:11:05</span>
                                        </div>
                                        <div class="d-flex">
                                            <span class="me-1 font-bold"><fmt:message key = "user.page.activity.finish.time"/></span>
                                            <span class="finish-time">Waiting...</span>
                                            <span class="spinner-border spinner-border-sm ms-1" role="status"
                                                  aria-hidden="true"></span>
                                        </div>
                                    </div>
                                    <div class="comment-footer mt-2">
                                        <button type="button"
                                                class="start_timekeeping btn btn-success btn-md text-white font-bold">
                                            <fmt:message key = "user.page.button.start.activity"/>
                                        </button>
                                        <button type="button" class="btn btn-danger btn-md text-white font-bold">
                                            <fmt:message key = "user.page.button.stop.activity"/>
                                        </button>
                                        <button type="button" class="btn btn-dark btn-md text-white font-bold">
                                            <fmt:message key = "user.page.button.remove.activity"/>
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-5">
                    <form action="controller" method="POST" class="card">
                        <input type="hidden" name="command" value="add_activity_request">
                        <div class="card-body">
                            <h5 class="card-title">
                                <fmt:message key = "user.page.list.all.activities"/>
                            </h5>
                            <div class="form-group row mb-0">
                                <label class="col-md-3 mt-2">
                                    <fmt:message key = "user.page.button.choose.activity"/>
                                </label>
                                <div class="col-md-9">
                                    <select  class="select2 form-select shadow-none" style="width: 100%; height: 36px">
                                        <option disabled selected>
                                            <fmt:message key = "user.page.button.select.activity"/>
                                        </option>
                                        <c:forEach var="entry" items="${allActivitiesByCategories}">
                                            <c:set var="category" value="${entry.key}"/>
                                            <c:forEach var="activity" items="${entry.value}">
                                                <optgroup label="<c:out value="${language == 'en' ? category.nameEn : category.nameUk}"/>">
                                                    <option value="${activity}">
                                                        <c:out value="${language == 'en' ? activity.nameEn : activity.nameUk}"/>
                                                    </option>
                                                </optgroup>
                                            </c:forEach>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="border-top">
                            <div class="card-body">
                                <button type="submit" class="btn btn-primary">
                                    <fmt:message key = "user.page.button.add.new.activity"/>
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="col-md-1"></div>
            </div>
        </div>
        <footer class="footer text-center text-light">
            Timekeeping, EPAM Java final project, 2022
        </footer>
    </div>
</div>

<div class="preloader">
    <div class="lds-ripple">
        <div class="lds-pos"></div>
        <div class="lds-pos"></div>
    </div>
</div>
<!-- ============================================================== -->
<!-- All Jquery -->
<!-- ============================================================== -->
<%@ include file="/WEB-INF/fragments/scripts.jspf" %>
</body>
</html>
