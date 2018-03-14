<#import "notestool.ftl" as ntool/>
<#import "noteslist.ftl" as nlist/>
<#import "noteeditor.ftl" as nedit/>
<#macro notesview content>
<div class="container" id="notes-view">
    <div class="row">
        <div class="col-md-12">
            <@ntool.notestool current=content.getNote(content.currentNote)/>
            <@nlist.noteslist notes=content.getUserNotes() current=content.currentNote/>
            <@nedit.noteeditor note=content.getNote(content.currentNote)/>
        </div>
    </div>
</div>
</#macro>