window.Bookmarks = Ember.Application.create();

Bookmarks.Store = DS.Store.extend({
    revision : 12,
    adapter : DS.RESTAdapter.extend({
        
    })
});