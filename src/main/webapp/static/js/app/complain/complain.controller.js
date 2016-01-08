App.Controllers.ComplainController = {
    list: function() {
        console.log("list in ComplainController");
        if (App.isAnonymousUser()) {
            App.vent.trigger("user:login");
        } else {
            App.showView("ComplainListView");
        }
    },
    adminlist: function() {
        console.log("adminlist in ComplainController");
        if (App.isAnonymousUser()) {
            App.vent.trigger("user:login");
        } else {
            App.showView("ComplainAdminListView");
        }
    }
};