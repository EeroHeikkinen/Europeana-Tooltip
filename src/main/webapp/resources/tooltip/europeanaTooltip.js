(function($) {
	$.fn.europeanaTooltip = function(options) {	
			var settings = {
				thumbnailSize: "50",
				template: "default",
				precache: true,
				url: "http://localhost:8080/europeana_hackathon/get.json"
			};
			
			var tooltip;
			var active = false;
		    var template;
		    
		    if ( options ) { 
		        $.extend( settings, options );
		      }
		    
		    if(settings.template == "default")
		    	template = '\
		    		<div style="text-align: left; color: white;">\
					<div style="padding: 5px; background-color: #333; background-color: rgba(0, 0, 0, 0.8);););width: ${thumbnailSize}px;height: ${thumbnailSize}px;border-radius: 8px;float: left;">\
						<div>\
							<img width="${thumbnailSize}" height="${thumbnailSize}" src="${fields["europeana:object"]}">\
						</div>   \
					</div>\
					<div style="margin-left: 5px; padding: 5px;  background-color: #333; background-color: rgba(0, 0, 0, 0.8);););width: 300px; border-radius: 8px;float: left;">\
						<div style="padding: 0px 2px 0px 2px">\
							<b style="color: #0070DD !important;">${fields["dc:title"]}</b>\
							<div style="float:right;color: #FFF">\
								${fields["europeana:type"]}\
							</div>\
							<br>\
							<div style="color: #1EFF00 !important; margin-top: 3px">${fields["dc:description"]}</div>\
							<div style="margin-top: 3px">\
								${fields["europeana:dataProvider"]}\
								<br/>\
								${fields["europeana:provider"]}\
							</div>\
						</div>\
					</div>\
				</div>';
		    else if(typeof(settings.template) === 'string')
		    	template = $("#"+settings.template);
			
			return this.each(function() {
			    var cached;
				var href = $(this).attr("href");
				var start = href.indexOf("/portal/record/") + 15;
				if(start == 14) // indexOf returned -1
					start = href.indexOf("/item/") + 6;
				if(start == 5) // indexOf returned -1
					return; // no id found
				var end = href.indexOf(".html");
				var id = href.substring(start, end);

			    function processData(data) {
			    	
			    	if(data.fields["europeana:type"])
						data.fields["europeana:type"][0] =
							data.fields["europeana:type"][0].substring(0,1).toUpperCase()
							+ data.fields["europeana:type"][0].slice(1).toLowerCase();
			    	
			    	return data;
			    }
			    
			    if(settings.precache) {
			    	$.ajax({
						url:settings.url,
						dataType: 'jsonp',
						cache: true,
						data: {
							id : id
						},
						success: function(data) {
					    	processData(data)
					    	cached=$.tmpl(template, $.extend(data, settings));
					}});
			    }

				$(this).mouseover(
					function(e) {
						active = true;
						
						if(cached) {
							if(tooltip) tooltip.remove();
							tooltip = cached
							.css("position","absolute")
							.css("left", e.pageX + 10)
							.css("top", e.pageY)
							.appendTo('body');
							
							return;
						}
					    
					    $.ajax({
							url:settings.url,
							dataType: 'jsonp',
							cache: true,
							data: {
								id : id
							},
							success: function(data) {
								if(active) {
									data = processData(data);
									if(tooltip) tooltip.remove();
									tooltip = $.tmpl(template, $.extend(data, settings))
										.css("position","absolute")
										.css("left", e.pageX + 10)
										.css("top", e.pageY)
										.appendTo('body');
									cached = tooltip;
								}
						    }});
					});
						
						$(this).mousemove( function(e) {
							if(tooltip) {
								if(tooltip.height() < ($(window).height() - e.clientY))
									tooltip
									.css("position","absolute")
									.css("left", e.pageX + 10)
									.css("top", e.pageY);
								else if(tooltip.height() < e.clientY)
									tooltip
									.css("position","absolute")
									.css("left", e.pageX + 10)
									.css("top", e.pageY-tooltip.height());
								else 
									tooltip
									.css("position","absolute")
									.css("left", e.pageX + 10)
									.css("top", $(window).scrollTop() + 5);
							}		
						})
						$(this)
						.mouseout(function() {
							active = false;
							if(tooltip)
								tooltip.remove();
						});
						
					})
	};
})(jQuery);