TiaonaerApp.Controllers.ComplainController = {
    list: function() {
        console.log("list in ComplainController");
        if (TiaonaerApp.isAnonymousUser()) {
            TiaonaerApp.vent.trigger("user:login");
        } else {
            console.log("changePage list in ComplainController");
            if ( TiaonaerApp.ViewInstances.ComplainListView === undefined ) {
                console.log("create & show ComplainListView");
                TiaonaerApp.ViewInstances.ComplainListView = new TiaonaerApp.Views.ComplainListView();
                TiaonaerApp.showPage(TiaonaerApp.ViewInstances.ComplainListView, true);
            } else {
                console.log("show ComplainListView");
                TiaonaerApp.showPage(TiaonaerApp.ViewInstances.ComplainListView, false);
            }
        }
    },
    adminlist: function() {
        console.log("adminlist in ComplainController");
        if (TiaonaerApp.isAnonymousUser()) {
            TiaonaerApp.vent.trigger("user:login");
        } else {
            console.log("changePage list in ComplainController");
            if ( TiaonaerApp.ViewInstances.ComplainAdminListView === undefined ) {
                console.log("create & show ComplainAdminListView");
                TiaonaerApp.ViewInstances.ComplainAdminListView = new TiaonaerApp.Views.ComplainAdminListView();
                TiaonaerApp.showPage(TiaonaerApp.ViewInstances.ComplainAdminListView, true);
            } else {
                console.log("show ComplainAdminListView");
                TiaonaerApp.showPage(TiaonaerApp.ViewInstances.ComplainAdminListView, false);
            }
        }
    }
};