TiaonaerApp.Views.LoginView = Marionette.ItemView.extend({
    template: "#template-login-view",
    events: {
        "click #login-button": "login"
    },
    login: function() {
        console.log("Log in");
        var user = {};
        user.username = $("#user-username").val();
        user.password = $("#user-password").val();
        $.post("/api/login", user, function(){
            console.log("Trigger user:loginSuccess");
            TiaonaerApp.vent.trigger("user:loginSuccess");
        })
    }
});

TiaonaerApp.Views.UserHomeView = Backbone.View.extend({
    id: 'home_page',
    //template: false,
    //template: "#template-home-view",

    initialize:function () {
        this.template = _.template(tpl.get('template-home-view'));
    },

    render:function (eventName) {
        $(this.el).html(this.template());
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
    },
});
