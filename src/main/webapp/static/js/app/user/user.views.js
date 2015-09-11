TodoApp.Views.LoginView = Marionette.ItemView.extend({
    template: "#template-login-view",
    events: {
        "click #login-button": "login"
    },
    login: function() {
        console.log("Log in");
        var user = {};
        user.username = $("#user-username").val();
        user.password = $("#user-password").val();
        $.post("/api/login", user, function(){
            TodoApp.vent.trigger("user:loginSuccess");
        })
    }
});
