<#macro notestool current>
<div id="toolbar">
	<style scoped>
		#toolbar {
		  float: left;
		  width: 80px;
		  height: 100%;
		  background-color: #30414D;
		  color: #767676;
		  padding: 35px 25px 25px 25px;
		}
		
		#toolbar i {
		  font-size: 30px;
		  margin-bottom: 35px;
		  cursor: pointer;
		  opacity: 0.8;
		  transition: opacity 0.5s ease;
		}
		
		#toolbar i:hover {
		  opacity: 1;
		}
		
		.starred {
		  color: #F7AE4F;
		}
	</style>

    <i onclick="NotesTools.addNewNote()" class="glyphicon glyphicon-plus"></i>
    <i onclick="NotesTools.toggleFavorite(${current.noteId})" class="glyphicon glyphicon-star <#if current.favorite>starred</#if>"></i>
    <i onclick="NotesTools.deleteNote(${current.noteId})" class="glyphicon glyphicon-remove"></i>
 </div>
 
 <script type="text/freemarker">
  (function(){
	
	//singleton function
	 function NotesTools(){};
	 
	 NotesTools.prototype.addNewNote = function(){
	 	var user = $("#users-list").val();
		submit("post", "/rws/mvc/notes/user/" + user, [user]);
	 }
	 
	 NotesTools.prototype.toggleFavorite = function(){
	 
	 }
	 
	 NotesTools.prototype.deleteNote = function(){
	 
	 }
	 
	 window.NotesTools = new NotesTools();
  })();
 </script>
</#macro>