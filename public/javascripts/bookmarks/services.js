var bookmarksServices = angular.module('bookmarksServices', [ 'ngResource' ]);

bookmarksServices.factory('Bookmark', [ '$resource', function($resource) {
	return $resource('/bookmarks/:bookmarkId', {}, {
		all : {
			method : 'GET',
			isArray : true
		},
		one : {
			method : 'GET',
			params : {
				bookmarkId : ""
			}
		},
		create : {
			method : 'POST'
		}
	});
} ]);

bookmarksServices.factory('Tag', [ '$resource', function($resource) {
	return $resource('/tags', {}, {
		all : {
			method : 'GET',
			isArray : true
		}
	});
} ]);