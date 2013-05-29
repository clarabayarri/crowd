function() {
	if (this.executions) {
		for (var i = 0; i < this.executions.length; ++i) {
			var val = this.executions[i].contents[fieldName];
			if (val != null) {
				emit(val, 1);
			}
		}
	}
}