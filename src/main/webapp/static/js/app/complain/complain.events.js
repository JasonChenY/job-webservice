App.vent.on("routing:started", function(){
    if( ! Backbone.History.started) Backbone.history.start();
});

App.vent.on("complain:list", function() {
    console.log("Processing complain:list event");
    Backbone.history.navigate("#/complain/list");
});

App.vent.on("complain:adminlist", function() {
    console.log("Processing complain:adminlist event");
    Backbone.history.navigate("#/complain/adminlist");
});

App.vent.on("complain:detail", function(model) {
    console.log("Processing complain:detail event");
    App.showView("ComplainDetailView", model);
});

App.vent.on("complain:admindetail", function(model) {
    console.log("Processing complain:admindetail event");
    App.showView("ComplainAdminDetailView", model);
});
