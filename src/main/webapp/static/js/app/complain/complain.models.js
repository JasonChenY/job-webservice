App.Models.Complain = Backbone.Model.extend({
    urlRoot: App.ServiceUrl + "/" + "api/complain",

    updateAttr: function(attrs) {
        this.save(attrs,
            {
                wait:true,
                xhrFields: {
                    withCredentials: true
                },
                crossDomain: true,
                beforeSend: function(xhr) {
                    if ( isCordovaApp() ) {
                        xhr.setRequestHeader("Origin",App.ServerHost);
                    }
                },
                success: function(model) {
                    console.log("save succeed");
                },
                fail: function(model) {
                    console.log("save fail");
                }
            });
    }
});

App.Collections.ComplainList = Backbone.PageableCollection.extend({
    model: App.Models.Complain,
    url: App.ServiceUrl + "/" + "api/complain",
    state: {
        firstPage: 1,
        order: 1,
        pageSize: 10,
        sortKey: "creationTime"
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
        return resp.complains;
    }
});

App.Collections.ComplainAdminList = Backbone.PageableCollection.extend({
    model: App.Models.Complain,
    url: App.ServiceUrl + "/" + "api/complain/admin",
    state: {
        firstPage: 1,
        order: 1,
        pageSize: 10,
        sortKey: "creationTime"
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
        return resp.complains;
    }
});