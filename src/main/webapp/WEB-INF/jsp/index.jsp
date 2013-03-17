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
    <li class="active">Home</li>
</ul>

<div class="container">
    <div class="row">
        <div class="span8 offset2">
            <div class="page-header">
                <h1>Crowd platform</h1>
            </div>

            <sec:authorize access="!isAuthenticated()">
                <section class="hero-unit">
                    <p>Welcome to Crowd Platform!</p>
                    <p>Register or log in to start managing crowdsourcing tasks :)</p>
                </section>

                <section>
                    <a href="/login" class="btn">Login</a>
                    <a href="/register" class="btn">Register</a>
                </section>
            </sec:authorize>

            <sec:authorize access="isAuthenticated()">
                <section class="hero-unit">
                    <p>Welcome <sec:authentication property="principal.username" />!</p>
                </section>

                <section>
                    <a href="/projects" class="btn">My projects</a>
                </section>
            </sec:authorize>
            
        </div>
    </div>
</div>

</body>
</html>
