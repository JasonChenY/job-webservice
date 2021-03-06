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

App.Controllers.JobController = {
    list: function() {
        console.log("list in JobController");
        App.showView("JobListView");
    },
    view: function(id) {
        var job = new App.Models.Job({id: id});
        job.fetch({success:function() {
            App.showView("JobInfoView", job);
        }});
    },
    update: function(id) {
        if (App.isAnonymousUser()) {
            App.vent.trigger("user:login");
        }
        else {
            var updateJobView = new App.Views.UpdateJobView({id: id});
            App.mainRegion.show(updateJobView);
        }
    },

    search: function() {
        // Only registered user can use search function.
        //if (App.isAnonymousUser()) {
        //    App.vent.trigger("user:login");
        //} else {
            App.showView("JobSearchView");
        //}
    },

    // filter should again translate the QueryString to hash, then apply to paginator
    // we dont need authentication here and use dont need bookmark this.
    filter: function(filters) {
        console.log("Filter job entries:" + filters);
        // Only registered user can use search function.
        //if (App.isAnonymousUser()) {
            //App.vent.trigger("user:login");
        //} else {
            if ( App.filters ) {
                var jobListView = App.ViewContainer.findByCustom("JobListView");
                //jobListView.switchCollection(filters?parseQueryString(filters):null);
                jobListView.switchCollection(App.filters);
                App.filters = null;
            }
            App.showView("JobListView");
        //}
    }
};
