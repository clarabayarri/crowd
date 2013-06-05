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
    <script src="http://d3js.org/d3.v3.min.js"></script>

</head>

<body>
<%@ include file="/WEB-INF/jsp/header.jsp" %>

<ul class="breadcrumb">
    <li><a href="/">Home</a> <span class="divider">/</span></li>
    <li><a href="/projects">Projects</a> <span class="divider">/</span></li>
    <li><a href="/project/${project.id}">${project.name}</a> <span class="divider">/</span></li>
    <c:if test="${!(batch eq null)}">
        <li><a href="/project/${project.id}/batch/${batch.id}">${batch.name}</a> <span class="divider">/</span></li>
    </c:if>
    <li class="active">Graphs</li>
</ul>

<div class="container">
    <div class="row">
        <div class="span8 offset2">

            <div class="page-header">
                <h1>${project.name}</h1>
            </div>

            <div class="clear-fix"></div>

            <h2>Daily executions</h2>

            <div id="executions-graph">
                <c:choose>
                    <c:when test="${batch eq null}">
                        <c:set var="dataUrl" value="/project/${project.id}/data/field/date" />
                    </c:when>
                    <c:otherwise>
                        <c:set var="dataUrl" value="/project/${project.id}/batch/${batch.id}/data/field/date" />
                    </c:otherwise>
                </c:choose>
                <%@ include file="/resources/js/executionsGraph.html" %>
            </div>

            <c:forEach items="${project.outputFields}" var="field">
                <h3>${field.name}</h3>
                <c:choose>
                    <c:when test="${batch eq null}">
                        <c:set var="dataUrl" value="/project/${project.id}/data/field/${field.name}" />
                    </c:when>
                    <c:otherwise>
                        <c:set var="dataUrl" value="/project/${project.id}/batch/${batch.id}/data/field/${field.name}" />
                    </c:otherwise>
                </c:choose>
                <div id="${field.name}-graph">
                    <c:choose>
                        <c:when test="${field.type eq 'STRING' || field.type eq 'MULTIVALUATE_STRING'}">
                            <%@ include file="/resources/js/wordsGraph.html" %>
                        </c:when>
                        <c:when test="${field.type eq 'INTEGER'}">
                            <%@ include file="/resources/js/barGraph.html" %>
                        </c:when>
                    </c:choose>
                </div>
            </c:forEach>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/footer.jsp" %>

</body>
</html>
