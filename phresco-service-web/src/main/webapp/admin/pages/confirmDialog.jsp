<%--
  ###
  Service Web Archive
  
  Copyright (C) 1999 - 2012 Photon Infotech Inc.
  
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  
       http://www.apache.org/licenses/LICENSE-2.0
  
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
  ###
  --%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<!-- Confirmation dialog starts -->	
<div id="confirm" class="modal confirm">
	<div class="modal-header">
		<h3><s:text name="label.delete.confirm"/></h3>
		<a id="close" href="#" class="close">&times;</a>
	</div>
	
	<div class="modal-body">
		<p id="confirmationText" />
	</div>
	
	<div class="modal-footer">
		<input id="cancel" type="button" value="<s:text name="label.no"/>" class="btn btn-primary"/>
		<input type="button" value="<s:text name="label.yes"/>" class="btn btn-primary" id="ok" 
			onClick="continueDeletion()" /> <!-- continueDeletion() will call Triggering list.jsp -->
	</div>
</div>
<!-- Confirmation dialog ends -->

<script>
	$(document).ready(function() {
		$('#cancel,#close').click(function() {
			confirmDialog('none');
		});
	});
	
	function confirmDialog(enableProp, msg) {
	    $(".modal-backdrop").css("display", enableProp);
	    $(".confirm").show().css("display", enableProp);
	    $("#confirmationText").text(msg);
	}
</script>	