App.vent.on("routing:started", function(){
    if( ! Backbone.History.started) {
        Backbone.history.start();
    }
});

App.vent.on("app:download", function() {
    Backbone.history.navigate("#/app/download");
});

App.vent.on("app:about", function() {
    Backbone.history.navigate("#/app/about");
});
