TiaonaerApp.vent.on("routing:started", function(){
    if( ! Backbone.History.started) Backbone.history.start();
});

TiaonaerApp.vent.on("complain:list", function() {
    console.log("Processing complain:list event");
    Backbone.history.navigate("#/complain/list");
});

TiaonaerApp.vent.on("complain:adminlist", function() {
    console.log("Processing complain:adminlist event");
    Backbone.history.navigate("#/complain/adminlist");
});

TiaonaerApp.vent.on("complain:detail", function(model) {
    console.log("Processing complain:detail event");
    if ( TiaonaerApp.ViewInstances.ComplainDetailView === undefined ) {
        TiaonaerApp.ViewInstances.ComplainDetailView = new TiaonaerApp.Views.ComplainDetailView({model:model});
        TiaonaerApp.showPage(TiaonaerApp.ViewInstances.ComplainDetailView, true);
    } else {
        TiaonaerApp.ViewInstances.ComplainDetailView.switchModel(model);
        TiaonaerApp.showPage(TiaonaerApp.ViewInstances.ComplainDetailView, false);
    }
});

TiaonaerApp.vent.on("complain:admindetail", function(model) {
    console.log("Processing complain:admindetail event");
    if ( TiaonaerApp.ViewInstances.ComplainAdminDetailView === undefined ) {
        TiaonaerApp.ViewInstances.ComplainAdminDetailView = new TiaonaerApp.Views.ComplainAdminDetailView({model:model});
        TiaonaerApp.showPage(TiaonaerApp.ViewInstances.ComplainAdminDetailView, true);
    } else {
        TiaonaerApp.ViewInstances.ComplainAdminDetailView.switchModel(model);
        TiaonaerApp.showPage(TiaonaerApp.ViewInstances.ComplainAdminDetailView, false);
    }
});
