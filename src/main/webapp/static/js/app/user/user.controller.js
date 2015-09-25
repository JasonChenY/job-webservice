TiaonaerApp.Controllers.UserController = {
    home: function() {
        window.log("Rendering initial home view.");
        if ( TiaonaerApp.ViewInstances.UserHomeView === undefined ) {
            var user = new TiaonaerApp.Models.User();
            TiaonaerApp.ViewInstances.UserHomeView = new TiaonaerApp.Views.UserHomeView({model:user});
            TiaonaerApp.showPage(TiaonaerApp.ViewInstances.UserHomeView, true);
        } else {
            TiaonaerApp.showPage(TiaonaerApp.ViewInstances.UserHomeView, false);
        }
    },

    login: function() {
        console.log("Rendering login page");
        if ( TiaonaerApp.ViewInstances.LoginView === undefined ) {
            TiaonaerApp.ViewInstances.LoginView = new TiaonaerApp.Views.LoginView();
            TiaonaerApp.showPage(TiaonaerApp.ViewInstances.LoginView, true);
        } else {
            TiaonaerApp.showPage(TiaonaerApp.ViewInstances.LoginView, false);
        }
    },

    logout: function() {
        console.log("Logging user out");
        if ( !TiaonaerApp.isAnonymousUser() ) {
            $.get("/api/logout", function(data) {
                window.log("Logout successful. Received data: ", data);
                TiaonaerApp.vent.trigger("user:logoutSuccess");
            });
        } else {
            console.log("Not logged in yet");
        }
    },
};