Ember.Handlebars.helper('dateFormat', function(value, options) {
	return moment(value).format("DD/MM/YYYY hh:mm:ss");
});