TiaonaerApp.Controllers.ComplainController = {
    list: function() {
        console.log("list in ComplainController");
        if (TiaonaerApp.isAnonymousUser()) {
            TiaonaerApp.vent.trigger("user:login");
        } else {
            TiaonaerApp.showView("ComplainListView");
        }
    },
    adminlist: function() {
        console.log("adminlist in ComplainController");
        if (TiaonaerApp.isAnonymousUser()) {
            TiaonaerApp.vent.trigger("user:login");
        } else {
            TiaonaerApp.showView("ComplainAdminListView");
        }
    }
};