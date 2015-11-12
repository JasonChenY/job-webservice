TiaonaerApp.ErrorRouting = function(){
    var ErrorRouting = {};

    ErrorRouting.Router = Backbone.Marionette.AppRouter.extend({
        appRoutes: {
            "error/404": "404",
            "error/notAuthorized": "notAuthorized",
            "error/error": "error"
        }
    });

    TiaonaerApp.addInitializer(function(){
        ErrorRouting.router = new ErrorRouting.Router({
            controller: TiaonaerApp.Controllers.ErrorController
        });
    });

    return ErrorRouting;
}();

