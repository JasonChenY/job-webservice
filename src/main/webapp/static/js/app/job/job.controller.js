var parseQueryString = function (url) {
   /*
   var reg = /([^\?\=\&]+)\=([^\?\=\&]*)/g;
   var obj = {};
   while (reg.exec (url)) {
       obj[RegExp.$1] = RegExp.$2;
   }
   return obj;
   */

   // Due to stupid job_company name with invalid '&', manully decode it.
   var obj = {};
   var keys = [];
   var pos = [];
   var reg = /([a-zA-Z]+)\=/g;
   var first=true;
   while ( reg.exec(url) ) {
       keys.push(RegExp.$1);

       if ( first ) {
           pos.push(url.indexOf(RegExp.$1 + "="));
           first = false;
       } else {
           pos.push(url.indexOf("&" + RegExp.$1 + "=") + 1);
       }
   };

   for ( var i = 0; i < keys.length; i++ ) {
       obj[keys[i]] = url.substring(pos[i]+keys[i].length+1, (i<keys.length-1)?(pos[i+1]-1):url.length);
   };

   return obj;
};

TiaonaerApp.Controllers.JobController = {
/*
    add: function() {
        window.log("Rendering add todo view.");
        if (this.isAnonymousUser()) {
            TiaonaerApp.vent.trigger("user:login");
        }
        else {
            var addTodoView = new TiaonaerApp.Views.AddTodoView();
            TiaonaerApp.mainRegion.show(addTodoView);
        }
    },
*/
    list: function() {
        console.log("list in JobController");
        if ( TiaonaerApp.ViewInstances.JobListView === undefined ) {
            console.log("create & show JobListView");
            TiaonaerApp.ViewInstances.JobListView = new TiaonaerApp.Views.JobListView();
            //TiaonaerApp.mainRegion.show(jobListView);
            TiaonaerApp.showPage(TiaonaerApp.ViewInstances.JobListView, true);
        } else {
            console.log("show JobListView");
            //TiaonaerApp.ViewInstances.JobListView.render();
            //$.mobile.changePage($('#joblist_page'));
            TiaonaerApp.showPage(TiaonaerApp.ViewInstances.JobListView, false);
        }
    },
    view: function(id) {
        window.log("Rendering view page for todo entry with id: ", id);
        if (TiaonaerApp.isAnonymousUser()) {
            TiaonaerApp.vent.trigger("user:login");
        }
        else {
            var viewJobView = new TiaonaerApp.Views.ViewJobView({id: id});
            TiaonaerApp.mainRegion.show(viewJobView);
        }
    },

    update: function(id) {
        window.log("Rendering update todo view.")
        if (TiaonaerApp.isAnonymousUser()) {
            TiaonaerApp.vent.trigger("user:login");
        }
        else {
            var updateJobView = new TiaonaerApp.Views.UpdateJobView({id: id});
            TiaonaerApp.mainRegion.show(updateJobView);
        }
    },

    search: function() {
        // Only registered user can use search function.
        if (TiaonaerApp.isAnonymousUser()) {
            TiaonaerApp.vent.trigger("user:login");
        } else {
            if ( TiaonaerApp.ViewInstances.JobSearchView === undefined ) {
                console.log("create & show JobSearchView");
                TiaonaerApp.ViewInstances.JobSearchView = new TiaonaerApp.Views.JobSearchView();
                TiaonaerApp.showPage(TiaonaerApp.ViewInstances.JobSearchView, true);
            } else {
                console.log("show JobSearchView");
                TiaonaerApp.showPage(TiaonaerApp.ViewInstances.JobSearchView, false);
            }
        }
    },

    // filter should again translate the QueryString to hash, then apply to paginator
    // we dont need authentication here and use dont need bookmark this.
    filter: function(filters) {
        console.log("Filter job entries:" + filters);
        if (TiaonaerApp.isAnonymousUser()) {
            TiaonaerApp.vent.trigger("user:login");
        } else {
            if ( TiaonaerApp.ViewInstances.JobListView === undefined ) {
                console.log("create & show JobListView");
                TiaonaerApp.ViewInstances.JobListView = new TiaonaerApp.Views.JobListView();
                TiaonaerApp.showPage(TiaonaerApp.ViewInstances.JobListView, true);
            }
            TiaonaerApp.ViewInstances.JobListView.updateCollection(parseQueryString(filters));
            TiaonaerApp.showPage(TiaonaerApp.ViewInstances.JobListView, false);
        }
    },
};
