var restView = new Vue({
    el: "#notes-view",
    data: window.pagestore || {},
    mounted: function(){
        console.log("notes-view mounted");
    }
});
