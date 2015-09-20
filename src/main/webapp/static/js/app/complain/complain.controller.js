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
};