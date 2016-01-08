App.ComplainRouting = function(){
    var ComplainRouting = {};

    ComplainRouting.Router = Backbone.Marionette.AppRouter.extend({
        appRoutes: {
            "complain/list": "list",
            "complain/adminlist": "adminlist"
        }
    });

    App.addInitializer(function(){
        ComplainRouting.router = new ComplainRouting.Router({
            controller: App.Controllers.ComplainController
        });
        App.vent.trigger("routing:started");
    });

    return ComplainRouting;
}();