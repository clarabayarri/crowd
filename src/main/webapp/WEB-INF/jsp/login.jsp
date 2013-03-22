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

</head>

<body>
<%@ include file="/WEB-INF/jsp/header.jsp" %>

<ul class="breadcrumb">
    <li><a href="/">Home</a> <span class="divider">/</span></li>
    <li class="active">Login</li>
</ul>

<div class="container">
    <div class="row">
        <div class="span8 offset2">
            <div class="page-header">
                <h1>Crowd platform</h1>
            </div>

            <h2>Sign in</h2>

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
                                <small><a href="/account/resend_password">Forgot?</a></small>
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
            </script>
        </div>
    </div>
</div>

</body>
</html>
