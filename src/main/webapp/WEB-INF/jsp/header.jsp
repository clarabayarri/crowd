<div class="navbar navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container">
            <a href="/" class="brand">Crowd platform</a>

            <sec:authorize access="isAuthenticated()">
            	<a href="/static/j_spring_security_logout" class="pull-right">Logout</a>
        	</sec:authorize>
            <a href="/" class="brand" id="heroku">by <strong>Clara</strong></a>
        </div>
    </div>
</div>