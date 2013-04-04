<!doctype html>
<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
<head>
    <meta charset="utf-8">
    <title>Crowd platform</title>

    <meta content="IE=edge,chrome=1" http-equiv="X-UA-Compatible">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <%@ include file="/WEB-INF/jsp/style.jsp" %>

</head>

<body>
<%@ include file="/WEB-INF/jsp/header.jsp" %>

<ul class="breadcrumb">
    <li><a href="/">Home</a> <span class="divider">/</span></li>
    <li class="active">Reset password</li>
</ul>

<div class="container">
    <div class="row">
        <div class="span8 offset2">
            <div class="page-header">
                <h1>Crowd platform</h1>
            </div>

            <h2>Change password</h2>

            <form:form method="post" action="/reset" modelAttribute="passwordResetData">

                <c:set var="errors"><form:errors path="*"/></c:set>
                <c:if test="${not empty errors}">
                    <div class="alert alert-error">
                        <h4>Warning!</h4>
                        <p><spring:message code="password.change.error" /></p>
                    </div>
                </c:if>

                <fieldset>
                    <form:hidden path="uid" value="${uid}"/>
                    <table>
                        <tr>
                            <th><label for="password">Password:</label></th>
                            <td>
                                <form:input path="password" type="password" id="password" />
                                <div class="control-group error">
                                    <div class="controls">
                                        <form:errors path="password" cssClass="error help-inline" />
                                    </div>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <th><label for="confirm-password">Confirm password:</label></th>
                            <td>
                                <form:input path="confirmPassword" type="password" id="confirm-password" />
                                <div class="control-group error">
                                    <div class="controls">
                                        <form:errors path="confirmPassword" cssClass="error help-inline" />
                                    </div>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <th></th>
                            <td><input name="commit" type="submit" class="btn btn-primary" value="Register"/></td>
                        </tr>
                    </table>
                </fieldset>
                    
            </form:form>

            <script type="text/javascript">
                document.getElementById('password').focus();
            </script>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/footer.jsp" %>

</body>
</html>
