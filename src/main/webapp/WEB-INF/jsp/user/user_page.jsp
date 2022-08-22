<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html dir="ltr" lang="en">
<c:set var="title" value="Settings" scope="page" />
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
                            <select class="form-select shadow-none">
                                <option class="d-none d-md-block">EN</option>
                                <option class="d-none d-md-block">UK</option>
                            </select>
                        </div>
                    </li>
                    <!-- ============================================================== -->
                    <!-- User profile -->
                    <!-- ============================================================== -->
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle text-muted waves-effect waves-dark pro-pic" href="#"
                           id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                            <img src="assets/images/users/1.jpg" alt="user" class="rounded-circle" width="31"/>
                        </a>
                        <ul class="dropdown-menu dropdown-menu-end user-dd animated" aria-labelledby="navbarDropdown">
                            <a class="dropdown-item" href="javascript:void(0)">
                                <em class="mdi mdi-account me-1 ms-1"></em> My Profile
                            </a>
                            <div class="dropdown-divider"></div>
                            <a class="dropdown-item" href="javascript:void(0)">
                                <em class="fa fa-power-off me-1 ms-1"></em> Logout
                            </a>
                        </ul>
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
                <div class="col-md-1"></div>
                <div class="col-md-5">
                    <div class="card">
                        <div class="card-body">
                            <h4 class="card-title mb-0">List of your activities</h4>
                        </div>
                        <div class="comment-widgets scrollable border-bottom border-1">
                            <div class="d-flex flex-row comment-row">
                                <div class="comment-text w-100">
                                    <div class="d-flex align-items-baseline mb-2">
                                        <h5 class="pe-2 mb-0">Activity:</h5>
                                        <span class="font-16">Reading</span>
                                    </div>
                                    <div class="activity-status d-flex my-2">
                                        <div class="d-flex me-3">
                                            <span class="me-1 font-bold">Start time:</span>
                                            <span class="started-time">08:11:05</span>
                                        </div>
                                        <div class="d-flex">
                                            <span class="me-1 font-bold">Finish time:</span>
                                            <span class="finish-time">Waiting...</span>
                                            <span class="spinner-border spinner-border-sm ms-1" role="status"
                                                  aria-hidden="true"></span>
                                        </div>
                                    </div>
                                    <div class="comment-footer mt-2">
                                        <button type="button"
                                                class="start_timekeeping btn btn-success btn-md text-white font-bold">
                                            Start
                                        </button>
                                        <button type="button" class="btn btn-danger btn-md text-white font-bold">
                                            Stop
                                        </button>
                                        <button type="button" class="btn btn-dark btn-md text-white font-bold">
                                            Remove activity
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-5">
                    <div class="card">
                        <div class="card-body">
                            <h5 class="card-title">List all activities by category</h5>
                            <div class="form-group row mb-0">
                                <label class="col-md-3 mt-2">Choose activity</label>
                                <div class="col-md-9">
                                    <select class="select2 form-select shadow-none" style="width: 100%; height: 36px">
                                        <option>Select</option>
                                        <optgroup label="Alaskan/Hawaiian Time Zone">
                                            <option value="AK">Alaska</option>
                                            <option value="HI">Hawaii</option>
                                        </optgroup>
                                        <optgroup label="Pacific Time Zone">
                                            <option value="CA">California</option>
                                            <option value="NV">Nevada</option>
                                            <option value="OR">Oregon</option>
                                            <option value="WA">Washington</option>
                                        </optgroup>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="border-top">
                            <div class="card-body">
                                <button type="button" class="btn btn-primary">
                                    Submit request to add the selected activity
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-1"></div>
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
