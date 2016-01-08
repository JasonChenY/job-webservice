App.AppRouting = function() {
    var AppRouting = {};
    AppRouting.Router = Backbone.Marionette.AppRouter.extend({
        appRoutes: {
            "app/download": "app_download",
            "app/about": "app_about",
        }
    });

    App.addInitializer(function(){
        AppRouting.router = new AppRouting.Router({
            controller: App.Controllers.AppController
        });
        App.vent.trigger("routing:started");
    });

    return AppRouting;
}();