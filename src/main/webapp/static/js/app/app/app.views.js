TiaonaerApp.Views.AppDownloadView = Backbone.View.extend({
    id: 'app-download-page',
    template: "#template-app-download-view",
    initialize:function () {
        this.template = Marionette.TemplateCache.get(Marionette.getOption(this, "template"));
    },
    render:function () {
        $(this.el).html(this.template());
        if ( isCordovaApp() ) {
            $("#download-android", this.el).attr("href", TiaonaerApp.ServiceUrl + "/" + $("#download-android", this.el).attr("href"));
            $("#download-ios", this.el).attr("href", TiaonaerApp.ServiceUrl + "/" + $("#download-ios", this.el).attr("href"));
        };
        return this;
    },
});

TiaonaerApp.Views.AppAboutView = Backbone.View.extend({
    id: 'app-about-page',
    template: "#template-app-about-view",
    initialize:function () {
        this.template = Marionette.TemplateCache.get(Marionette.getOption(this, "template"));
    },
    render:function () {
        $(this.el).html(this.template());
        var self=this;
        cordova.getAppVersion.getVersionNumber(function(versionNumber) {
            $('#app-cur-version', self.el).html('v'+versionNumber);
        });
        if ( TiaonaerApp.appVersion ) {
           $("#app-about-version-p", this.el).css("color","red");
           $("#app-about-version-p", this.el).html('有新版本');
        }
        return this;
    },
    events: {
        'click a#app-about-version': "app_about_version"
    },
    app_about_version: function(e) {
        if ( TiaonaerApp.appVersion ) {
            var r = confirm(TiaonaerApp.appVersion.description);
            if(r){
               window.open(TiaonaerApp.appVersion.location, '_system', 'location=yes');
            }
        }
    }
});