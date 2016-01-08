App.UserRouting = function() {
    var UserRouting = {};
    console.log("UserRouting")
    UserRouting.Router = Backbone.Marionette.AppRouter.extend({
        appRoutes: {
            "": "home",
            "user/login": "login",
            "user/logout": "logout",
            "user/register": "register"
        }
    });

    App.addInitializer(function(){
        UserRouting.router = new UserRouting.Router({
            controller: App.Controllers.UserController
        });
        App.vent.trigger("routing:started");
    });

    return UserRouting;
}();