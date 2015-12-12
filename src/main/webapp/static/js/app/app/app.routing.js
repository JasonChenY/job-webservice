TiaonaerApp.AppRouting = function() {
    var AppRouting = {};
    AppRouting.Router = Backbone.Marionette.AppRouter.extend({
        appRoutes: {
            "app/download": "app_download",
            "app/about": "app_about",
        }
    });

    TiaonaerApp.addInitializer(function(){
        AppRouting.router = new AppRouting.Router({
            controller: TiaonaerApp.Controllers.AppController
        });
        TiaonaerApp.vent.trigger("routing:started");
    });

    return AppRouting;
}();