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
        TiaonaerApp.ViewInstances.ComplainDetailView.render();
        TiaonaerApp.showPage(TiaonaerApp.ViewInstances.ComplainDetailView, false);
    }
});
