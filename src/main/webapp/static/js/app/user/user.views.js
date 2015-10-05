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
        "click #btn-user-login": "login",
        "click #btn-login-cancel": "login_cancel"
    },

    login: function() {
        console.log("Log in");
        $('#for-display-error', this.el).hide();
        var user = {};
        user.username = $("#user-username").val();
        user.password = $("#user-password").val();

        //var pathname = $(location).attr('pathname');
        //var path = pathname.substring(0, pathname.lastIndexOf('/'));

        /*$.post(TiaonaerApp.ServiceUrl + "/api/login", user, function(){
            console.log("Trigger user:loginSuccess");
            TiaonaerApp.vent.trigger("user:loginSuccess");
        });*/
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
            }
        });
    },

    login_cancel : function() {
        // Not use window.history.back() or window.history.go(-2)
        // Because we might be here from homeview->...   or homeview->favorite/list->...
        // Might consider introduce source view later.
        Backbone.history.navigate("#/");
    },

    login_failed: function(error) {
        $('#for-display-error', this.el).show();
    }
});

TiaonaerApp.Views.UserHomeView = Backbone.View.extend({
    id: 'home-page',
    template: "#template-home-view",
    model: TiaonaerApp.Models.User,
    initialize:function () {
        //this.template = _.template(tpl.get('template-home-view'));
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
    }
});
