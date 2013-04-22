<!doctype html>
<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
<head>
    <meta charset="utf-8">
    <title>Crowd platform</title>

    <meta content="IE=edge,chrome=1" http-equiv="X-UA-Compatible">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <%@ include file="/WEB-INF/jsp/style.jsp" %>

    <script src="http://code.jquery.com/jquery-1.7.1.js"></script>
    <script type="text/javascript" src="/resources/js/bootstrap.min.js"></script>

</head>

<body>
<%@ include file="/WEB-INF/jsp/header.jsp" %>

<ul class="breadcrumb">
    <li><a href="/">Home</a> <span class="divider">/</span></li>
    <li><a href="/projects">Projects</a> <span class="divider">/</span></li>
    <li><a href="/project/${project.id}">${project.name}</a> <span class="divider">/</span></li>
    <li class="active">${batch.name}</li>
</ul>

<div id="confirm" class="modal hide fade">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h3>Are you sure?</h3>
    </div>
    <div class="modal-body">
        <p>All associated tasks and executions will be deleted.</p>
    </div>
    <div class="modal-footer">
        <button data-dismiss="modal" aria-hidden="true" class="btn">Cancel</button>
        <a href="/project/${project.id}/batch/${batch.id}/delete" class="btn btn-danger" id="delete-send">Delete</a>
    </div>
</div>

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

            <ul class="pull-right">
                <li>
                <c:choose>
                    <c:when test="${batch.state eq 'RUNNING'}">
                        <a href="/project/${project.id}/batch/${batch.id}/pause" class="btn btn-danger pull-right">Stop</a>
                    </c:when>
                    <c:when test="${batch.state eq 'PAUSED'}">
                        <a href="/project/${project.id}/batch/${batch.id}/start" class="btn btn-success pull-right">Start</a>
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
                </li>

                <li><a href="/project/${project.id}/batch/${batch.id}/download" class="btn btn-info">Download executions</a></li>

                <li><a href="/project/${project.id}/batch/${batch.id}/export" class="btn btn-info" target="_blank">Export to Fusiontables</a></li>

            </ul>

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
                        <p><strong>Contents:</strong></p>
                        <dl class="dl-horizontal">
                            <c:forEach var="entry" items="${task.contentsMap}">
                                <dt><c:out value="${entry.key}"/></dt>
                                <dd><c:out value="${entry.value}"/></dd>
                            </c:forEach>
                        </dl>
                        <c:set var="perc" value="${100*task.numExecutions / batch.executionsPerTask}" />
                        <div class="progress progress-info progress-striped">
                            <div class="bar" style="width: ${perc}%"></div>
                        </div>
                    </div>
                    
                </div>
            </c:forEach>

            <div>
                <a href="#confirm" data-toggle="modal" class="btn btn-danger pull-right">Delete batch</a>
                
            </div>

            <div class="clear-fix"></div>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/footer.jsp" %>

<script type="text/javascript">
    $(".alert").alert();
</script>
</body>
</html>
