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
    if ( TiaonaerApp.ViewInstances.JobDetailView === undefined ) {
        console.log("create & show JobDetailView");
        TiaonaerApp.ViewInstances.JobDetailView = new TiaonaerApp.Views.JobDetailView({model:model});
        TiaonaerApp.showPage(TiaonaerApp.ViewInstances.JobDetailView, true);
    } else {
        console.log("show JobDetailView");
        TiaonaerApp.ViewInstances.JobDetailView.switchModel(model);

        TiaonaerApp.ViewInstances.JobDetailView.render();
        TiaonaerApp.showPage(TiaonaerApp.ViewInstances.JobDetailView, false);
    }
});

TiaonaerApp.vent.on("job:search", function() {
    console.log("job search page view");
    Backbone.history.navigate("#/job/search");
});

TiaonaerApp.vent.on("job:filter", function(filters) {
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

    Backbone.history.navigate("#/job/filter?"+qs);
});
