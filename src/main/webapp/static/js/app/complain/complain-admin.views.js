TiaonaerApp.Views.ComplainAdminItemView = Marionette.View.extend({
    tagName: "li",
    initialize:function () {
        this.template = _.template(tpl.get('template-complainitem-admin-view'));
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
    },
});

TiaonaerApp.Views.ComplainAdminListView = Marionette.View.extend({
    id: "complainlist_admin_page",
    model: TiaonaerApp.Models.Complain,

    initialize:function () {
        console.log("ComplainAdminListView's initialize");
        this.template = _.template(tpl.get('template-complainlist-admin-view'));
        this.collection = new TiaonaerApp.Collections.ComplainAdminList();
        this.listenTo(this.collection, "reset", this.collectionReset);
        this.fetch_type = 0; // initial fetch, render pagination bar
        this.totalRecords = 1;
        this.FetchByCondition(-1, 0);
    },
    FetchByCondition: function(type, status) {
        var defqs = {
            currentPage: "page.page",
            pageSize: "page.size",
            sortKey: "page.sort",
            order: "page.sort.dir",
            directions: 1,
        };
        var filters = {};
        if ( type !== -1 && type !== "-1" ) filters['type'] = type;
        if ( status !== -1 && status !== "-1" ) filters['status'] = status;
        this.collection.state.currentPage = 1;
        this.collection.queryParams = _.extend(defqs, filters);
        this.fetch_type = 0;
        this.collection.state.currentPage = 1;
        this.collection.fetch({reset:true});
    },
    collectionReset: function() {
        console.log("enter collectionReset");
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
        this.collectionReset();
        return this;
    },

    events: {
        'change #complaintype': "complain_filter",
        'change #complainstatus': "complain_filter"
    },

    complain_filter: function(e) {
        var type = $('#complaintype', this.el).val();
        var status = $('#complainstatus', this.el).val();
        console.log(type + ": " + status);
        this.FetchByCondition(type, status);
    }
});

TiaonaerApp.Views.ComplainAdminDetailView = Marionette.View.extend({
    id: "complain_admin_detail_page",
    model: TiaonaerApp.Models.Complain,
    initialize:function() {
        this.template = _.template(tpl.get('template-complain-admin-detail-view'));
        this.listenTo(this.model, "change", this.modelAttrChanged);
        this.job_model = new TiaonaerApp.Models.Job({id:encodeURIComponent(this.model.get("job_id"))});
        this.job_model_ready = false;
        var self = this;
        this.job_model.fetch({success:function() {self.job_model_ready = true; self.render();}});
    },
    events: {
        'click a.goback': function(e) { window.history.back(); e.preventDefault(); },
        'click input.accept': function(e) { this.model.updateAttr({status:1}); e.preventDefault();} ,
        'click input.reject': function(e) { this.model.updateAttr({status:2}); e.preventDefault();} ,
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
    },
});