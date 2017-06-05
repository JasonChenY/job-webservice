window.debug = true;
if( !window.debug ){
    window.console = {log : function(){}};
}

// usage: log('inside coolFunc', this, arguments);
// paulirish.com/2009/log-a-lightweight-wrapper-for-consolelog/
window.log = function(){
    log.history = log.history || [];   // store logs to an array for reference
    log.history.push(arguments);
    if(this.console){
        console.log( Array.prototype.slice.call(arguments) );
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

var App = new Backbone.Marionette.Application();

/* Base View for views sensitive to different users */
App.View = Backbone.View.extend({
    constructor: function() {
        this.valid = true;
        Backbone.View.prototype.constructor.apply(this, arguments);
    },
    getValid: function() { return this.valid; },
    setValid: function(valid) { this.valid = valid;}
});
/*
_.extend(App.View.prototype, {
    getValid: function() { return this.valid; },
    setValid: function(valid) { this.valid = valid;}
});
*/

App.Collections = {};
App.Controllers = {};
App.Models = {};
App.Translations = {};
App.Vents = {};
App.Views = {};
App.ViewContainer = new Backbone.ChildViewContainer();

App.ServerHost = "http://www.tiaonr.com";
App.ServiceUrl = App.ServerHost + "/jobws";
/*
App.spinner = new Spinner({
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
*/
App.isAnonymousUser = function() {
    var UserHomeView = App.ViewContainer.findByCustom("UserHomeView");
    if ( !UserHomeView || !UserHomeView.isUserLoggedIn() ) {
        console.log('isAnonymousUser');
        return true;
    } else {
        return false;
    }
};

App.getUserRole = function() {
    var UserHomeView = App.ViewContainer.findByCustom("UserHomeView");
    if ( !UserHomeView || !UserHomeView.isUserLoggedIn() ) {
        return 'ROLE_ANONYMOUS';
    } else {
        return UserHomeView.getUserRole();
    }
};

App.showView = function(viewName, model) {
    var view = App.ViewContainer.findByCustom(viewName);
    if ( !view ) {
        switch ( viewName ) {
            case "UserHomeView":
                view = new App.Views.UserHomeView();
                break;
            case "LoginView":
                /* dont need this any more, we trust the certification
                if ( isCordovaApp() ) {
                    VerifySslCertificate(App.ServerHost, "DA DC D0 FC B3 58 AD B9 56 CA 07 E1 E2 EB 5C F0 BB 5A F2 64");
                }
                */
                view = new App.Views.LoginView();
                break;
            case "UserRegisterView":
                view = new App.Views.UserRegisterView();
                break;
            case "JobListView":
                view = new App.Views.JobListView();
                break;
            case "JobSearchView":
                view = new App.Views.JobSearchView();
                break;
            case "JobDetailView":
                view = new App.Views.JobDetailView({model:model});
                break;
            case "JobInfoView":
                view = new App.Views.JobInfoView({model:model});
                break;
            case "FavoriteListView":
                view = new App.Views.FavoriteListView();
                break;
            case "ComplainListView":
                view = new App.Views.ComplainListView();
                break;
            case "ComplainDetailView":
                view = new App.Views.ComplainDetailView({model:model});
                break;
            case "ComplainAdminListView":
                view = new App.Views.ComplainAdminListView();
                break;
            case "ComplainAdminDetailView":
                view = new App.Views.ComplainAdminDetailView({model:model});
                break;
            case "AppDownloadView":
                view = new App.Views.AppDownloadView();
                break;
            case "AppAboutView":
                view = new App.Views.AppAboutView();
                break;
        }
        $(view.el).attr('data-role', 'page');
        view.render();
        $('body').append($(view.el));
        App.ViewContainer.add(view, viewName);
    } else {
        if ( model ) {
            switch ( viewName ) {
                case "JobDetailView":
                case "JobInfoView":
                case "ComplainDetailView":
                case "ComplainAdminDetailView":
                    view.switchModel(model);
                    break;
            }
        } else {
            if ( view.getValid && !view.getValid() ) {
                view.setValid(true);
                view.resetForNewUser();
            }
        }
    }
/*
    strange behavior if handled in this common place:
    $('a[data-rel="back"]', $(view.el)).click(function(event) {
        event.stopPropagation();
        //$.mobile.changePage($(this).attr('href'));
        //window.history.back();
        //$.mobile.back();
        Backbone.history.history.back();
        return true;
    });
*/
    $.mobile.changePage($(view.el));
    return view;
};

$(document).bind('ajaxStart', function() {
    //App.spinner.spin(document.getElementById('activity-indicator'));
}).bind('ajaxError', function(event, request ,settings) {
    console.log('ajaxError with status code: ', request.status);
    //App.spinner.stop();
    if ( request.status === 401 ) {
        App.vent.trigger("user:login");
    } else if ( request.status === 500 ) {
        alert("非常抱歉，服务目前暂不可用，稍后重试！");
    }
    /*
    if (request.status !== 400) {
        if (request.status === 404) {
            App.vent.trigger("error:404");
        } else if (request.status === 401) {
            if ( App.isAnonymousUser() ) {
                App.vent.trigger("user:login");
            } else {
                App.vent.trigger("error:notAuthorized");
            }
        } else {
            App.vent.trigger("error:error");
        }
    }
    */
}).bind('ajaxStop', function() {
    //App.spinner.stop();
});

function checkUpdate(){
    var url = App.ServiceUrl + '/api/checkupdate?device=' + window.device.platform;
    cordova.getAppVersion.getVersionCode(function(versionCode) {
        $.get(url,{},function(version){
            if (versionCode < version.versionCode) {
                App.appVersion = version; //saved for later usage.
                var r = confirm(version.description);
                if(r){
                    window.open(version.location, '_system', 'location=yes');
                }
            }
        });
    });
}

$(document).ready(function(){
    /*
    i18n.init({
        debug: true,
        resStore: App.Translations.resources
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
            App.start();
        });
    */

    App.start();

    if ( isCordovaApp() ) {
       setTimeout(function(){ checkUpdate(); },50);
    }
});

