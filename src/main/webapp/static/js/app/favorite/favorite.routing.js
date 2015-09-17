TiaonaerApp.FavoriteRouting = function(){
    var FavoriteRouting = {};

    FavoriteRouting.Router = Backbone.Marionette.AppRouter.extend({
        appRoutes: {
            "favorite/view/:id": "view",
            "favorite/update/:id": "update",
            "favorite/search": "search",
            "favorite/list": "list"
        }
    });

    TiaonaerApp.addInitializer(function(){
        FavoriteRouting.router = new FavoriteRouting.Router({
            controller: TiaonaerApp.Controllers.FavoriteController
        });

        TiaonaerApp.vent.trigger("routing:started");
    });

    return FavoriteRouting;
}();