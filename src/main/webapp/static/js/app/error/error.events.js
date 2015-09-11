TodoApp.vent.on("error:404", function() {
    console.log("Processing 404 event.")
    Backbone.history.navigate("#/error/404");
});

TodoApp.vent.on("error:notAuthorized", function() {
    console.log("Processing not authorized event");
    Backbone.history.navigate("#/error/notAuthorized")
});

TodoApp.vent.on("error:error", function(){
    console.log("Processing error event")
    Backbone.history.navigate("#/error/error");
});