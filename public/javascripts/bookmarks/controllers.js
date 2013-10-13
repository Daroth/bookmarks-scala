'use strict';

var bookmarksControllers = angular.module('bookmarksControllers', []);

bookmarksControllers.controller('BookmarksCtrl', function BookmarksCtrl($scope,
		$http) {
	$http.get('bookmarks').success(function(data) {
		$scope.bookmarks = data;
	});

	$http.get('tags').success(function(data) {
		$scope.tags = TagsCloud.process(data);
	});
});

bookmarksControllers.controller('BookmarkEditCtrl', function BookmarksCtrl(
		$scope, $routeParams, $http) {
	var bookmarkId = $routeParams.bookmarkId;
	$http.get('bookmarks/' + bookmarkId).success(function(data) {
		$scope.bookmark = data;
	});
});