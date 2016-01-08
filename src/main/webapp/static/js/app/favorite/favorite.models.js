App.Models.Favorite = Backbone.Model.extend({
    urlRoot: App.ServiceUrl + "/" + "api/favorite"
});

App.Collections.FavoriteList = Backbone.PageableCollection.extend({
    model: App.Models.Favorite,
    url: App.ServiceUrl + "/" + "api/favorite",
    state: {
        firstPage: 1,
        order: 1,
        pageSize: 10,
        sortKey: "id"
    },

    queryParams: {
        currentPage: "page.page",
        pageSize: "page.size",
        sortKey: "page.sort",
        order: "page.sort.dir",
        directions: {"-1": "asc", "1": "desc"}
    },

    parseState: function (resp, queryParams, state, options) {
        console.log("parseState called");
        return { totalRecords: resp.totalElements};
    },

    parseRecords: function (resp, options) {
        console.log("parseRecords called");
        return resp.favorites;
    }
});