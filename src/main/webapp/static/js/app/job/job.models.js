TiaonaerApp.Models.Job = Backbone.Model.extend({
    urlRoot: TiaonaerApp.ServiceUrl + "/" + "api/job",

    toggle_favorite: function() {
        var self = this;
        if ( self.get("favorite_id") !== 0 ) {
            console.log("unfavorite job " + self.get("id"));
            //var encoded = encodeURIComponent(self.model.get("id"));
            //console.log(encoded);
            var favorite = new TiaonaerApp.Models.Favorite({id: self.get("favorite_id")});
            favorite.destroy({
                wait:true,
                xhrFields: {
                    withCredentials: true
                },
                crossDomain: true,
                beforeSend: function(xhr) {
                    if ( isCordovaApp() ) {
                        xhr.setRequestHeader("Origin",TiaonaerApp.ServerHost);
                    }
                },
                success: function(favorite) {
                    self.set({
                        favorite_id: 0,
                        favorities_num: self.get("favorities_num")-1
                    });
                },
                error: function(data, status, xhr) {
                    alert(status.responseText);
                }
            });
        } else {
            console.log("favorite job " + self.get("id"));
            var attrs = { job_id: self.get("id") };
            var favorite = new TiaonaerApp.Models.Favorite();
            favorite.save(attrs, {
                wait: true,
                /* for mobile app where default origin: file:// */
                xhrFields: {
                    withCredentials: true
                },
                crossDomain: true,
                beforeSend: function(xhr) {
                    if ( isCordovaApp() ) {
                        //tomcat will reject with 403 for 'Origin: file://'
                        xhr.setRequestHeader("Origin",TiaonaerApp.ServerHost);
                    }
                },
                success: function (favorite) {
                    self.set({
                        favorite_id: favorite.get("id"),
                        favorities_num: self.get("favorities_num")+1
                    });
                       /* in theory, here we can let view listen to model change
                        * or call view.render(); direclty to reender only one item.
                        * but JQM not work perfectly, we let listview to listen to collection change event.
                        */
                },
                error: function(data, status, xhr) {
                    alert(status.responseText);
                }
            });
        }
    },
    complain: function(complain_type) {
        console.log("complain_type: " + complain_type);
        var self = this;
        if ( self.get("complain_pending") ) {
            console.log("job has pending complain " + self.get("id"));
        } else {
            console.log("complain job " + self.get("id"));
            var attrs = {
                job_id: self.get("id"),
                type: complain_type
            };
            var complain = new TiaonaerApp.Models.Complain();
            complain.save(attrs, {
                wait: true,
                xhrFields: {
                    withCredentials: true
                },
                crossDomain: true,
                beforeSend: function(xhr) {
                   //tomcat will reject with 403 for 'Origin: file://'
                   xhr.setRequestHeader("Origin", TiaonaerApp.ServerHost);
                },
                success: function (complain) {
                    self.set({
                        complain_pending: true
                    });
                },
                error: function(data, status, xhr) {
                    if ( status.responseText === 'You have complained about this job before' ) {
                        alert('不可以重复投诉同一职位!');
                    } else {
                        alert(status.responseText);
                    }
                }
            });
        }
    }
});

TiaonaerApp.Collections.JobList = Backbone.PageableCollection.extend({
    model: TiaonaerApp.Models.Job,

    url: TiaonaerApp.ServiceUrl + "/" + "api/job",
    /*url: function() {
        return "api/job?q=*:*&job_type:0&job_expired:false&facet=true&facet.field=job_company&facet.field=job_location&facet.mincount=1&sort=job_post_date+desc&wt=json&indent=true";
    },*/
    state: {
        firstPage: 1,
        order: 1,
        pageSize: 10,
        sortKey: "job_post_date"
    },

    queryParams: {
        currentPage: "page.page",
        pageSize: "page.size",
        sortKey: "page.sort",
        order: "page.sort.dir",
        fq: "job_expired:false"
        //facet: true,
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
    }
});
