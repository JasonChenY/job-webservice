TiaonaerApp.UserRouting = function() {
    var UserRouting = {};
    console.log("UserRouting")
    UserRouting.Router = Backbone.Marionette.AppRouter.extend({
        appRoutes: {
            "": "home",
            "user/login": "login",
            "user/logout": "logout"
        }
    });

    TiaonaerApp.addInitializer(function(){
        UserRouting.router = new UserRouting.Router({
            controller: TiaonaerApp.Controllers.UserController
        });
        console.log("Trigger routing:started")
        TiaonaerApp.vent.trigger("routing:started");
    });

    return UserRouting;
}();