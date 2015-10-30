TiaonaerApp.Views.ComplainAdminItemView = Backbone.View.extend({
    tagName: "li",
    template: '#template-complainitem-admin-view',
    initialize:function () {
        this.template = Marionette.TemplateCache.get(Marionette.getOption(this, "template"));
        this.listenTo(this.model, "change", this.render);
    },
    render:function (eventName) {
        $(this.el).html(this.template(this.model.toJSON()));
        return this;
    },
    events: {
        'click input.accept': function(e) { this.model.updateAttr({status:1}); e.preventDefault();} ,
        'click input.reject': function(e) { this.model.updateAttr({status:2}); e.preventDefault();} ,
        'click a.detail': function(e) { TiaonaerApp.vent.trigger("complain:admindetail", this.model); }
    }
});

TiaonaerApp.Views.ComplainAdminListView = TiaonaerApp.View.extend({
    id: "complainlist-admin-page",
    template: '#template-complainlist-admin-view',
    model: TiaonaerApp.Models.Complain,

    initialize:function () {
        console.log("ComplainAdminListView's initialize");
        this.template = Marionette.TemplateCache.get(Marionette.getOption(this, "template"));
        this.collection = new TiaonaerApp.Collections.ComplainAdminList();
        this.listenTo(this.collection, "reset", this.collectionSwitched);
        this.switchCollection();
    },

    resetForNewUser: function() { this.switchCollection();},

    switchCollection: function(type, status) {
        var defqs = {
            currentPage: "page.page",
            pageSize: "page.size",
            sortKey: "page.sort",
            order: "page.sort.dir",
            directions: 1
        };
        var filters = {};
        if ( type !== undefined && type !== -1 && type !== "-1" ) filters['type'] = type;
        if ( status === undefined ) {
            filters['status'] = 0;
        } else if ( status !== -1 && status !== "-1" ) {
            filters['status'] = status;
        }

        this.collection.queryParams = _.extend(defqs, filters);
        this.fetch_type = 0;
        this.collection.state.currentPage = 1;
        this.collection.fetch({reset:true});
    },

    collectionSwitched: function() {
        console.log("enter collectionSwitched");
        this.$('#complainlist').empty();
        _.each(this.collection.models, function (item) {
                this.$('#complainlist').append(new TiaonaerApp.Views.ComplainAdminItemView({model:item}).render().el);
        }, this);
        this.$('#complainlist').listview().listview('refresh');
        this.$('.complainlist_iscroller_wrapper').iscrollview().iscrollview("refresh");

        if ( this.fetch_type === 0 ) {
            console.log("fetch_type === 0, length: " + this.collection.state.totalRecords);
            var self = this;
            $(".pagination", this.el).pagination({
                items: self.collection.state.totalRecords,
                itemsOnPage: 10,
                cssStyle: 'dark-theme',
                displayedPages: 3,
                edges: 2,
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
        console.log("render complainadminlist view");
        $(this.el).html(this.template());
        return this;
    },

    events: {
        'change #complaintype': "complain_filter",
        'change #complainstatus': "complain_filter",
        'click a[data-rel="back"]': function(e) {Backbone.history.history.back(); e.stopPropagation(); return false;}
    },

    complain_filter: function(e) {
        var type = $('#complaintype', this.el).val();
        var status = $('#complainstatus', this.el).val();
        console.log(type + ": " + status);
        this.switchCollection(type, status);
    }
});

TiaonaerApp.Views.ComplainAdminDetailView = Backbone.View.extend({
    id: "complain-admin-detail-page",
    template: '#template-complain-admin-detail-view',
    model: TiaonaerApp.Models.Complain,
    initialize:function() {
        this.template = Marionette.TemplateCache.get(Marionette.getOption(this, "template"));
        this.listenTo(this.model, "change", this.modelAttrChanged);
        var self = this;
        self.job_model = new TiaonaerApp.Models.Job({id:encodeURIComponent(this.model.get("job_id"))});
        self.job_model_ready = false;
        self.job_model.fetch({success:function() {self.job_model_ready = true; self.render();}});
    },
    events: {
        'click a.job_url': "goto_job_url",
        'click input.accept': function(e) { this.model.updateAttr({status:1}); e.preventDefault();} ,
        'click input.reject': function(e) { this.model.updateAttr({status:2}); e.preventDefault();} ,
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
        self.job_model = new TiaonaerApp.Models.Job({id:encodeURIComponent(this.model.get("job_id"))});
        self.job_model_ready = false;
        self.job_model.fetch({success:function() {self.job_model_ready = true; self.render();}});
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