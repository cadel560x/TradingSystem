<!DOCTYPE html>
<html>
	<head>
	<meta charset="UTF-8">
		<title>Stock Broker</title>
		 <script src="http://ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js"></script> 
	</head>
	<body>
		<h1>STOCK BROKER CLIENT</h1>
		<form>
			<div>
		        <label for="userId">UserId:</label>
		        <input type="text" id="name" name="userId">
		    </div>
		    <div>
		        <label for="stockTag">StockTag:</label>
		        <input type="text" id="stockTag" name="stockTag">
		    </div>
		    <div>
		        <label for="type">Type:</label>
		        <input type="radio" name="type" id="type" value="BUY" checked>Buy
				<input type="radio" name="type" id="type" value="SELL">Sell
		    </div>
		    <div>
		        <label for="condition">Condition:</label>
		        <input type="radio" name="condition" value="MARKET" checked>Market
				<input type="radio" name="condition" value="LIMIT">Limit
				<input type="radio" name="condition" value="STOPLOSS">Stop Loss
		    </div>
		    <div>
		        <label for="price">Price:</label>
		        <input type="number" id="name" name="price" step=0.0001>
		    </div>
		    <div>
		        <label for="volume">Volume:</label>
		        <input type="number" id="volume" name="volume">
		    </div>
		    <div>
		        <label for="partialFill">Allow partial fill:</label>
		        <input type="checkbox" name="partialFill" id="partialFill">
		    </div>
		    <div>
		        <label for="expirationTime">Expiration Time:</label>
		        <input type="text" name="expirationTime" id="expirationTime" size="50">
		    </div>
		    <div class="button">
			  <button type="submit">Send Order</button>
			</div>
		</form>
		<h3>Result:</h3>
		<textarea rows="5" cols="60"></textarea>
		<script>
			$(document).ready(function() {

			    // process the form
			    $('form').submit(function(event) {
	
			        // get the form data
			        // there are many ways to get this data using jQuery (you can use the class or id also)
			        var formData = {
			            'userId'              : $('input[name=userId]').val(),
			            'stockTag'             : $('input[name=stockTag]').val(),
			            'type'			    : $('input[name=type]:checked').val(),
			            'condition'              : $('input[name=condition]:checked').val(),
			            'price'             : $('input[name=price]').val(),
			            'volume'			    : $('input[name=volume]').val(),
			            'partialFill'             : $('input[name=partialFill]').is(":checked"),
			            'expirationTime'			    : $('input[name=expirationTime]').val()
			        };
	
			        // process the form
			        $.ajax({
			        		headers: { 
			                'Accept': 'application/json',
			                'Content-Type': 'application/json' 
			            },
			            type        : 'POST', // define the type of HTTP verb we want to use (POST for our form)
			            url         : 'http://localhost:8080/' + $('input[name=stockTag]').val(), // the url where we want to POST
			            data        : JSON.stringify(formData), // our data object
			            dataType    : 'json', // what type of data do we expect back from the server
			            encode      : true
			        })
			            // using the done promise callback
			            .done(function(data) {
	
			                // log data to the console so we can see
			                //console.log(data); 
	
			                $('textarea').text(data.message);
			            });
	
			        // stop the form from submitting the normal way and refreshing the page
			        event.preventDefault();
			        
			    }); // end $('form').submit
	
			}); // end $(document).ready
			
		</script>
	</body>
</html>