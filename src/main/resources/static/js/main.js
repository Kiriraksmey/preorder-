$(document).ready(function($) {

	$('#metfone-owl-carousel').owlCarousel({
	    items: 1,
	    margin: 0,
	    nav: true,
	    dots: true,
	    autoplay: false,
	    loop: true,
	    autoHeight: true
	});

	$('#btn-mainmenu').on('click', function() {
		$('.wrap-menu-mobile').addClass('openmenu');
	});

	$('#closemenu').on('click', function() {
		$('.wrap-menu-mobile').removeClass('openmenu');
	})

	$('#btn-showall-exchange').on('click', function() {
		$('.money-account .row-exchange .exchange').show(500);
		$(this).css('display', 'none');
	});

	$('.panel-heading').on('click', function() {	
		$(this).toggleClass('open');	
		$(this).parent('.panel').find('.panel-body').slideToggle('show');
	});

	/*$('.panel-heading').on('click', function() {		
		$('.panel-heading').toggleClass('open');
		$('.panel-body').slideToggle('show');
	});*/

	$(window).scroll(function(){	
		var height = $('#header').height(); 
		var wp = $(window).scrollTop();		
		
		if(wp > 0) {
			$("#header").addClass('nta-static');
			$('.maincontent').css('padding-top', height);				
		}else{			
			$("#header").removeClass('nta-static');		
			$('.maincontent').css('padding-top', '0');	
		}
	}).scroll();

});