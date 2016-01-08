App.Controllers.UserController = {
    home: function() {
        console.log("Rendering initial home view.");
        App.showView("UserHomeView");
    },

    login: function() {
        console.log("Rendering login page");
        App.showView("LoginView");
    },

    register: function() {
        console.log("Rendering register page");
        App.showView("UserRegisterView");
    },

    logout: function() {
        console.log("Logging user out");
        if ( !App.isAnonymousUser() ) {
            /*$.get(App.ServiceUrl+"/api/logout", function(data) {
                console.log("Logout successful. Received data: ", data);
                App.vent.trigger("user:logoutSuccess");
            });*/
            $.ajax({
                type: "GET",
                url: App.ServiceUrl + "/api/logout",
                xhrFields: {
                    withCredentials: true
                },
                crossDomain: true,
                success: function(data, status, xhr){
                    console.log("Trigger user:logoutSuccess");
                    App.vent.trigger("user:logoutSuccess");
                }
            });
        } else {
            console.log("Not logged in yet");
        }
    }
};