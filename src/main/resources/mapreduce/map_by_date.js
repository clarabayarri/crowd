function() {
	var formatDate = function(d) {
		var dd = d.getDate();
		if ( dd < 10 ) dd = '0' + dd;
		var mm = d.getMonth()+1;
		if ( mm < 10 ) mm = '0' + mm;
		var yy = d.getFullYear();
		if ( yy < 10 ) yy = '0' + yy;
		return yy+'-'+mm+'-'+dd;
	}
	if (this.executions) {
		for (var i = 0; i < this.executions.length; ++i) {
			emit(formatDate(this.executions[i].date), 1);
		}
	}
}