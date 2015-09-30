TiaonaerApp.vent.on("routing:started", function(){
    if( ! Backbone.History.started) {
        Backbone.history.start();
    }
})

TiaonaerApp.vent.on("user:login", function(){
    console.log("handle user:login")
    Backbone.history.navigate("#/user/login");
});

TiaonaerApp.vent.on("user:loginFailed", function() {
    // extend to include detail error info.
    TiaonaerApp.ViewInstances.LoginView.login_failed(null);
});

TiaonaerApp.vent.on("user:loginSuccess", function() {
    console.log("handle user:loginSuccess");
    $.ajax({
        async: false,
        type: "GET",
        url: TiaonaerApp.ServiceUrl+"/api/user",
        success: function(user) {
            if (user.username) {
                console.log("Found logged in user: ", user);
                TiaonaerApp.ViewInstances.UserHomeView.userLoggedIn(user);

                // restore back to the view triggered the login process.
                window.history.back();
            } else {
                console.log("Logged in user was not found.")
            }
        }
    });

    /*
    var restore_page = function() {
        window.history.back();
    }
    TiaonaerApp.getLoggedInUser(restore_page);
    */
});

TiaonaerApp.vent.on("user:logout", function() {
    Backbone.history.navigate("#/user/logout");
});

TiaonaerApp.vent.on("user:logoutSuccess", function() {
    TiaonaerApp.ViewInstances.UserHomeView.userLoggedOut();
    Backbone.history.navigate("#/");
});
