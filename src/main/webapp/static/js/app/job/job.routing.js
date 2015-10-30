TiaonaerApp.JobRouting = function(){
    var JobRouting = {};

    JobRouting.Router = Backbone.Marionette.AppRouter.extend({
        appRoutes: {
            "job/view/:id": "view",
            "job/update/:id": "update",
            "job/list": "list",
            "job/search": "search",
            "job/filter?:filters": "filter",
            "job/filter": "filter"
        }
    });

    TiaonaerApp.addInitializer(function(){
        JobRouting.router = new JobRouting.Router({
            controller: TiaonaerApp.Controllers.JobController
        });

        TiaonaerApp.vent.trigger("routing:started");
    });

    return JobRouting;
}();
