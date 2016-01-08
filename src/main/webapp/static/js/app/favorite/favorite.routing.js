App.FavoriteRouting = function(){
    var FavoriteRouting = {};

    FavoriteRouting.Router = Backbone.Marionette.AppRouter.extend({
        appRoutes: {
            //"favorite/view/:id": "view",
            //"favorite/update/:id": "update",
            //"favorite/search": "search",
            "favorite/list": "list"
        }
    });

    App.addInitializer(function(){
        FavoriteRouting.router = new FavoriteRouting.Router({
            controller: App.Controllers.FavoriteController
        });
        App.vent.trigger("routing:started");
    });

    return FavoriteRouting;
}();