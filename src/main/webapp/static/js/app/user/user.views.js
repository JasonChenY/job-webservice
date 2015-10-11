TiaonaerApp.Views.LoginView = Backbone.View.extend({
    id: 'login-page',
    template: '#template-login-view',
    initialize:function () {
        this.template = Marionette.TemplateCache.get(Marionette.getOption(this, "template"));
    },

    render:function (eventName) {
        $(this.el).html(this.template());
        $('#for-display-error', this.el).hide();
        return this;
    },

    events: {
        "click #btn-user-register": function() { TiaonaerApp.vent.trigger("user:register"); },
        "click #btn-user-login": "login",
        "click #btn-login-cancel": "login_cancel"
    },

    login: function() {
        console.log("Log in");
        $('#for-display-error', this.el).hide();
        var user = {};
        user.username = $("#user-username", this.el).val();
        user.password = $("#user-password", this.el).val();

        //var pathname = $(location).attr('pathname');
        //var path = pathname.substring(0, pathname.lastIndexOf('/'));

        /*$.post(TiaonaerApp.ServiceUrl + "/api/login", user, function(){
            console.log("Trigger user:loginSuccess");
            TiaonaerApp.vent.trigger("user:loginSuccess");
        });*/
        var self = this;
        $.ajax({
            type: "POST",
            url: TiaonaerApp.ServiceUrl + "/api/login",
            data: user,
            xhrFields: {
                withCredentials: true
            },
            crossDomain: true,
            success: function(data, status, xhr){
                console.log("Trigger user:loginSuccess");
                TiaonaerApp.vent.trigger("user:loginSuccess");
            },
            error: function(data, status, xhr) {
                self.login_failed(data);
            }/*,
            complete: function(data, status, xhr) {
                self.login_failed(data);
            }*/
        });
    },

    login_cancel : function() {
        // Not use window.history.back() or window.history.go(-2)
        // Because we might be here from homeview->...   or homeview->favorite/list->...
        // Might consider introduce source view later.
        $('#for-display-error', this.el).hide();
        Backbone.history.navigate("#/");
    },

    login_failed: function(data, status, xhr) {
        var reason = data.getResponseHeader("Reason");
        if (reason === "Bad credentials") {

        }
        $('#for-display-error', this.el).show();
    }
});

TiaonaerApp.Views.UserHomeView = Backbone.View.extend({
    /* Change to use el direclty, put a div with specified id directly in index.html
     * this way save some logic in showPage, dont need to set the "Page" class in el, and add el to body
     * el: '#home-page',
     * Revert Back again to use id, refer to comments in JobSearchView: render.
     */
    id: 'home-page',
    template: "#template-home-view",
    model: TiaonaerApp.Models.User,
    initialize:function () {
        //this.template = _.template(tpl.get('template-home-view'));
        this.model = new TiaonaerApp.Models.User();
        this.template = Marionette.TemplateCache.get(Marionette.getOption(this, "template"));
        // Cant listen to model here, we are using a fake model, without url.
        //this.listenTo(this.model, "change", this.render());
    },

    render:function () {
        $(this.el).html(this.template(this.model.toJSON()));
        $(this.el).trigger('create');
        return this;
    },

/*  bindUIElements not work for manual template
    ui: {
        job: '#btn_job',
        favorite: '#btn_favorite',
    },
*/
    events: {
        'click #btn_job': function() { TiaonaerApp.vent.trigger("job:list"); },
        'click #btn_favorite': function() { TiaonaerApp.vent.trigger("favorite:list"); },
        'click #btn_complain': function() { TiaonaerApp.vent.trigger("complain:list"); },
        'click #btn_complain_admin': function() { TiaonaerApp.vent.trigger("complain:adminlist"); },
        'click #btn_login': function() { TiaonaerApp.vent.trigger("user:login"); },
        'click #btn_logout': function() { TiaonaerApp.vent.trigger("user:logout"); }
    },

    isUserLoggedIn: function () {
        return !this.model.isAnonymousUser();
    },

    userLoggedIn: function(user) {
        this.model.set({
            username: user.username,
            role: user.role,
            when: _.now()
        });
        console.log(this.model.toJSON());
        this.render();
    },

    userLoggedOut: function() {
        this.model.set({
            username: "anonymous",
            role: "ROLE_ANONYMOUS",
            when: _.now()
        });
        this.render();

        TiaonaerApp.ViewContainer.apply("setValidForCurrentUser", [false]);
    }
});

TiaonaerApp.Views.UserRegisterView = Backbone.View.extend({
    id: 'user-register-page',
    template: '#template-user-register-view',
    initialize:function () {
        this.template = Marionette.TemplateCache.get(Marionette.getOption(this, "template"));
    },

    render:function (eventName) {
        $(this.el).html(this.template());
        $('#for-display-error', this.el).hide();
        return this;
    },

    events: {
        "click #btn-user-register": "register",
        "click #btn-register-cancel": "register_cancel"
    },

    register: function() {
        console.log("register");
        $('#for-display-error', this.el).hide();
        var user = {
            username: $("#user-username", this.el).val(),
            password: $("#user-password", this.el).val()
        };
        var self = this;
        $.ajax({
            type: "POST",
            url: TiaonaerApp.ServiceUrl + "/api/register",
            data: JSON.stringify(user),
            contentType: "application/json; charset=UTF-8",
            xhrFields: {
                withCredentials: true
            },
            crossDomain: true,
            success: function(data, status, xhr){
                console.log("Trigger user:registerSuccess");
                TiaonaerApp.vent.trigger("user:registerSuccess", data);
            },
            error: function(data, status, xhr) {
                self.register_failed(data);
            }
        });
    },

    register_cancel : function() {
        $('#for-display-error', this.el).hide();
        Backbone.history.navigate("#/");
    },

    register_failed: function(error) {
        //Duplicate entry 'chenyong4' for key 'PRIMARY'
        var reg = /Duplicate entry \'([^\']+)\' for key/g;
        if ( reg.exec(error.responseText) ) {
           var userid = RegExp.$1;
           $('#display-error', this.el).text(userid + " 已被占用，换个名字试试！");
        }

        //if ( error.responseText.indexOf('Duplicate entry') === 0 ) {
        //    $('#display-error', this.el).text($("#user-username", this.el).val() + "已被占用，换个名字试试！");
        //}
        $('#for-display-error', this.el).show();
    }
});