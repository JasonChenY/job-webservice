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
        "keypress #user-password": function(e) { if ( e.keyCode === 13 ) this.login(); },
        "click #btn-login-cancel": "login_cancel",
        "click #btn-testServer-login": function(e) { this.thirdParty_login("testServer"); },
        "click #btn-weixin-login": function(e) { this.thirdParty_login("weixin"); },
        "click #btn-qq-login": function(e) { this.thirdParty_login("qq"); },
        "click #btn-sina-login": function(e) { this.thirdParty_login("weibo"); }
    },
    thirdParty_login: function(type) {
    /*
        !!!!!
        Cant use AJAX for third party login, 301/302 will be intercepted by browser,
        and AJAX will get final third party site login page in text/html format.
        even we can render the login page with that html content, we lose the url of third party login page,
        no way to authenticate toward it.

        var self = this;
        $.ajax({
            type: "GET",
            url: TiaonaerApp.ServiceUrl + "/testServer/login",
            xhrFields: {
                withCredentials: true
            },
            crossDomain: true,
            success: function(data, status, xhr){
                console.log("success for /testServer/login");
                var contentType = xhr.getResponseHeader("Content-Type");
                if (xhr.status === 200 && contentType.toLowerCase().indexOf("text/html") >= 0) {
                    self.setValid(false);
                    $(self.el).html(data);
                }
            }
        });
    */
        $('#for-display-error', this.el).hide();
        TiaonaerApp.childWin = window.open(TiaonaerApp.ServiceUrl + "/" + type + "/login");
        if ( isCordovaApp() ) {
            // loadstart only for mobile InAppBrowser, instead of loadstop because we might get login result in first page.
            TiaonaerApp.childWin.addEventListener('exit', function(event) {
                if ( TiaonaerApp.loopForChildWin ) {
                    clearInterval(TiaonaerApp.loopForChildWin);
                    TiaonaerApp.loopForChildWin = null;
                };
            });

            if ( type === 'weixin' ) {
            TiaonaerApp.childWin.addEventListener("loadstop", function(event) {
                TiaonaerApp.childWin.executeScript({ code:
                //"function save(f) { alert(f);var w=window.open(f,'_blank');sleep(5); w.document.execCommand('SaveAs',null,'weixin_qrcode.jpg');w.close();};var ele=document.getElementsByClassName('qrcode')[0];if (ele && !document.getElementById('saveqr')) {var v=document.createElement('div');v.className='status status_browser js_status normal';v.id='saveqr';var a=document.createElement('a');var at=document.createTextNode('或者保存二微码图片到相册再用微信扫描');a.appendChild(at);a.href='#';a.onclick=function(e){save(ele.src);};v.appendChild(a);var next=document.getElementById('wx_after_cancel');next.parentNode.insertBefore(v,next);};"
                //"function save(f) { var w=window.open(f);};var ele=document.getElementsByClassName('qrcode')[0];if (ele && !document.getElementById('saveqr')) {var v=document.createElement('div');v.className='status status_browser js_status normal';v.id='saveqr';var a=document.createElement('a');var at=document.createTextNode('或者保存二微码图片到相册再用微信扫描');a.appendChild(at);a.href='#';a.onclick=function(e){save(ele.src);};v.appendChild(a);var next=document.getElementById('wx_after_cancel');next.parentNode.insertBefore(v,next);};"
                //"function save(f){I1.document.location=f;savepic();};function savepic(){if(I1.document.readyState=='complete') {I1.document.execCommand('saveas', null, 'test.jpg');} else {window.setTimeout('savepic()',10);}};var ele=document.getElementsByClassName('qrcode')[0];if (ele && !document.getElementById('saveqr')) {var v=document.createElement('div');v.className='status status_browser js_status normal';v.id='saveqr';var a=document.createElement('a');var at=document.createTextNode('或者保存二微码图片到相册再用微信扫描');a.appendChild(at);a.href='#';a.onclick=function(e){save(ele.src);};v.appendChild(a);var next=document.getElementById('wx_after_cancel');next.parentNode.insertBefore(v,next);var f=document.createElement('iframe');f.name='I1';f.style.display='none';document.body.appendChild(f);};"
                //"function save(f){I1.document.location=f;savepic();};function savepic(){if(I1.document.readyState=='complete') {I1.document.execCommand('saveas', null, 'test.jpg');} else {window.setTimeout('savepic()',10);}};var ele=document.getElementsByClassName('qrcode')[0];if (ele && !document.getElementById('saveqr')) {var v=document.createElement('div');v.className='status status_browser js_status normal';v.id='saveqr';var a=document.createElement('a');var at=document.createTextNode('或者保存二微码图片到相册再用微信扫描');a.appendChild(at);a.href=ele.src;a.download='test.jpg';v.appendChild(a);var next=document.getElementById('wx_after_cancel');next.parentNode.insertBefore(v,next);};"
                "var ele=document.getElementsByClassName('qrcode')[0];if (ele && !document.getElementById('saveqr')) {var v=document.createElement('div');v.className='status status_browser js_status normal';v.id='saveqr';var p=document.createElement('p');p.innerHTML='或者截屏再用微信到相册扫描';v.appendChild(p);var next=document.getElementById('wx_after_cancel');next.parentNode.insertBefore(v,next);localStorage.setItem('LoginResult', ele.src);};"
                });
            });
            }

            TiaonaerApp.childWin.addEventListener("loadstart", function(event) {
                if ( TiaonaerApp.loopForChildWin ) return;
                TiaonaerApp.childWin.executeScript({ code: "localStorage.removeItem('LoginResult');" });
                TiaonaerApp.childWin_loadstop = 0;
                TiaonaerApp.loopForChildWin = setInterval(function() {
                    TiaonaerApp.childWin.executeScript(
                        {
                            code: "localStorage.getItem( 'LoginResult' )"
                        },
                        function( values ) {
                            var result = values[0];
                            if ( result != null ) {
                              if ( result === true || result === false || result === 'true' || result === 'false' ) {
                                clearInterval( TiaonaerApp.loopForChildWin );
                                TiaonaerApp.loopForChildWin = null;

                                if ( TiaonaerApp.childWin ) {
                                    TiaonaerApp.childWin.close();
                                    TiaonaerApp.childWin = null;
                                }
                                ThirdPartyLoginInCallback(result);
                              } else if ( result.substr(0,4) === 'http' ) {
                                if ( TiaonaerApp.childWin_loadstop === 0 ) {
                                  //removeEventListener not take effect.
                                  TiaonaerApp.childWin_loadstop = 1;
                                  localStorage.removeItem('LoginResult');
                                  TiaonaerApp.childWin.removeEventListener("loadstop", function() {});
                                  alert('操作步骤: \n 右键选择保存二维码图片 \n 进入微信 \n 扫一扫 \n 点击右上角按钮，从相册选取二维码 \n');
                                  window.open(result, '_system', 'location=yes');
                                }
                              }
                            }
                        }
                    );
                },(type==='weixin')?1000:15000);
                //should use some reasonable value, because this will kill the keyboard ui, can use too short value, too long bad experience
            });
        }
    },

    login: function() {
        console.log("Log in");
        localStorage.removeItem("username");
        $('#for-display-error', this.el).hide();
        var user = {};
        user.username = $("#user-username", this.el).val().Trim();
        user.password = $("#user-password", this.el).val().Trim();

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
            beforeSend: function(xhr) {
                if ( isCordovaApp() ) {
                    xhr.setRequestHeader("Origin",TiaonaerApp.ServerHost);
                }
            },
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
/*      var reason = data.getResponseHeader("Reason");
        if (reason === "Bad credentials") {

        }
*/
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
        var btn_ui_user = $('.ui-user-figure', this.el);
        switch(this.model.get("type")) {
            case 0: btn_ui_user.buttonMarkup({ icon: "user" }); break;
            case 1: btn_ui_user.buttonMarkup({ icon: "testserver" }); break;
            case 2: btn_ui_user.buttonMarkup({ icon: "qq" }); break;
            case 3: btn_ui_user.buttonMarkup({ icon: "sina" }); break;
            case 4: btn_ui_user.buttonMarkup({ icon: "weixin" }); break;
            case 5: btn_ui_user.buttonMarkup({ icon: "baidu" }); break;
        }
        //if ( isCordovaApp() ) { $("#btn_download", this.el).hide(); }
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
        'click #btn_logout': function() { TiaonaerApp.vent.trigger("user:logout"); },
        'click #btn_app_download': function() { TiaonaerApp.vent.trigger("app:download"); },
        'click #btn_app_about': function() { TiaonaerApp.vent.trigger("app:about"); }
    },

    isUserLoggedIn: function () {
        return !this.model.isAnonymousUser();
    },

    getUserRole: function() {
        return this.model.role;
    },

    userLoggedIn: function(user) {
        this.model.set({
            username: user.username,
            role: user.role,
            type: user.identity_type,
            when: _.now()
        });
        console.log(this.model.toJSON());
        this.render();
    },

    userLoggedOut: function() {
        this.model.set({
            username: "anonymous",
            role: "ROLE_ANONYMOUS",
            type: 0,
            when: _.now()
        });
        this.render();

        TiaonaerApp.ViewContainer.apply("setValid", [false]);
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
        "blur #user-username": "check_user_validity",
        "blur #user-email": "check_email_validity",
        "keypress #user-password": function(e) { if ( e.keyCode === 13 ) this.register(); },
        "click #btn-user-register": "register",
        "click #btn-register-cancel": "register_cancel"
    },

    check_user_validity: function() {
        var username = $("#user-username", this.el).val();
        if ( !username || !(/^[a-zA-Z0-9_\-]{4,14}$/.test(username)) ) {
            $('#display-error', this.el).text("请输入4到14位字母数字下划线破折号的用户名！");
            $('#for-display-error', this.el).show();
            $('#user-username', this.el).focus();
        } else {
            var self = this;
            $.ajax({
                url: TiaonaerApp.ServiceUrl + "/api/user/" + username,
                success: function(data, status, xhr){
                    if ( data ) {
                        $('#display-error', self.el).text(username + " 已被占用，换个名字试试！");
                        $('#for-display-error', self.el).show();
                        $('#user-username', self.el).focus();
                    } else {
                        $('#for-display-error', self.el).hide();
                    }
                }
            });
        }
    },
    check_email_validity: function() {
        var email = $('#user-email', this.el).val();
        var filter  = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
        if ( !filter.test(email) ) {
            $('#display-error', this.el).text("邮箱地址不正确！");
            $('#for-display-error', this.el).show();
            $('#user-email', this.el).focus();
        } else {
            var self = this;
            $.ajax({
                url: TiaonaerApp.ServiceUrl + "/api/user",
                type: 'POST',
                data: JSON.stringify({email: email}),
                contentType: "application/json; charset=UTF-8",
                success: function(data, status, xhr){
                    if ( data ) {
                        $('#display-error', self.el).text(email + " 已注册！");
                        $('#for-display-error', self.el).show();
                        $('#user-email', self.el).focus();
                    } else {
                        $('#for-display-error', self.el).hide();
                    }
                }
            });
        }
    },
    register: function() {
        if ( $('#for-display-error', this.el).is(':visible') ) {
            //$('#for-display-error', this.el).hide();
            return;
        }
        var user = {
            username: $("#user-username", this.el).val().Trim(),
            password: $("#user-password", this.el).val().Trim(),
            email: $("#user-email", this.el).val().Trim()
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
            beforeSend: function(xhr) {
                if ( isCordovaApp() ) {
                    xhr.setRequestHeader("Origin",TiaonaerApp.ServerHost);
                }
            },
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
