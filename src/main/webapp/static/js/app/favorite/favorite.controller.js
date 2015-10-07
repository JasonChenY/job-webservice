TiaonaerApp.Controllers.FavoriteController = {
    list: function() {
        console.log("list in FavoriteController");
        if (TiaonaerApp.isAnonymousUser()) {
            TiaonaerApp.vent.trigger("user:login");
        } else {
            TiaonaerApp.showView("FavoriteListView");
        }
    }
};
