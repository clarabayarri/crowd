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
                    <a href="/login" class="btn btn-primary">Login</a>
                    <a href="/register" class="btn btn-info">Register</a>
                </section>
            </sec:authorize>

            <sec:authorize access="isAuthenticated()">
                <section class="hero-unit">
                    <p>Welcome <sec:authentication property="principal.username" />!</p>
                </section>

                <section>
                    <a href="/projects" class="btn btn-info">My projects</a>
                </section>
            </sec:authorize>
            
        </div>
    </div>
</div>

</body>
</html>
