TodoApp.Controllers.UserController = {
    login: function() {
        console.log("Rendering login page");
        var loginView = new TodoApp.Views.LoginView();
        TodoApp.mainRegion.show(loginView);
    },
    logout: function() {
        console.log("Logging user out")
        $.get("/api/logout", function(data) {
            window.log("Logout successful. Received data: ", data);
            TodoApp.vent.trigger("user:logoutSuccess");
        })
    }
};