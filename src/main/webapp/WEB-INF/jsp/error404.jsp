<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html xml:lang="en">
<c:set var="title" value="Settings" scope="page" />
<%@ include file="/WEB-INF/fragments/head.jspf" %>

<body>
<div class="main-wrapper">
    <div class="preloader">
        <div class="lds-ripple">
            <div class="lds-pos"></div>
            <div class="lds-pos"></div>
        </div>
    </div>

    <div class="error-box">
        <div class="error-body text-center">
            <h1 class="error-title text-danger">404</h1>
            <h3 class="text-uppercase error-subtitle">PAGE NOT FOUND !</h3>
            <p class="text-muted mt-4 mb-4">
                YOU SEEM TO BE TRYING TO FIND WRONG WAY
            </p>
            <a href="index.html" class="btn btn-danger btn-rounded waves-effect waves-light mb-5 text-white">Back to home</a>
        </div>
    </div>
</div>
<!-- ============================================================== -->
<!-- All Required js -->
<!-- ============================================================== -->
<script src="js/jquery.min.js"></script>
<!-- Bootstrap tether Core JavaScript -->
<script src="js/bootstrap.min.js"></script>
<!-- ============================================================== -->
<!-- This page plugin js -->
<!-- ============================================================== -->
<script>
    $(".preloader").fadeOut();
</script>
</body>
</html>
