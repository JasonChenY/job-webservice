App.Controllers.ErrorController = {
    404: function() {
        window.log("Rendering 404 view.");
        var notFoundView = new App.Views.NotFoundView();
        App.mainRegion.show(notFoundView);
    },
    notAuthorized: function() {
        window.log("Rendering not authorized view");
        var notAuthorizedView = new App.Views.NotAuthorizedView();
        App.mainRegion.show(notAuthorizedView);
    },
    error: function() {
        window.log("Rendering error view.");
        var errorView = new App.Views.ErrorView();
        App.mainRegion.show(errorView);
    }

};
