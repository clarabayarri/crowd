<div class="navbar navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container">
            <a href="/" class="brand">Crowd platform</a>

            <a href="http://www.clarabayarri.com" class="brand" id="clara">by Clara</a>
            
            <sec:authorize access="isAuthenticated()">
            	<a href="/static/j_spring_security_logout" class=" btn btn-small pull-right">Logout</a>
        	</sec:authorize>
        </div>
    </div>
</div>