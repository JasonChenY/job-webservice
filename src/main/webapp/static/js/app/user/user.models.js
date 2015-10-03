TiaonaerApp.Models.User = Backbone.Model.extend({
    //idAttribute: "username",
    defaults: {
        "username":  "anonymous",
        "role": "ROLE_ANONYMOUS"
    },

    isAnonymousUser: function() {
        if (this.get("username") === 'anonymous') {
            console.log("User is anonymous");
            return true;
        }
        return false;
    }
});