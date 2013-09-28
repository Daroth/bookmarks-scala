Bookmarks.Router.map(function() {
	this.resource('bookmarks', {
		path : '/'
	});
});

Bookmarks.BookmarksRoute = Ember.Route.extend({
	model : function() {
		return this.store.find('bookmark');
	}
});