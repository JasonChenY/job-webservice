TiaonaerApp.Models.Job = Backbone.Model.extend({
    urlRoot: "/api/job"
});

TiaonaerApp.Collections.JobList = Backbone.PageableCollection.extend({
    model: TiaonaerApp.Models.Job,

    url: "api/job",
    /*url: function() {
        return "api/job?q=*:*&job_type:0&job_expired:false&facet=true&facet.field=job_company&facet.field=job_location&facet.mincount=1&sort=job_post_date+desc&wt=json&indent=true";
    },*/
    state: {
        firstPage: 1,
        order: 1,
        pageSize: 10,
        sortKey: "job_post_date",
    },

    queryParams: {
        currentPage: "page.page",
        pageSize: "page.size",
        sortKey: "page.sort",
        order: "page.sort.dir",
        facet: true,
        /* paginator has problem with multiple facet fields, this is done by service
        q: "*:*",
        "facet.field": "job_company",
        "facet.field": "job_location",
        "facet.mincount": 1,
        wt: "json",
        indent: true
        */
    },

    parseState: function (resp, queryParams, state, options) {
        console.log("parseState called");
        return { totalRecords: resp.totalElements};
    },

    parseRecords: function (resp, options) {
        console.log("parseRecords called");
        return resp.jobs;
    },
});
