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
<div class="navbar navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container">
            <a href="/" class="brand">Crowd platform</a>
            <a href="/" class="brand" id="heroku">by <strong>Clara</strong></a>
        </div>
    </div>
</div>

<ul class="breadcrumb">
  <li class="active">Home</li>
</ul>

<div class="container">
    <div class="row">
        <div class="span8 offset2">
            <div class="page-header">
                <h1>Crowd platform</h1>
            </div>


            <c:if  test="${!empty projectList}">
                <h2>Projects</h2>

                <c:forEach items="${projectList}" var="project">
                    <div class="well well-small">
                        <div class="pull-right">
                            <p><a href="/project/${project.id}" class="btn btn-info">Details</a></p>
                        </div>

                        <h4>${project.name}</h4>

                        <div class="clear-fix"></div>

                        
                        
                    </div>
                </c:forEach>
            </c:if>
        </div>
    </div>
</div>

<div class="span5 offset2">
    <a href="/sample/" class="btn btn-primary" type="button">Create sample data</a>
    <a href="/sample/clean" class="btn" type="button">Clean sample data</a>
</div>

</body>
</html>
