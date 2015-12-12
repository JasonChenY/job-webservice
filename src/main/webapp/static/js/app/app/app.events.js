TiaonaerApp.vent.on("routing:started", function(){
    if( ! Backbone.History.started) {
        Backbone.history.start();
    }
});

TiaonaerApp.vent.on("app:download", function() {
    Backbone.history.navigate("#/app/download");
});

TiaonaerApp.vent.on("app:about", function() {
    Backbone.history.navigate("#/app/about");
});
