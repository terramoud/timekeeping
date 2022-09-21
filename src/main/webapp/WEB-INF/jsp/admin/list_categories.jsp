<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/fragments/directive/taglib.jspf" %>
<c:set var="language"
       value="${not empty sessionScope.language ? sessionScope.language :
         not empty cookie['defaultLocale'].getValue() ? cookie['defaultLocale'].getValue() :pageContext.request.locale}"
       scope="session"/>
<fmt:setLocale value="${language}"/>

<!DOCTYPE html>
<html lang="${language}">
<c:set var="title" value="List user's categories" scope="page"/>
<%@ include file="/WEB-INF/fragments/head.jspf" %>

<body>
<div class="preloader">
    <div class="lds-ripple">
        <div class="lds-pos"></div>
        <div class="lds-pos"></div>
    </div>
</div>

<div id="main-wrapper" data-layout="vertical" data-navbarbg="skin5" data-sidebartype="full"
     data-sidebar-position="absolute" data-header-position="absolute" data-boxed-layout="full">
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
                    <form method="POST" action="controller"
                          name="formEditCategory"
                          onsubmit="return editCategoryFormValidation(event, 'formEditCategory')" class="card" >
                        <input type="hidden" name="command" value="admin_create_category">
                        <div class="card-body">
                            <h5 class="card-title">
                                <fmt:message key="admin.page.add.new.category"/>
                            </h5>
                            <div class="form-group row">
                                <label class="col-sm-3 text-end control-label col-form-label">
                                    <fmt:message key="admin.page.category.name"/>
                                </label>
                                <div class="col-md-9">
                                    <input required type="text" class="form-control" id="fnameNewCategory"
                                           name="name_en"
                                           placeholder="<fmt:message key="admin.page.category.name.placeholder"/>"/>
                                </div>
                            </div>
                            <div class="form-group row">
                                <label class="col-sm-3 text-end control-label col-form-label">
                                    <fmt:message key="admin.page.category.name_uk"/>
                                </label>
                                <div class="col-sm-9">
                                    <input required type="text" class="form-control" id="fnameNewCategoryUK"
                                           name="name_uk"
                                           placeholder="<fmt:message key="admin.page.category.name.placeholder"/>"/>
                                </div>
                            </div>
                        </div>
                        <div class="border-top">
                            <div class="card-body">
                                <button type="submit" class="btn btn-primary">
                                    <fmt:message key="admin.page.add.new.category"/>
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="col-md-12">
                    <div class="card">
                        <div class="card-body">
                            <h5 class="card-title mb-0">
                                <fmt:message key="admin.page.table.categories.name"/>
                            </h5>
                            <table class="table">
                                <thead>
                                <tr>
                                    <th scope="col">
                                        <a href="controller?command=admin_list_categories&pageNumber=${requestScope.pageNumber}&order_by=name_en&desc=${requestScope.desc}">
                                            <fmt:message key="admin.page.category.name"/>
                                        </a>
                                    </th>

                                    <th scope="col">
                                        <a href="controller?command=admin_list_categories&pageNumber=${requestScope.pageNumber}&order_by=name_uk&desc=${requestScope.desc}">
                                            <fmt:message key="admin.page.category.name_uk"/>
                                        </a>
                                    </th>
                                    <th scope="col"><fmt:message key="admin.page.requests.table.header.actions"/></th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="category" items="${categories}" varStatus="status">
                                    <tr>
                                        <td class="align-middle"><c:out value="${category.nameEn}"/></td>
                                        <td class="align-middle"><c:out value="${category.nameUk}"/></td>
                                        <td>
                                            <button type="submit" class="btn btn-info me-1" data-bs-toggle="modal"
                                                    data-bs-target="#staticBackdrop${category.id}">
                                                <fmt:message key="admin.page.button.edit"/>
                                            </button>
                                            <!-- Modal -->
                                            <div class="modal fade" id="staticBackdrop${category.id}" data-bs-backdrop="static"
                                                 data-bs-keyboard="false" tabindex="-1"
                                                 aria-labelledby="staticBackdrop${category.id}Label" aria-hidden="true">
                                                <div class="modal-dialog modal-dialog-centered modal-dialog-scrollable modal-lg">
                                                    <form name="formEditCategory${category.id}" method="POST" action="controller"
                                                          class="modal-content"
                                                          onSubmit="return editCategoryFormValidation(event, 'formEditCategory${category.id}')"
                                                          onkeydown="return event.key != 'Enter'">
                                                        <input type="hidden" name="command" value="admin_edit_category">
                                                        <input type="hidden" name="category_id" value="${category.id}">
                                                        <div class="modal-header">
                                                            <h5 class="modal-title" id="staticBackdrop${category.id}Label">
                                                                <fmt:message key="admin.page.category.modal"/>
                                                            </h5>
                                                            <button type="button" class="btn-close" data-bs-dismiss="modal"
                                                                    aria-label="Close"></button>
                                                        </div>
                                                        <div class="modal-body pb-0">
                                                            <div class="card mb-0">
                                                                <div class="card-body">
                                                                    <div class="form-group row">
                                                                        <label class="col-sm-3 text-end control-label col-form-label">
                                                                            <fmt:message key="admin.page.category.name"/>
                                                                        </label>
                                                                        <div class="col-md-9">
                                                                            <input type="text" class="form-control"
                                                                                   id="fname"
                                                                                   name="name_en" value="${category.nameEn}"
                                                                                   placeholder="<fmt:message key="admin.page.category.name.placeholder"/>"/>
                                                                        </div>
                                                                    </div>
                                                                    <div class="form-group row">
                                                                        <label class="col-sm-3 text-end control-label col-form-label">
                                                                            <fmt:message key="admin.page.category.name_uk"/>
                                                                        </label>
                                                                        <div class="col-sm-9">
                                                                            <input type="text" class="form-control"
                                                                                   id="fnameUK"
                                                                                   name="name_uk" value="${category.nameUk}"
                                                                                   placeholder="<fmt:message key="admin.page.category.name.placeholder"/>"/>
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
                                                <input type="hidden" name="command" value="admin_remove_category">
                                                <input type="hidden" name="category_id" value="${category.id}">
                                                <button type="submit" class="btn btn-danger text-white">
                                                    <fmt:message key="admin.page.button.delete"/>
                                                </button>
                                                <show:submit_result/>
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
                                                                     commandName="admin_list_categories"
                                                                     paramsOtherPaginations="&order_by=${requestScope.order_by}&desc=${!requestScope.desc}"
                                                                     paramName="pageNumber"/>

                                        <c:forEach var="i" begin="1" end="${totalPages}">
                                            <c:if test="${i==pageNumber}">
                                                <li class="page-item active">
                                                    <a class="page-link"
                                                       href="controller?command=admin_list_categories&pageNumber=${i}&order_by=${requestScope.order_by}&desc=${!requestScope.desc}">${i}</a>
                                                </li>
                                            </c:if>
                                            <c:if test="${i!=pageNumber}">
                                                <li class="page-item">
                                                    <a class="page-link"
                                                       href="controller?command=admin_list_categories&pageNumber=${i}&order_by=${requestScope.order_by}&desc=${!requestScope.desc}">${i}</a>
                                                </li>
                                            </c:if>
                                        </c:forEach>

                                        <show:pagination_next_button pageNum="${pageNumber}"
                                                                     paramName="pageNumber"
                                                                     commandName="admin_list_categories"
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
    function editCategoryFormValidation(e, formName) {
        e.preventDefault();
        let nameEn = document.querySelector('form[name="' + formName + '"]').name_en.value;
        let nameUk = document.querySelector('form[name="' + formName + '"]').name_uk.value;
        const regNameEn = /^[a-zA-Z][a-zA-Z \-']{0,30}[a-zA-Z]$/g; // Javascript reGex for activity name validation
        const regNameUk = /^[\p{L}][\p{L} \-']{0,30}[\p{L}]$/gu; // Javascript reGex for activity name validation

        if (!regNameEn.test(nameEn)) {
            window.alert(`<fmt:message key = "admin.page.category.name_en.invalid"/>`);
            return false;
        }

        if (!regNameUk.test(nameUk)) {
            window.alert(`<fmt:message key = "admin.page.category.name.invalid"/>`);
            return false;
        }

        document.querySelector('form[name="' + formName + '"]').submit();
        return true;
    }
</script>
</body>
</html>

