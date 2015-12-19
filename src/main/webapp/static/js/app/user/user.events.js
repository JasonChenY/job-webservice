TiaonaerApp.vent.on("routing:started", function(){
    if( ! Backbone.History.started) {
        Backbone.history.start();
    }
})

TiaonaerApp.vent.on("user:register", function(){
    console.log("handle user:register")
    Backbone.history.navigate("#/user/register");
});

TiaonaerApp.vent.on("user:registerSuccess", function(user) {
    console.log("handle user:registerSuccess");
    Backbone.history.navigate("#/");
    TiaonaerApp.ViewContainer.findByCustom("UserHomeView").userLoggedIn(user);
});

TiaonaerApp.vent.on("user:login", function(){
    console.log("handle user:login")
    Backbone.history.navigate("#/user/login");
});

TiaonaerApp.vent.on("user:loginFailed", function() {
    // extend to include detail error info.
    TiaonaerApp.ViewContainer.findByCustom("LoginView").login_failed();
});

TiaonaerApp.vent.on("user:loginSuccess", function() {
    console.log("handle user:loginSuccess");
    $.ajax({
        //async: false,
        type: "GET",
        url: TiaonaerApp.ServiceUrl + "/api/user",
        xhrFields: {
            withCredentials: true
        },
        crossDomain: true,
        beforeSend:function(){
        },
        success: function(user) {
            if (user.username && (user.username!=='anonymousUser')) {
                console.log("Found logged in user: ", user);
                var userHomeView = TiaonaerApp.ViewContainer.findByCustom("UserHomeView");
                if ( userHomeView ) {
                    userHomeView.userLoggedIn(user);
                    // restore back to the view triggered the login process.
                    window.history.back();
                } else {
                    // in case access login page directly, homeview not created yet.
                    userHomeView = TiaonaerApp.showView("UserHomeView");
                    userHomeView.userLoggedIn(user);
                }
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
    TiaonaerApp.ViewContainer.findByCustom("UserHomeView").userLoggedOut();
    Backbone.history.navigate("#/");
});
