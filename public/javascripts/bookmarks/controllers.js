'use strict';

var bookmarksControllers = angular.module('bookmarksControllers',
		[ 'bookmarksServices' ]);

bookmarksControllers.controller('BookmarksCtrl', [ '$scope', 'Bookmark', 'Tag',
		function BookmarksCtrl($scope, Bookmark, Tag) {
			$scope.bookmarks = Bookmark.all();

			$scope.tags = Tag.all();

			$scope.resetCreationForm = function() {
				$scope.newBookmark = undefined;
			}
			$scope.saveCreationForm = function() {
				if ($scope.newBookmark) {
					var bookmark = {
						title : $scope.newBookmark.title,
						description : $scope.newBookmark.description,
						link : $scope.newBookmark.link,
						tags : (function parseTags(tags) {
							var ret;
							if (tags) {
								ret = tags.split(/\s*,\s*/)
							} else {
								ret = [];
							}
							return ret;
						})($scope.newBookmark.tags)
					}
					Bookmark.create(bookmark, function createBookmarkSuccess() {
						$scope.newBookmark = undefined;
						$scope.bookmarks = Bookmark.all();
						$scope.tags = Tag.all();
					}, function createBookmarkFail() {
						console.log("create bookmarks failed :", arguments);
					});
				}
			}
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