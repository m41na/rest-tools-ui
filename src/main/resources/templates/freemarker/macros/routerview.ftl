<#macro routerview view content>
	
    <#import "userslist.ftl" as users>
    <@users.userslist view=view users=content.getUsers() current=content.currentUser />

    <#if view=='tasks'>
        <#import "tasksview.ftl" as tasks/>
        <@tasks.tasksview content=content/>
    <#elseif view=='rest'>
        <#import "restview.ftl" as rest/>
        <@rest.restview content=content/>
    <#elseif view=='notes'>
        <#import "notesview.ftl" as notes/>
        <@notes.notesview content=content/>
    <#else>
        <#import "tasksview.ftl" as tasks/>
        <@tasks.tasksview content=content/>
    </#if>
</#macro>