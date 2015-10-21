TiaonaerApp.Views.FavoriteItemView = Backbone.View.extend({
    tagName: "li",
    template: '#template-favoriteitem-view',
    initialize:function () {
        this.template = Marionette.TemplateCache.get(Marionette.getOption(this, "template"));
    },
    render:function (eventName) {
        $(this.el).html(this.template(this.model.toJSON()));
        return this;
    },
    events: {
        'click a.favorite': function(e) { this.favorite(); e.preventDefault();} ,
        'click a.detail': function(e) {TiaonaerApp.vent.trigger("favorite:detail", this.model);}
    },
    favorite: function(e) {
        this.model.destroy({
            wait:true,
            success: function(favorite) {
                console.log("detroy succeed");
            },
            fail: function(favorite) {
                console.log("destroy fail");
            }
        });
    }
});

TiaonaerApp.Views.FavoriteListView = TiaonaerApp.View.extend({
    id: "favoritelist-page",
    template: '#template-favoritelist-view',
    model: TiaonaerApp.Models.Favorite,

    initialize:function () {
        console.log("FavoriteListView's initialize");
        this.template = Marionette.TemplateCache.get(Marionette.getOption(this, "template"));
        this.collection = new TiaonaerApp.Collections.FavoriteList();
        this.listenTo(this.collection, "reset", this.collectionSwitched);
        this.listenTo(this.collection, "remove", this.itemRemoved);
        this.switchCollection();
    },

    resetForNewUser: function() { this.switchCollection();},

    switchCollection: function() {
        this.fetch_type = 0; // initial fetch, render pagination bar
        this.totalRecords = 1;
        this.collection.state.currentPage = 1;
        this.collection.fetch({reset:true});
    },

    itemRemoved: function() {
        console.log("itemRemove " + this.collection.length);
        this.totalRecords--;
        console.log("totalRecords: " + this.totalRecords);
        $(".pagination", this.el).pagination('updateItems', this.totalRecords);
        if ( this.collection.length === 0 ) {
            this.fetch_type = 0;
            var currentPage = $(".pagination", this.el).pagination('getCurrentPage');
            console.log("currentPage: " + currentPage);
            if ( currentPage > 1 ) {
                $(".pagination", this.el).pagination('prevPage');
                //prevPage will invoke onPageClick, which will refetch again.
            } else {
                //if this is firstPage, prevPage do nothing, we refetch 1st page forcelly.
                console.log("refetch first page forcelly");
                this.collection.getPage(1, {reset:true});
            }
        } else {
            this.collectionSwitched();
        }
    },

    collectionSwitched: function() {
        console.log("enter collectionSwitched");
        this.$('#favoritelist').empty();
        _.each(this.collection.models, function (favoriteitem) {
                this.$('#favoritelist').append(new TiaonaerApp.Views.FavoriteItemView({model:favoriteitem}).render().el);
        }, this);
        this.$('#favoritelist').listview().listview('refresh');
        this.$('.favoritelist_iscroller_wrapper').iscrollview().iscrollview("refresh");

        if ( this.fetch_type === 0 ) {
            this.totalRecords = this.collection.state.totalRecords;
            console.log("fetch_type === 0, length: " + this.totalRecords);
            var self = this;
            $(".pagination", this.el).pagination({
                items: self.totalRecords,
                itemsOnPage: 10,
                cssStyle: 'dark-theme',
                displayedPages: 3,
                prevText: '<',
                nextText: '>',
                onPageClick: function(page, event) {
                    if ( event !== undefined ) event.preventDefault();
                    self.fetch_type = 1;
                    console.log("getPage " + page);
                    self.collection.getPage(page, {reset:true});
                }
            });
        }
    },

    render:function (eventName) {
        console.log("render favoritelist view");
        $(this.el).html(this.template());
        return this;
    }
});



