TiaonaerApp.Models.Complain = Backbone.Model.extend({
    urlRoot: TiaonaerApp.ServiceUrl + "/" + "api/complain",

    updateAttr: function(attrs) {
        this.save(attrs,
            {
                wait:true,
                xhrFields: {
                    withCredentials: true
                },
                crossDomain: true,
                beforeSend: function(xhr) {
                    if ( isMobile() ) {
                        xhr.setRequestHeader("Origin",TiaonaerApp.ServerHost);
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

TiaonaerApp.Collections.ComplainList = Backbone.PageableCollection.extend({
    model: TiaonaerApp.Models.Complain,
    url: TiaonaerApp.ServiceUrl + "/" + "api/complain",
    state: {
        firstPage: 1,
        order: 1,
        pageSize: 10
    },

    queryParams: {
        currentPage: "page.page",
        pageSize: "page.size",
        sortKey: "page.sort",
        order: "page.sort.dir"
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

TiaonaerApp.Collections.ComplainAdminList = Backbone.PageableCollection.extend({
    model: TiaonaerApp.Models.Complain,
    url: TiaonaerApp.ServiceUrl + "/" + "api/complain/admin",
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
    }
});