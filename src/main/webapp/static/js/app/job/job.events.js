App.vent.on("routing:started", function(){
    if( ! Backbone.History.started) Backbone.history.start();
});
/*
App.vent.on("todo:added", function(model) {
    window.log("Processing todo added event for model: ", model.toJSON());
    Backbone.history.navigate("#/todo/" + model.get("id"));

    var translatedMessage = i18n.t("todoAdded", { title: model.get("title") });
    var feedbackMessage = new App.Models.FeedbackMessage({message: translatedMessage});
    var messageView = new App.Views.FeedbackMessageView({model: feedbackMessage});
    App.messageRegion.show(messageView);
});
*/
App.vent.on("job:list", function() {
    console.log("Processing job:list event");
    Backbone.history.navigate("#/job/list");
});
App.vent.on("job:deleted", function(model) {
    window.log("Processing job deleted event for model: ", model.toJSON());

    Backbone.history.navigate("#/");

    var translatedMessage = i18n.t("jobDeleted", {title: model.get("title")});
    var feedBackMessage = new App.Models.FeedbackMessage({message: translatedMessage});
    var messageView = new App.Views.FeedbackMessageView({model: feedBackMessage});
    App.messageRegion.show(messageView);
});



App.vent.on("job:updated", function(model) {
    window.log("Processing job updated event for model: ", model.toJSON());

    Backbone.history.navigate("#/todo/" + model.get("id"));

    var translatedMessage = i18n.t("todoUpdated", {title: model.get("title")});
    var feedbackMessage = new App.Models.FeedbackMessage({message: translatedMessage});
    var messageView = new App.Views.FeedbackMessageView({model: feedbackMessage});
    App.messageRegion.show(messageView);
});

// used for navigate from joblist directly with exising model, dont need fetch
App.vent.on("jobdetail:view", function(model) {
    App.showView("JobDetailView", model);
});

App.vent.on("job:search", function() {
    console.log("job search page view");
    Backbone.history.navigate("#/job/search");
});

App.vent.on("job:filter", function(filters) {
/*
    console.log("Processing job filter event: " + filters);

    // problem to serialize filters object to string in url.
    var qs = "";
    var first = true;
    _.each(_.keys(filters), function(key) {
        if ( first ) {
            first = false;
        } else {
            qs += "&";
        }
        qs += key + "=" + filters[key];
    });
    if ( qs.length > 0 ) {
        qs = "?" + qs;
    }

    Backbone.history.navigate("#/job/filter"+qs);
*/
    App.filters=filters;
    Backbone.history.navigate("#/job/filter");
});
