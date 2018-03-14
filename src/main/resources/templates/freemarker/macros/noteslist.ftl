<#macro noteslist notes current>
<div id="notes-list">
	<style scoped>
		#list-header {
		  padding: 5px 25px 25px 25px;
		}
		
		#list-header h2 {
		  font-weight: 300;
		  text-transform: uppercase;
		  text-align: center;
		  font-size: 22px;
		  padding-bottom: 8px;
		}
		
		#notes-list {
		  float: left;
		  width: 300px;
		  height: 100%;
		  background-color: #F5F5F5;
		  font-family: 'Raleway', sans-serif;
		  font-weight: 400;
		}
		
		#notes-list .container {
		  height: calc(100% - 137px);
			max-height: calc(100% - 137px);
			overflow: auto;
		  width: 100%;
		  padding: 0;
		}
		
		#notes-list .container .list-group-item {
		  border: 0;
		  border-radius: 0;
		}
		
		.list-group-item-heading {
		  font-weight: 300;
		  font-size: 15px;
		}
	</style>

    <div id="list-header">
      <h2>Rapid Thoughts</h2>
      <div class="btn-group btn-group-justified" role="group">
        <!-- All Notes button -->
        <div class="btn-group" role="group">
          <button type="button" class="btn btn-default"
            onclick="showNotes = 'all'"
            :class="{active: show === 'all'}">
            All Notes
          </button>
        </div>
        <!-- Favorites Button -->
        <div class="btn-group" role="group">
          <button type="button" class="btn btn-default" @click="show = 'favorites'" :class="{active: show === 'favorites'}">
            Favorites
          </button>
        </div>
      </div>
    </div>
    <!-- render notes in a list -->
    <div class="container">
      <div class="list-group">
        <#list notes.notes as note>
        <a class="list-group-item" href="#" class="<#if note.noteId==current>active</#if>" onclick="NotesList.updateActiveNote(${note.noteId})">
          <h4 class="list-group-item-heading">
            ${note.getSummary()}
          </h4>
        </a>
        </#list>
      </div>
    </div>

  </div>
  
  <script type="text/freemarker">
  (function(){
	
	//singleton function
	 function NotesList(){};
	 
	 NotesList.prototype.showAll = function(note){
	 
	 }
	 
	 NotesList.prototype.updateActiveNote = function(note){
	 	var user = $("#users-list").val();
		submit("post", "/rws/mvc/notes/user/" + user +"/note/" + note, [user, note]);
	 }
	 
	 window.NotesList = new NotesList();
  })();
 </script>
</#macro>
