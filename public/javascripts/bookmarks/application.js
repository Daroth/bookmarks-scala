'use strict'; 

var bookmarksApp = angular.module('BookmarksApp', [ 'ngRoute',
		'bookmarksControllers', 'bookmarksServices' ]);

bookmarksApp.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/bookmarks', {
		templateUrl : 'partials/bookmarks-list',
		controller : 'BookmarksCtrl'
	}).when('/bookmarks/:bookmarkId', {
		templateUrl : 'partials/bookmark-edit',
		controller : 'BookmarkEditCtrl'
	}).otherwise({
		redirectTo : '/bookmarks'
	});
}]);