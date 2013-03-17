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
    <li><a href="/projects">Projects</a> <span class="divider">/</span></li>
    <li class="active">${project.name}</li>
</ul>

<div class="container">
    <div class="row">
        <div class="span8 offset2">
            <div class="pull-right">
                <a href="/project/${project.id}/batch/new" class="btn">New batch</a>
            </div>

            <div class="page-header">
                <h1>${project.name}</h1>
            </div>

            <div class="clear-fix"></div>

            <h3>Input fields</h3>
            <dl class="dl-horizontal">
                <c:forEach items="${project.orderedInputFields}" var="field">
                    <dt>${field.name}</dt>
                    <dd>${field.type}</dd>
                </c:forEach>
            </dl>

            <c:if  test="${!empty project.batches}">
                <h2>Batches</h2>

                <c:forEach items="${project.orderedBatches}" var="batch">
                    <div class="well well-small">
                        <c:choose>
                            <c:when test="${batch.state eq 'RUNNING'}">
                                <img src="/resources/img/status_green.png" class="pull-right"/>
                            </c:when>
                            <c:when test="${batch.state eq 'PAUSED'}">
                                <img src="/resources/img/status_grey.png" class="pull-right"/>
                            </c:when>
                            <c:when test="${batch.state eq 'COMPLETE'}">
                                <img src="/resources/img/status_blue.png" class="pull-right"/>
                            </c:when>
                        </c:choose>
                        
                        

                        <h4>${batch.name}</h4>

                        <div class="clear-fix"></div>

                        <div class="pull-right">
                            <a href="/project/${project.id}/batch/${batch.id}" class="btn btn-info">Details</a>
                        </div>

                        <div class="extra-info">
                            <p><strong>Tasks: </strong>${batch.numTasks}</p>
                            <p><strong>Executions per task: </strong>${batch.executionsPerTask}</p>
                            <p><strong>Completed: </strong>${batch.percentageComplete} %</p>
                        </div>
                        
                    </div>
                </c:forEach>
            </c:if>
        </div>
    </div>
</div>

</body>
</html>
