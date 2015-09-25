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
    var translatedErrorMessage = i18n.t("loginFailed");
    var errorMessage = new TiaonaerApp.Models.FeedbackMessage({message: translatedErrorMessage});
    var errorMessageView = new TiaonaerApp.Views.ErrorMessageView({model: errorMessage});
    TiaonaerApp.messageRegion.show(errorMessageView);
});

TiaonaerApp.vent.on("user:loginSuccess", function() {
    console.log("handle user:loginSuccess");
    $.ajax({
        async: false,
        type: "GET",
        url: "/api/user",
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
