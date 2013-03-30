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
    <script type="text/javascript" src="http://twitter.github.com/bootstrap/assets/js/bootstrap-alert.js"></script>

</head>

<body>
<%@ include file="/WEB-INF/jsp/header.jsp" %>

<ul class="breadcrumb">
    <li><a href="/">Home</a> <span class="divider">/</span></li>
    <li><a href="/projects">Projects</a> <span class="divider">/</span></li>
    <li><a href="/project/${batch.project.id}">${batch.project.name}</a> <span class="divider">/</span></li>
    <li class="active">${batch.name}</li>
</ul>

<div class="container">
    <div class="row">
        <div class="span8 offset2">

            <c:if test="${created}">
                <div class="alert alert-success alert-block fade in">
                    <button type="button" class="close" data-dismiss="alert">&times;</button>
                    <h4>Success!</h4>
                    <p>Your batch was created. Please click the start button on your right to start providing these tasks.</p>
                </div>

                <div class="clear-fix"></div>
            </c:if>

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
            
            <div class="clear-fix"></div>

            <div class="page-header">
                <h1>Batch ${batch.name}</h1>
            </div>

            <div class="clear-fix"></div>

            <dl class="dl-horizontal">
                <dt>Tasks</dt>
                <dd>${batch.numTasks}</dd>
                <dt>Executions per task</dt>
                <dd>${batch.executionsPerTask}</dd>
                <dt>Date created</dt>
                <dd><fmt:formatDate value="${batch.creationDate}" type="date"/></dd>
                <dt>Completed</dt>
                <dd><fmt:formatNumber type="number" 
            maxFractionDigits="2" value="${batch.percentageComplete}" /> %</dd>
            </dl>
            <p>
                <c:choose>
                    <c:when test="${batch.state eq 'RUNNING'}">
                        <div class="progress progress-info progress-striped">
                            <div class="bar" style="width: ${batch.percentageComplete}%"></div>
                        </div>
                    </c:when>
                    <c:when test="${batch.state eq 'PAUSED'}">
                        <div class="progress progress-warning progress-striped">
                            <div class="bar" style="width: ${batch.percentageComplete}%"></div>
                        </div>
                    </c:when>
                    <c:when test="${batch.state eq 'COMPLETE'}">
                        <div class="progress progress-success progress-striped">
                            <div class="bar" style="width: ${batch.percentageComplete}%"></div>
                        </div>
                    </c:when>
                </c:choose>
            </p>

            <h3>Tasks</h3>

            <c:forEach items="${batch.orderedTasks}" var="task">
                <div class="well well-small">
                    <div class="extra-info">
                        <p><strong>Id: </strong>${task.id}</p>
                        <p><strong>Executions: </strong>${task.numExecutions}</p>
                        <p><strong>Contents:</p>
                        <dl class="dl-horizontal">
                            <c:forEach var="entry" items="${task.contentsMap}">
                                <dt><c:out value="${entry.key}"/></dt>
                                <dd><c:out value="${entry.value}"/></dd>
                            </c:forEach>
                        </dl>
                    </div>
                    
                </div>
            </c:forEach>
        </div>
    </div>
</div>

<script type="text/javascript">
    $(".alert").alert();
</script>

</body>
</html>
