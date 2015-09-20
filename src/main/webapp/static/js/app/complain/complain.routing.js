TiaonaerApp.ComplainRouting = function(){
    var ComplainRouting = {};

    ComplainRouting.Router = Backbone.Marionette.AppRouter.extend({
        appRoutes: {
            "complain/list": "list"
        }
    });

    TiaonaerApp.addInitializer(function(){
        ComplainRouting.router = new ComplainRouting.Router({
            controller: TiaonaerApp.Controllers.ComplainController
        });
        TiaonaerApp.vent.trigger("routing:started");
    });

    return ComplainRouting;
}();