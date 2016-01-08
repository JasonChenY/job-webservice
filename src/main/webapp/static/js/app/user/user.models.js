App.Models.User = Backbone.Model.extend({
    //idAttribute: "username",
    defaults: {
        "username":  "anonymous",
        "role": "ROLE_ANONYMOUS",
        "type": 0
    },

    isAnonymousUser: function() {
        if (this.get("username") === 'anonymous') {
            console.log("User is anonymous");
            return true;
        }
        return false;
    }
});