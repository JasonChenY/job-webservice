App.vent.on("routing:started", function(){
    if( ! Backbone.History.started) Backbone.history.start();
});

App.vent.on("favorite:list", function() {
    console.log("Processing favorite:list event");
    Backbone.history.navigate("#/favorite/list");
});

App.vent.on("favorite:detail", function(model) {
    console.log("Processing favorite:list event");
    Backbone.history.navigate("#/job/view/"+model.get('job_id'));
});

