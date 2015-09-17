TiaonaerApp.Controllers.FavoriteController = {
    list: function() {
        console.log("list in FavoriteController");
        if (this.isAnonymousUser()) {
            TiaonaerApp.vent.trigger("user:login");
        } else {
            var jobListView = new TiaonaerApp.Views.JobListView();
            console.log("changePage list in JobController");
            TiaonaerApp.changePage(jobListView);
        }
    },
};
