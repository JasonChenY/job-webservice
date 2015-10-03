TiaonaerApp.Views.ComplainItemView = Marionette.View.extend({
    tagName: "li",
    initialize:function () {
        this.template = _.template(tpl.get('template-complainitem-view'));
    },
    render:function (eventName) {
        $(this.el).html(this.template(this.model.toJSON()));
        return this;
    },
    events: {
        'click a.detail': function(e) { TiaonaerApp.vent.trigger("complain:detail", this.model); }
    }
});

TiaonaerApp.Views.ComplainListView = Marionette.View.extend({
    id: "complainlist_page",
    model: TiaonaerApp.Models.Complain,

    initialize:function () {
        console.log("ComplainListView's initialize");
        this.template = _.template(tpl.get('template-complainlist-view'));
        this.collection = new TiaonaerApp.Collections.ComplainList();
        this.listenTo(this.collection, "reset", this.collectionReset);
        this.fetch_type = 0; // initial fetch, render pagination bar
        this.totalRecords = 1;
        this.collection.fetch({reset:true});
    },

    collectionReset: function() {
        console.log("enter collectionReset");
        this.$('#complainlist').empty();
        _.each(this.collection.models, function (item) {
                this.$('#complainlist').append(new TiaonaerApp.Views.ComplainItemView({model:item}).render().el);
        }, this);
        this.$('#complainlist').listview().listview('refresh');
        this.$('.complainlist_iscroller_wrapper').iscrollview().iscrollview("refresh");

        if ( this.fetch_type === 0 ) {
            this.totalRecords = this.collection.state.totalRecords;
            console.log("fetch_type === 0, length: " + this.totalRecords);
            var self = this;
            $(".pagination", this.el).pagination({
                items: self.totalRecords,
                itemsOnPage: 10,
                cssStyle: 'dark-theme',
                displayedPages: 3,
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
        this.collectionReset();
        return this;
    }
});

TiaonaerApp.Views.ComplainDetailView = Marionette.View.extend({
    id: "complain_detail_page",
    model: TiaonaerApp.Models.Complain,
    initialize:function() {
        this.template = _.template(tpl.get('template-complain-detail-view'));
        this.listenTo(this.model, "change", this.modelAttrChanged);
        this.job_model = new TiaonaerApp.Models.Job({id:encodeURIComponent(this.model.get("job_id"))});
        this.job_model_ready = false;
        var self = this;
        this.job_model.fetch({success:function() {self.job_model_ready = true; self.render();}});
    },
    events: {
        'click a.goback': function(e) { window.history.back(); e.preventDefault(); },
    },
    switchModel: function(model) {
        this.stopListening(this.model);
        this.model = model;
        this.listenTo(this.model, "change", this.modelAttrChanged);

        this.job_model = new TiaonaerApp.Models.Job({id:encodeURIComponent(this.model.get("job_id"))});
        var self = this;
        this.job_model.fetch({success:function() {self.render();}});
    },
    modelAttrChanged: function() {
        this.render();
    },
    render:function (eventName) {
        if ( !this.job_model_ready) return;
        var merge = _.extend(this.model.toJSON(), this.job_model.toJSON());

        $(this.el).html(this.template(merge));
        $(this.el).trigger('create');
        $('.iscroll-wrapper', this.el).iscrollview().iscrollview("refresh");
        $('.ui-footer', this.el).toolbar('refresh');
        return this;
    }
});


