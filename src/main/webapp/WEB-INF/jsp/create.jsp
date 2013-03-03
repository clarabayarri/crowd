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


    <link href="/resources/css/bootstrap-fileupload.min.css" rel="stylesheet">
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

            <form:form method="POST" modelAttribute="batch" action="/batches/create" enctype="multipart/form-data">
                <div class="control-group error">
                    <div class="controls">
                        <form:errors path="*" cssClass="error help-inline" />
                    </div>
                </div>

                <fieldset>
                    <table>
                        <tr>
                            <th><label for="batch_name">Batch name:</label></th>
                            <td>
                                <form:input path="name" size="15" id="batch_name" />
                            </td>
                        </tr>
                        <tr>
                            <th><label for="batch_num_exec">Number of executions:</label></th>
                            <td>
                                <form:input path="executionsPerTask" size="15" id="batch_num_exec" />
                            </td>
                        </tr>
                        <tr>
                            <th><label for="batch_task_file">Task file:</label></th>
                            <td>
                                <div class="fileupload fileupload-new" data-provides="fileupload" id="batch_task_file">
                                    <div class="input-append">
                                        <div class="uneditable-input span3"><i class="icon-file fileupload-exists"></i> <span class="fileupload-preview"></span></div><span class="btn btn-file"><span class="fileupload-new">Select file</span><span class="fileupload-exists">Change</span><input type="file" name="taskFile" /></span><a href="#" class="btn fileupload-exists" data-dismiss="fileupload">Remove</a>
                                    </div>
                                </div>
                            </td>
                        </tr>
                    </table>
                </fieldset>
                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">Save</button>
                    </div>
            </form:form>
        </div>
    </div>
</div>


<script type="text/javascript" src="http://code.jquery.com/jquery.js"></script>
<script type="text/javascript" src="/resources/js/bootstrap-fileupload.min.js"></script>
</body>
</html>
