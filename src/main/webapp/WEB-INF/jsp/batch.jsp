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
  <li><a href="/projects">Home</a> <span class="divider">/</span></li>
  <li><a href="/project/${batch.project.id}">${batch.project.name}</a> <span class="divider">/</span></li>
  <li class="active">${batch.name}</li>
</ul>

<div class="container">
    <div class="row">
        <div class="span8 offset2">
            <div class="pull-right">
                <p>
                <c:choose>
                    <c:when test="${batch.state eq 'RUNNING'}">
                        <a href="/project/${batch.project.id}/batch/${batch.id}/pause" class="btn btn-danger pull-right">Stop</a>
                    </c:when>
                    <c:when test="${batch.state eq 'PAUSED'}">
                        <a href="/project/${batch.project.id}/batch/${batch.id}/start" class="btn btn-success pull-right">Start</a>
                    </c:when>
                </c:choose>

                <c:choose>
                    <c:when test="${batch.state eq 'RUNNING'}">
                        <img src="/resources/img/status_green.png"/>
                    </c:when>
                    <c:when test="${batch.state eq 'PAUSED'}">
                        <img src="/resources/img/status_grey.png"/>
                    </c:when>
                    <c:when test="${batch.state eq 'COMPLETE'}">
                        <img src="/resources/img/status_blue.png"/>
                    </c:when>
                </c:choose>
                </p>
                <p><a href="/project/${batch.project.id}/batch/${batch.id}/download" class="btn btn-info">Download executions</a></p>

            </div>
            

            <div class="page-header">
                <h1>Batch ${batch.name}</h1>
            </div>

            <dl class="dl-horizontal">
                <dt>Tasks</dt>
                <dd>${batch.numTasks}</dd>
                <dt>Executions per task</dt>
                <dd>${batch.executionsPerTask}</dd>
                <dt>Completed</dt>
                <dd>${batch.percentageComplete} %</dd>
            </dl>

            <h3>Tasks</h3>

            <c:forEach items="${batch.orderedTasks}" var="task">
                <div class="well well-small">
                    <h4>${task.id}</h4>

                    <div class="extra-info">
                        <p><strong>Executions: </strong>${task.numExecutions}</p>
                        <p><strong>Contents: </strong>${task.contents}</p>
                    </div>
                    
                </div>
            </c:forEach>
        </div>
    </div>
</div>

</body>
</html>
