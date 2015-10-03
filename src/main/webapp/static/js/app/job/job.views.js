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

    events: {
        'click #jobsearch' : function(e) { TiaonaerApp.vent.trigger("job:search"); e.preventDefault();}
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
        } else if ( this.fetchtype === 3 ) {
            $(".pagination", this.el).pagination('updateItems', this.collection.state.totalRecords);
        }
    },

    updateCollection: function(filters) {
        this.collection.state.currentPage = 1;
        var defqs={
            currentPage: "page.page",
            pageSize: "page.size",
            sortKey: "page.sort",
            order: "page.sort.dir",
            directions: 1,
        };
        this.collection.queryParams = _.extend(defqs, filters);
        this.fetchtype = 3;
        this.collection.fetch({reset:true});
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

Date.prototype.minusDays = function(days) {
    this.setDate(this.getDate() - days);
    return this;
};

TiaonaerApp.Views.JobSearchView = Backbone.View.extend({
    id: "jobsearch_page",
    initialize:function() {
        console.log("JobSearchView's initialize");
        this.template = _.template(tpl.get('template-jobsearch-view'));
        // in order to decouple between joblist and jobsearch view, use ajax directly.
        var self = this;
        $.ajax({
            url: TiaonaerApp.ServiceUrl + "/api/job?facet=true",
            dataType: 'json',
            xhrFields: {
                withCredentials: true
            },
            crossDomain: true,
            success:  function(facets) {
                self.GotData(facets);
            }
        });
    },

    render:function () {
        $(this.el).html(this.template());
        $(this.el).trigger('create');
    },

    GotData: function(facets) {
        var jobfilter_company_ctlgrp=$('#jobfilter_company_ctlgrp', this.el);
        jobfilter_company_ctlgrp.controlgroup().controlgroup("option", "type", "vertical");
        var index = 0;
        _.each(_.keys(facets.companies), function(key) {
            var val = facets.companies[key];
            var $el = $("<label for='jf_company_cb-"+index+"'>"+key+"("+val+")" + "</label><input id='jf_company_cb-" + index + "' type='checkbox' value='"+key+"'></input>");
            jobfilter_company_ctlgrp.controlgroup("container")['append']($el);
            $($el[1]).checkboxradio();
            $($el[1]).parents(".ui-checkbox").attr("data-filtertext", makePy(key));
            index++;
        });
        $('.company_iscroller_wrapper', this.el).iscrollview().iscrollview("refresh");

        //$('#page_jobfilter_location_iscroller_wrapper').css("z-index", 3);
        var jobfilter_location_ctlgrp=$('#jobfilter_location_ctlgrp', this.el);
        jobfilter_location_ctlgrp.controlgroup().controlgroup("option", "type", "vertical");
        index = 0;
        _.each(_.keys(facets.locations), function(key) {
            var val = facets.locations[key];
            var $el = $("<label for='jf_location_cb-"+index+"'>"+key+"("+val+")" + "</label><input id='jf_location_cb-" + index + "' type='checkbox' value='"+key+"'></input>");
            jobfilter_location_ctlgrp.controlgroup("container")['append']($el);
            $($el[1]).checkboxradio();
            $($el[1]).parents(".ui-checkbox").attr("data-filtertext", makePy(key));
            index++;
        });
        $('.location_iscroller_wrapper', this.el).iscrollview().iscrollview("refresh");

        return this;
    },

    events: {
        'click [data-role="navbar"] a': "refresh_iscroll",
        'click #jobfilter_reset': "reset_filter",
        'click #jobfilter_search': "job_filter",
    },

    refresh_iscroll: function(event) {
        var tab = $(event.target).attr('href');
        var wrapper = $(tab, this.el).find('.iscroll-wrapper');
        if ( wrapper ) {
            wrapper.resize();
            wrapper.iscrollview("refresh");
        }
    },

    reset_filter: function(e) {
        $('input[type="checkbox"]', this.el).attr('checked',false).checkboxradio("refresh");

        $('input[type="radio"]:first', this.el).prop("checked", true);
        $('input[type="radio"]', this.el).checkboxradio("refresh");

        $('#jobfilter_keyword', this.el).val("");

        e.preventDefault();
    },

    job_filter: function(e) {
        console.log("enter job_filter");
        filters = {};

        var company="";
        var first = true;
        $('#jobfilter_company_ctlgrp input[type="checkbox"]:checked', this.el).each(function () {
            var value = $(this).val();
            if ( first ) {
                company += 'job_company:(';
                first = false;
            } else {
                company += ' OR ';
            }
            //Dont need encode here, navigate will decode it again anyway.
            //company += "\"" + encodeURIComponent(value) + "\"";
            company += "\"" + value + "\"";
        });
        if ( company.length > 0) company += ')';

        var location="";
        first = true;
        $('#jobfilter_location_ctlgrp input[type="checkbox"]:checked', this.el).each(function () {
            var value = $(this).val();
            if ( first ) {
                location += 'job_location:(';
                first = false;
            } else {
                location += ' OR ';
            }
            location += value;
        });
        if ( location.length > 0) location += ')';

        var date = $('#jobfilter_date_ctlgrp input[type="radio"]:checked', this.el).val();

        var fqstr = "";

        if ( company.length > 0 ) {
            if ( fqstr.length > 0 ) fqstr += ' AND ';
            fqstr += company;
        }

        if ( location.length > 0 ) {
            if ( fqstr.length > 0 ) fqstr += ' AND ';
            fqstr += location;
        }

        if ( date > -1 ) {
            var startDate = (new Date()).minusDays(date).toISOString();
            if ( fqstr.length > 0 ) fqstr += ' AND ';
            fqstr += "job_post_date:[" + startDate + " TO *]";
        }

        // filter items with job_expired: true, might offer UI later.
        if ( fqstr.length > 0 ) fqstr += ' AND ';
        fqstr += "job_expired:false";

        fqstr = "("+fqstr+")";

        filters['fq'] = fqstr;

        var keyword =  $('#jobfilter_keyword', this.el).val();
        if ( !keyword || /^\s*$/.test(keyword) )  {
        }  else {
            filters['q'] = keyword;
        }

        console.log(filters);

        TiaonaerApp.vent.trigger("job:filter", filters);
        e.preventDefault();
    }
});

