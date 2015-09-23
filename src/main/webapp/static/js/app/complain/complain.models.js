TiaonaerApp.Models.Complain = Backbone.Model.extend({
    urlRoot: "/api/complain",

    updateAttr: function(attrs) {
        this.save(attrs,
            {
                wait:true,
                success: function(model) {
                    console.log("save succeed");
                },
                fail: function(model) {
                    console.log("save fail");
                }
            });
    }
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

TiaonaerApp.Collections.ComplainAdminList = Backbone.PageableCollection.extend({
    model: TiaonaerApp.Models.Complain,
    url: "api/complain/admin",
    state: {
        firstPage: 1,
        order: 1,
        pageSize: 10,
        sortKey: "creationTime",
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