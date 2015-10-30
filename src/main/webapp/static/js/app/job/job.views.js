TiaonaerApp.Views.JobItemView = Backbone.View.extend({
    tagName: "li",
    template: '#template-jobitem-view',
    initialize:function () {
        this.template = Marionette.TemplateCache.get(Marionette.getOption(this, "template"));
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
        'click a.favorite': "favorite",
        'click a.jobdetail': "jobdetail"
    },

    favorite: function(e) {
        this.model.toggle_favorite();
        var listview = TiaonaerApp.ViewContainer.findByCustom("FavoriteListView");
        if (listview) listview.setValid(false);
        e.preventDefault();
    },

    jobdetail: function(e) {
        TiaonaerApp.vent.trigger("jobdetail:view", this.model);
    }
});

TiaonaerApp.Views.JobListView = TiaonaerApp.View.extend({
    id: "joblist-page",
    template: '#template-joblist-view',
    model: TiaonaerApp.Models.Job,

    initialize:function () {
        console.log("JobListView's initialize");
        this.template = Marionette.TemplateCache.get(Marionette.getOption(this, "template"));
        this.collection =  new TiaonaerApp.Collections.JobList();
        this.listenTo(this.collection, "reset", this.collectionSwitched);
        this.listenTo(this.collection, "change", this.collectionSwitched);
        this.switchCollection();
    },

    events: {
        'click #jobsearch' : function(e) { TiaonaerApp.vent.trigger("job:search"); e.preventDefault();}
    },

    collectionSwitched: function() {
        console.log("enter collectionSwitched");

        this.$('#joblist').empty();
        _.each(this.collection.models, function (jobitem) {
            this.$('#joblist').append(new TiaonaerApp.Views.JobItemView({model:jobitem}).render().el);
        }, this);
        this.$('#joblist').listview().listview('refresh');
        this.$('.joblist_iscroller_wrapper').iscrollview().iscrollview("refresh");

        if ( this.fetch_type === 0 ) {
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
                    event.preventDefault();
                    self.fetch_type = 1;
                    self.collection.getPage(page, {reset:true});
                }
            });
        }
    },

    resetForNewUser: function() { this.switchCollection();},

    switchCollection: function(filters) {
        var defqs={
            currentPage: "page.page",
            pageSize: "page.size",
            sortKey: "page.sort",
            order: "page.sort.dir",
            directions: 1,
            fq: "job_expired:false"
        };
        filters = filters || {};
        this.collection.queryParams = _.extend(defqs, filters);
        this.fetch_type = 0;
        this.collection.state.currentPage = 1;
        this.collection.fetch({reset:true});
    },

    render:function (eventName) {
        console.log("render joblist view");
        $(this.el).html(this.template());
        return this;
    }
});

TiaonaerApp.Views.JobDetailView = Backbone.View.extend({
    id: "jobdetail-page",
    template: '#template-jobdetail-view',
    model: TiaonaerApp.Models.Job,
    initialize:function() {
        console.log("JobDetailView's initialize");
        this.template = Marionette.TemplateCache.get(Marionette.getOption(this, "template"));
        this.listenTo(this.model, "change", this.modelAttrChanged);
    },
    events: {
        'click a.job_url': "goto_job_url",
        'click a.favorite': "favorite",
        'click button.complain': "complain"
    },
    goto_job_url: function(e) {
        //window.open(e.target.href, '_blank', 'location=yes,fullscreen=yes,scrollbars=yes');
        window.open(e.target.href);
        e.preventDefault();
    },
    favorite: function(e) {
        this.model.toggle_favorite();
        var listview = TiaonaerApp.ViewContainer.findByCustom("FavoriteListView");
        if (listview) listview.setValid(false);
        e.preventDefault();
    },
    complain: function(e) {
        this.model.complain($('#complain_type :selected', this.el).val());
        var listview = TiaonaerApp.ViewContainer.findByCustom("ComplainListView");
        if (listview) listview.setValid(false);
        e.preventDefault();
    },
    switchModel: function(model) {
        this.stopListening(this.model);
        this.model = model;
        this.listenTo(this.model, "change", this.modelAttrChanged);
        this.render();
    },
    modelAttrChanged: function() {
        console.log("JobDetailView's modelAttrChanged");
        this.render();
    },
    render:function (eventName) {
        $(this.el).html(this.template(this.model.toJSON()));
        $(this.el).trigger('create');
        // to let jqm enhance widgets
        $('.iscroll-wrapper', this.el).iscrollview().iscrollview("refresh");
        $('#complain_type', this.el).selectmenu({inline: true});
        $('.ui-footer', this.el).toolbar('refresh');
        return this;
    }
});

Date.prototype.minusDays = function(days) {
    this.setDate(this.getDate() - days);
    return this;
};

TiaonaerApp.Views.JobSearchView = TiaonaerApp.View.extend({
    id: "jobsearch-page",
    template: '#template-jobsearch-view',
    initialize:function() {
        console.log("JobSearchView's initialize");
        this.template = Marionette.TemplateCache.get(Marionette.getOption(this, "template"));
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
        /*!!!Strange problem!!!:
         * It is possible to pre-add the PAGE DIV element in index.html,
         * and then use existing "el: '#div_id'" in Backbone.view to render the page.
         * BUT there is some problem with the "footer" element:
         *   - it is fine if PAGE DIV is first one in index.html
         *   - for other PAGE DIV, JQM trigger('create') will place the footer as global footer
         * Now the solution is fallback to let Backbone create the PAGE DIV with "id: 'div_id'",
         * then in showView to setup the "data-role: page" attribute,
         * in this way, it is fine to trigger('create') for either first or other PAGE DIV.
         */
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
        'click #jobfilter_search': "job_filter"
    },

    refresh_iscroll: function(event) {
        var tab = $(event.target).attr('href');
        var wrapper = $(tab, this.el).find('.iscroll-wrapper');
        if ( wrapper ) {
            wrapper.resize();
            wrapper.iscrollview("refresh");
        }
    },

    resetForNewUser: function() { this.reset_filter();},

    reset_filter: function(e) {
        $('input[type="checkbox"]', this.el).attr('checked',false).checkboxradio("refresh");

        $('#jobfilter_date_ctlgrp input[type="radio"]:first', this.el).prop("checked", true);
        $('#jobfilter_date_ctlgrp input[type="radio"]', this.el).checkboxradio("refresh");

        $('#jobfilter_keyword', this.el).val("");

        $('#jobfilter_expired_ctlgrp input[type="radio"]:first', this.el).prop("checked", true);
        $('#jobfilter_expired_ctlgrp input[type="radio"]', this.el).checkboxradio("refresh");

        if ( e ) e.preventDefault();
    },

    job_filter: function(e) {
        console.log("enter job_filter");
        var filters = {};

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
        var expired = $('#jobfilter_expired_ctlgrp input[type="radio"]:checked', this.el).val();
        if ( expired === 'true' ) {
           if ( fqstr.length > 0 ) fqstr += ' AND ';
           fqstr += "job_expired:true";
        }

        if ( fqstr.length > 0 ) {
           fqstr = "("+fqstr+")";
           filters['fq'] = fqstr;
        }

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

