<%@ include file="/WEB-INF/fragments/directive/taglib.jspf" %>
<c:set var="language"
       value="${not empty sessionScope.language ? sessionScope.language :
         not empty cookie['defaultLocale'].getValue() ? cookie['defaultLocale'].getValue() :pageContext.request.locale}"
       scope="session"/>
<fmt:setLocale value="${language}"/>

<c:if test="${sessionScope.request_status == 'success'}">
    <script>
        window.onload = function () {
            document.querySelector('button[data-selector="requestResult"]').click()
        }
    </script>
    <button data-selector="requestResult" type="button" class="d-none" data-bs-toggle="modal"
            data-bs-target="#requestResultModal"></button>
    <div class="modal fade" id="requestResultModal" tabindex="-1"
         aria-labelledby="requestResultModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="btn-close" data-bs-dismiss="modal"
                            aria-label="Close"></button>
                </div>
                <div class="modal-body font-20 font-bold d-flex justify-content-center">
                    <fmt:message key="custom.tag.request.message"/>
                </div>
            </div>
<%--    <div class="alert alert-success" role="alert">--%>
<%--        A simple success alert—check it out!--%>
<%--    </div>--%>
        </div>
    </div>
</c:if>
<c:if test="${sessionScope.request_status == 'failed'}">
    <script>
        window.onload = function () {
            document.querySelector('button[data-selector="requestResult"]').click()
        }
    </script>
    <button data-selector="requestResult" type="button" class="d-none" data-bs-toggle="modal"
            data-bs-target="#requestResultModal"></button>
    <div class="modal fade" id="requestResultModal" tabindex="-1"
         aria-labelledby="requestResultModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content border border-danger">
                <div class="modal-header">
                    <button type="button" class="btn-close" data-bs-dismiss="modal"
                            aria-label="Close">
                    </button>
                </div>
                <div class="modal-body text-danger font-20 font-bold d-flex justify-content-center">
                    <fmt:message key="custom.tag.request.failed.message"/>
                </div>
            </div>
        </div>
    </div>
</c:if>
<c:remove var="request_status" scope="session"/>
