window.debug = true;

// usage: log('inside coolFunc', this, arguments);
// paulirish.com/2009/log-a-lightweight-wrapper-for-consolelog/
window.log = function(){
    if(window.debug){
        log.history = log.history || []; // store logs to an array for reference
        log.history.push(arguments);
        if(this.console) {
            arguments.callee = arguments.callee.caller;
            var newarr = [].slice.call(arguments);
            (typeof console.log === 'object' ? log.apply.call(console.log, console, newarr) : console.log.apply(console, newarr));
        }
    }
};

/* for IE9 cors issue ( not support XmlHttpRequest ) */
$.support.cors = true;

/* for ajax request from backbone */
(function() {
  var proxiedSync = Backbone.sync;
  Backbone.sync = function(method, model, options) {
    options || (options = {});
    if (!options.crossDomain) {
      options.crossDomain = true;
    }
    if (!options.xhrFields) {
      options.xhrFields = {withCredentials:true};
    }
    return proxiedSync(method, model, options);
  };
})();

var TiaonaerApp = new Backbone.Marionette.Application();

TiaonaerApp.Collections = {};
TiaonaerApp.Controllers = {};
TiaonaerApp.Models = {};
TiaonaerApp.Translations = {};
TiaonaerApp.Vents = {};
TiaonaerApp.Views = {};
TiaonaerApp.ViewInstances = {};
TiaonaerApp.ViewContainer = new Backbone.ChildViewContainer();

TiaonaerApp.ServiceUrl = "https://192.168.137.128/jobws";

TiaonaerApp.spinner = new Spinner({
    lines: 13, // The number of lines to draw
    length: 30, // The length of each line
    width: 7, // The line thickness
    radius: 19, // The radius of the inner circle
    corners: 1, // Corner roundness (0..1)
    rotate: 0, // The rotation offset
    color: '#000', // #rgb or #rrggbb
    speed: 1, // Rounds per second
    trail: 60, // Afterglow percentage
    shadow: false, // Whether to render a shadow
    hwaccel: false, // Whether to use hardware acceleration
    className: 'spinner', // The CSS class to assign to the spinner
    zIndex: 2e9, // The z-index (defaults to 2000000000)
    top: '100', // Top position relative to parent in px
    left: 'auto' // Left position relative to parent in px
});

TiaonaerApp.isAnonymousUser = function() {
    var UserHomeView = TiaonaerApp.ViewContainer.findByCustom("UserHomeView");
    if ( !UserHomeView || !UserHomeView.isUserLoggedIn() ) {
        console.log('isAnonymousUser');
        return true;
    } else {
        return false;
    }
};

TiaonaerApp.showView = function(viewName, model) {
    var view = TiaonaerApp.ViewContainer.findByCustom(viewName);
    if ( !view ) {
        switch ( viewName ) {
            case "UserHomeView":
                view = new TiaonaerApp.Views.UserHomeView();
                break;
            case "LoginView":
                view = new TiaonaerApp.Views.LoginView();
                break;
            case "JobListView":
                view = new TiaonaerApp.Views.JobListView();
                break;
            case "JobSearchView":
                view = new TiaonaerApp.Views.JobSearchView();
                break;
            case "JobDetailView":
                view = new TiaonaerApp.Views.JobDetailView({model:model});
                break;
            case "FavoriteListView":
                view = new TiaonaerApp.Views.FavoriteListView();
                break;
            case "ComplainListView":
                view = new TiaonaerApp.Views.ComplainListView();
                break;
            case "ComplainDetailView":
                view = new TiaonaerApp.Views.ComplainDetailView({model:model});
                break;
            case "ComplainAdminListView":
                view = new TiaonaerApp.Views.ComplainAdminListView();
                break;
            case "ComplainAdminDetailView":
                view = new TiaonaerApp.Views.ComplainAdminDetailView({model:model});
                break;
        }
        $(view.el).attr('data-role', 'page');
        view.render();
        $('body').append($(view.el));
        TiaonaerApp.ViewContainer.add(view, viewName);
    } else {
        if ( model ) {
            switch ( viewName ) {
                case "JobDetailView":
                case "ComplainDetailView":
                case "ComplainAdminDetailView":
                    view.switchModel(model);
                    view.render();
                    break;
            }

        }
    }
    $.mobile.changePage($(view.el));
};

$(document).bind('ajaxStart', function() {
    console.log("ajaxStart");
    TiaonaerApp.spinner.spin(document.getElementById('activity-indicator'));
}).bind('ajaxError', function(event, request ,settings) {
    console.log('ajaxError with status code: ', request.status);
    TiaonaerApp.spinner.stop();

    if (request.status !== 400) {
        if (request.status === 404) {
            TiaonaerApp.vent.trigger("error:404");
        } else if (request.status === 401) {
            if ( !TiaonaerApp.isAnonymousUser() ) {
                TiaonaerApp.vent.trigger("error:notAuthorized");
            } else {
                if (request.statusText === "Bad credentials") {
                    console.log("Login failed.")
                    TiaonaerApp.vent.trigger("user:loginFailed");
                } else {
                    console.log("User is anonymous")
                    TiaonaerApp.vent.trigger("user:login");
                }
            }
        } else {
            TiaonaerApp.vent.trigger("error:error");
        }
    }
}).bind('ajaxStop', function() {
    console.log("ajaxStop");
    TiaonaerApp.spinner.stop();
});

$(document).ready(function(){
    console.log("document.ready");
    /*
    i18n.init({
        debug: true,
        resStore: TiaonaerApp.Translations.resources
    });
    */
    /*tpl.loadTemplates([
            'template-home-view',
            'template-jobitem-view',
            'template-joblist-view',
            'template-jobdetail-view',
            'template-jobsearch-view',
            'template-favoriteitem-view',
            'template-favoritelist-view',
            'template-complainitem-view',
            'template-complainlist-view',
            'template-complainitem-admin-view',
            'template-complainlist-admin-view',
            'template-complain-detail-view',
            'template-complain-admin-detail-view',
            'template-login-view'
        ],
        function () {
            TiaonaerApp.start();
        });
    */
    TiaonaerApp.start();
});

