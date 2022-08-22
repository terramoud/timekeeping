<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html xml:lang="en">
<c:set var="title" value="Settings" scope="page" />
<%@ include file="/WEB-INF/fragments/head.jspf" %>

<body class="bg-dark">
<h1 class="text-light d-flex justify-content-center my-5">Welcome to time keeping app</h1>
<div class="main-wrapper">

    <div class="preloader">
        <div class="lds-ripple">
            <div class="lds-pos"></div>
            <div class="lds-pos"></div>
        </div>
    </div>

    <div class="auth-wrapper d-flex no-block justify-content-center
                        align-items-top bg-dark px-3 pb-3 mx-auto row container">
        <!-- ============================================================== -->
        <!-- login block -->
        <!-- ============================================================== -->
        <div class="auth-box bg-dark border-top border-secondary col-md-4 col-sm-6 px-3">
            <div>
                <div class="text-center pt-3 pb-3">
                    <h2 class="text-light">Sing in</h2>
                </div>
                <form class="form-horizontal mt-3" id="loginform" action="index.html">
                    <div class="row pb-4">
                        <div class="col-12">
                            <div class="input-group mb-3">
                                <div class="input-group-prepend">
                                            <span class="input-group-text bg-success text-white h-100">
                                                <em class="mdi mdi-account fs-4"></em>
                                            </span>
                                </div>
                                <input
                                        type="text"
                                        class="form-control form-control-lg"
                                        placeholder="Username"
                                        aria-label="Username"
                                        aria-describedby="basic-addon1"
                                        required=""
                                />
                            </div>
                            <div class="input-group mb-3">
                                <div class="input-group-prepend">
                                            <span class="input-group-text bg-warning text-white h-100">
                                                <em class="mdi mdi-lock fs-4"></em>
                                            </span>
                                </div>
                                <input
                                        type="password"
                                        class="form-control form-control-lg"
                                        placeholder="Password"
                                        aria-label="Password"
                                        aria-describedby="basic-addon1"
                                        required=""
                                />
                            </div>
                        </div>
                    </div>
                    <div class="row border-top border-secondary">
                        <div class="col-12">
                            <div class="form-group">
                                <div class="pt-3">
                                    <button class="btn btn-info" id="to-recover" type="button">
                                        <em class="mdi mdi-lock fs-4 me-1"></em> Lost password?
                                    </button>
                                    <button class="btn btn-success float-end text-white" type="submit">Login</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div id="recoverform" style="display: none;">
                <div class="text-center">
                    <span class="text-white">Enter your e-mail address below and we will send you
                        instructions how to recover a password.
                    </span>
                </div>
                <div class="row mt-3">
                    <form class="col-12" action="index.html">
                        <div class="input-group mb-3">
                            <div class="input-group-prepend">
                                        <span class="input-group-text bg-danger text-white h-100">
                                            <em class="mdi mdi-email fs-4"></em>
                                        </span>
                            </div>
                            <input
                                    type="email"
                                    class="form-control form-control-lg"
                                    placeholder="Email Address"
                                    aria-label="Username"
                                    aria-describedby="basic-addon1"
                            />
                        </div>
                        <div class="row mt-3 pt-3 border-top border-secondary">
                            <div class="col-12">
                                <a class="btn btn-success text-white" href="#" id="to-login">Back To Login</a>
                                <button class="btn btn-info float-end" type="button" name="action">Recover</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>


        <!-- ============================================================== -->
        <!-- Registration block -->
        <!-- ============================================================== -->
        <div class="auth-box bg-dark border-top border-secondary col-md-4 col-sm-6 px-3">
            <div>
                <div class="text-center pt-3 pb-3">
                    <h2 class="text-light">Registration form</h2>
                </div>
                <form class="form-horizontal mt-3" action="index.html">
                    <div class="row pb-4">
                        <div class="col-12">
                            <div class="input-group mb-3">
                                <div class="input-group-prepend">
                                            <span class="input-group-text bg-success text-white h-100">
                                                <em class="mdi mdi-account fs-4"></em>
                                            </span>
                                </div>
                                <input
                                        type="text"
                                        class="form-control form-control-lg"
                                        placeholder="Username"
                                        aria-label="Username"
                                        aria-describedby="basic-addon1"
                                        required
                                />
                            </div>
                            <!-- email -->
                            <div class="input-group mb-3">
                                <div class="input-group-prepend">
                                            <span class="input-group-text bg-danger text-white h-100">
                                                <em class="mdi mdi-email fs-4"></em>
                                            </span>
                                </div>
                                <input
                                        type="email"
                                        class="form-control form-control-lg"
                                        placeholder="Email Address"
                                        aria-label="Username"
                                        aria-describedby="basic-addon1"
                                        required
                                />
                            </div>
                            <div class="input-group mb-3">
                                <div class="input-group-prepend">
                                            <span class="input-group-text bg-warning text-white h-100">
                                                <em class="mdi mdi-lock fs-4"></em>
                                            </span>
                                </div>
                                <input
                                        type="password"
                                        class="form-control form-control-lg"
                                        placeholder="Password"
                                        aria-label="Password"
                                        aria-describedby="basic-addon1"
                                        required
                                />
                            </div>
                            <div class="input-group mb-3">
                                <div class="input-group-prepend">
                                            <span class="input-group-text bg-info text-white h-100">
                                                <em class="mdi mdi-lock fs-4"></em>
                                            </span>
                                </div>
                                <input
                                        type="password"
                                        class="form-control form-control-lg"
                                        placeholder=" Confirm Password"
                                        aria-label="Password"
                                        aria-describedby="basic-addon1"
                                        required
                                />
                            </div>
                        </div>
                    </div>
                    <div class="row border-top border-secondary">
                        <div class="col-12">
                            <div class="form-group">
                                <div class="pt-3 d-grid">
                                    <button class="btn btn-block btn-lg btn-info" type="submit">Sign Up</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
        <footer class="footer text-center text-light">
            Timekeeping, EPAM Java final project, 2022
        </footer>
    </div>
</div>

<!-- ============================================================== -->
<!-- All Required js -->
<!-- ============================================================== -->
<%@ include file="/WEB-INF/fragments/scripts.jspf" %>
<!-- ============================================================== -->
<!-- This page plugin js -->
<!-- ============================================================== -->
<script>
    $(".preloader").fadeOut();
    // ==============================================================
    // Login and Recover Password
    // ==============================================================
    $("#to-recover").on("click", function () {
        $("#loginform").slideUp();
        $("#recoverform").fadeIn();
    });
    $("#to-login").click(function () {
        $("#recoverform").hide();
        $("#loginform").fadeIn();
    });
</script>
</body>
</html>

