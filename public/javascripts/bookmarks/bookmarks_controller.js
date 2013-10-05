var TagsParser = (function() {
	
	var parse;
	
	parse = function(raw_text) {
		return raw_text.split(/\s*,\s*/i)
	}
	
	return {
		parse: parse
	};
})();

Bookmarks.BookmarksController = Ember.ArrayController.extend({
	actions : {
		create : function() {
			// Get the todo title set by the "New Todo" text field
			var link = this.get('link');
			var title = this.get('title');
			var tags = this.get('tags');
			var description = this.get('description');
			var that = this;
			
			
						// Create the new Todo model
			var bookmark = this.store.createRecord('bookmark', {
				link : link,
				title : title,
				description : description
			});
			
			
			TagsParser.parse(tags).forEach(function(e) {
				var tag = that.store.createRecord('tag', {"name": e});
				bookmark.get('tags').addObject(tag)
			})

			// reset
			this.set('link', '');
			this.set('title', '');
			this.set('tags', '');
			this.set('description', '');

			// Save the new model
			bookmark.save();
		}
	}
});