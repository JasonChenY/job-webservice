App.Views.ComplainItemView = Backbone.View.extend({
    tagName: "li",
    template: '#template-complainitem-view',
    initialize:function () {
        this.template = Marionette.TemplateCache.get(Marionette.getOption(this, "template"));
    },
    render:function (eventName) {
        $(this.el).html(this.template(this.model.toJSON()));
        return this;
    },
    events: {
        'click a.detail': function(e) { App.vent.trigger("complain:detail", this.model); }
    }
});

App.Views.ComplainListView = App.View.extend({
    id: "complainlist-page",
    template: '#template-complainlist-view',
    model: App.Models.Complain,

    initialize:function () {
        console.log("ComplainListView's initialize");
        this.template = Marionette.TemplateCache.get(Marionette.getOption(this, "template"));
        this.collection = new App.Collections.ComplainList();
        this.listenTo(this.collection, "reset", this.collectionSwitched);
        this.switchCollection();
    },

    resetForNewUser: function() {
        //$(".pagination", this.el).pagination('destroy');
        this.switchCollection();
        //$(".pagination", this.el).pagination('redraw',1);
    },

    switchCollection: function() {
        this.fetch_type = 0;
        this.collection.state.currentPage = 1;
        this.collection.fetch({reset:true});
    },

    collectionSwitched: function() {
        console.log("enter collectionSwitched");
        this.$('#complainlist').empty();
        _.each(this.collection.models, function (item) {
                this.$('#complainlist').append(new App.Views.ComplainItemView({model:item}).render().el);
        }, this);
        this.$('#complainlist').listview().listview('refresh');
        this.$('.complainlist_iscroller_wrapper').iscrollview("scrollTo", 0, 0, 10, null);

        if ( this.fetch_type === 0 ) {
            console.log("fetch_type === 0, length: " + this.totalRecords);
            var self = this;
            $(".pagination", this.el).pagination({
                items: self.collection.state.totalRecords,
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
        console.log("render complainlist view");
        $(this.el).html(this.template());
        return this;
    },

    events: {
        'click a[data-rel="back"]': function(e) {Backbone.history.history.back(); e.stopPropagation(); return false;}
    }
});

App.Views.ComplainDetailView = Backbone.View.extend({
    id: "complain-detail-page",
    template: '#template-complain-detail-view',
    model: App.Models.Complain,
    initialize:function() {
        this.template = Marionette.TemplateCache.get(Marionette.getOption(this, "template"));
        this.listenTo(this.model, "change", this.modelAttrChanged);

        var self = this;
        self.job_model = new App.Models.Job({id:encodeURIComponent(this.model.get("job_id"))});
        self.job_model_ready = false;
        this.job_model.fetch({success:function() {self.job_model_ready = true; self.render();}});
    },
    events: {
        'click a.job_url': "goto_job_url",
        'click a[data-rel="back"]': function(e) {Backbone.history.history.back(); e.stopPropagation(); return false;}
    },
    goto_job_url: function(e) {
        window.open(e.target.href);
        e.preventDefault();
    },
    switchModel: function(model) {
        this.stopListening(this.model);
        this.model = model;
        this.listenTo(this.model, "change", this.modelAttrChanged);

        var self = this;
        self.job_model = new App.Models.Job({id:encodeURIComponent(this.model.get("job_id"))});
        self.job_model_ready = false;
        this.job_model.fetch({success:function() {self.job_model_ready = true; self.render();}});
    },
    modelAttrChanged: function() {
        this.render();
    },
    render:function (eventName) {
        if ( !this.job_model_ready ) return;
        var merge = _.extend(this.model.toJSON(), this.job_model.toJSON());

        $(this.el).html(this.template(merge));
        $(this.el).trigger('create');
        $('.iscroll-wrapper', this.el).iscrollview().iscrollview("refresh");
        $('.ui-footer', this.el).toolbar('refresh');
        return this;
    }
});


