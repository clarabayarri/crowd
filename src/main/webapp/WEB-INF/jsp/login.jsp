<!doctype html>
<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
<head>
    <meta charset="utf-8">
    <title>Crowd platform</title>

    <meta content="IE=edge,chrome=1" http-equiv="X-UA-Compatible">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link href="http://twitter.github.com/bootstrap/assets/css/bootstrap.css" rel="stylesheet">
    <link href="http://twitter.github.com/bootstrap/assets/css/bootstrap-responsive.css" rel="stylesheet">

    <!--
      IMPORTANT:
      This is Heroku specific styling. Remove to customize.
    -->
    <link href="http://heroku.github.com/template-app-bootstrap/heroku.css" rel="stylesheet">
    <!-- /// -->

    <script src="http://code.jquery.com/jquery-1.7.1.js"></script>
    <script type="text/javascript" src="http://twitter.github.com/bootstrap/assets/js/bootstrap-modal.js"></script>
    <script type="text/javascript" src="http://twitter.github.com/bootstrap/assets/js/bootstrap-alert.js"></script>

</head>

<body>
<%@ include file="/WEB-INF/jsp/header.jsp" %>

<ul class="breadcrumb">
    <li><a href="/">Home</a> <span class="divider">/</span></li>
    <li class="active">Login</li>
</ul>

<div id="forgot" class="modal hide fade">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h3>Forgot password?</h3>
    </div>
    <form action="/forgot" id="forgot-form" method="post">
        <div class="modal-body">
            <table>
                <tr>
                    <th><label for="forgot-username">Username or email:</label></th>
                    <td><input id="forgot-username" name="username" type="text" /></td>
                </tr>
            </table>
        </div>
        <div class="modal-footer">
            <button data-dismiss="modal" aria-hidden="true" class="btn">Close</button>
            <button data-dismiss="modal" data-toggle="alert" class="btn btn-primary" id="forgot-send">Send reset email</button>
        </div>
    </form>
</div>

<div class="container">
    <div class="row">
        <div class="span8 offset2">
            <div class="page-header">
                <h1>Crowd platform</h1>
            </div>

            <h2>Sign in</h2>

            <div class="alert alert-success fade in hide" id="forgot-confirm" data-alert="alert">
                <button type="button" class="close" data-dismiss="alert">&times;</button>
                <strong>Success!</strong> An email has been sent to the email associated to your account with instructions on how to change your password.
            </div>

            <c:if test="${error}">
                <div class="alert alert-error" data-alert="alert">
                    <button type="button" class="close" data-dismiss="alert">&times;</button>
                    <h4>Error!</h4> 
                    <p>There was an error with your login, please try again.</p>
                </div>
            </c:if>

            <form method="post" action="/static/j_spring_security_check">
                <fieldset>
                    <table>
                        <tr>
                            <th><label for="username_or_email">Username or email:</label></th>
                            <td>
                                <input id="username_or_email" name="j_username" type="text" />
                            </td>
                        </tr>
                        <tr>
                            <th><label for="password">Password:</label></th>
                            <td>
                                <input name="j_password" type="password" id="password" />
                                <small><a href="#forgot" data-toggle="modal">Forgot?</a></small>
                            </td>
                        </tr>
                        <tr>
                            <th><label for="remember_me">Remember me</label></th>
                            <td>
                                <input name="_spring_security_remember_me" type="checkbox" id="remember_me" />
                            </td>
                        </tr>
                        <tr>
                            <th></th>
                            <td><input name="commit" type="submit" class="btn btn-primary" value="Sign In"/></td>
                    </div>
                        </tr>
                    </table>
                </fieldset>
                    
            </form>

            <script type="text/javascript">
                document.getElementById('username_or_email').focus();
                $("#forgot-send").click(function() {
                    $.post("/forgot", $("#forgot-form").serialize());
                    $("#forgot-confirm").removeClass("hide");
                });
                $(".alert").alert();
            </script>
        </div>
    </div>
</div>

</body>
</html>
