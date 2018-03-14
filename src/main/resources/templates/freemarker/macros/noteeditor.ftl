<#macro noteeditor note>
<div id="note-editor">
	<style scoped>
		#note-editor {
		  height: 100%;
		  margin-left: 380px;
		}
		
		#note-editor textarea {
		  height: 100%;
		  border: 0;
		  border-radius: 0;
	}
	</style>
    <textarea value="note.content" @input="editNote" class="form-control" rows="30">
    </textarea>
  </div>
  
  <script type="text/freemarker">
  (function(){
	
	//singleton function
	 function NoteEditor(){};
	 
	 NoteEditor.prototype.updateNote = function(){
	 
	 }
	 
	 window.NoteEditor = new NoteEditor();
  })();
 </script>
</#macro>