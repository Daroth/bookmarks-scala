Bookmarks.Bookmark = DS.Model.extend({
	link : DS.attr('string'),
	title : DS.attr('string'),
	tags : DS.attr('string'),
	description : DS.attr('string'),
	date : DS.attr('date')
});
