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
    <li class="active">${project.name}</li>
</ul>

<div id="confirm" class="modal hide fade">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h3>Are you sure?</h3>
    </div>
    <div class="modal-body">
        <p>All associated batches, users, tasks and executions will be deleted.</p>
    </div>
    <div class="modal-footer">
        <button data-dismiss="modal" aria-hidden="true" class="btn">Cancel</button>
        <a href="/project/${project.id}/delete" class="btn btn-danger" id="delete-send">Delete</a>
    </div>
</div>

<div class="container">
    <div class="row">
        <div class="span8 offset2">
            <div class="pull-right">
                <a href="/project/${project.id}/batch/create" class="btn">New batch</a>
            </div>

            <div class="page-header">
                <h1>${project.name}</h1>
            </div>

            <div class="clear-fix"></div>

            <h2>Project data</h2>

            <div class="tabbable">
                <ul id="tab-nav" class="nav nav-tabs">
                    <li class="active"><a data-toggle="tab" href="#general">General</a></li>
                    <li><a data-toggle="tab" href="#input">Input fields</a></li>
                    <li><a data-toggle="tab" href="#output">Output fields</a></li>
                    <li><a data-toggle="tab" href="#security">Security</a></li>
                </ul>

                <div class="tab-content">
                    <div class="tab-pane active" id="general">
                        <dl class="dl-horizontal">
                            <dt>Date created</dt>
                            <dd><fmt:formatDate value="${project.creationDate}" type="date"/></dd>
                            <dt>Registered users</dt>
                            <dd>${project.numUsers}</dd>
                        </dl>
                    </div>
                    <div class="tab-pane" id="input">
                        <dl class="dl-horizontal">
                            <c:forEach items="${project.orderedInputFields}" var="field">
                                <dt>${field.name}</dt>
                                <dd>${field.type}</dd>
                            </c:forEach>
                        </dl>
                    </div>
                    <div class="tab-pane" id="output">
                        <dl class="dl-horizontal">
                            <c:forEach items="${project.orderedOutputFields}" var="field">
                                <dt>${field.name}</dt>
                                <dd>${field.type}</dd>
                            </c:forEach>
                        </dl>
                    </div>
                    <div class="tab-pane" id="security">
                        <dl class="dl-horizontal">
                            <dt>Project id</dt>
                            <dd>${project.id}</dd>
                            <dt>Security code</dt>
                            <dd>${project.uid} <a href="/project/${project.id}/resetUID" class="btn btn-mini btn-danger">reset</a></dd>
                        </dl>
                    </div>
                </div>
            </div>

            <script>
                $('#general a').click(function (e) {
                    e.preventDefault();
                    $(this).tab('show');
                })
                $('#input a').click(function (e) {
                    e.preventDefault();
                    $(this).tab('show');
                })
                $('#output a').click(function (e) {
                    e.preventDefault();
                    $(this).tab('show');
                })
            </script>

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
                            <p><strong>Completed: </strong><fmt:formatNumber type="number" 
            maxFractionDigits="2" value="${batch.percentageComplete}" /> %</p>
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
                        </div>
                        
                    </div>
                </c:forEach>
            </c:if>

            <a href="#confirm" data-toggle="modal" class="btn btn-danger pull-right">Delete project</a>

            <div class="clear-fix"></div>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/footer.jsp" %>

</body>
</html>
