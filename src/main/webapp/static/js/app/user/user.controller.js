TiaonaerApp.Controllers.UserController = {
    home: function() {
        window.log("Rendering initial home view.");
        TiaonaerApp.showView("UserHomeView");
    },

    login: function() {
        console.log("Rendering login page");
        TiaonaerApp.showView("LoginView");
    },

    register: function() {
        console.log("Rendering register page");
        TiaonaerApp.showView("UserRegisterView");
    },

    logout: function() {
        console.log("Logging user out");
        if ( !TiaonaerApp.isAnonymousUser() ) {
            /*$.get(TiaonaerApp.ServiceUrl+"/api/logout", function(data) {
                console.log("Logout successful. Received data: ", data);
                TiaonaerApp.vent.trigger("user:logoutSuccess");
            });*/
            $.ajax({
                type: "GET",
                url: TiaonaerApp.ServiceUrl + "/api/logout",
                xhrFields: {
                    withCredentials: true
                },
                crossDomain: true,
                success: function(data, status, xhr){
                    console.log("Trigger user:logoutSuccess");
                    TiaonaerApp.vent.trigger("user:logoutSuccess");
                }
            });
        } else {
            console.log("Not logged in yet");
        }
    },

    download: function() {
        TiaonaerApp.showView("DownloadView");
    }
};