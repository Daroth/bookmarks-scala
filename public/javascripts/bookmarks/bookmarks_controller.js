Bookmarks.BookmarksController = Ember.ArrayController.extend({
	actions : {
		create : function() {
			// Get the todo title set by the "New Todo" text field
			var link = this.get('link');
			var title = this.get('title');
			var tags = this.get('tags');
			var description = this.get('description');
			if (!title.trim()) {
				return;
			}

			// Create the new Todo model
			var bookmark = this.store.createRecord('bookmark', {
				link : link,
				title : title,
				tags : tags,
				description : description
			});

			// Clear the "New Todo" text field
			this.set('newTitle', '');

			this.set('link', '');
			this.set('title', '');
			this.set('tags', '');
			this.set('description', '');

			// Save the new model
			bookmark.save();
		}
	}
});