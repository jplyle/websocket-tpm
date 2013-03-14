
function createLogger(outputId, resultId) {
  var logMessage = {};
  var finished = false;
  logMessage.success = function(message) {
    var pre = $('<p class="logSuccess"></p>');
    pre.text(message);
    $(outputId).append(pre);
  }
  logMessage.failure = function(message) {
    var pre = $('<p class="logFailure"></p>');
    pre.text(message);
    $(outputId).append(pre);
  }
  logMessage.message = function(message) {
    var pre = $('<p class="logMessage"></p>');
    pre.text(message);
    $(outputId).append(pre);
  }
  logMessage.finished = function(result) {
    if (finished) return;
    finished = true;
    console.log("Finished - did we have any success? " + result);
    var msg = "";
    if (result) {
      msg = "Success!";
      $(resultId).addClass("successResult");
    } else {
      msg = "Failure";
      $(resultId).addClass("failureResult");      
    }
    $(resultId).text(msg);
    $(resultId).show();
  }
  return logMessage;
}

function clear(outputId, resultId) {
  $(outputId).empty();
  $(resultId).empty();
  $(outputId).text("");
  $(resultId).text("");
  $(resultId).hide();
}

function reload() {
  location.reload(true);
}

var logger;
var expectClose = false;

function runTest(outputId, resultId, address) {
  console.log("Running tests on " + address + " and outputting to " + outputId + ", final result will be in : " + resultId);
  logger = createLogger(outputId, resultId); 
  var websocket = openWebSocket(address, function() {
    startTests( websocket );
  });
}

function openWebSocket(address, callback) {
  var websocket = new WebSocket(address);
  websocket.onopen = function() {
     logger.success("Successfully opened socket");
     callback();
  };
  websocket.onclose = onClose;
  websocket.onmessage = onMessage;
  websocket.onerror = onError;
  return websocket;
}

function onClose(evt) {
  if (expectClose) { 
    logger.message("Socket closed");
  } else {
    logger.failure("Error: Socket closed");
    logger.failure(JSON.stringify(evt));
    logger.finished(false);
  }
}

function onError(evt) {
    logger.failure("WebSocket error!");
    logger.failure(JSON.stringify(evt));
    logger.finished(false);
}

/* */

var waitFor = {};

function checkResponse( msg ) {
  if (waitFor.hasOwnProperty(msg.subject)) {
    console.log("Triggering callback for '" + msg.subject + "' ...");
    waitFor[msg.subject]( msg );
    //delete waitFor[msg.subject];
  }
}

function waitForResponse( subject, onResponse ) {
  waitFor[subject] = onResponse;
}

function onMessage(evt) {
  var msg = JSON.parse(evt.data);
  logger.message( "RECEIVED: " + JSON.stringify(msg) );
  checkResponse(msg);
}

function sendMessage(websocket, obj) {
  console.log("Sending a message");
  var message = JSON.stringify(obj);
  logger.message("SENT: " + message);
  websocket.send(message);
}

function startTests( websocket ) {
  testGetPcr(websocket, doFailure, function() {
    testGetKey(websocket, doFailure, function() {
      testSign(websocket, doFailure, function() {
        logger.success("Finished tests");
        logger.finished(true);
      });
    });
  });
}

function doFailure() {
  logger.finished(false);
}

function testGetPcr( websocket , failed, next ) {
  var reg = { 
      "subject" : "GetPCRValues", 
  };
  waitForResponse(reg.subject, function(reply) {
    if (!reply.hasOwnProperty("pcrValues")) {
      logger.failure("No pcr values returned");
      failed();
    } else if (reply.pcrValues.length < 24 ) {
      logger.failure("Less than 24 PCRs");
      failed();
    } else  {
      logger.success("Got PCR values!");
    }
    next();
  });
  sendMessage(websocket, reg);  
}

function testGetKey( websocket, failed, next ) {
  var msg = { "subject" : "GenerateKey", "usage" : "Signing" };
  waitForResponse(msg.subject, function(reply) {
    console.log("Response to getkey!");
    var success = true;
    success = success && assert ( reply.hasOwnProperty("uuid"), "Has UUID");
    success = success && assert ( reply.hasOwnProperty("usage"), "Has usage");
    success = success && assert ( reply.usage === "Signing", "Has correct usage value");
    if (success) {
      logger.success("Got a key");
      if (typeof next !== "undefined") next();
    } else {
      failed();
    }
  });
  sendMessage(websocket, msg);  
}

function testSign( websocket , failed, next ) {
  var msg = { "subject" : "GenerateKey", "usage" : "Signing" };
  waitForResponse(msg.subject, function(keyReply) {
    var signMsg = { "subject" : "Sign", "key" : keyReply.uuid, "data" : [0,1,2,3,4,5,6,7,8,9] };
    waitForResponse(signMsg.subject, function(signReply) {
      var success = true;
      success = success && assert ( signReply.hasOwnProperty("signature"), "Has signature");
      success = success && assert ( signReply.signature.length > 0 );
      if (success) {
        var verifyMsg = { 
          "subject" : "Verify", 
          "key" : keyReply.uuid, 
          "data" : signMsg.data, 
          "signedData" : signReply.signature 
        };
        waitForResponse( verifyMsg.subject , function(verifyReply) {
          success = success && assert ( verifyReply.hasOwnProperty("result"), "Has result");
          success = success && assert ( verifyReply.result, "Signature verified");
          if (success) {
            next();
          } else {
            failed();
          }
        });
        sendMessage(websocket, verifyMsg);
      }
    });
    sendMessage(websocket, signMsg);    
  });
  sendMessage(websocket, msg);  
}

function assert ( test, goodMessage ) {
  if (!test) {
    logger.failure("Failed : " + goodMessage);
  } else {
    logger.success(goodMessage);
  }
  return test;
}


