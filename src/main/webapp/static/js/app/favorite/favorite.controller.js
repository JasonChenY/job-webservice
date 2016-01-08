App.Controllers.FavoriteController = {
    list: function() {
        console.log("list in FavoriteController");
        if (App.isAnonymousUser()) {
            App.vent.trigger("user:login");
        } else {
            App.showView("FavoriteListView");
        }
    }
};
