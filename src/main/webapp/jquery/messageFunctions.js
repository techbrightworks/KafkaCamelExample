function doSendProducerMessage() {
    // get the form values
    var producerMessage = $('#producerMessage').val();

    $.ajax({
        type: "POST",
        url: "/KafkaCamelExample/kafkaworks/message.mvc",
        data: "producerMessage=" + producerMessage,
        success: function(response) {
            // we have the response
            $('#infoconsumermessage').html(response);
            $('#producerMessage').val('');

        },
        error: function(e) {
            alert('Error: ' + e);
        }
    });
};


        
        
        
     