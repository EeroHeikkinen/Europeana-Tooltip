# Europeana Tooltip

##Overview
A simple yet highly customizable jQuery plugin that allows you to enrich your site with content from Europeana.

The Plugin was developed in 2011 during the 3-5 October Hackathon organized during EuropeanaTech conference in Vienna. The demo API key is no longer functional, so using this project requires you to have your own API key. Europeana itself has plans to provide functionality similar to the server-side component in this project itself so the part which requires an API key might no longer be needed in the future.

##Components
The program consists of two seperate components: a server-side component and a client-side component. 
###Server-side component
The server-side component simply requests XML responses from the Europeana API, and converts them to JSON.
###Client-side component
The client-side component utilizes the JSON from the server-side component. It renders the raw data in a user-friendly format using Javascript in the browser.

##Usage
###Getting started
1. include the plugin

        <script type="text/javascript" src="europeanaTooltip.js"></script>
2. add link(s) to europeana with a css class of your liking (I used "tooltip" here)

        <a href="http://www.europeana.eu/portal/record/09405a/BE2C421E780DBBF0BF3BD81860C84F0DB3317567.html" class="tooltip">Example</a>

3. initialize the plugin

        <script type="text/javascript">
        $(document).ready(function() {
            $("a.tooltip").europeanaTooltip();
        }));
        </script>

###Using templates
1. add a template inside a script tag

        <script id="myTemplate" type="text/x-jquery-tmpl">
            <div>
                <h1>${fields["dc:title"]}</h1>
                <i>${fields["dc:description"]}</i>
            </div>
        </script>
2. include template id as an option

        $(document).ready(function() {
            $("a.tooltip").europeanaTooltip({
            template: "myTemplate"
            });
        }));

##Options
    var settings = {
        service: ”localhost:8080/europeana_hackathon/",
        thumbnailSize: ”50",
        precache: true,
        template: "defaultTemplate",
    };