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

Backbone.Marionette.TemplateCache.prototype.compileTemplate = function(rawTemplate){
    return rawTemplate;
};

//Configures Backbone.Marionette to render the Handlebar template with the given data
Backbone.Marionette.Renderer.render = function(template, data){
    var handleBarsTemplate = Handlebars.compile($(template).html());
    return handleBarsTemplate(data);
};

var TiaonaerApp = new Backbone.Marionette.Application();

TiaonaerApp.addRegions({
    mainRegion: "#view-content",
    messageRegion: "#message-holder",
    modalRegion: "#dialog-holder"
});

TiaonaerApp.Collections = {};
TiaonaerApp.Controllers = {};
TiaonaerApp.Models = {};
TiaonaerApp.Translations = {};
TiaonaerApp.Vents = {};
TiaonaerApp.Views = {};
TiaonaerApp.ViewInstances = {};

TiaonaerApp.SearchFilter = {};

TiaonaerApp.Pagination = {
    order: 1, //1 means descending and -1 ascending order
    pageSize: 10,
    sortProperty: "id"
}

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

TiaonaerApp.user = 'anonymous';
TiaonaerApp.isAnonymousUser = function() {
    if (TiaonaerApp.user === 'anonymous') {
        window.log("User is anonymous");
        return true;
    }
    return false;
}

TiaonaerApp.addInitializer(function(){
    TiaonaerApp.getLoggedInUser(TiaonaerApp.showLogoutLinkAndSearchForm);
    this.firstPage = true;
});
/*
TiaonaerApp.addInitializer(function(){
    $(".navbar").on("keypress", ".search-query", function(e) {
        if (e.keyCode == 13) {
            TiaonaerApp.vent.trigger("todo:search", $(this).val());
        }
    });
});
*/
TiaonaerApp.getLoggedInUser = function(callback) {
    $.ajax({
        async: false,
        type: "GET",
        url: "/api/user",
        success: function(data) {
            if (data.username) {
                console.log("Found logged in user: ", data);
                TiaonaerApp.user = new TiaonaerApp.Models.User(data);
                if (callback) {
                    callback();
                }
            }
            else {
                console.log("Logged in user was not found.")
            }
        }
    });
}

TiaonaerApp.setUserAsAnonymous = function() {
    $("#logout-link-holder").addClass("hidden");
    $("#search-form-holder").addClass("hidden");
    TiaonaerApp.user = 'anonymous';
}

TiaonaerApp.showLogoutLinkAndSearchForm = function() {
    $("#logout-link-holder").removeClass("hidden");
    $("#search-form-holder").removeClass("hidden");
};

//Helper function to switch page
TiaonaerApp.showPage = function (page, firsttime) {
    console.log('changePage');
    if ( firsttime ) {
        $(page.el).attr('data-role', 'page');
        page.render();
        $('body').append($(page.el));
    } else {
        // let view decide whether need render, sometimes dont need render.
    }
/*
    var transition = $.mobile.defaultPageTransition;
    // We don't want to slide the first page
    if (this.firstPage) {
        transition = 'none';
        this.firstPage = false;
    }
    $.mobile.changePage($(page.el), {changeHash:true, transition: transition});
*/
    $.mobile.changePage($(page.el));
};

$(document).bind('ajaxStart', function() {
    console.log("ajaxStart");
    TiaonaerApp.spinner.spin(document.getElementById('activity-indicator'));
}).bind('ajaxError', function(event, request ,settings) {
    console.log('ajaxError with status code: ', request.status);

    TiaonaerApp.spinner.stop();

    if (request.status != 400) {
        if (request.status == 404) {
            TiaonaerApp.vent.trigger("error:404");
        }
        else if (request.status == 401) {
            if (TiaonaerApp.user !== 'anonymous') {
                console.log("Found user: ", TiaonaerApp.user)
                TiaonaerApp.vent.trigger("error:notAuthorized");
            }
            else {
                if (request.statusText === "Bad credentials") {
                    console.log("Login failed.")
                    TiaonaerApp.vent.trigger("user:loginFailed");
                }
                else {
                    console.log("User is anonymous")
                    TiaonaerApp.vent.trigger("user:login");
                }
            }
        }
        else {
            TiaonaerApp.vent.trigger("error:error");
        }
    }
}).bind('ajaxStop', function() {
    console.log("ajaxStop");
    TiaonaerApp.spinner.stop();
});

$(document).ready(function(){
    console.log("document.ready");
    i18n.init({
        debug: true,
        resStore: TiaonaerApp.Translations.resources
    });

    tpl.loadTemplates([
            'template-home-view',
            'template-jobitem-view',
            'template-joblist-view',
            'template-jobdetail-view',
            'template-jobsearch-view',
            'template-favoriteitem-view',
            'template-favoritelist-view'
        ],
        function () {
            console.log("before TiaonaerApp.start");
            TiaonaerApp.start();
            console.log("After TiaonaerApp.start");
        });
});

