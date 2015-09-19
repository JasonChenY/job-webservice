TiaonaerApp.Controllers.FavoriteController = {
    list: function() {
        console.log("list in FavoriteController");
        if (TiaonaerApp.isAnonymousUser()) {
            TiaonaerApp.vent.trigger("user:login");
        } else {
            var favoriteListView = new TiaonaerApp.Views.FavoriteListView();
            console.log("changePage list in FavoriteController");
            if ( TiaonaerApp.ViewInstances.FavoriteListView === undefined ) {
                console.log("create & show FavoriteListView");
                TiaonaerApp.ViewInstances.FavoriteListView = new TiaonaerApp.Views.FavoriteListView();
                TiaonaerApp.showPage(TiaonaerApp.ViewInstances.FavoriteListView, true);
            } else {
                console.log("show FavoriteListView");
                TiaonaerApp.showPage(TiaonaerApp.ViewInstances.FavoriteListView, false);
            }
        }
    },
};
