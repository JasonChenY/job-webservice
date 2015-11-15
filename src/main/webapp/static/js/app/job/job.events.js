TiaonaerApp.vent.on("routing:started", function(){
    if( ! Backbone.History.started) Backbone.history.start();
});
/*
TiaonaerApp.vent.on("todo:added", function(model) {
    window.log("Processing todo added event for model: ", model.toJSON());
    Backbone.history.navigate("#/todo/" + model.get("id"));

    var translatedMessage = i18n.t("todoAdded", { title: model.get("title") });
    var feedbackMessage = new TiaonaerApp.Models.FeedbackMessage({message: translatedMessage});
    var messageView = new TiaonaerApp.Views.FeedbackMessageView({model: feedbackMessage});
    TiaonaerApp.messageRegion.show(messageView);
});
*/
TiaonaerApp.vent.on("job:list", function() {
    console.log("Processing job:list event");
    Backbone.history.navigate("#/job/list");
});
TiaonaerApp.vent.on("job:deleted", function(model) {
    window.log("Processing job deleted event for model: ", model.toJSON());

    Backbone.history.navigate("#/");

    var translatedMessage = i18n.t("jobDeleted", {title: model.get("title")});
    var feedBackMessage = new TiaonaerApp.Models.FeedbackMessage({message: translatedMessage});
    var messageView = new TiaonaerApp.Views.FeedbackMessageView({model: feedBackMessage});
    TiaonaerApp.messageRegion.show(messageView);
});



TiaonaerApp.vent.on("job:updated", function(model) {
    window.log("Processing job updated event for model: ", model.toJSON());

    Backbone.history.navigate("#/todo/" + model.get("id"));

    var translatedMessage = i18n.t("todoUpdated", {title: model.get("title")});
    var feedbackMessage = new TiaonaerApp.Models.FeedbackMessage({message: translatedMessage});
    var messageView = new TiaonaerApp.Views.FeedbackMessageView({model: feedbackMessage});
    TiaonaerApp.messageRegion.show(messageView);
});

// used for navigate from joblist directly with exising model, dont need fetch
TiaonaerApp.vent.on("jobdetail:view", function(model) {
    TiaonaerApp.showView("JobDetailView", model);
});

TiaonaerApp.vent.on("job:search", function() {
    console.log("job search page view");
    Backbone.history.navigate("#/job/search");
});

TiaonaerApp.vent.on("job:filter", function(filters) {
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
    Backbone.history.navigate("#/job/filter");
});
