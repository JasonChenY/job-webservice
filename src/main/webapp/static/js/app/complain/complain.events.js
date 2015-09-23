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
