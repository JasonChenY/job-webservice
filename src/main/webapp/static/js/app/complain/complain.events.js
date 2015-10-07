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
    TiaonaerApp.showView("ComplainDetailView", model);
});

TiaonaerApp.vent.on("complain:admindetail", function(model) {
    console.log("Processing complain:admindetail event");
    TiaonaerApp.showView("ComplainAdminDetailView", model);
});
