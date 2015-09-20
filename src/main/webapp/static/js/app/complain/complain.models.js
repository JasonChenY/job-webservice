TiaonaerApp.Models.Complain = Backbone.Model.extend({
    urlRoot: "/api/complain"
});

TiaonaerApp.Collections.ComplainList = Backbone.PageableCollection.extend({
    model: TiaonaerApp.Models.Complain,
    url: "api/complain",
    state: {
        firstPage: 1,
        order: 1,
        pageSize: 10,
    },

    queryParams: {
        currentPage: "page.page",
        pageSize: "page.size",
        sortKey: "page.sort",
        order: "page.sort.dir",
    },

    parseState: function (resp, queryParams, state, options) {
        console.log("parseState called");
        return { totalRecords: resp.totalElements};
    },

    parseRecords: function (resp, options) {
        console.log("parseRecords called");
        return resp.complains;
    },
});