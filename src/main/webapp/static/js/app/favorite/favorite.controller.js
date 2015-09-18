TiaonaerApp.Controllers.FavoriteController = {
    list: function() {
        console.log("list in FavoriteController");
        if (TiaonaerApp.isAnonymousUser()) {
            TiaonaerApp.vent.trigger("user:login");
        } else {
            var favoriteListView = new TiaonaerApp.Views.FavoriteListView();
            console.log("changePage list in FavoriteController");
            TiaonaerApp.changePage(favoriteListView);
        }
    },
};
