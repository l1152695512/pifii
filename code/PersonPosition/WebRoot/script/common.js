	function maximizeWin() { 
		if (window.screen) { 
			var aw = screen.availWidth; 
			var ah = screen.availHeight; 
			if(window.screenLeft!=0&&window.screenTop!=0)window.moveTo(0, 0); 
			window.resizeTo(aw, ah); 
		} 
	}
	