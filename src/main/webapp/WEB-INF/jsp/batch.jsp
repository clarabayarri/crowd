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

    <script type="text/javascript" src="https://apis.google.com/js/client.js"></script>
    <script type="text/javascript">
      //var CLIENT_ID = '584658910433-o8r4lo2art5fgji23o52ffmlv7ell173.apps.googleusercontent.com';
      var CLIENT_ID = '584658910433-nbp0k67iof7lc1dn88a0frfck1gd78aa.apps.googleusercontent.com';
      var SCOPES = [
          'https://www.googleapis.com/auth/drive.file',
          'https://www.googleapis.com/auth/fusiontables',
          // Add other scopes needed by your application.
        ];

      /**
       * Check if the current user has authorized the application.
       */
      function checkAuth() {
        gapi.auth.authorize(
            {'client_id': CLIENT_ID, 'scope': SCOPES.join(' '), 'immediate': true},
            handleAuthResult);
      }

      /**
       * Called when authorization server replies.
       *
       * @param {Object} authResult Authorization result.
       */
      function handleAuthResult(authResult) {
        if (authResult) {
          // Access token has been successfully retrieved, requests can be sent to the API
          $.post("/project/${project.id}/batch/${batch.id}/export", authResult.access_token, function(result) {
            window.location = result;
          });
        } else {
          // No access token could be retrieved, force the authorization flow.
          gapi.auth.authorize(
              {'client_id': CLIENT_ID, 'scope': SCOPES, 'immediate': false},
              handleAuthResult);
        }
      }
    </script>



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
            <c:if test="${! (created eq null)}">
                <div class="alert alert-success alert-block fade in">
                    <button type="button" class="close" data-dismiss="alert">&times;</button>
                    <h4>Success!</h4>
                    <p>Your batch was created. Please click the start button on your right to start providing these tasks.</p>
                </div>

                <div class="clear-fix"></div>
            </c:if>

            <div class="page-header">
                <div>
                    <p>
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
                                <img src="/resources/img/status_green.png" class="pull-right"/>
                            </c:when>
                            <c:when test="${batch.state eq 'PAUSED'}">
                                <img src="/resources/img/status_grey.png" class="pull-right"/>
                            </c:when>
                            <c:when test="${batch.state eq 'COMPLETE'}">
                                <img src="/resources/img/status_blue.png" class="pull-right"/>
                            </c:when>
                        </c:choose>
                    </p>
                    <div class="clearfix"></div>
                    
 
                </div>
                <div class="clearfix"></div>
                <div class="pull-right">
                    <p><a href="/project/${project.id}/batch/${batch.id}/download" class="btn btn-info pull-right">Download executions</a></p>
                    <p><button onclick="checkAuth()" href="/project/${project.id}/batch/${batch.id}/export" class="btn btn-info pull-right" target="_blank" id="export-link">Export to Fusiontables</button></p>
                    <p><a href="/project/${project.id}/batch/${batch.id}/graphs" class="btn btn-info pull-right">View graphs</a></p>
                    <div class="clearfix"></div>
                </div>
                
                <h1>Batch ${batch.name}</h1>
            </div>

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

            <c:forEach items="${batch.tasks}" var="task">
                <div class="well well-small">
                    <div class="extra-info">
                        <p><strong>Id: </strong>${task.id}</p>
                        <p><strong>Executions: </strong>${task.numExecutions}</p>
                        <p><strong>Contents:</strong></p>
                        <dl class="dl-horizontal">
                            <c:forEach var="entry" items="${task.contents}">
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
