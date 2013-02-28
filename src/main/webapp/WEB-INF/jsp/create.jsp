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

<div class="container">
    <div class="row">
        <div class="span8 offset2">
            <div class="page-header">
                <h1>Create batch</h1>
            </div>


            <sf:form method="POST" modelAttribute="batch" action="/batches/create">
                <fieldset>
                    <table>
                        <tr>
                            <th><label for="batch_name">Batch name:</label></th>
                            <td><sf:input path="name" size="15" id="batch_name" /></td>
                        </tr>
                        <tr>
                            <th><label for="batch_num_exec">Number of executions:</label></th>
                            <td><sf:input path="executionsPerTask" size="15" id="batch_num_exec" />
                                <sf:errors path="executionsPerTask" cssClass="error" />
                            </td>
                        </tr>
                    </table>
                </fieldset>
            </sf:form>
        </div>
    </div>
</div>

</body>
</html>
