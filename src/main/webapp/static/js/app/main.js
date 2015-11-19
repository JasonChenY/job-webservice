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

function ThirdPartyLoginInCallback(result) {
    if ( result === true || result === 'true' ) {
        TiaonaerApp.vent.trigger("user:loginSuccess");
    } else {
        TiaonaerApp.vent.trigger("user:loginFailed");
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

/* Base View for views sensitive to different users */
TiaonaerApp.View = Backbone.View.extend({
    constructor: function() {
        this.valid = true;
        Backbone.View.prototype.constructor.apply(this, arguments);
    },
    getValid: function() { return this.valid; },
    setValid: function(valid) { this.valid = valid;}
});
/*
_.extend(TiaonaerApp.View.prototype, {
    getValid: function() { return this.valid; },
    setValid: function(valid) { this.valid = valid;}
});
*/

TiaonaerApp.Collections = {};
TiaonaerApp.Controllers = {};
TiaonaerApp.Models = {};
TiaonaerApp.Translations = {};
TiaonaerApp.Vents = {};
TiaonaerApp.Views = {};
TiaonaerApp.ViewContainer = new Backbone.ChildViewContainer();

TiaonaerApp.ServerHost = "https://www.tiaonr.com";
TiaonaerApp.ServiceUrl = TiaonaerApp.ServerHost + "/jobws";
/*
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
*/
TiaonaerApp.isAnonymousUser = function() {
    var UserHomeView = TiaonaerApp.ViewContainer.findByCustom("UserHomeView");
    if ( !UserHomeView || !UserHomeView.isUserLoggedIn() ) {
        console.log('isAnonymousUser');
        return true;
    } else {
        return false;
    }
};

TiaonaerApp.getUserRole = function() {
    var UserHomeView = TiaonaerApp.ViewContainer.findByCustom("UserHomeView");
    if ( !UserHomeView || !UserHomeView.isUserLoggedIn() ) {
        return 'ROLE_ANONYMOUS';
    } else {
        return UserHomeView.getUserRole();
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
                /* dont need this any more, we trust the certification
                if ( isCordovaApp() ) {
                    VerifySslCertificate(TiaonaerApp.ServerHost, "DA DC D0 FC B3 58 AD B9 56 CA 07 E1 E2 EB 5C F0 BB 5A F2 64");
                }
                */
                view = new TiaonaerApp.Views.LoginView();
                break;
            case "UserRegisterView":
                view = new TiaonaerApp.Views.UserRegisterView();
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
            case "DownloadView":
                view = new TiaonaerApp.Views.DownloadView();
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
};

$(document).bind('ajaxStart', function() {
    //TiaonaerApp.spinner.spin(document.getElementById('activity-indicator'));
}).bind('ajaxError', function(event, request ,settings) {
    console.log('ajaxError with status code: ', request.status);
    //TiaonaerApp.spinner.stop();
    if ( request.status === 401 ) {
        TiaonaerApp.vent.trigger("user:login");
    } else if ( request.status === 500 ) {
        alert("非常抱歉，服务目前暂不可用，稍后重试！");
    }
    /*
    if (request.status !== 400) {
        if (request.status === 404) {
            TiaonaerApp.vent.trigger("error:404");
        } else if (request.status === 401) {
            if ( TiaonaerApp.isAnonymousUser() ) {
                TiaonaerApp.vent.trigger("user:login");
            } else {
                TiaonaerApp.vent.trigger("error:notAuthorized");
            }
        } else {
            TiaonaerApp.vent.trigger("error:error");
        }
    }
    */
}).bind('ajaxStop', function() {
    //TiaonaerApp.spinner.stop();
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

