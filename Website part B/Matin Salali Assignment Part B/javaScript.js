	function resizeText(multiplier) {
  if (document.body.style.fontSize == "") {
    document.body.style.fontSize = "2.0em";
  }
  document.body.style.fontSize = parseFloat(document.body.style.fontSize) + (multiplier * 0.2) + "em";
}

function toggle() {
    		var el = document.getElementById("style1");
    		if (el.href.match("home.css")) {
        	el.href = "home2.css";    
    	}
    	else {
        	el.href = "home.css";  
    	}
		}