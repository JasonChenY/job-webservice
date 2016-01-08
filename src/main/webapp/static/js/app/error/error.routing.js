App.ErrorRouting = function(){
    var ErrorRouting = {};

    ErrorRouting.Router = Backbone.Marionette.AppRouter.extend({
        appRoutes: {
            "error/404": "404",
            "error/notAuthorized": "notAuthorized",
            "error/error": "error"
        }
    });

    App.addInitializer(function(){
        ErrorRouting.router = new ErrorRouting.Router({
            controller: App.Controllers.ErrorController
        });
    });

    return ErrorRouting;
}();

