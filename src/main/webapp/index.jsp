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
<c:set var="title" value="Home page" scope="page"/>
<%@ include file="/WEB-INF/fragments/head.jspf" %>

<body class="bg-dark">
<div class="position-absolute" style="right: 0">
    <c:set var="currentPageCommandName" value="index_page" scope="page"/>
    <%@ include file="/WEB-INF/fragments/language_switcher.jspf" %>
</div>
<h1 class="text-light d-flex justify-content-center my-5">
    <fmt:message key="index_jsp.header"/>
</h1>
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
                    <h2 class="text-light">
                        <fmt:message key="index_jsp.form.login.name"/>
                    </h2>
                </div>
                <form name="formLogin" action="controller" method="POST" class="form-horizontal mt-3" id="loginform"
                      onkeydown="return event.key != 'Enter'" onSubmit="return validateLoginForm(event)">
                    <input type="hidden" name="command" value="login">
                    <div class="row pb-4">
                        <div class="col-12">
                            <div class="input-group mb-3">
                                <div class="input-group-prepend">
                                    <span class="input-group-text bg-success text-white h-100">
                                        <em class="mdi mdi-account fs-4"></em>
                                    </span>
                                </div>
                                <input type="text" class="form-control form-control-lg"
                                       placeholder="<fmt:message key = "index_jsp.placeholder.login"/>"
                                       name="login"
                                       aria-label="<fmt:message key = "index_jsp.placeholder.login"/>"
                                       aria-describedby="basic-addon1" required=""/>
                            </div>
                            <div class="input-group mb-3">
                                <div class="input-group-prepend">
                                    <span class="input-group-text bg-warning text-white h-100">
                                        <em class="mdi mdi-lock fs-4"></em>
                                    </span>
                                </div>
                                <input type="password" class="form-control form-control-lg"
                                       placeholder="<fmt:message key = "index_jsp.placeholder.password"/>"
                                       name="password"
                                       aria-label="<fmt:message key = "index_jsp.placeholder.password"/>"
                                       aria-describedby="basic-addon1" required=""/>
                            </div>
                        </div>
                    </div>
                    <div class="row border-top border-secondary">
                        <div class="col-12">
                            <div class="form-group">
                                <div class="pt-3 d-grid">
                                    <button class="btn btn-block btn-lg btn-success text-white" type="submit">
                                        <fmt:message key="index_jsp.button.sign_in"/>
                                    </button>

                                    <%--sign in result modal window--%>
                                    <button id="loginResult" type="button" class="d-none" data-bs-toggle="modal"
                                            data-bs-target="#loginResultModal"></button>
                                    <div class="modal fade" id="loginResultModal" tabindex="-1"
                                         aria-labelledby="loginResultModalLabel" aria-hidden="true">
                                        <div class="modal-dialog">
                                            <div class="modal-content">
                                                <div class="modal-header">
                                                    <button type="button" class="btn-close" data-bs-dismiss="modal"
                                                            aria-label="Close"></button>
                                                </div>
                                                <div class="modal-body text-success font-20 font-bold d-flex justify-content-center">
                                                    <span class="spinner-border spinner-border-lg ms-1" role="status"
                                                          aria-hidden="false"></span>
                                                </div>
                                                <div class="modal-footer"></div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>

        <!-- ============================================================== -->
        <!-- Registration block -->
        <!-- ============================================================== -->
        <div class="auth-box bg-dark border-top border-secondary col-md-4 col-sm-6 px-3">
            <div>
                <div class="text-center pt-3 pb-3">
                    <h2 class="text-light">
                        <fmt:message key="index_jsp.form.register.name"/>
                    </h2>
                </div>
                <form name="RegForm" onSubmit="return regFormValidation(event)" action="controller" method="POST"
                      class="validatedForm form-horizontal mt-3" onkeydown="return event.key != 'Enter'">
                    <input type="hidden" name="command" value="register">
                    <div class="row pb-4">
                        <div class="col-12">
                            <!-- login -->
                            <div class="input-group mb-3">
                                <div class="input-group-prepend">
                                    <span class="input-group-text bg-success text-white h-100" id="basic-addon1">
                                        <em class="mdi mdi-account fs-4"></em>
                                    </span>
                                </div>
                                <input type="text" class="form-control form-control-lg"
                                       placeholder="<fmt:message key = "index_jsp.placeholder.login"/>"
                                       name="login"
                                       aria-label="<fmt:message key = "index_jsp.placeholder.login"/>"
                                       aria-describedby="basic-addon1" required/>
                            </div>
                            <!-- email -->
                            <div class="input-group mb-3">
                                <div class="input-group-prepend">
                                    <span class="input-group-text bg-danger text-white h-100" id="basic-addon2">
                                        <em class="mdi mdi-email fs-4"></em>
                                    </span>
                                </div>
                                <input type="email" class="form-control form-control-lg"
                                       placeholder="<fmt:message key = "index_jsp.placeholder.email"/>"
                                       name="email"
                                       aria-label="<fmt:message key = "index_jsp.placeholder.email"/>"
                                       aria-describedby="basic-addon2" required/>
                            </div>
                            <!-- password -->
                            <div class="input-group mb-3">
                                <div class="input-group-prepend">
                                    <span class="input-group-text bg-warning text-white h-100" id="basic-addon3">
                                        <em class="mdi mdi-lock fs-4"></em>
                                    </span>
                                </div>
                                <input type="password" class="form-control form-control-lg"
                                       placeholder="<fmt:message key = "index_jsp.placeholder.password"/>"
                                       name="password"
                                       aria-label="<fmt:message key = "index_jsp.placeholder.password"/>"
                                       aria-describedby="basic-addon3" required
                                />
                            </div>
                            <div class="input-group mb-3">
                                <div class="input-group-prepend">
                                    <span class="input-group-text bg-info text-white h-100" id="basic-addon4">
                                        <em class="mdi mdi-lock fs-4"></em>
                                    </span>
                                </div>
                                <input type="password" class="form-control form-control-lg"
                                       placeholder="<fmt:message key = "index_jsp.placeholder.confirm_password"/>"
                                       name="password_confirm"
                                       aria-label="<fmt:message key = "index_jsp.placeholder.confirm_password"/>"
                                       aria-describedby="basic-addon4" required
                                />
                            </div>
                        </div>
                    </div>
                    <div class="row border-top border-secondary">
                        <div class="col-12">
                            <div class="form-group">
                                <div class="pt-3 d-grid">
                                    <button type="submit" class="btn btn-block btn-lg btn-info">
                                        <fmt:message key="index_jsp.button.register"/>
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
        <footer class="footer text-center text-light ">
            Timekeeping, EPAM Java final project, 2022
        </footer>
    </div>
</div>
<show:submit_result/>
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
    $("#to-login").click(function () {
        $("#recoverform").hide();
        $("#loginform").fadeIn();
    });

    function regFormValidation(e) {
        e.preventDefault();
        let login = document.forms.RegForm.login.value;
        let email = document.forms.RegForm.email.value;
        let pass = document.forms.RegForm.password.value;
        let pass_confirm = document.forms.RegForm.password_confirm.value;
        let regEmail = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/g;  //Javascript reGex for Email Validation.
        let regLogin = /^[\p{L}][\p{L}0-9]{4,29}$/gu;          // Javascript reGex for Login validation
        let patternPass = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,}$/g;

        if (login === "" || !regLogin.test(login)) {
            alert(`<fmt:message key = "index_jsp.form.register.validate_login"/>`);
            return false;
        }

        if (email === "" || !regEmail.test(email)) {
            alert(`<fmt:message key = "index_jsp.form.register.validate_email"/>`);
            return false;
        }

        if (!patternPass.test(pass)) {
            alert(`<fmt:message key = "index_jsp.form.register.validate_password"/>`);
            return false;
        }

        if (pass_confirm !== pass) {
            alert(`<fmt:message key = "index_jsp.form.register.validate_password.confirm"/>`);
            return false;
        }
        setTimeout(()=>{
            document.forms.RegForm.submit();
        }, 500)
        return true;
    }

    function validateLoginForm(e) {
        e.preventDefault();
        let login = document.forms.formLogin.login.value;
        let pass = document.forms.formLogin.password.value;
        let regLogin = /^[\p{L}][\p{L}0-9]{4,29}$/gu;
        let patternPassword = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,}$/g;

        if (login === "" || !regLogin.test(login)) {
            alert(`<fmt:message key = "index_jsp.form.register.validate_login"/>`);
            return false;
        }
        if (!patternPassword.test(pass)) {
            console.log(pass)
            alert(`<fmt:message key = "index_jsp.form.register.validate_password"/>`);
            return false;
        }
        setTimeout(()=>{
            document.forms.formLogin.submit();
        }, 500)
        document.querySelector("#loginResult").click();
        return true;
    }
</script>
</body>
</html>

