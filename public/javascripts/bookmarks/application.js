window.Bookmarks = Ember.Application.create();

Bookmarks.Adapter = DS.RESTAdapter.extend({
	bulkCommit : false
});

Bookmarks.Adapter.map('Bookmarks.Bookmark', {
	tags : {
		embedded : 'always'
	}
});