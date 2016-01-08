App.JobRouting = function(){
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

    App.addInitializer(function(){
        JobRouting.router = new JobRouting.Router({
            controller: App.Controllers.JobController
        });

        App.vent.trigger("routing:started");
    });

    return JobRouting;
}();
