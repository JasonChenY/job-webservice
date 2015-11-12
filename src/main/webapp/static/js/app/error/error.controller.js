TiaonaerApp.Controllers.ErrorController = {
    404: function() {
        window.log("Rendering 404 view.");
        var notFoundView = new TiaonaerApp.Views.NotFoundView();
        TiaonaerApp.mainRegion.show(notFoundView);
    },
    notAuthorized: function() {
        window.log("Rendering not authorized view");
        var notAuthorizedView = new TiaonaerApp.Views.NotAuthorizedView();
        TiaonaerApp.mainRegion.show(notAuthorizedView);
    },
    error: function() {
        window.log("Rendering error view.");
        var errorView = new TiaonaerApp.Views.ErrorView();
        TiaonaerApp.mainRegion.show(errorView);
    }

};
