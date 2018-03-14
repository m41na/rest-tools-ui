<#macro resttools content>
<#assign coll = content.currentCollection/>
<#assign mode = content.currentMode/>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4>Collections
            <span class="pull-right" style="display:inline-block;padding-left:10px"><a href="/#" title="delete" ><span class="glyphicon glyphicon-trash"></span></a></span>
            <span class="pull-right" style="display:inline-block;padding-left:10px"><a href="/#" title="update" onclick="showUpdate(event)"><span class="glyphicon glyphicon-pencil"></span></a></span>
            <span class="pull-right" style="display:inline-block;padding-left:10px"><a href="/#" title="create" onclick="showCreate(event)"><span class="glyphicon glyphicon-plus"></span></a></span>
        </h4>
    </div>
    <div class="panel-body">
        <#if mode=='normal'>
        <select class="form-control" name="collection" style="margin:15px 0px 25px 0px" onchange="selectCollection(event)">
      	<#list content.getCollectionTitles() as item>
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
            <span style="display:inline-block;padding-left:10px"><a id="cancel-create" href="/#" title="cancel" onclick="cancelCreate(event);"><span class="glyphicon glyphicon-remove"></span></a></span>
        </form>
        </#if>

        <div class="list-group">
      	<#list content.getUserEndpoints(coll) as endpoint>
            <a href="#" class="list-group-item" onclick="selectEndpoint(event, ${endpoint.id})">
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
</#macro>