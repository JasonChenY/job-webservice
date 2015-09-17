TiaonaerApp.Controllers.UserController = {
    home: function() {
        window.log("Rendering initial home view.");
        if (this.isAnonymousUser()) {
            TiaonaerApp.vent.trigger("user:login");
        } else {
            var userHomeView = new TiaonaerApp.Views.UserHomeView();
            //TiaonaerApp.mainRegion.show(userHomeView);
            TiaonaerApp.changePage(userHomeView);
        }
    },

    login: function() {
        console.log("Rendering login page");
        var loginView = new TiaonaerApp.Views.LoginView();
        TiaonaerApp.mainRegion.show(loginView);
    },
    logout: function() {
        console.log("Logging user out")
        $.get("/api/logout", function(data) {
            window.log("Logout successful. Received data: ", data);
            TiaonaerApp.vent.trigger("user:logoutSuccess");
        })
    },
    isAnonymousUser: function() {
        if (TiaonaerApp.user === 'anonymous') {
            window.log("User is anonymous");
            return true;
        }
        return false;
    },
};