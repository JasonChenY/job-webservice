TiaonaerApp.vent.on("routing:started", function(){
    if( ! Backbone.History.started) {
        console.log("Backbone.history.start()");
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
    var showTodoList = function() {
        Backbone.history.navigate("#/");
        TiaonaerApp.showLogoutLinkAndSearchForm();
    }

    TiaonaerApp.getLoggedInUser(showTodoList);
});

TiaonaerApp.vent.on("user:logoutSuccess", function() {
    TiaonaerApp.setUserAsAnonymous();
    Backbone.history.navigate("#/");
});
