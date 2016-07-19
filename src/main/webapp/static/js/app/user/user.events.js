window.addEventListener('message',function(e) {
   ThirdPartyLoginInCallback(e.data);
},false);

function ThirdPartyLoginInCallback(result) {
    if ( typeof(result) === 'object' ) {
        App.vent.trigger("user:loginSuccess", result);
    } else  {
        App.vent.trigger("user:loginFailed");
    }
};

App.vent.on("routing:started", function(){
    if( ! Backbone.History.started) {
        Backbone.history.start();
    }
})

App.vent.on("user:register", function(){
    console.log("handle user:register")
    Backbone.history.navigate("#/user/register");
});

App.vent.on("user:registerSuccess", function(user) {
    console.log("handle user:registerSuccess");
    Backbone.history.navigate("#/");
    App.ViewContainer.findByCustom("UserHomeView").userLoggedIn(user);
});

App.vent.on("user:login", function(){
    console.log("handle user:login")
    Backbone.history.navigate("#/user/login");
});

App.vent.on("user:loginFailed", function() {
    // extend to include detail error info.
    App.ViewContainer.findByCustom("LoginView").login_failed();
});

App.vent.on("user:loginSuccess", function(user) {
    var userHomeView = App.ViewContainer.findByCustom("UserHomeView");
    if ( userHomeView ) {
        userHomeView.userLoggedIn(user);
        window.history.back();
    } else {
        userHomeView = App.showView("UserHomeView");
        userHomeView.userLoggedIn(user);
    }
/*
    // save one API toward server.
    $.ajax({
        //async: false,
        type: "GET",
        url: App.ServiceUrl + "/api/user",
        xhrFields: {
            withCredentials: true
        },
        crossDomain: true,
        beforeSend:function(){
        },
        success: function(user) {
            if (user.username && (user.username!=='anonymousUser')) {
                console.log("Found logged in user: ", user);
                var userHomeView = App.ViewContainer.findByCustom("UserHomeView");
                if ( userHomeView ) {
                    userHomeView.userLoggedIn(user);
                    // restore back to the view triggered the login process.
                    window.history.back();
                } else {
                    // in case access login page directly, homeview not created yet.
                    userHomeView = App.showView("UserHomeView");
                    userHomeView.userLoggedIn(user);
                }
            } else {
                console.log("Logged in user was not found.")
            }
        }
    });
*/
});

App.vent.on("user:logout", function() {
    Backbone.history.navigate("#/user/logout");
});

App.vent.on("user:logoutSuccess", function() {
    App.ViewContainer.findByCustom("UserHomeView").userLoggedOut();
    Backbone.history.navigate("#/");
});
