'use strict';

var bookmarksControllers = angular.module('bookmarksControllers',
		[ 'bookmarksServices' ]);

bookmarksControllers.controller('BookmarksCtrl', [ '$scope', 'Bookmark', 'Tag',
		function BookmarksCtrl($scope, Bookmark, Tag) {
			$scope.bookmarks = Bookmark.all();
			$scope.tags = Tag.all();
		} ]);

bookmarksControllers.controller('BookmarkEditCtrl', [ '$scope', '$routeParams',
		'Bookmark', function BookmarksCtrl($scope, $routeParams, Bookmark) {
			// var bookmarkId = $routeParams.bookmarkId;
			// $http.get('bookmarks/' + bookmarkId).success(function(data) {
			$scope.bookmark = Bookmark.one({
				'bookmarkId' : $routeParams.bookmarkId
			});
			// });
		} ]);