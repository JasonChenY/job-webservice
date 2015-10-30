$(document).bind({
"mobileinit": function () {
    console.log('mobileinit');
    $.mobile.ajaxEnabled = false;
    $.mobile.linkBindingEnabled = false;
    $.mobile.hashListeningEnabled = false;
    $.mobile.pushStateEnabled = false;


   // Otherwise after mobileinit, it tries to load a landing page
   // $.mobile.autoInitializePage = false;

   // we want to handle caching and cleaning the DOM ourselves
   $.mobile.page.prototype.options.domCache = false;

// consider due to compatibility issues
   // not supported by all browsers
   $.mobile.pushStateEnabled = false;
   // Solves phonegap issues with the back-button
   $.mobile.phonegapNavigationEnabled = true;
   //no native datepicker will conflict with the jQM component
   $.mobile.page.prototype.options.degradeInputs.date = true;

    // Remove page from DOM when it's being replaced
    $('div[data-role="page"]').on('pagehide', function (event, ui) {
        console.log('remove old page from DOM');
        $(event.currentTarget).remove();
    });

    $.mobile.loader.prototype.options.text = "请稍等...";
    $.mobile.loader.prototype.options.textVisible = true;
    $.mobile.loader.prototype.options.theme = "a";
    $.mobile.loader.prototype.options.html = "";
    $.mobile.pageLoadErrorMessage = '服务器连接失败，请检查网络或者稍后重试!';
  },
ajaxStart: function() {
    setTimeout(function() {$.mobile.loading('show'); }, 0);
  },
ajaxStop: function() {
    setTimeout(function() {$.mobile.loading('hide'); }, 0);
  }
});
/*
$(document).on('pagebeforechange', function(e, data){
    console.log("enter pagebeforechange");
    var to = data.toPage,
        from = data.options.fromPage;
    if (typeof to  === 'string') {
        var u = $.mobile.path.parseUrl(to);
        console.log(u);
        console.log(u.pathname);
        console.log(u.pathname.substring(1));
        to = u.hash || '#' + u.pathname.substring(1);
        if (from) {
            console.log(from);
            from = '#' + from.attr('id');
            console.log(from);
        }

        if (from === '#index' && to === '#second') {
            alert('Can not transition from #index to #second!');
            e.preventDefault();
            e.stopPropagation();

            // remove active status on a button, if transition was triggered with a button
            $.mobile.activePage.find('.ui-btn-active').removeClass('ui-btn-active ui-focus ui-btn');;
        }
    }
});*/
