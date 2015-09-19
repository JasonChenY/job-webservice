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
    search: function(searchTerm) {
        window.log("Searching todo entries with search term:", searchTerm);
        if (TiaonaerApp.isAnonymousUser()) {
            TiaonaerApp.vent.trigger("user:login");
        }
        else {
            var searchResultView = new TiaonaerApp.Views.SearchResultView({searchTerm: searchTerm});
            TiaonaerApp.mainRegion.show(searchResultView);
        }
    }
};
