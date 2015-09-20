TiaonaerApp.Views.JobItemView = Marionette.View.extend({
    tagName: "li",
    initialize:function () {
        this.template = _.template(tpl.get('template-jobitem-view'));
        //this.modelBinder = new Backbone.ModelBinder();
    },
    render:function (eventName) {
        $(this.el).html(this.template(this.model.toJSON()));

        /* modelBinder can do data transform/conversion, but can only bind to html tag
        $(this.el).html(this.template);
        this.modelBinder.bind(this.model, this.el);
        */
        return this;
    },
    events: {
        'click a.favorite': function(e) { this.model.toggle_favorite(); e.preventDefault(); } ,
        'click a.jobdetail': "jobdetail",
    },

    jobdetail: function(e) {
        TiaonaerApp.vent.trigger("jobdetail:view", this.model);
    },
});

TiaonaerApp.Views.JobListView = Marionette.View.extend({
    id: "joblist_page",
    model: TiaonaerApp.Models.Job,

    initialize:function () {
        console.log("JobListView's initialize");
        this.template = _.template(tpl.get('template-joblist-view'));
        this.collection =  new TiaonaerApp.Collections.JobList();
        this.listenTo(this.collection, "reset", this.collectionReset);
        this.listenTo(this.collection, "change", this.collectionReset);
        this.collection.fetch({reset:true});
        this.fetchtype = 0; // initial fetch, render pagination bar
    },

    collectionReset: function() {
        console.log("enter collectionReset");
        this.$('#joblist').empty();
        _.each(this.collection.models, function (jobitem) {
                this.$('#joblist').append(new TiaonaerApp.Views.JobItemView({model:jobitem}).render().el);
        }, this);
        this.$('#joblist').listview().listview('refresh');
        this.$('.joblist_iscroller_wrapper').iscrollview().iscrollview("refresh");

        if ( this.fetchtype === 0 ) {
            var self = this;
            $(".pagination", this.el).pagination({
                items: self.collection.state.totalRecords,
                itemsOnPage: 10,
                cssStyle: 'dark-theme',
                displayedPages: 3,
                onPageClick: function(page, event) {
                    event.preventDefault();
                    self.fetchtype = 1;
                    self.collection.getPage(page, {reset:true});
                }
            });
        }
    },

    //template: "#template-joblist-view",
    /*
    * CollectionView require ItemView, but
    * if itemView is setup, will automatically populate subitems in the top el
    * itemView: TiaonaerApp.Views.JobItemView,
    */
    //itemViewContainer: ".page-content",

    render:function (eventName) {
        console.log("render joblist view");
        $(this.el).html(this.template());
        this.collectionReset();
        return this;
    },
});

TiaonaerApp.Views.JobDetailView = Marionette.View.extend({
    id: "jobdetail_page",
    model: TiaonaerApp.Models.Job,
    initialize:function() {
        console.log("JobDetailView's initialize");
        this.template = _.template(tpl.get('template-jobdetail-view'));
        this.listenTo(this.model, "change", this.modelAttrChanged);
    },
    events: {
        'click a.goback': function(e) {console.log("before goback"); window.history.back();console.log("after goback"); e.preventDefault(); },
        'click a.favorite': function(e) { this.model.toggle_favorite(); e.preventDefault(); },
        'click button.complain': function(e) {
             this.model.complain($('#complain_type :selected', this.el).val());
             e.preventDefault();
        }
    },
    switchModel: function(model) {
        this.stopListening(this.model);
        this.model = model;
        this.listenTo(this.model, "change", this.modelAttrChanged);
    },
    modelAttrChanged: function() {
        console.log("JobDetailView's modelAttrChanged");
        this.render();
    },
    render:function (eventName) {
        $(this.el).html(this.template(this.model.toJSON()));
        // to let jqm enhance widgets
        $(this.el).trigger('create');
        $('.iscroll-wrapper', this.el).iscrollview().iscrollview("refresh");
        $('#complain_type', this.el).selectmenu({inline: true});
        $('.ui-footer', this.el).toolbar('refresh');
        return this;
    },
});


