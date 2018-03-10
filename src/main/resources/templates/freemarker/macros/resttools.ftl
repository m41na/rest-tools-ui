<#macro resttools content>
<#assign user = content.currentUser/>
<#assign coll = content.currentCollection/>
<#assign mode = content.currentMode/>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4>Collections
          <span class="pull-right" style="display:inline-block;padding-left:10px"><a href="/#" title="delete" ><span class="glyphicon glyphicon-trash"></span></a></span>
          <span class="pull-right" style="display:inline-block;padding-left:10px"><a href="/#" title="update" onclick="RestTools.showUpdate(event)"><span class="glyphicon glyphicon-pencil"></span></a></span>
          <span class="pull-right" style="display:inline-block;padding-left:10px"><a href="/#" title="create" onclick="RestTools.showCreate(event)"><span class="glyphicon glyphicon-plus"></span></a></span>
        </h4>
    </div>
    <div class="panel-body">
      <#if mode=='normal'>
      <select class="form-control" name="collection" style="margin:15px 0px 25px 0px" onchange="RestTools.selectCollection(this, '${user}')">
      	<#list content.getCollectionTitles(user) as item>
        <option value="${item.collectionId}" <#if coll == item.collectionId>selected</#if>>${item.collectionTitle}</option>
        </#list>
      </select>
      </#if>

	  <#if mode!='normal'>
      <form class="form-inline" style="margin:15px 0px 25px 0px">
        <div class="form-group">
          <label class="sr-only" for="collection">Collection</label>
          <input type="text" class="form-control" id="collection" placeholder="Collection">
        </div>
        <#if mode=='create'>
        <button type="submit" class="btn btn-default" id="show-create">Create</button>
        </#if>
        <#if mode=='update'>
        <button type="submit" class="btn btn-default" id="show-update">Update</button>
        </#if>
        <span style="display:inline-block;padding-left:10px"><a id="cancel-create" href="/#" title="cancel" onclick="RestTools.cancelCreate(event)"><span class="glyphicon glyphicon-remove"></span></a></span>
      </form>
      </#if>

      <div class="list-group">
      	<#list content.getUserEndpoints(user, coll) as endpoint>
        <a href="#" class="list-group-item" onclick="RestTools.selectEndpoint(event, ${endpoint.id})">
          <h4 class="list-group-item-heading">${endpoint.method}
            <span class="glyphicon glyphicon-remove pull-right remove" style="color:#C9302C"></span>
          </h4>
          <p class="list-group-item-text">
            <span>${endpoint.path}</span>
          </p>
        </a>
        </#list>
      </div>
    </div>
</div>

<script>
  (function(){
	
	//singleton function
	 function RestTools(){
	 	this.mode = 'normal';
	 };
	
	 RestTools.prototype.showCreate = function(e){
	 	e.preventDefault();
	    var mode = 'create';
	    submit("post", "/rws/mvc/rest/mode/" + mode, [mode]);
	 }
	
	 RestTools.prototype.cancelCreate = function(e){
	 	e.preventDefault();
	    var mode = 'normal';
	    submit("post", "/rws/mvc/rest/mode/" + mode, [mode]);
	 }
	
	 RestTools.prototype.showUpdate = function(e){
	 	e.preventDefault();
	    var mode = 'update';
	    submit("post", "/rws/mvc/rest/mode/" + mode, [mode]);
	 }
	 
	 //select user
	 RestTools.prototype.selectRestUser = function(el){
		var user = $(el).val();
		submit("post", "/rws/mvc/rest/user/" + user, [user]);
	 }
	
	 //select collection
	 RestTools.prototype.selectCollection = function(el, user){
		var list = $(el).val();
		submit("post", "/rws/mvc/rest/user/" + user +"/collection/" + list, [user, list]);
	 }
	
	 RestTools.prototype.selectEndpoint = function(e, id){
	  	if(e.target.tagName == 'span'){
	  		alert('this will delete endpoint');
	  	}
	  	else{
	  		submit("post", "/rws/mvc/rest/user/" + ${user} +"/collection/" + ${coll} + "/endpoint/" + id, [${user}, ${coll}, id]);
	  	}
	 }
	 
	 window.RestTools = new RestTools();
  })();
</script>
</#macro>