var appElement = document.getElementById("rest-view");
appElement.addEventListener('EventBusReady', function (e) {
    console.log("EventBusReady invoked");

    window.EventBus.register(RestTools.EDNPOINT_UPDATED, "#endpoint-form", function(){
        console.log("EDNPOINT_UPDATED invoked");
    });

}, false);
