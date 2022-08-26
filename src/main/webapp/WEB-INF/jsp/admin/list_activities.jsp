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
    <%@ include file="/WEB-INF/fragments/admin_header.jspf" %>

    <%@ include file="/WEB-INF/fragments/admin_sidebar.jspf" %>

    <!-- ============================================================== -->
    <!-- Page wrapper  -->
    <!-- ============================================================== -->
    <div class="page-wrapper">
        <div class="page-breadcrumb">
            <div class="row">
                <div class="col-12 d-flex no-block align-items-center">
                    <h4 class="page-title">Admin dashboard</h4>
                    <div class="ms-auto text-end">
                        <nav aria-label="breadcrumb">
                            <ol class="breadcrumb">
                                <li class="breadcrumb-item"><a href="#">Home</a></li>
                                <li class="breadcrumb-item active" aria-current="page">Library</li>
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
                    <form class="card">
                        <div class="card-body">
                            <h5 class="card-title">Add new activity</h5>
                            <div class="form-group row">
                                <label class="col-sm-3 text-end control-label col-form-label">Choose category for
                                    activity</label>
                                <div class="col-md-9">
                                    <select class="select2 form-select shadow-none" style="width: 100%; height: 36px">
                                        <option selected disabled>Select</option>
                                        <option value="AK">Alaska</option>
                                        <option value="HI">Hawaii</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group row">
                                <label class="col-sm-3 text-end control-label col-form-label">Activity name en</label>
                                <div class="col-md-9">
                                    <input type="text" class="form-control" id="fname" placeholder="First Name Here"/>
                                </div>
                            </div>
                            <div class="form-group row">
                                <label class="col-sm-3 text-end control-label col-form-label">Activity name uk</label>
                                <div class="col-sm-9">
                                    <input type="text" class="form-control" id="fname" placeholder="First Name Here"/>
                                </div>
                            </div>
                        </div>
                        <div class="border-top">
                            <div class="card-body">
                                <button type="button" class="btn btn-primary">Add new activity</button>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="col-md-12">
                    <div class="card">
                        <div class="card-body">
                            <h5 class="card-title mb-0">List all activities</h5>
                            <table class="table">
                                <thead>
                                <tr>
                                    <th scope="col">Activity en</th>
                                    <th scope="col">Activity uk</th>
                                    <th scope="col">Activity category</th>
                                    <th scope="col">Actions</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td class="align-middle">Swiming</td>
                                    <td class="align-middle">Swiming</td>
                                    <td class="align-middle">Sport</td>
                                    <td>
                                        <button type="submit" class="btn btn-info  me-1" data-bs-toggle="modal"
                                                data-bs-target="#staticBackdrop">Edit
                                        </button>
                                        <!-- Modal -->
                                        <div class="modal fade" id="staticBackdrop" data-bs-backdrop="static"
                                             data-bs-keyboard="false" tabindex="-1"
                                             aria-labelledby="staticBackdropLabel" aria-hidden="true">
                                            <div class="modal-dialog modal-dialog-centered modal-dialog-scrollable modal-lg">
                                                <form class="modal-content">
                                                    <div class="modal-header">
                                                        <h5 class="modal-title" id="staticBackdropLabel">Edit
                                                            activity</h5>
                                                        <button type="button" class="btn-close" data-bs-dismiss="modal"
                                                                aria-label="Close"></button>
                                                    </div>
                                                    <div class="modal-body pb-0">
                                                        <div class="card mb-0">
                                                            <div class="card-body">
<%--                                                                <h5 class="card-title">Edit activity</h5>--%>
                                                                <div class="form-group row">
                                                                    <label class="col-sm-3 text-end control-label col-form-label">Choose category for activity</label>
                                                                    <div class="col-md-9">
                                                                        <select class="select2 form-select shadow-none"
                                                                                style="width: 100%; height: 36px">
                                                                            <option selected disabled>Select</option>
                                                                            <option value="AK">Alaska</option>
                                                                            <option value="HI">Hawaii</option>
                                                                        </select>
                                                                    </div>
                                                                </div>
                                                                <div class="form-group row">
                                                                    <label class="col-sm-3 text-end control-label col-form-label">Activity name en</label>
                                                                    <div class="col-md-9">
                                                                        <input type="text" class="form-control"
                                                                               id="fname"
                                                                               placeholder="First Name Here"/>
                                                                    </div>
                                                                </div>
                                                                <div class="form-group row">
                                                                    <label class="col-sm-3 text-end control-label col-form-label">Activity name uk</label>
                                                                    <div class="col-sm-9">
                                                                        <input type="text" class="form-control"
                                                                               id="fname"
                                                                               placeholder="First Name Here"/>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="modal-footer">
                                                        <button type="button" class="btn btn-secondary"
                                                                data-bs-dismiss="modal">Close
                                                        </button>
                                                        <button type="button" class="btn btn-primary">Save</button>
                                                    </div>
                                                </form>
                                            </div>
                                        </div>
                                        <%--Delete button--%>
                                        <button type="submit" class="btn btn-danger text-white">Delete</button>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                            <div class="row float-end">
                                <nav aria-label="Page navigation example">
                                    <ul class="pagination">
                                        <li class="page-item">
                                            <a class="page-link" href="#">Previous</a>
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
                                            <a class="page-link" href="#">Next</a>
                                        </li>
                                    </ul>
                                </nav>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <footer class="footer text-center">
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

