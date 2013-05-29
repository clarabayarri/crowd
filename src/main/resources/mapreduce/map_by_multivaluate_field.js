function() {
	if (this.executions) {
		for (var i = 0; i < this.executions.length; ++i) {
			var val = this.executions[i].contents[fieldName];
			if (val != null) {
				for (var j = 0; j < val.length; ++j) {
					emit(val[j], 1);
				}
			}
		}
	}
}